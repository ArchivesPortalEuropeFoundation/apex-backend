package eu.apenet.dashboard.services.eag;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import net.sf.saxon.s9api.SaxonApiException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.xslt.EagXslt;
import eu.apenet.dashboard.manual.APEnetEAGDashboard;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ParseEAGAction extends ActionSupport {

	private final static Logger logger = Logger.getLogger(ParseEAGAction.class); // TODO
	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath(); // instance
	List<String> eagsNotConverted = new ArrayList<String>();

	@Override
	public String execute() throws Exception {
		// parseAllExistingEAGToEAG2012();
		parseEAGs();
		return SUCCESS;
	}

	private void parseEAGs() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {

		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> institutions = aiDao.findAll();
		if (institutions != null && institutions.size() > 0) {
			Iterator<ArchivalInstitution> institutionsIterator = institutions.iterator();
			while (institutionsIterator.hasNext()) {
				ArchivalInstitution institution = institutionsIterator.next();
				String eagPath = "/" + institution.getCountry().getIsoname() + "/" + institution.getAiId()
						+ "/EAG/";
				String tempDirOutputPath = institution.getEagPath();
				if (tempDirOutputPath != null && !tempDirOutputPath.isEmpty()) {
					String prefixPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
					tempDirOutputPath = prefixPath + tempDirOutputPath;
					if (!new File(tempDirOutputPath).exists()) {
						prefixPath = APEnetUtilities.getDashboardConfig().getRepoDirPath();
						tempDirOutputPath = prefixPath + institution.getEagPath();
					}
					String filePattern = "[a-zA-Z0-9-_\\.]+";
					File tempFile = new File(tempDirOutputPath);
					File oldEagFile = tempFile;
					if (!Pattern.matches(filePattern, tempFile.getName())) {
						String fileName = oldEagFile.getName().replaceAll("[^a-zA-Z0-9\\-\\.]", "_");
						oldEagFile = new File(tempFile.getParentFile(), fileName);
						logger.info("Move: " + tempFile.getAbsolutePath() + " to: " + oldEagFile.getAbsolutePath());
						FileUtils.moveFile(tempFile, oldEagFile);	
						institution.setEagPath(eagPath + fileName);
						aiDao.store(institution);
					}
					if (oldEagFile.exists()) {
						String pattern = institution.getCountry().getIsoname() + "-[a-zA-Z0-9:/\\-]{1,11}";
						String repositoryCode = institution.getRepositorycode();
						boolean validRepositoryCode = Pattern.matches(pattern, repositoryCode);
						if (validRepositoryCode) {

						} else {
							logger.warn(institution.getRepositorycode() + " does not match pattern: " + pattern);
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
							factory.setNamespaceAware(false);
							DocumentBuilder builder = factory.newDocumentBuilder();
							Document doc = builder.parse(new InputSource(tempDirOutputPath));
							doc.getDocumentElement().normalize();
							XPathExpression eagIdExpression = XPATH.compile("/eag/eagheader/eagid");
							String xmlEagid = (String) eagIdExpression.evaluate(doc, XPathConstants.STRING);
							XPathExpression repoCodeExpression = XPATH
									.compile("/eag/archguide/identity/repositorid/@repositorycode");
							String repoCode = (String) repoCodeExpression.evaluate(doc, XPathConstants.STRING);
							if (Pattern.matches(pattern, xmlEagid)) {
								repositoryCode = xmlEagid;
								logger.info("Use eagid: " + repositoryCode);
							} else if (Pattern.matches(pattern, repoCode)) {
								repositoryCode = repoCode;
								logger.info("Use repositorycode: " + repositoryCode);
							} else if (Pattern.matches(pattern, institution.getCountry().getIsoname() + "-" + repoCode)) {
								repositoryCode = institution.getCountry().getIsoname() + "-" + repoCode;
								logger.info("Use country + repositorycode: " + repositoryCode);
							} else {
								repositoryCode = institution.getCountry().getIsoname() + "-D" + institution.getAiId();
								logger.info("Use db: " + repositoryCode);
							}

						}
						String eagFile = repositoryCode.replaceAll("[^a-zA-Z0-9\\-\\.]", "_") + ".xml";
						logger.info(repositoryCode + " : " + eagPath + eagFile);
						File newEagFile = new File(prefixPath + eagPath + eagFile);
						boolean converted = convert(institution.getAiname(), repositoryCode, oldEagFile, newEagFile);
						if (converted){
							institution.setRepositorycode(repositoryCode);
							institution.setEagPath(eagPath + eagFile);
							aiDao.store(institution);
				    		APEnetEAGDashboard eag = new APEnetEAGDashboard(institution.getAiId(), newEagFile.getAbsolutePath());
				    		eag.setEagPath(eagPath);
				    		eag.setId(repositoryCode);
				    		eag.modifyArchivalLandscape();
						}
					} else {
						eagsNotConverted.add("NOT EXIST: " + oldEagFile.getAbsolutePath());
						logger.warn("'File does not exist: '" + institution.getEagPath());
					}
				}

			}
		}
		if (eagsNotConverted.size() > 0) {
			logger.warn("---------------");
			logger.warn("The following EAGs (in total: " + eagsNotConverted.size()
					+ ") have not been correctly validated against EAG 2012, please take care of them manually:");
		}
		for (String eagNotConverted : eagsNotConverted) {
			logger.warn(eagNotConverted);
		}
	}

	private boolean convert(String aiName, String repositoryCode, File oldEagFile, File newEagFile)
			throws SAXException, IOException {

		FileInputStream in = null;
		File newTempOutputFile = new File(newEagFile.getAbsolutePath() + ".new");
		File oldTempOutputFile = new File(newEagFile.getAbsolutePath() + ".old");
		String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR
				+ "eag2eag2012.xsl";
		logger.info("'" + aiName + "' is parsing file: '" + oldEagFile.getAbsolutePath());
		in = new FileInputStream(oldEagFile);
		if (DocumentValidation.xmlValidation(oldEagFile, Xsd_enum.XSD_EAG_2012_SCHEMA) != null) {
			File validationErrorFile = new File(newTempOutputFile.getParentFile(), "validation_errors.txt");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("repositoryCode", repositoryCode);
			TransformationTool.createTransformation(in, newTempOutputFile, new File(xslFilePath), parameters, true,
					true, null, true, null, APEnetUtilities.getDashboardConfig().getSystemXslDirPath());

			List<SAXParseException> exceptions = DocumentValidation.xmlValidation(newTempOutputFile,
					Xsd_enum.XSD_EAG_2012_SCHEMA);
			if (exceptions == null) {
				try {
					FileUtils.moveFile(oldEagFile, oldTempOutputFile); // backup
																		// old
																		// EAGs,
																		// if
																		// it's
																		// not
																		// needed
																		// comment
																		// this
																		// line
					FileUtils.moveFile(newTempOutputFile, newEagFile);
				} catch (IOException e) {
					logger.error("problem moving file " + newEagFile.getAbsolutePath());
				}
				if (validationErrorFile.exists())
					validationErrorFile.delete();
				File oldFile = oldEagFile;
				while (!oldFile.getParentFile().getName().equals("EAG") && !oldFile.getName().equals("EAG"))
				{
					oldFile = oldFile.getParentFile();
					logger.info("Delete old file:" + oldFile.getAbsolutePath());
					oldFile.delete();
				}
				return true;
			} else {
				eagsNotConverted.add(oldEagFile.getParentFile().getAbsolutePath());
				logger.error("The converted EAG 2012 file is not valid, we will not use this file for now, it has to be checked manually. The list of errors can be found in the file validation_errors.txt");
				StringBuilder warn = new StringBuilder();
				for (SAXParseException exception : exceptions) {
					warn.append("l.").append(exception.getLineNumber()).append(" c.")
							.append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
							.append("\n");
				}
				FileUtils.writeStringToFile(validationErrorFile, warn.toString(), "UTF-8");

			}
		}
		return false;
	}

	private boolean parseAllExistingEAGToEAG2012() throws FileNotFoundException {
		boolean failure = false;
		// 1. get all institutions and his EAG
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List<ArchivalInstitution> institutions = aiDao.findAll();
		Iterator<ArchivalInstitution> institutionsIterator = null;
		String eagPath = null;
		if (institutions != null && institutions.size() > 0) {
			institutionsIterator = institutions.iterator();
			while (institutionsIterator.hasNext() && !failure) {
				ArchivalInstitution institution = institutionsIterator.next();
				eagPath = institution.getEagPath(); // get path to be checked
				// 2. check if eag is valid
				if (eagPath != null && !eagPath.isEmpty()) {
					eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + eagPath;
					if (!new File(eagPath).exists()) {
						eagPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + institution.getEagPath();
					}
					// APEnetEAGDashboard eag = new
					// APEnetEAGDashboard(institution.getAiId(), eagPath);
					// if(eag.APEnetEAGValidate(eagPath)){
					// 3. parse EAG to EAG2012_temp
					try {
						File eagPathFile = new File(eagPath);
						File destinationFile = new File(eagPathFile.getParentFile(), eagPathFile.getName()
								+ "_convertedToEAG2012.xml");
						PrintWriter writer = new PrintWriter(destinationFile);
						EagXslt.convertAi(writer, eagPathFile, null);
					} catch (SaxonApiException e) {
						logger.error("problems parsing EAG to EAG2012", e);
						failure = true; // stop
					}
					// }
				}
			}
			if (!failure) {
				// 4. if all is ok remove EAG and move _convertedToEAG2012.xml
				institutionsIterator = institutions.iterator();
				while (institutionsIterator.hasNext()) {
					eagPath = institutionsIterator.next().getEagPath();
					if (eagPath != null && !eagPath.isEmpty() && eagPath.contains("_convertedToEAG2012.xml")) {
						eagPath = APEnetUtilities.getDashboardConfig().getRepoDirPath() + eagPath;
						File file = new File(eagPath);
						File outputFile = new File(eagPath.substring(0, eagPath.indexOf("_convertedToEAG2012.xml"))
								+ ".xml");
						try {
							FileUtils.moveFile(outputFile, new File(outputFile.getAbsolutePath() + ".old")); // backup
																												// old
																												// EAGs,
																												// if
																												// it's
																												// not
																												// needed
																												// comment
																												// this
																												// line
							FileUtils.moveFile(outputFile, file);
						} catch (IOException e) {
							logger.error("problem moving file " + file.getAbsolutePath());
						}
					}
				}
			}
		}
		return failure;
	}

}
