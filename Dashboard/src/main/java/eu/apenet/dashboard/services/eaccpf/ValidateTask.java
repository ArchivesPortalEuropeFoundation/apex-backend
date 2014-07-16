/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.eaccpf;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.factory.DAOFactory;
//import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;
import eu.apenet.persistence.vo.ValidatedState;
import eu.apenet.persistence.vo.Warnings;

import java.io.*;
//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.xml.sax.SAXException;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamReader;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

import javax.management.modelmbean.XMLParseException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
//import org.xml.sax.helpers.LocatorImpl;

/**
 *
 * @author papp
 */
class ValidateTask extends AbstractEacCpfTask {

    @Override
    protected void execute(EacCpf eacCpf, Properties properties) throws APEnetException {
        if (valid(eacCpf)) {
            Xsd_enum schema = Xsd_enum.XSD_APE_EAC_SCHEMA;
            ArchivalInstitution archivalInstitution = eacCpf.getArchivalInstitution();
            String filepath = APEnetUtilities.getConfig().getRepoDirPath() + eacCpf.getPath();
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
                    logger.warn("ERROR - not UTF-8 ? - Trying to convert it to UTF-8", e);
                    try {
                        simpleUtf8Conversion(
                                APEnetUtilities.getDashboardConfig().getRepoDirPath() + eacCpf.getPath(),
                                archivalInstitution.getAiId());
                        exceptions = null;
                        logger.trace("File converted to UTF-8, we try to validate like it would normally.");
                    } catch (Exception ex) {
                        exceptions.add(new SAXParseException("File could not be converted to UTF-8 using before.xsl",
                                new LocatorImpl()));
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
                    if (eacCpf.isConverted()) {
                        eacCpf.setValidated(ValidatedState.FATAL_ERROR);
                    }
                    boolean warningExists = false;
                    Set<Warnings> warningsFromEacCpf = eacCpf.getWarningses();
                    if (!warningsFromEacCpf.isEmpty()) {
                        for (Warnings warning : warningsFromEacCpf) {
                            if (warning.getIswarning()) {
                                warningExists = true;
                                warning.setAbstract_(warn.toString());
                            } else {
                                warningsFromEacCpf.remove(warning);
                            }
                        }
                    }
                    if (!warningExists) {
                        Warnings warnings = new Warnings();
                        warnings.setAbstract_(warn.toString());
                        warnings.setIswarning(true);
                        warnings.setEacCpf(eacCpf);
                        eacCpf.getWarningses().add(warnings);
                    }

                } else {
                    eacCpf.setValidated(ValidatedState.VALIDATED);
                    Set<Warnings> warningsFromEacCpf = eacCpf.getWarningses();
                    if (!warningsFromEacCpf.isEmpty()) {
                        for (Warnings warning : warningsFromEacCpf) {
                            if (!warning.getIswarning()) {
                                warningsFromEacCpf.remove(warning);
                            }
                        }
                    }
                }
                DAOFactory.instance().getEacCpfDAO().store(eacCpf);
                logAction(eacCpf);
            } catch (FileNotFoundException e) {
                logAction(eacCpf, e);
                throw new APEnetException("Could not validate the file with ID: " + eacCpf.getId() + " – Reason: File not found", e);
            } catch (SAXException e) {
                logAction(eacCpf, e);
                throw new APEnetException("Could not validate the file with ID: " + eacCpf.getId() + " – Reason: SAXException", e);
            } catch (XMLStreamException e) {
                logAction(eacCpf, e);
                throw new APEnetException("Could not validate the file with ID: " + eacCpf.getId() + " – Reason: XMLStreamException", e);
            } catch (IOException e) {
                logAction(eacCpf, e);
                throw new APEnetException("Could not validate the file with ID: " + eacCpf.getId() + " – Reason: IOException", e);
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

    @Override
    protected String getActionName() {
        return "validate";
    }

    public static boolean valid(EacCpf eacCpf) {
        return ValidatedState.NOT_VALIDATED.equals(eacCpf.getValidated());
    }
}
