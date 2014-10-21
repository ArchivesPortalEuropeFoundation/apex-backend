package eu.apenet.dashboard.services.ead;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;

public class ConvertTask extends AbstractEadTask {

	@Override
	protected String getActionName() {
		return "convert to APE EAD";
	}
	public static boolean valid(Ead ead){
		return !ead.getValidated().equals(ValidatedState.VALIDATED);
	}

	@Override
	protected void execute(Ead ead, Properties properties) throws APEnetException {
		if (valid(ead)) {
			CounterCLevelCall counterCLevelCall = null;
			String xslFileName = "default-apeEAD.xsl";
			ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
			String mainagencycode = archivalInstitution.getRepositorycode();
            Map<String, String> parameters = getConversionProperties(properties);

			if (mainagencycode != null)
				parameters.put("mainagencycode", mainagencycode);
			String countryCode = archivalInstitution.getCountry().getIsoname();
			if (StringUtils.isNotEmpty(countryCode))
				parameters.put("countrycode", countryCode);
			String languageXmlPath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
					+ APEnetUtilities.FILESEPARATOR + "languages.xml";
			languageXmlPath = languageXmlPath.replaceAll("\\\\", "/");
			if (new File(languageXmlPath).exists())
				parameters.put("loclanguage", languageXmlPath);

			String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath();
			File file = new File(filepath);

			try {
				InputStream in;
				StringWriter xslMessages;
				File outputfile;
				String tempDirOutputPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
						+ APEnetUtilities.FILESEPARATOR + archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR;
				if (xslFileName == null || xslFileName.equals("default-apeEAD.xsl")) {
					String tempOutputFilePath = tempDirOutputPath + "convert_" + ead.getId() + "_.xml";
					File tempDirOutput = new File(tempDirOutputPath);
					if (!tempDirOutput.exists()) {
						tempDirOutput.mkdirs();
					}
					File tempOutputFile = new File(tempOutputFilePath);
					String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
							+ APEnetUtilities.FILESEPARATOR + "before.xsl";
					logger.info("'" + archivalInstitution.getAiname() + "' is converting file: '" + ead.getEadid()
							+ "' with id: '" + ead.getId() + "'");
					in = new FileInputStream(file);
					TransformationTool.createTransformation(in, tempOutputFile, new File(xslFilePath), null, true,
							true, null, true, null, APEnetUtilities.getDashboardConfig().getSystemXslDirPath());

					File xslFile = new File(APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
							+ APEnetUtilities.FILESEPARATOR + "default-apeEAD.xsl");

					in = new FileInputStream(tempOutputFile);
					outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
					xslMessages = TransformationTool.createTransformation(in, outputfile, xslFile, parameters, true,
							true, null, true, counterCLevelCall, APEnetUtilities.getDashboardConfig().getSystemXslDirPath());
					tempOutputFile.delete();
                                        if(tempDirOutput.listFiles().length == 0)
                                            FileUtils.deleteDirectory(tempDirOutput);
				} else {
					String xslFilePath = APEnetUtilities.getDashboardConfig().getXslDirPath()
							+ APEnetUtilities.FILESEPARATOR + xslFileName;
					in = new FileInputStream(file);
					outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
					xslMessages = TransformationTool.createTransformation(in, outputfile,
							FileUtils.openInputStream(new File(xslFilePath)), null, true, true, null, true, null);
				}

				StringBuilder xslWarnings = new StringBuilder();

				List<String> xslWarningLines = convertToWarnings(xslMessages);
				int count = 0;
				for (String xslWarningLine : xslWarningLines) {
					if ((count++) % 2 == 0)
						xslWarnings.append("<span class=\"validation-warning\">");
					else
						xslWarnings.append("<span class=\"validation-error\">");
					xslWarnings.append(xslWarningLine).append("</span>").append("<br/>");
				}

				boolean warningExists = false;
				Set<Warnings> warningsFromEad = ead.getWarningses();
				if (!warningsFromEad.isEmpty()) {
					for (Warnings warning : warningsFromEad) {
						if (warning.getIswarning()) {
                            if(StringUtils.isEmpty(xslMessages.toString())) {
                                warningsFromEad.remove(warning);
                            } else {
                                warningExists = true;
                                warning.setAbstract_(xslWarnings.toString());
                            }
						} else {
							warningsFromEad.remove(warning);
						}
					}
				}
				if (!warningExists && StringUtils.isNotEmpty(xslMessages.toString())) {
					Warnings warnings = new Warnings();
					warnings.setAbstract_(xslWarnings.toString());
					warnings.setIswarning(true);
					warnings.setEad(ead);
					ead.getWarningses().add(warnings);
				}

				ead.setConverted(true);
				logger.debug("Converted file takes name of original file.");
				FileUtils.copyFile(outputfile, file);
				outputfile.delete();
				ead = DAOFactory.instance().getEadDAO().store(ead);
				LinkingService.linkWithHgOrSg(ead);
				logAction(ead);
			} catch (Exception e) {
				logAction(ead, e);
				throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
			}
		}
	}

	private List<String> convertToWarnings(StringWriter xslMessages){
		List<String> result = new ArrayList<String>();
		Map<String, Integer> lines = new LinkedHashMap<String, Integer>();
		String[] xslWarningLines = xslMessages.toString().split("\n");
		for (String warning: xslWarningLines){
			if (lines.containsKey(warning)){
				Integer value = lines.get(warning);
				lines.put(warning, value+1);
			}else {
				lines.put(warning, 1);
			}
		}
		for (Map.Entry<String,Integer> entry: lines.entrySet()){
			if (entry.getValue() > 1){
				result.add(entry.getKey() + " (" + entry.getValue()+" times)");
			}else {
				result.add(entry.getKey());
			}
		}
		return result;
	}
	
    private Map<String, String> getConversionProperties(Properties properties) {
        if(properties == null)
            return new HashMap<String, String>();
        Map<String, String> parameters = new HashMap<String, String>();
        String option_default = properties.getProperty("defaultRoleType");
        String option_use_existing = properties.getProperty("useDefaultRoleType");
        boolean option_use_existing_bool = Boolean.parseBoolean(option_use_existing);
        parameters.put("defaultRoleType", option_default);
        parameters.put("useDefaultRoleType", Boolean.toString(option_use_existing_bool));
        return parameters;
    }
}
