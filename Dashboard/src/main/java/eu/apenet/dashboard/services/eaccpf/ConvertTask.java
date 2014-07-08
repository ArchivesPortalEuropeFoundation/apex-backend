/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.eaccpf;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.SAXException;

/**
 *
 * @author papp
 */
public class ConvertTask extends AbstractEacCpfTask {

    @Override
    protected void execute(EacCpf eacCpf, Properties properties) throws APEnetException {
        if (valid(eacCpf)) {
            String xslFileName = "default-apeEAC-CPF.xsl";
            ArchivalInstitution archivalInstitution = eacCpf.getArchivalInstitution();
            String mainagencycode = archivalInstitution.getRepositorycode();
            String recordId = eacCpf.getIdentifier() + "";
            Map<String, String> parameters = getConversionProperties(properties);

            if (mainagencycode != null) {
                parameters.put("mainagencycode", mainagencycode);
            }
            if (recordId != null) {
                parameters.put("recordId", recordId);
            }
//            String countryCode = archivalInstitution.getCountry().getIsoname();
//            if (StringUtils.isNotEmpty(countryCode)) {
//                parameters.put("countrycode", countryCode);
//            }
//            String languageXmlPath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
//                    + APEnetUtilities.FILESEPARATOR + "languages.xml";
//            languageXmlPath = languageXmlPath.replaceAll("\\\\", "/");
//            if (new File(languageXmlPath).exists()) {
//                parameters.put("loclanguage", languageXmlPath);
//            }

            String filepath = APEnetUtilities.getConfig().getRepoDirPath() + eacCpf.getPath();
            File file = new File(filepath);

            try {
                InputStream in;
                StringWriter xslMessages;
                File outputfile;
                String tempDirOutputPath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
                        + APEnetUtilities.FILESEPARATOR + archivalInstitution.getAiId() + APEnetUtilities.FILESEPARATOR;
                File outputpath = new File(tempDirOutputPath);
                if (!outputpath.exists()) {
                    FileUtils.forceMkdir(outputpath);
                }
                String xslFilePath = APEnetUtilities.getDashboardConfig().getXslDirPath()
                        + APEnetUtilities.FILESEPARATOR + "system" + APEnetUtilities.FILESEPARATOR + xslFileName;
                in = new FileInputStream(file);
                outputfile = new File(tempDirOutputPath + "converted_" + file.getName());
                xslMessages = TransformationTool.createTransformation(in, outputfile,
                        FileUtils.openInputStream(new File(xslFilePath)), parameters, true, true, null, true, null);

                StringBuilder xslWarnings = new StringBuilder();
                String[] xslWarningLines = xslMessages.toString().split("\n");

                int count = 0;
                for (String xslWarningLine : xslWarningLines) {
                    if ((count++) % 2 == 0) {
                        xslWarnings.append("<span class=\"colorwarning1\">");
                    } else {
                        xslWarnings.append("<span class=\"colorwarning2\">");
                    }
                    xslWarnings.append(xslWarningLine).append("</span>").append("<br/>");
                }

                boolean warningExists = false;
                Set<Warnings> warningsFromEacCpf = eacCpf.getWarningses();
                if (!warningsFromEacCpf.isEmpty()) {
                    for (Warnings warning : warningsFromEacCpf) {
                        if (warning.getIswarning()) {
                            if (StringUtils.isEmpty(xslMessages.toString())) {
                                warningsFromEacCpf.remove(warning);
                            } else {
                                warningExists = true;
                                warning.setAbstract_(xslWarnings.toString());
                            }
                        } else {
                            warningsFromEacCpf.remove(warning);
                        }
                    }
                }
                if (!warningExists && StringUtils.isNotEmpty(xslMessages.toString())) {
                    Warnings warnings = new Warnings();
                    warnings.setAbstract_(xslWarnings.toString());
                    warnings.setIswarning(true);
                    warnings.setEacCpf(eacCpf);
                    eacCpf.getWarningses().add(warnings);
                }

                eacCpf.setConverted(true);
                logger.debug("Converted file takes name of original file.");
                FileUtils.copyFile(outputfile, file);
                outputfile.delete();
                eacCpf = DAOFactory.instance().getEacCpfDAO().store(eacCpf);
                logAction(eacCpf);
                if (outputpath.listFiles().length == 0) { // There aren't any file in the directory, so it should be removed
                    FileUtils.forceDelete(outputpath);
                }
            } catch (IOException e) {
                logAction(eacCpf, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            } catch (SAXException e) {
                logAction(eacCpf, e);
                throw new APEnetException(this.getActionName() + " " + e.getMessage(), e);
            }
        }

    }

    @Override
    protected String getActionName() {
        return "convert to APE EAC-CPF";
    }

    public static boolean valid(EacCpf eacCpf) {
        return !eacCpf.isConverted() && !eacCpf.getValidated().equals(ValidatedState.VALIDATED);
    }

    private Map<String, String> getConversionProperties(Properties properties) {
        if (properties == null) {
            return new HashMap<String, String>();
        }
        Map<String, String> parameters = new HashMap<String, String>();
        return parameters;
    }

}
