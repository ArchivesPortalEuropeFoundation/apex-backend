package eu.apenet.dashboard.manual.eaccpf.actions;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.eaccpf.CreateEacCpf;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dashboard.manual.eag.utils.ParseEag2012Errors;
import eu.apenet.dpt.utils.eaccpf.Identity;
import eu.apenet.dpt.utils.eaccpf.Part;
import eu.apenet.dpt.utils.eaccpf.namespace.EacCpfNamespaceMapper;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.ValidatedState;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author papp
 */
public class StoreEacCpfAction extends EacCpfAction {

    private List<String> warnings_ead = new ArrayList<String>();

    @Override
    public String execute() throws Exception {
        Logger log = Logger.getLogger(StoreEacCpfAction.class);
        String countryCode = new ArchivalLandscape().getmyCountry();
        String basePath = APEnetUtilities.FILESEPARATOR + countryCode + APEnetUtilities.FILESEPARATOR
                + this.getAiId() + APEnetUtilities.FILESEPARATOR + "EAC-CPF" + APEnetUtilities.FILESEPARATOR;

        CreateEacCpf creator = new CreateEacCpf(getServletRequest(), getAiId());
        EacCpf eac = creator.getJaxbEacCpf();
        String filename = APEnetUtilities.convertToFilename(creator.getDatabaseEacCpf().getEncodedIdentifier()) + ".xml";

        // Save XML.
        try {
            String path = basePath + filename;
            String tempPath = basePath + "eacCpf_temp.xml";

            JAXBContext jaxbContext = JAXBContext.newInstance(EacCpf.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:isbn:1-931666-33-4 http://www.archivesportaleurope.net/Portal/profiles/apeEAC-CPF.xsd");
            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EacCpfNamespaceMapper());

            // Save in temporal file
            FileUtils.forceMkdir(new File(APEnetUtilities.getConfig().getRepoDirPath() + basePath));
            File eacCpfTempFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + tempPath));
            jaxbMarshaller.marshal(eac, eacCpfTempFile);

            // It is necessary to validate the file against apeEAC-CPF schema.
            log.debug("Beginning EAC-CPF validation");
            if (validateFile(eacCpfTempFile)) {
                log.info("EAC-CPF file is valid");

                // Move temp file to final file.
                File eacCpfFinalFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
                if (eacCpfFinalFile.exists()) {
                    try {
                        FileUtils.forceDelete(eacCpfFinalFile);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                FileUtils.moveFile(eacCpfTempFile, eacCpfFinalFile);
                if (eacCpfTempFile.exists()) {
                    try {
                        FileUtils.forceDelete(eacCpfTempFile);
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                }

                //update ddbb entry
                EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
                eu.apenet.persistence.vo.EacCpf storedEacEntry = creator.getDatabaseEacCpf();
                storedEacEntry.setTitle(getTitleFromFile(eac));
                storedEacEntry.setUploadDate(new Date());
                storedEacEntry.setPath(path);
                storedEacEntry.setValidated(ValidatedState.VALIDATED);
                eacCpfDAO.update(storedEacEntry);
            } else {
                log.warn("The file " + filename + " is not valid");
                for (int i = 0; i < warnings_ead.size(); i++) {
                    String warning = warnings_ead.get(i).replace("<br/>", "");
                    log.debug(warning);
                    ParseEag2012Errors parseEag2012Errors = new ParseEag2012Errors(warning, false, this);
                    if (this.getActionMessages() != null && !this.getActionMessages().isEmpty()) {
                        String currentError = parseEag2012Errors.errorsValidation();
                        if (!this.getActionMessages().contains(currentError)) {
                            addActionMessage(parseEag2012Errors.errorsValidation());
                        }
                    } else {
                        addActionMessage(parseEag2012Errors.errorsValidation());
                    }
                }
            }
        } catch (JAXBException jaxbe) {
            log.error(jaxbe.getMessage(), jaxbe);
        } catch (APEnetException e) {
            log.error(e.getMessage(), e);
        } catch (SAXException e) {
            log.error(e.getMessage());
        }

        return SUCCESS;
    }

    public boolean validateFile(File tempFile) throws APEnetException, SAXException, IOException {
        Xsd_enum schema = Xsd_enum.XSD_APE_EAC_SCHEMA;
        InputStream in = null;
        try {
            in = new FileInputStream(tempFile);
            List<SAXParseException> exceptions = DocumentValidation.xmlValidation(in, schema);
            if (exceptions != null) {
                StringBuilder warn;
                for (SAXParseException exception : exceptions) {
                    warn = new StringBuilder();
                    warn.append("l.").append(exception.getLineNumber()).append(" c.")
                            .append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
                            .append("<br/>");
                    warnings_ead.add(warn.toString());
                }
                return false;
            }
        } catch (FileNotFoundException e) {
            throw new APEnetException("Exception while validating: File not found", e);
        } catch (SAXException e) {
            throw new APEnetException("Exception while validating: SAXException", e);
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return true;
    }

    private String getTitleFromFile(EacCpf eacCpf) {
        String title = "";
        StringBuilder builderTitle = new StringBuilder();
        try {
            List<Object> allNameEntries = eacCpf.getCpfDescription().getIdentity().getNameEntryParallelOrNameEntry();
            List<Identity.NameEntry> nameEntries = new ArrayList<Identity.NameEntry>();
            for (Object object : allNameEntries) {
                if (object instanceof Identity.NameEntry) {
                    nameEntries.add((Identity.NameEntry) object);
                }
            }
            if (!nameEntries.isEmpty()) {
                Identity.NameEntry firstEntry = nameEntries.get(0);
                List<Part> parts = firstEntry.getPart();
                if (!parts.isEmpty()) {

                    String result = parts.get(0).getLocalType();
                    if (result.equalsIgnoreCase("corpname") || (result.equalsIgnoreCase("persname")) || result.equalsIgnoreCase("famname") || result.equalsIgnoreCase("error")) {
                        // the title is the first occurrence of the element nameEntry/part
                        title = parts.get(0).getContent();
                    } else {
                        // the title is formed by "surname, firstname patronymic"
                        StringBuilder surname = new StringBuilder();
                        StringBuilder firstname = new StringBuilder();
                        StringBuilder patronymic = new StringBuilder();
                        for (Part part : parts) {
                            if (part.getLocalType().equals("surname")) {
                                if(surname.length() != 0){
                                    surname.append(" ");
                                }
                                surname.append(part.getContent());
                            }
                            if (part.getLocalType().equals("firstname")) {
                                if(firstname.length() != 0){
                                    firstname.append(" ");
                                }
                                firstname.append(part.getContent());
                            }
                            if (part.getLocalType().equals("patronymic")) {
                                if(patronymic.length() != 0){
                                    patronymic.append(" ");
                                }
                                patronymic.append(part.getContent());
                            }
                        }
                        // build the title
                        builderTitle.append(surname);
                        if (builderTitle.length() != 0) {
                            builderTitle.append(", ");
                        }
                        builderTitle.append(firstname);
                        if (builderTitle.length() != 0) {
                            builderTitle.append(" ");
                        }
                        builderTitle.append(patronymic);
                        if (builderTitle.length() == 0) {
                            builderTitle.append(" ");
                        }
                        title = builderTitle.toString();
                    }
                }
            }
        } catch (Exception e) {
            title = "";
        }
        return title;
    }
}
