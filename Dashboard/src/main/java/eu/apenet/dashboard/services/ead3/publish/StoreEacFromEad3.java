/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.eaccpf.CreateEacCpf;
import eu.apenet.dashboard.services.eaccpf.EacCpfService;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.Identity;
import eu.apenet.dpt.utils.eaccpf.Part;
import eu.apenet.dpt.utils.eaccpf.namespace.EacCpfNamespaceMapper;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ead3;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.util.ApeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author kaisar
 */
public class StoreEacFromEad3 {

    private Map parameters;
    private String countryCode;
    private String eacDaoId;
    private String fileId;
    private int fileToLoad;
    private int aiId;
    private String ead3Identifier;
    private List<String> warnings_ead = new ArrayList<>();

    Logger LOG = Logger.getLogger(StoreEacFromEad3.class);

    public StoreEacFromEad3(Map parameters, String countryCode, int aiId, String ead3Identifier) {
        this.parameters = parameters;
        this.countryCode = countryCode;
        this.aiId = aiId;
        this.ead3Identifier = ead3Identifier;
    }

    public void storeEacCpf() throws Exception {
        String basePath = APEnetUtilities.FILESEPARATOR + countryCode + APEnetUtilities.FILESEPARATOR
                + this.getAiId() + APEnetUtilities.FILESEPARATOR + "EAC-CPF" + APEnetUtilities.FILESEPARATOR;
        CreateEacCpf creator = new CreateEacCpf(parameters, this.getAiId());
        EacCpf eac = creator.getJaxbEacCpf();
        String filename;
        if (this.getFileId() == null || this.getFileId().isEmpty()) {
            filename = APEnetUtilities.convertToFilename(ApeUtil.encodeSpecialCharactersWithSpaces(creator.getDatabaseEacCpf().getEncodedIdentifier()));
            this.setFileId(filename);
        } else {
            filename = this.getFileId();
        }
        filename = filename + ".xml";

        try {
            String path = basePath + filename;
            String tempPath = basePath + "eacCpf_temp.xml";

            JAXBContext jaxbContext = JAXBContext.newInstance(EacCpf.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "urn:isbn:1-931666-33-4 http://www.archivesportaleurope.net/Portal/profiles/apeEAC-CPF.xsd");
            jaxbMarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new EacCpfNamespaceMapper());

            // Save in temporal file
            LOG.info("Temp dir " + new File(APEnetUtilities.getConfig().getRepoDirPath() + basePath));
            FileUtils.forceMkdir(new File(APEnetUtilities.getConfig().getRepoDirPath() + basePath));
            LOG.info("Temp dir " + new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath));
            File eacCpfTempFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + tempPath);
            jaxbMarshaller.marshal(eac, eacCpfTempFile);

            // It is necessary to validate the file against apeEAC-CPF schema.
            LOG.debug("Beginning EAC-CPF validation");
            EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();

            if (validateFile(eacCpfTempFile)) {
                LOG.info("EAC-CPF file is valid");

                // Move temp file to final file.
                File eacCpfFinalFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
                if (eacCpfFinalFile.exists()) {
                    try {
                        // Windows file lock workaround; uncomment if necessary
                        System.gc();
                        Thread.sleep(2000);
                        FileUtils.forceDelete(eacCpfFinalFile);
                    } catch (IOException e) {
//                        LOG.error(e.getMessage(), e);
                    }
                }
                FileUtils.moveFile(eacCpfTempFile, eacCpfFinalFile);
                if (eacCpfTempFile.exists()) {
                    try {
                        FileUtils.forceDelete(eacCpfTempFile);
                    } catch (IOException e) {
//                        LOG.error(e.getMessage(), e);
                    }
                }
                
                //update ddbb entry
                eu.apenet.persistence.vo.EacCpf storedEacEntry = null;

                if (this.getEacDaoId() != null
                        && !this.getEacDaoId().isEmpty()
                        && Integer.valueOf(this.getEacDaoId()) > 0) {
//                	storedEacEntry = eacCpfDAO.findById(Integer.parseInt(this.getEacDaoId()));
                    storedEacEntry = eacCpfDAO.findById(Integer.valueOf(this.getEacDaoId()));
                } else {
                    storedEacEntry = creator.getDatabaseEacCpf();
                }

                Ead3 ead3 = DAOFactory.instance().getEad3DAO().getEad3ByIdentifier(aiId, ead3Identifier);

                storedEacEntry.setTitle(getTitleFromFile(eac));
                storedEacEntry.setUploadDate(new Date());
                storedEacEntry.setPath(path);
                storedEacEntry.setValidated(ValidatedState.VALIDATED);
                storedEacEntry.setEad3(ead3);
                storedEacEntry = eacCpfDAO.update(storedEacEntry);
                //add to queue
                EacCpfService.convertValidatePublish(storedEacEntry.getId(), new Properties(), "dut");
                
                this.setEacDaoId(Integer.toString(storedEacEntry.getId()));
                this.setFileToLoad(storedEacEntry.getId());
                ead3.getEacCpfs().add(storedEacEntry);

                DAOFactory.instance().getEad3DAO().update(ead3);
            } else {
                LOG.info("The file " + filename + " is not valid");
                if (eacCpfTempFile.exists()) {
                    try {
                        FileUtils.forceDelete(eacCpfTempFile);
                    } catch (IOException e) {
                        LOG.error(e.getMessage(), e);
                    }
                }
            }

        } catch (JAXBException | APEnetException | SAXException ex) {
            LOG.debug("Ead3 to Eac-CPF store exception ", ex);
        }
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
                    // the title is formed by "surname, firstname patronymic"
                    StringBuilder surname = new StringBuilder();
                    StringBuilder firstname = new StringBuilder();
                    StringBuilder patronymic = new StringBuilder();
                    for (Part part : parts) {
                        if (part.getLocalType().equals("persname") || part.getLocalType().equals("famname") || part.getLocalType().equals("corpname")) {
                            builderTitle.append(part.getContent());
                        }
                        if (part.getLocalType().equals("surname")) {
                            if (surname.length() != 0) {
                                surname.append(" ");
                            }
                            surname.append(part.getContent());
                        }
                        if (part.getLocalType().equals("firstname")) {
                            if (firstname.length() != 0) {
                                firstname.append(" ");
                            }
                            firstname.append(part.getContent());
                        }
                        if (part.getLocalType().equals("patronymic")) {
                            if (patronymic.length() != 0) {
                                patronymic.append(" ");
                            }
                            patronymic.append(part.getContent());
                        }
                    }
                    if (builderTitle.length() == 0) {
                        // build the title
                        builderTitle.append(surname);
                        if (builderTitle.length() > 0 && ((firstname != null && firstname.length() > 0) || (patronymic != null && patronymic.length() > 0))) {
                            builderTitle.append(", ");
                        }
                        builderTitle.append(firstname);
                        if (builderTitle.length() != 0) {
                            builderTitle.append(" ");
                        }
                        builderTitle.append(patronymic);
                    }
                    title = builderTitle.toString();
                }
                //issue 1442
                if (title.trim().isEmpty()) {
                    for (int i = 0; i < parts.size(); i++) {
                        builderTitle.append(parts.get(i).getContent());
                        if (i < (parts.size() - 1)) {
                            builderTitle.append(", ");
                        }
                    }
                    title = builderTitle.toString();
                }
            }

        } catch (Exception e) {
            title = "";
        }

        return title;
    }

    public boolean validateFile(File tempFile) throws APEnetException, SAXException, IOException {
        boolean result = true;
        Xsd_enum schema = Xsd_enum.XSD_APE_EAC_SCHEMA;
        InputStream in = null;
        try {
            in = new FileInputStream(tempFile);
            List<SAXParseException> exceptions = DocumentValidation.xmlValidation(in, schema);
            if (exceptions != null && !exceptions.isEmpty()) {
                StringBuilder warn;
                for (SAXParseException exception : exceptions) {
                    warn = new StringBuilder();
                    warn.append("l.").append(exception.getLineNumber()).append(" c.")
                            .append(exception.getColumnNumber()).append(": ").append(exception.getMessage())
                            .append("<br/>");
                    warnings_ead.add(warn.toString());
                }
                result = false;
                throw exceptions.get(0);
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
        return result;
    }

    public Map getParameters() {
        return parameters;
    }

    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getEacDaoId() {
        return eacDaoId;
    }

    public void setEacDaoId(String eacDaoId) {
        this.eacDaoId = eacDaoId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getFileToLoad() {
        return fileToLoad;
    }

    public void setFileToLoad(int fileToLoad) {
        this.fileToLoad = fileToLoad;
    }

    public List<String> getWarnings_ead() {
        return warnings_ead;
    }

    public void setWarnings_ead(List<String> warnings_ead) {
        this.warnings_ead = warnings_ead;
    }

    public int getAiId() {
        return aiId;
    }

    public void setAiId(int aiId) {
        this.aiId = aiId;
    }

    public String getEad3Identifier() {
        return ead3Identifier;
    }

    public void setEad3Identifier(String ead3Identifier) {
        this.ead3Identifier = ead3Identifier;
    }

}
