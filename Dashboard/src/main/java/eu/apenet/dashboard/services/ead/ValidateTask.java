package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.stream.mets.IntegrateMETSParser;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;

public class ValidateTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "validate";
	}
	public static boolean valid(Ead ead){
		return ValidatedState.NOT_VALIDATED.equals(ead.getValidated());
	}
	@Override
	protected void execute(Ead ead, Properties properties) throws APEnetException {
		if (valid(ead)) {
			Xsd_enum schema = Xsd_enum.XSD_APE_SCHEMA;
			String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath();
			File file = new File(filepath);

			try {
				List<SAXParseException> exceptions = utf8Check(ead, file);

				boolean errors = false;
				StringBuilder warn = new StringBuilder();
				if (exceptions == null) {
					try {
						exceptions = DocumentValidation.xmlValidation(new FileInputStream(file), schema);
					}catch (SAXException sax){
						errors = true;
						warn.append("<span class=\"validation-error\">");
						warn.append(sax.getMessage()).append("</span>").append("<br />");
						ead.setValidated(ValidatedState.FATAL_ERROR);
					}

					if (ead.getArchivalInstitution().isUsingMets() && !errors && exceptions == null){
						/*
						 * Merge METS in EAD
						 */
						long startTime = System.currentTimeMillis();
						String error = IntegrateMETSParser.mergeMETSinEAD(ead);
						if (error == null){
							logger.info("Merging METS done in: " + (System.currentTimeMillis() - startTime) + "ms");
							try {
								exceptions = DocumentValidation.xmlValidation(new FileInputStream(file), schema);
							}catch (SAXException sax){
								errors = true;
								warn.append("<span class=\"validation-error\">");
								warn.append(sax.getMessage()).append("</span>").append("<br />");
								ead.setValidated(ValidatedState.FATAL_ERROR);
							}	
						}else {
							errors = true;
							warn.append("<span class=\"validation-error\">");
							warn.append(error).append("</span>").append("<br />");
							ead.setValidated(ValidatedState.FATAL_ERROR);
						}
					}
				}

				if (exceptions != null) {
					errors = true;
					int count = 0;
					for (SAXParseException exception : exceptions) {
						if ((count++) % 2 == 0) {
							warn.append("<span class=\"validation-warning\">");
						} else {
							warn.append("<span class=\"validation-error\">");
						}
						warn.append("l.").append(exception.getLineNumber()).append(" c.")
								.append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
								.append("</span>").append("<br />");
					}
                    if(ead.isConverted())
                        ead.setValidated(ValidatedState.FATAL_ERROR);


				}
				
				if (errors){
					boolean warningExists = false;
					Set<Warnings> warningsFromEad = ead.getWarningses();
					if (!warningsFromEad.isEmpty()) {
						for (Warnings warning : warningsFromEad) {
							if (warning.getIswarning()) {
								warningExists = true;
								warning.setAbstract_(warn.toString());
							} else {
								warningsFromEad.remove(warning);
							}
						}
					}
					if (!warningExists) {
						Warnings warnings = new Warnings();
						warnings.setAbstract_(warn.toString());
						warnings.setIswarning(true);
						warnings.setEad(ead);
						ead.getWarningses().add(warnings);
					}	
				}else {
					ead.setValidated(ValidatedState.VALIDATED);
					Set<Warnings> warningsFromEad = ead.getWarningses();
					if (!warningsFromEad.isEmpty()) {
						for (Warnings warning : warningsFromEad) {
							if (!warning.getIswarning()) {
								warningsFromEad.remove(warning);
							}
						}
					}
				}
				DAOFactory.instance().getEadDAO().store(ead);
				logAction(ead);
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException("Could not validate the file with ID: " + ead.getId(), e);
			}
		}
	}

	private static void simpleUtf8Conversion(String filePath, Integer aiId) throws APEnetException {
		try {
			File file = new File(filePath);
			String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
					+ APEnetUtilities.FILESEPARATOR + "before.xsl";

			InputStream in = new FileInputStream(file);
			File outputfile = new File(APEnetUtilities.getDashboardConfig().getRepoDirPath()
					+ APEnetUtilities.FILESEPARATOR + aiId.toString() + APEnetUtilities.FILESEPARATOR + "converted_"
					+ file.getName());
			TransformationTool.createTransformation(in, outputfile, FileUtils.openInputStream(new File(xslFilePath)),
					null, true, true, null, true, null);
			in.close();

			FileUtils.copyFile(outputfile, file);
			outputfile.delete();
		} catch (Exception e) {
			throw new APEnetException("Could not convert to UTF8");
		}
	}
	private List<SAXParseException> utf8Check(Ead ead, File file) throws Exception{
		/* Special for Spanish non UTF8 data */
		List<SAXParseException> exceptions = null;
		XMLStreamReader reader = null;
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			reader = XMLInputFactory.newInstance().createXMLStreamReader(inputStream, "UTF-8");
			reader.next();
		} catch (Exception e) {
			exceptions = new ArrayList<SAXParseException>();
			exceptions
					.add(new SAXParseException(
							"The file is not UTF-8 - We will try to convert it right now to UTF-8 or make sure your file is UTF-8 before re-uploading. If not, the file will not pass the index step and will fail.",
							new LocatorImpl()));
			logger.warn("ERROR - not UTF-8 ? - Trying to convert it to UTF-8");
			try {
				simpleUtf8Conversion(
						APEnetUtilities.getDashboardConfig().getRepoDirPath() + ead.getPath(),
						ead.getAiId());
				exceptions = null;
				logger.trace("File converted to UTF-8, we try to validate like it would normally.");
			} catch (Exception ex) {
				exceptions.add(new SAXParseException("File could not be converted to UTF-8 using before.xsl",
						new LocatorImpl()));
			}
		} finally {
			if (reader != null)
				reader.close();
			if (inputStream != null){
				inputStream.close();
			}
		}	
		/* End: Special for Spanish non UTF8 data */
		return exceptions;
	}
}
