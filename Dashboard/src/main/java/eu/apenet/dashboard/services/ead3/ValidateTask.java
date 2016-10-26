/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

class ValidateTask extends AbstractEad3Task {

    @Override
    protected void execute(Ead3 ead3, Properties properties) throws APEnetException {
        if (notValidated(ead3)) {
            Xsd_enum schema = Xsd_enum.XSD_EAD3_SCHEMA;
            ArchivalInstitution archivalInstitution = ead3.getArchivalInstitution();
            String filepath = APEnetUtilities.getConfig().getRepoDirPath() + ead3.getPath();
            File file = new File(filepath);

            try {
                List<SAXParseException> exceptions = null;
                /* Special for Spanish non UTF8 data */
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
                                APEnetUtilities.getDashboardConfig().getRepoDirPath() + ead3.getPath(),
                                archivalInstitution.getAiId());
                        logger.trace("File converted to UTF-8, we try to validate like it would normally.");
                        exceptions = null;
                    } catch (Exception ex) {
                        exceptions.add(new SAXParseException("File could not be converted to UTF-8 using before.xsl",
                                new LocatorImpl()));
                        logger.error("Could not convert using before-eaccpf.xsl");
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                /* End: Special for Spanish non UTF8 data */

                if (exceptions == null) {
                    exceptions = DocumentValidation.xmlValidation(new FileInputStream(file), schema);
                }
                if (exceptions != null) {
                    StringBuilder warn = new StringBuilder();
                    int count = 0;
                    for (SAXParseException exception : exceptions) {
                        if ((count++) % 2 == 0) {
                            warn.append("<span class=\"colorwarning1\">");
                        } else {
                            warn.append("<span class=\"colorwarning2\">");
                        }
                        warn.append("l.").append(exception.getLineNumber()).append(" c.")
                                .append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
                                .append("</span>").append("<br />");
                    }
                    if (ead3.isConverted()) {
                        ead3.setValidated(ValidatedState.FATAL_ERROR);
                    }
                    boolean warningExists = false;
                    Set<Warnings> warningsFromEad3 = ead3.getWarningses();
                    if (!warningsFromEad3.isEmpty()) {
                        for (Warnings warning : warningsFromEad3) {
                            if (warning.getIswarning()) {
                                warningExists = true;
                                warning.setAbstract_(warn.toString());
                            } else {
                                warningsFromEad3.remove(warning);
                            }
                        }
                    }
                    if (!warningExists) {
                        Warnings warnings = new Warnings();
                        warnings.setAbstract_(warn.toString());
                        warnings.setIswarning(true);
                        warnings.setEad3(ead3);
                        ead3.getWarningses().add(warnings);
                    }

                } else {
                    ead3.setValidated(ValidatedState.VALIDATED);
                    Set<Warnings> warningsFromEad3 = ead3.getWarningses();
                    if (!warningsFromEad3.isEmpty()) {
                        for (Warnings warning : warningsFromEad3) {
                            if (!warning.getIswarning()) {
                                warningsFromEad3.remove(warning);
                            }
                        }
                    }
                }
                DAOFactory.instance().getEad3DAO().store(ead3);
                logAction(ead3);
            } catch (FileNotFoundException e) {
                logAction(ead3, e);
                throw new APEnetException("Could not validate the file with ID: " + ead3.getId() + " – Reason: File not found", e);
            } catch (SAXException e) {
                logAction(ead3, e);
                throw new APEnetException("Could not validate the file with ID: " + ead3.getId() + " – Reason: SAXException", e);
            } catch (XMLStreamException e) {
                logAction(ead3, e);
                throw new APEnetException("Could not validate the file with ID: " + ead3.getId() + " – Reason: XMLStreamException", e);
            } catch (IOException e) {
                logAction(ead3, e);
                throw new APEnetException("Could not validate the file with ID: " + ead3.getId() + " – Reason: IOException", e);
            }
        }
    }

    private static void simpleUtf8Conversion(String filePath, Integer aiId) throws APEnetException {
        try {
            File file = new File(filePath);
            String xslFilePath = APEnetUtilities.getDashboardConfig().getSystemXslDirPath()
                    + APEnetUtilities.FILESEPARATOR + "before-eaccpf.xsl";

            InputStream in = new FileInputStream(file);
            File outputfile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath()
                    + APEnetUtilities.FILESEPARATOR + "converted_" + file.getName());
            TransformationTool.createTransformation(in, outputfile, new File(xslFilePath),
                    null, true, true, null, true, null);
            in.close();

            FileUtils.copyFile(outputfile, file);
            outputfile.delete();
        } catch (Exception e) {
            throw new APEnetException("Could not convert to UTF8");
        }
    }

    @Override
    protected String getActionName() {
        return "validate";
    }

    public static boolean notValidated(Ead3 ead3) {
        return ValidatedState.NOT_VALIDATED.equals(ead3.getValidated());
    }
}
