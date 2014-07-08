package eu.apenet.dashboard.manual.eaccpf.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.eaccpf.CreateEacCpf;
import eu.apenet.dashboard.manual.eag.utils.ParseEag2012Errors;
import eu.apenet.dpt.utils.eaccpf.EacCpf;
import eu.apenet.dpt.utils.eaccpf.Identity;
import eu.apenet.dpt.utils.eaccpf.Part;
import eu.apenet.dpt.utils.eaccpf.namespace.EacCpfNamespaceMapper;
import eu.apenet.dpt.utils.service.DocumentValidation;
import eu.apenet.dpt.utils.util.Xsd_enum;
import eu.apenet.persistence.dao.EacCpfDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.ValidatedState;

/**
 *
 * @author papp
 */
public class StoreEacCpfAction extends EacCpfAction {
	/**
	 * Serializable.
	 */
	private static final long serialVersionUID = -2496853052970461291L;
	// Log.
	private final static Logger LOG = Logger.getLogger(StoreEacCpfAction.class);

	// Constants for the results.
	private static final String EXIT_CONTENT_MANAGER = "exitContentManager";
	private static final String EXIT_DASHBOARD_HOME = "exitDashboardHome";

	// Constants for the possible return pages.
	private static final String RETURN_CONTENT_MANAGER = "contentmanager";
//	private static final String RETURN_DASHBOARD_HOME = "dashboardHome";

	// Constants for the possible actions.
	private static final String EXIT = "exit";
	private static final String SAVE = "save";
	private static final String SAVE_EXIT = "save_exit";

	// Constants for encoding.
	private static final String UTF8 = "UTF-8";

    private List<String> warnings_ead = new ArrayList<String>();

    // Variable for the ids of the file and the eac dao.
    private String fileId;
    private String eacDaoId;

    // Variable for the desired actions.
    private String returnPage;
    private String saveOrExit;

    @Override
    public String execute() throws Exception {
    	String result = ERROR;
    	// Checks the selected  option.
    	if (StoreEacCpfAction.SAVE.equalsIgnoreCase(this.getSaveOrExit())) {
    		// TODO: issue 1223, for complete the reload when save first needed the edit
    		// of an apeEAC-CPF will be implemented.
    		// Save the contents.
//    		result = storeApeEacCpf();

    		// Define the params for the response.
    		getServletRequest().setCharacterEncoding(UTF8);
			getServletResponse().setCharacterEncoding(UTF8);
			getServletResponse().setContentType("application/json");
			Writer writer = new OutputStreamWriter(getServletResponse().getOutputStream(), UTF8);

    		// Save the contents.
    		result = storeApeEacCpf();

    		StringBuilder buffer = new StringBuilder();
    		if (result.equalsIgnoreCase(SUCCESS)) {
    			buffer.append("{\"fileId\":\"" + this.getFileId() + "\",");
    			buffer.append("\"eacDaoId\":\"" + this.getEacDaoId() + "\"}");
    		} else {
    			buffer.append("{\"error\":\"" + getText("content.message.error") + "\"}");
    		}

    		writer.write(buffer.toString());

    		try{
				if (writer != null) {
					writer.flush();
					writer.close();
				}
			}catch(IOException e){
				LOG.error(e.getMessage(), e);
			}
    	} else if (StoreEacCpfAction.EXIT.equalsIgnoreCase(this.getSaveOrExit())) {
    		// Checks the return page.
    		if (StoreEacCpfAction.RETURN_CONTENT_MANAGER.equalsIgnoreCase(this.getReturnPage())) {
    			result = StoreEacCpfAction.EXIT_CONTENT_MANAGER;
    		} else {
    			result = StoreEacCpfAction.EXIT_DASHBOARD_HOME;
    		}
    	} else if (StoreEacCpfAction.SAVE_EXIT.equalsIgnoreCase(this.getSaveOrExit())) {
    		// Save the contents.
    		result = storeApeEacCpf();

    		// Checks the return page.
    		if (result.equalsIgnoreCase(SUCCESS)
    				&& StoreEacCpfAction.RETURN_CONTENT_MANAGER.equalsIgnoreCase(this.getReturnPage())) {
    			result = StoreEacCpfAction.EXIT_CONTENT_MANAGER;
    		} else if (result.equalsIgnoreCase(SUCCESS)) {
    			result = StoreEacCpfAction.EXIT_DASHBOARD_HOME;
    		}
    	}

    	return result;
    }

    private String storeApeEacCpf() throws Exception {
        String countryCode = new ArchivalLandscape().getmyCountry();
        String basePath = APEnetUtilities.FILESEPARATOR + countryCode + APEnetUtilities.FILESEPARATOR
                + this.getAiId() + APEnetUtilities.FILESEPARATOR + "EAC-CPF" + APEnetUtilities.FILESEPARATOR;

        CreateEacCpf creator = new CreateEacCpf(getServletRequest(), getAiId(), Integer.parseInt(this.getEacDaoId()));
        EacCpf eac = creator.getEacCpf();
//        String filename = eac.getControl().getRecordId().getValue() + ".xml";

        String filename = "";
        if (this.getFileId() == null || this.getFileId().isEmpty()) {
        	filename = APEnetUtilities.convertToFilename(creator.getDatabaseEacCpf().getEncodedIdentifier());
        	this.setFileId(filename);
        } else {
        	filename = this.getFileId();
        }
    	filename = filename + ".xml";

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
            LOG.debug("Beginning EAC-CPF validation");
            if (validateFile(eacCpfTempFile)) {
            	LOG.info("EAC-CPF file is valid");

                // Move temp file to final file.
                File eacCpfFinalFile = new File((APEnetUtilities.getConfig().getRepoDirPath() + path));
                if (eacCpfFinalFile.exists()) {
                    try {
                        FileUtils.forceDelete(eacCpfFinalFile);
                    } catch (IOException e) {
                    	LOG.error(e.getMessage(), e);
                    }
                }
                FileUtils.moveFile(eacCpfTempFile, eacCpfFinalFile);
                if (eacCpfTempFile.exists()) {
                    try {
                        FileUtils.forceDelete(eacCpfTempFile);
                    } catch (IOException e) {
                    	LOG.error(e.getMessage(), e);
                    }
                }

                //update ddbb entry
                EacCpfDAO eacCpfDAO = DAOFactory.instance().getEacCpfDAO();
//                ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(getAiId());
//                eu.apenet.persistence.vo.EacCpf storedEacEntry = eacCpfDAO.getEacCpfByIdentifier(archivalInstitution.getRepositorycode(), "eac_" + archivalInstitution.getRepositorycode());
                eu.apenet.persistence.vo.EacCpf storedEacEntry = null;

                if (this.getEacDaoId() != null
                		&& !this.getEacDaoId().isEmpty()
                		&& Integer.valueOf(this.getEacDaoId()) > 0) {
//                	storedEacEntry = eacCpfDAO.findById(Integer.parseInt(this.getEacDaoId()));
                	storedEacEntry = eacCpfDAO.findById(Integer.valueOf(this.getEacDaoId()));
                } else {
                	storedEacEntry = creator.getDatabaseEacCpf();
                }
                storedEacEntry.setTitle(getTitleFromFile(eac));
                storedEacEntry.setUploadDate(new Date());
                storedEacEntry.setPath(path);
                UploadMethod uploadMethod = new UploadMethod();
                uploadMethod.setMethod(UploadMethod.HTTP);
                uploadMethod.setId(3);
                storedEacEntry.setUploadMethod(uploadMethod);
                storedEacEntry.setIdentifier(eac.getControl().getRecordId().getValue());
                storedEacEntry.setValidated(ValidatedState.VALIDATED);
                eacCpfDAO.update(storedEacEntry);

                this.setEacDaoId(Integer.toString(storedEacEntry.getId()));
            } else {
            	LOG.warn("The file " + filename + " is not valid");
                for (int i = 0; i < warnings_ead.size(); i++) {
                    String warning = warnings_ead.get(i).replace("<br/>", "");
                    LOG.debug(warning);
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
        	LOG.error(jaxbe.getMessage(), jaxbe);
        } catch (APEnetException e) {
        	LOG.error(e.getMessage(), e);
        } catch (SAXException e) {
        	LOG.error(e.getMessage());
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

	/**
	 * @return the fileId
	 */
	public String getFileId() {
		return this.fileId;
	}

	/**
	 * @param fileId the fileId to set
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	/**
	 * @return the eacDaoId
	 */
	public String getEacDaoId() {
		return this.eacDaoId;
	}

	/**
	 * @param eacDaoId the eacDaoId to set
	 */
	public void setEacDaoId(String eacDaoId) {
		this.eacDaoId = eacDaoId;
	}

	/**
	 * @return the returnPage
	 */
	public String getReturnPage() {
		return this.returnPage;
	}

	/**
	 * @param returnPage the returnPage to set
	 */
	public void setReturnPage(String returnPage) {
		this.returnPage = returnPage;
	}

	/**
	 * @return the saveOrExit
	 */
	public String getSaveOrExit() {
		return this.saveOrExit;
	}

	/**
	 * @param saveOrExit the saveOrExit to set
	 */
	public void setSaveOrExit(String saveOrExit) {
		this.saveOrExit = saveOrExit;
	}
}
