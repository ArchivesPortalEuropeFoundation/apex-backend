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

import eu.apenet.dpt.utils.service.stax.CheckIsEadFile;
import eu.apenet.persistence.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.actions.ajax.AjaxConversionOptionsConstants;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.extendxsl.CounterCLevelCall;
import eu.apenet.persistence.factory.DAOFactory;

public class ConvertTask extends AbstractEadTask {

    @Override
    protected String getActionName() {
        return "convert to APE EAD";
    }

    public static boolean valid(Ead ead) {
        return !ead.isConverted() && !ead.getValidated().equals(ValidatedState.VALIDATED);
    }

    @Override
    protected void execute(Ead ead, Properties properties) throws APEnetException {
        if (valid(ead)) {
            CounterCLevelCall counterCLevelCall = null;
            String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath() + APEnetUtilities.FILESEPARATOR + "default-apeEAD.xsl";
            ArchivalInstitution archivalInstitution = ead.getArchivalInstitution();
            String mainagencycode = archivalInstitution.getRepositorycode();
            Map<String, String> parameters = getConversionProperties(properties);
            String xslName = getXslName(properties);
            if (xslName != null) {
                xslFilePath = APEnetUtilities.getDashboardConfig().getXslDirPath() + APEnetUtilities.FILESEPARATOR + xslName;
            }

            if (mainagencycode != null) {
                parameters.put("mainagencycode", mainagencycode);
            }
            String countryCode = archivalInstitution.getCountry().getIsoname();
            if (StringUtils.isNotEmpty(countryCode)) {
                parameters.put("countrycode", countryCode);
            }
            String languageXmlPath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
                    + APEnetUtilities.FILESEPARATOR + "languages.xml";
            languageXmlPath = languageXmlPath.replaceAll("\\\\", "/");
            if (new File(languageXmlPath).exists()) {
                parameters.put("loclanguage", languageXmlPath);
            }

            String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead.getPath();
            File file = new File(filepath);

            try {
                InputStream in;
                StringWriter xslMessages;
                File outputfile;
                String tempDirOutputPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
                        + APEnetUtilities.FILESEPARATOR + archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR;
                CheckIsEadFile checkIsEadFile = new CheckIsEadFile(file);
                checkIsEadFile.run();
                if (checkIsEadFile.isEadRoot()) {
                    String tempOutputFilePath = tempDirOutputPath + "convert_" + ead.getId() + "_.xml";
                    File tempDirOutput = new File(tempDirOutputPath);
                    if (!tempDirOutput.exists()) {
                        tempDirOutput.mkdirs();
                    }
                    File tempOutputFile = new File(tempOutputFilePath);
                    String xslBeforeFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
                            + APEnetUtilities.FILESEPARATOR + "before.xsl";
                    logger.info("'" + archivalInstitution.getAiname() + "' is converting file: '" + ead.getEadid()
                            + "' with id: '" + ead.getId() + "'");
                    in = new FileInputStream(file);
                    TransformationTool.createTransformation(in, tempOutputFile, new File(xslBeforeFilePath), null, true,
                            true, null, true, null, APEnetUtilities.getDashboardConfig().getSystemXslDirPath());

                    File xslFile = new File(xslFilePath);

                    in = new FileInputStream(tempOutputFile);
                    outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
                    xslMessages = TransformationTool.createTransformation(in, outputfile, xslFile, parameters, true,
                            true, null, true, counterCLevelCall, APEnetUtilities.getDashboardConfig().getSystemXslDirPath());
                    tempOutputFile.delete();
                    if (tempDirOutput.listFiles().length == 0) {
                        FileUtils.deleteDirectory(tempDirOutput);
                    }
                } else {
                    in = new FileInputStream(file);
                    outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
                    xslMessages = TransformationTool.createTransformation(in, outputfile,
                            new File(xslFilePath), null, true, true, null, true, null);
                }

                StringBuilder xslWarnings = new StringBuilder();

                List<String> xslWarningLines = convertToWarnings(xslMessages);
                int count = 0;
                for (String xslWarningLine : xslWarningLines) {
                    if ((count++) % 2 == 0) {
                        xslWarnings.append("<span class=\"validation-warning\">");
                    } else {
                        xslWarnings.append("<span class=\"validation-error\">");
                    }
                    xslWarnings.append(xslWarningLine).append("</span>").append("<br/>");
                }

                boolean warningExists = false;
                Set<Warnings> warningsFromEad = ead.getWarningses();
                if (!warningsFromEad.isEmpty()) {
                    for (Warnings warning : warningsFromEad) {
                        if (warning.getIswarning()) {
                            if (StringUtils.isEmpty(xslMessages.toString())) {
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

    private List<String> convertToWarnings(StringWriter xslMessages) {
        List<String> result = new ArrayList<String>();
        Map<String, Integer> lines = new LinkedHashMap<String, Integer>();
        String[] xslWarningLines = xslMessages.toString().split("\n");
        for (String warning : xslWarningLines) {
            if (lines.containsKey(warning)) {
                Integer value = lines.get(warning);
                lines.put(warning, value + 1);
            } else {
                lines.put(warning, 1);
            }
        }
        for (Map.Entry<String, Integer> entry : lines.entrySet()) {
            if (entry.getValue() > 1) {
                result.add(entry.getKey() + " (" + entry.getValue() + " times)");
            } else {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    private String getXslName(Properties properties) {
        if (properties == null) {
            return null;
        } else if (properties.containsKey(QueueItem.XSL_FILE)) {
            return properties.getProperty(QueueItem.XSL_FILE);
        } else {
            return null;
        }
    }

    private Map<String, String> getConversionProperties(Properties properties) {
        if (properties == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> parameters = new HashMap<String, String>();

        // Recover the values from the properties.
        // Parse the conversion options related to DAO type.
        String option_default = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_DEFAULT);
        String option_use_existing = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_USE_EXISTING);
        boolean option_use_existing_bool = Boolean.parseBoolean(option_use_existing);
        // Parse the conversion options related to rights statement for digital
        // objects.
        String option_default_rights_digital = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL);
        String option_default_rights_digital_text = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT);
        String option_rights_digital_description = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_DESCRIPTION);
        String option_rights_digital_holder = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_HOLDER);
        // Parse the conversion options related to rights statement for EAD
        // data.
        String option_default_rights_ead = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD);
        String option_default_rights_ead_text = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD_TEXT);
        String option_rights_ead_description = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_DESCRIPTION);
        String option_rights_ead_holder = properties.getProperty(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_HOLDER);

        // Add the values to the map.
        // Add options related to DAO type.
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT, option_default);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_USE_EXISTING, Boolean.toString(!option_use_existing_bool));
        // Add options related to rights statement for digital objects.
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL, option_default_rights_digital);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_DIGITAL_TEXT, option_default_rights_digital_text);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_DESCRIPTION, option_rights_digital_description);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_DIGITAL_HOLDER, option_rights_digital_holder);
        // Add options related to rights statement for EAD data.
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD, option_default_rights_ead);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_DEFAULT_RIGHTS_EAD_TEXT, option_default_rights_ead_text);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_DESCRIPTION, option_rights_ead_description);
        parameters.put(AjaxConversionOptionsConstants.SCRIPT_RIGHTS_EAD_HOLDER, option_rights_ead_holder);

        return parameters;
    }
}
