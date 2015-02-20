package eu.apenet.dashboard.actions.ajax;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.opensymphony.xwork2.Action;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.NotAuthorizedException;
import eu.apenet.dashboard.manual.EditParser;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.services.ead.LinkingService;
import eu.apenet.dashboard.services.ead.xml.ReconstructEadFile;
import eu.apenet.dashboard.services.ead.xml.stream.XmlEadParser;
import eu.apenet.persistence.dao.HgSgFaRelationDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HgSgFaRelation;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * <p>
 * Main class used to process the edition of the apeEAD files, from display
 * the structure an editable fields of the file to save the changes that the
 * user make.
 * </p>
 * <p>
 * The only editable types of apeEADs are the <i>"Finding aids"</i>, the 
 * <i>"Holdings guides"</i> and the <i>"Source guides"</i> are not editable.
 * </p><br/>
 * <p>
 * User: Yoann Moranville
 * </p>
 * <p>
 * Date: 3/10/11
 * </p><br/>
 *
 * @author Yoann Moranville
 */
public class EditEadAction extends AjaxControllerAbstractAction {
	/**
	 * <p>
	 * Constant for the interface {@link Serializable}.
	 * </p>
	 *
	 * @see Serializable
	 */
	private static final long serialVersionUID = 4831971826309950250L;
    // Log.
	/**
	 * <p>
	 * Constant for the class to use for the log.
	 * </p>
	 */
    private final Logger log = Logger.getLogger(EditEadAction.class);

	// Constants for the names of only editable elements.
//	private static final String ARCHDESC_LEVEL = "archdesc_level"; // Name of attibute "@level" in element <archdesc>.
    /**
	 * <p>
     * Constant for the name of the editable field of the element
     * <b>{@code <titleproper>}</b>.
	 * </p>
     */
	private static final String TITLEPROPER = "titleproper"; // Name of element <titleproper>.

	// Constants for the names of editable and appendable elements.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * element <b>{@code <eadid>}</b>.
	 * </p>
     */
	private static final String EADID = "eadid"; // Name of element <eadid>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * attribute <b>{@code @countrycode}</b> of the element
     * <i>{@code <eadid>}</i>.
	 * </p>
     */
	private static final String EADID_COUNTRYCODE = "eadid_countrycode"; // Name of attibute "@countrycode" in element <eadid>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * attribute <b>{@code @mainagencycode}</b> of the element
     * <i>{@code <eadid>}</i>.
	 * </p>
     */
	private static final String EADID_MAINAGENCYCODE = "eadid_mainagencycode"; // Name of attibute "@mainagencycode" in element <eadid>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * element<b>{@code <language>}</b>.
	 * </p>
     */
	private static final String LANGUAGE = "language"; // Name of element <language>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * attribute <b>{@code @langcode}</b> of the element
     * <i>{@code <language>}</i>.
	 * </p>
     */
	private static final String LANGUAGE_LANGCODE = "language_langcode"; // Name of attibute "@langcode" in element <language>.
    /**
	 * <p>
     * Constant for the name of the editable and appendable field of the
     * attribute <b>{@code @normal}</b> of the element
     * <i>{@code <unitdate>}</i>.
	 * </p>
     */
	private static final String UNITDATE_NORMAL = "unitdate_normal"; // Name of attibute "@normal" in element <unitdate>.

	// Constants for the names of the buttons for the appendable elements.
    /**
	 * <p>
     * Constant for the name of button for the editable and appendable
     * attribute <b>{@code @normal}</b> of the element
     * <i>{@code <unitdate>}</i>.
	 * </p>
     */
	private static final String BTN_UNITDATE_NORMAL = "btn_unitdate_normal"; // Name of the button to add attibute "@normal" in element <unitdate>.

	// Constants for the names used.
	/**
	 * <p>
	 * Constant for the name of the identifier of the <b>{@code <c>}</b>
	 * element.
	 * </p>
	 */
	private static final String C_LEVEL_ID = "id"; // Type of XML.
	/**
	 * <p>
	 * Constant for the name of the identifier of the file (the apeEAD) in the
	 * database.
	 * </p>
	 */
	private static final String FILEID = "fileId"; // Key for the identifier of the EAD in DB.
	/**
	 * <p>
	 * Constant for the name of the map, which is passed form the client side
	 * to the server side, that contains all the edited, added and non-modified
	 * information to save.
	 * </p>
	 */
	private static final String FORM_NAME = "'formValues'"; // Name of the map with all the values.
	/**
	 * <p>
	 * Constant for the name of the element which store the type of the XML.
	 * </p>
	 */
	private static final String XMLTYPEID = "xmlTypeId"; // Type of XML.

	// Variables.
	/**
	 * <p>
	 * Variable to store the identifier, in database, of the apeEAD.
	 * </p>
	 */
	private Long id;
	/**
	 * <p>
	 * Variable to store the identifier, in database, of the finding aid which
	 * should be edited.
	 * </p>
	 */
	private Long faId;
	/**
	 * <p>
	 * Variable to store the identifier, in database, of the finding aid which
	 * should be edited.
	 * </p>
	 */
	private Long hgId;
	/**
	 * <p>
	 * Variable to store the identifier, in database, of the level which should
	 * be edited.
	 * </p>
	 */
	private Integer fileId;
	/**
	 * <p>
	 * Variable to store the type of the XML which should be edited.
	 * </p><br/>
	 * <p>
	 * Possible values are:
	 * </p>
	 * <p>
	 *  <ul>
	 *   <li><b>0</b> - Finding Aid.</li>
	 *   <li><b>1</b> - Holdings Guide.</li>
	 *   <li><b>2</b> - EAC-CPF. <b>Note</b>: <i>this is not an apeEAD type</i>.</li>
	 *   <li><b>3</b> - Source Guide.</li>
	 *  </ul>
	 * </p>
	 */
	private int xmlTypeId;
	/**
	 * <p>
	 * Variable to store all the edited, added and non-modified
	 * information in the client side.
	 * </p>
	 */
	private Map<String, String> formValues;
	/**
	 * <p>
	 * Variable to store the edition type of the XML.<br/>
	 * </p>
	 * <p>
	 * Possible values are:
	 * </p>
	 * <p>
	 *  <ul>
	 *   <li><b>thisLevel</b> - Enables the edition only for the elements of the
	 *   	current level.</li>
	 *   <li><b>lowLevels</b> - Enables the edition only for the elements of the
	 *   	lower levels.</li>
	 *   <li><b>allLevels</b> - Enables the edition of all possible elements.</li>
	 *  </ul>
	 * </p>
	 */
	private String type;
	/**
	 * <p>
	 * Variable to store the value of the element <b>{@code <eadid>}</b> in
	 * case the user has changed the identifier of the apeEAD.
	 * </p>
	 */
	private String chagedEADID;

	/* GETTERS & SETTERS. */

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFaId() {
		return this.faId;
	}

	public void setFaId(Long faId) {
		this.faId = faId;
	}

	public Long getHgId() {
		return this.hgId;
	}

	public void setHgId(Long hgId) {
		this.hgId = hgId;
	}

	public int getXmlTypeId() {
		return this.xmlTypeId;
	}

	public void setXmlTypeId(int xmlTypeId) {
		this.xmlTypeId = xmlTypeId;
	}

	public Map<String, String> getFormValues() {
		return this.formValues;
	}

	public void setFormValues(Map<String, String> formValues) {
		this.formValues = formValues;
	}

	public String getType(){
		return this.type;
	}

	public void setType(String type){
		this.type = type;
	}


	public Integer getFileId() {
		return this.fileId;
	}

	/**
	 * @return the chagedEADID
	 */
	public String getChagedEADID() {
		return this.chagedEADID;
	}

	/**
	 * @param chagedEADID the chagedEADID to set
	 */
	public void setChagedEADID(String chagedEADID) {
		this.chagedEADID = chagedEADID;
	}

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}

	/**
	 * <p>
	 * Override the method to build the correct breadcrumb for this section.
	 * </p>
	 * <p>
	 * Home - {@code <Institution's name>} - Content manager - Edit EAD files
	 * </p>
	 */
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb("contentmanager.action", getText("breadcrumb.section.contentmanager"));
		addBreadcrumb(getText("breadcrumb.section.editionofEAD"));
	}

	/**
	 * <p>
	 * Override the method to execute the action.
	 * </p>
	 *
	 * @return {@link String} {@link Action#SUCCESS}
	 *
	 * @see com.opensymphony.xwork2.ActionSupport#execute()
	 */
	@Override
	public String execute() {
		return SUCCESS;
	}

	/**
	 * <p>
	 * This method is called thought the Struts action <i>"editEadCreateDbEntries"</i>.
	 * </p>
	 * <p>
	 * It is used to create the needed structure in the database which
	 * represents the contents of the apeEAD file, if they are not yet
	 * created.
	 * </p>
	 * <p>
	 * If it's already created, or has any problem during the creation time the
	 * user could see an appropriate message.
	 * </p><br/>
	 * <p>
	 * Possible messages:
	 * </p>
	 * <p>
	 *  <ul>
	 *   <li>When type of the document is not a FA or HG - No XML type defined
	 *  	or wrong type; it should be either EAD_FA or EAD_HG ? other types
	 *  	not working yet</li>
	 *   <li>When the apeEAD is published - The file state of the finding aid
	 * 		is not compliant with the edition action</li>
	 *   <li>When the apeEAD is a FA and is converted to Europeana - The file
	 * 		state of the finding aid is not compliant with the edition action</li>
	 *  </ul>
	 * </p><br/>
	 * <p>
	 * The response will be send as an {@link String} to the client after
	 * finishing the actions.
	 * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
	 *
	 * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
	 *
	 * @see XmlEadParser#parseEad(Ead)
	 * 
	 */
	public String createDbEntries(){
		this.log.debug("Entering method \"createDbEntries\".");

        XmlType xmlType = XmlType.getType(xmlTypeId);
        LOG.trace("Identifier of EAD: " + id + ", is XmlType: " + xmlType.getName());
        Writer writer = null;
        try {
            writer = openOutputWriter();

            if(xmlType != XmlType.EAD_FA && xmlType != XmlType.EAD_HG)
                throw new APEnetException(getText("editEadAction.noXmlTypedefinedOrWrongType"));

            Ead ead = DAOFactory.instance().getEadDAO().findById(id.intValue(), xmlType.getClazz());

            if(ead.isPublished())
                throw new APEnetException(getText("editEadAction.faStateNotCompliant"));
            if (ead instanceof FindingAid){
            	if (!EuropeanaState.NOT_CONVERTED.equals(((FindingAid) ead).getEuropeana())){
            		throw new APEnetException(getText("editEadAction.faStateNotCompliant"));
            	}
            }
            XmlEadParser.parseEad(ead);

            writer.append(new JSONObject().put("dbEntriesCreated", true).toString());
            writer.close();

        } catch (Exception e){
            LOG.error("Error", e);
            try {
                if(writer != null){
                    writer.append(new JSONObject().put("dbEntriesCreated", false).put("errorMsg", e.getMessage()).toString());
                    writer.close();
                }
            } catch (Exception ex){
                LOG.error("Could not provide the action with any data", ex);
            }
        }

        this.log.debug("Leaving method \"createDbEntries\".");		

        return null;
    }


	/**
	 * <p>
	 * This method is called thought the Struts action <i>"editEadXml"</i>.
	 * </p>
	 * <p>
	 * It is used to recover the information in database and construct the
	 * navigation tree for the current level (include its brothers).
	 * </p>
	 * <p>
	 * The identifier of the level could be found on {@link EditEadAction#id},
	 * if is the root level, or in {@link EditEadAction#fileId} in case of a
	 * lower level.
	 * </p>
	 * <p>
	 * The response will be send as an {@link String} to the client after
	 * convert the XML in a HTML page.
	 * </p>
	 *
	 * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
	 *
	 * @see EditParser#xmlToHtml(CLevel, EadContent)
	 */
    public String getXmlData() {
    	this.log.debug("Entering method \"getXmlData\".");

        XmlType xmlType = XmlType.getType(xmlTypeId);
        Writer writer = null;
        try {
            writer = openOutputWriter();

            JSONObject obj = new JSONObject();
            LOG.trace("id: " + id + ", faId: " + faId + ", hgId: " + hgId + ", xmlType: " + xmlType.getName());

            if(id != null && id != -1){
                CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(id);
                if(cLevel != null)
                    obj.put("xml", new EditParser().xmlToHtml(cLevel, null));
            } else if(fileId != null && fileId != -1 && xmlType != null){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFileId(fileId, xmlType.getEadClazz());
                if(eadContent != null)
                    obj.put("xml", new EditParser().xmlToHtml(null, eadContent));
            }
            writer.append(obj.toString());
            writer.close();
        } catch (Exception e){
            LOG.error("EXCEPTION", e);
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException ioe){}
        }

        this.log.debug("Leaving method \"getXmlData\".");

        return null;
    }

    //todo before production: Sanitize user input
    /**
     * <p>
     * This method is called thought the Struts action <i>"editEadXmlSaveLevel"</i>.
     * </p>
     * <p>
     * It is used to check the data filled by the user in the form and save it,
     * when needed.
     * </p><br/>
     * <p>
	 * This action could result in the next possible messages:
	 * </p>
	 * <p>
	 *  <ul>
	 *   <li>When the data is not received in the server side - No changes have
	 * 		been saved.</li>
	 *   <li>When an error occurs when saving the information in the XML file -
	 * 		No changes have been saved.</li>
	 *   <li>When an error occurs when saving the information in database -
	 * 		Error while updating the changes, please try again.</li>
	 *   <li>When no changes in the information is detected - No changes have
	 * 		been made.</li>
	 *   <li>When no error occurs and all is saved correctly - All your changes
	 * 		have been saved successfully.</li>
	 *  </ul>
	 * </p><br/>
	 * <p>
     * The response will be send as an {@link String} to the client after
	 * finishing the actions.
	 * </p>
     *
     * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
     *
     * @see EditEadAction#recoverMapFormValues()
     * @see EditParser#getNewXmlString(String, Map)
     * @see LinkingService#linkWithHgOrSg(Ead)
     * @see EditEadAction#saveAllXmlData()
     */
    public String saveXmlData() {
    	this.log.debug("Entering method \"saveXmlData\".");

        Writer writer = null;
        try {
            XmlType xmlType = XmlType.getType(this.getXmlTypeId());
            writer = openOutputWriter();

        	// Check if its necessary to recover formValues.
        	if (this.getFormValues() == null || this.getFormValues().isEmpty()) {
        		log.debug("Trying to recover the editable values.");
        		if (this.recoverMapFormValues().equalsIgnoreCase(Action.ERROR)) {
        			try {
                        if(writer != null){
                        	JSONObject jsonObject = new JSONObject();
                        	jsonObject.put("saved", false);
                        	jsonObject.put("savedText", getText("dashboard.editead.errorNoChanges"));
                            writer.append(jsonObject.toString());
                            writer.close();

                            this.log.debug("Leaving method \"saveXmlData\" when no data is received.");

                            return null;
                        }
                    } catch (Exception ex){
                        LOG.error("Could not send the JSON to the page...");
                    }
        		}
        	}

        	boolean dataChanged = false;
        	// Save the previous values in order to revert if an error occurs.
        	String initialXML = "";
        	String eadid = "";

            if(this.getId() != null){
                CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(this.getId());
                initialXML = cLevel.getXml();
                String newXml = new EditParser().getNewXmlString(cLevel.getXml(), this.getFormValues());
                cLevel.setXml(newXml);

                if (!newXml.equalsIgnoreCase(initialXML)) {
                    dataChanged = true;
                }
                JpaUtil.beginDatabaseTransaction();
                DAOFactory.instance().getCLevelDAO().update(cLevel);
                JpaUtil.commitDatabaseTransaction();
            } else if(xmlType == XmlType.EAD_FA){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(this.getFaId().intValue());
                initialXML = eadContent.getXml();
                eadid = eadContent.getEadid();
                String newXml = new EditParser().getNewXmlString(eadContent.getXml(), this.getFormValues());
                eadContent.setXml(newXml);
                //Check if <eadid> has been changed.
                if (this.getChagedEADID() != null && !this.getChagedEADID().isEmpty()) {
                	if (!this.getChagedEADID().equals(eadid)) {
    					eadContent.setEadid(this.getChagedEADID());
    					FindingAid findingAid = DAOFactory.instance().getFindingAidDAO().findById(this.getFaId().intValue());
    					findingAid.setEadid(this.getChagedEADID());

    					// Check if the current EAD is related with a HG or SG.
    					// Try to remove the relation between FA and HG or SG.
    					Set<HgSgFaRelation> hgSgFaRelationsSet = findingAid.getHgSgFaRelations();
    					if (hgSgFaRelationsSet != null
    							&& !hgSgFaRelationsSet.isEmpty()) {
    						HgSgFaRelationDAO hgSgFaRelationDAO = DAOFactory.instance().getHgSgFaRelationDAO();
							hgSgFaRelationDAO.delete(hgSgFaRelationsSet);
    					}

    					// Try to add the relation between FA and HG or SG.
    					Ead ead = (Ead) findingAid;
    					LinkingService.linkWithHgOrSg(ead);

    					dataChanged = true;
                	}
                }
                if (!newXml.equalsIgnoreCase(initialXML)) {
                    dataChanged = true;
                }
                JpaUtil.beginDatabaseTransaction();
                DAOFactory.instance().getEadContentDAO().update(eadContent);
                JpaUtil.commitDatabaseTransaction();
            }

            // Try to persist the changes in file (if needed).
            JSONObject jsonObject = new JSONObject();
            if (dataChanged) {
            	if (Action.ERROR.equalsIgnoreCase(this.saveAllXmlData())) {
					// Revert the changes.
					JpaUtil.beginDatabaseTransaction();
					if(this.getId() != null){
						CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(this.getId());
						cLevel.setXml(initialXML);
	
						DAOFactory.instance().getCLevelDAO().update(cLevel);
					} else if(xmlType == XmlType.EAD_FA){
						EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(this.getFaId().intValue());
						eadContent.setXml(initialXML);
						eadContent.setEadid(eadid);

						DAOFactory.instance().getEadContentDAO().update(eadContent);
					}

					JpaUtil.commitDatabaseTransaction();

                	jsonObject.put("saved", false);
                	jsonObject.put("savedText", getText("dashboard.editead.errorNoChanges"));

                	this.log.debug("Leaving method \"saveXmlData\" when an error occurs saving the information in the XML file.");
            	} else {
                    jsonObject.put("saved", true);
                    jsonObject.put("savedText", getText("dashboard.editead.correctSaved"));

                    this.log.debug("Leaving method \"saveXmlData\" when all is correcty saved.");
            	}
            } else {
            	jsonObject.put("saved", true);
                jsonObject.put("savedText", getText("dashboard.editead.notChanged"));

                this.log.debug("Leaving method \"saveXmlData\" when there is no changes.");
            }

            writer.append(jsonObject.toString());
            writer.close();
        } catch (Exception e){
            try {
                if(writer != null){
                	JSONObject jsonObject = new JSONObject();
                	jsonObject.put("saved", false);
                	jsonObject.put("savedText", getText("dashboard.editead.errorSavingToDatabase"));
                	writer.append(jsonObject.toString());
                    writer.close();
                }
            } catch (Exception ex){
                LOG.error("Could not send the JSON to the page...");
            }

            JpaUtil.rollbackDatabaseTransaction();
            LOG.error("EXCEPTION", e);

            this.log.debug("Leaving method \"saveXmlData\" when an error occurs saving the information in database.");
        }
        return null;
    }

    /**
     * <p>
     * This method is called from <i>"{@link EditEadAction#saveXmlData()}"</i>.
     * </p>
     * <p>
     * It is used to recover the data sent from the client side to the server
     * side in a map structure under the parameter name <i>"formValues"</i>.
     * </p>
     * <p>
     * This is derived from 
     * <a href="https://redmine.archivesportaleurope.net/issues/180" target="_blank">issue #180</a>
     * and bug in Struts2 (see:
     * <a href="https://issues.apache.org/jira/browse/WW-4176" target="_blank">Bug #4176</a>).
     * </p><br/>
     * <p>
     * The values which are recovered from the map are as follows:
     * </p>
     * <p>
     *  <ul>
     *   <li><b>xmlTypeId</b> - Value for {@link EditEadAction#xmlTypeId}</li>
     *   <li><b>id</b> - Value for {@link EditEadAction#id}</li>
     *   <li><b>fileId</b> - Value for {@link EditEadAction#fileId}</li>
     *   <li><b>eadid_countrycode</b> - {@link Set}{@code <}{@link String}{@code >}
     *   	of the values for attribute <b>{@code @countrycode}</b> of the
     *   	element <i>{@code <eadid>}</i>.</li>
     *   <li><b>eadid_mainagencycode</b> - {@link Set}{@code <}{@link String}{@code >}
     *   	of the values for attribute <b>{@code @mainagencycode}</b> of the
     *   	element <i>{@code <eadid>}</i>.</li>
     *   <li><b>eadid</b> - {@link Set}{@code <}{@link String}{@code >} of the
     *   	values for element <b>{@code <eadid>}</b>.</li>
     *   <li><b>language_langcode</b> - {@link Set}{@code <}{@link String}{@code >}
     *   	of the values for attribute <b>{@code @langcode}</b> of the element
     *   	<i>{@code <language>}</i>.</li>
     *   <li><b>language</b> - {@link Set}{@code <}{@link String}{@code >} of
     *   	the values for element<b>{@code <language>}</b>.</li>
     *   <li><b>titleproper</b> - {@link Set}{@code <}{@link String}{@code >}
     *   	of the values for element <b>{@code <titleproper>}</b>.</li>
     *   <li><b>unitdate_normal</b> and/or <b>btn_unitdate_normal</b> -
     * 		{@link Set}{@code <}{@link String}{@code >} of the values for
     * 		attribute <b>{@code @normal}</b> of the element <i>{@code <unitdate>}</i>.</li>
     *  </ul>
     * </p>
     * 
     * @return
     * <p>
     * {@link String} which represent the result of the process.
     * </p>
     * <p>
     * Could be one of the follows:
     * </p>
     * <p>
     *  <ul>
     *   <li><b>{@link Action#SUCCESS}</b> - If all the data is correctly
     * 		recovered.</li>
     *   <li><b>{@link Action#ERROR}</b> - If there is some problem in the
     * 		process of recover the data.</li>
     *  </ul>
     * </p>
     *
     * @see EditEadAction#ensureOrder(Set)
     * @see EditEadAction#ensureOrderLanguage(Set)
     * @see EditEadAction#addValuesToMap(JSONObject, Set)
     */
    private String recoverMapFormValues() {
    	this.log.debug("Entering method \"recoverMapFormValues\".");

    	this.log.debug("Starting process to recover values to change.");
    	String result = Action.SUCCESS;
		this.setFormValues(new LinkedHashMap<String, String>());
		String requestedData = getServletRequest().getParameter(EditEadAction.FORM_NAME);
		try {
			JSONObject jsonObject = new JSONObject(requestedData);
			log.debug("JSONObject recovered.");

			// Sets for different elements.
			log.debug("Start the process to recover all the information in the JSONObject.");
			Set<String> xmlTypeSet = new LinkedHashSet<String>();
			Set<String> idSet = new LinkedHashSet<String>();
			Set<String> faIdSet = new LinkedHashSet<String>();
//			Set<String> archdescLevelSet = new LinkedHashSet<String>();
			Set<String> cLevelSet = new LinkedHashSet<String>();
			Set<String> eadIdSet = new LinkedHashSet<String>();
			Set<String> eadIdCountryCodeSet = new LinkedHashSet<String>();
			Set<String> eadIdMainAgencyCodeSet = new LinkedHashSet<String>();
			Set<String> languageSet = new LinkedHashSet<String>();
			Set<String> languageLangCodeSet = new LinkedHashSet<String>();
			Set<String> titleProperSet = new LinkedHashSet<String>();
			Set<String> unitDateNormalSet = new LinkedHashSet<String>();

			Iterator<String> keysIt = jsonObject.keys();

			// Check iterator values.
			while (keysIt.hasNext()) {
				String key = keysIt.next();
				if (key.startsWith(EditEadAction.XMLTYPEID)) {
					// Keys for the type of the XML.
					xmlTypeSet.add(key);
				} else if (key.startsWith(EditEadAction.C_LEVEL_ID)) {
					// Keys for the identifier of the c-level.
					idSet.add(key);
				} else if (key.startsWith(EditEadAction.FILEID)) {
					// Keys for the identifier of the finding aid.
					faIdSet.add(key);
//				} else if (key.startsWith(EditEadAction.ARCHDESC_LEVEL)) {
//					// Keys for <archdesc@level>.
//					archdescLevelSet.add(key);
				} else if (key.startsWith(EditEadAction.EADID_COUNTRYCODE)) {
					// Keys for <eadid@countrycode>.
					eadIdCountryCodeSet.add(key);
				} else if (key.startsWith(EditEadAction.EADID_MAINAGENCYCODE)) {
					// Keys for <eadid@mainagencycode>.
					eadIdMainAgencyCodeSet.add(key);
				} else if (key.startsWith(EditEadAction.EADID)) {
					// Keys for <eadid@text>.
					eadIdSet.add(key);
				} else if (key.startsWith(EditEadAction.LANGUAGE_LANGCODE)) {
					// Keys for <language@langcode>.
					languageLangCodeSet.add(key);
				} else if (key.startsWith(EditEadAction.LANGUAGE)) {
					// Keys for <language>.
					languageSet.add(key);
				} else if (key.startsWith(EditEadAction.TITLEPROPER)) {
					// Keys for <titleproper>.
					titleProperSet.add(key);
				} else if (key.startsWith(EditEadAction.UNITDATE_NORMAL)
						|| key.startsWith(EditEadAction.BTN_UNITDATE_NORMAL)) {
					// Keys for <unitdate@normal>.
					unitDateNormalSet.add(key);
				}
			}

			// Ensure the elements are ordered in the lists.
//			archdescLevelSet = this.ensureOrder(archdescLevelSet);
			cLevelSet = this.ensureOrder(cLevelSet);
			eadIdSet = this.ensureOrder(eadIdSet);
			eadIdCountryCodeSet = this.ensureOrder(eadIdCountryCodeSet);
			eadIdMainAgencyCodeSet = this.ensureOrder(eadIdMainAgencyCodeSet);
			languageSet = this.ensureOrderLanguage(languageSet);
			languageLangCodeSet = this.ensureOrderLanguage(languageLangCodeSet);
			titleProperSet = this.ensureOrder(titleProperSet);
			unitDateNormalSet = this.ensureOrder(unitDateNormalSet);

			String value = "";
			String key = "";

			// Check if is setted the value of the "xmlTypeId".
			if (xmlTypeSet != null && !xmlTypeSet.isEmpty()) {
				key = xmlTypeSet.iterator().next();
				value = jsonObject.getString(key);
				try {
					if (value != null & !value.isEmpty()
							&& this.getXmlTypeId() != Integer.parseInt(value)) {
						this.setXmlTypeId(Integer.parseInt(value));
					}
				} catch (NumberFormatException nfe) {
					this.log.error("Error parsing to int the \"xmlTypeId\".");
					// Due to only finding aids should be editable set this
					// type by default.
					this.setXmlTypeId(XmlType.EAD_FA.getIdentifier());
				}
			}

			// Check if exists the identifier of the c level in DB.
			if (this.getId() == null) {
				if (idSet != null && !idSet.isEmpty()) {
					key = idSet.iterator().next();
					value = jsonObject.getString(key);
					if (value != null) {
						try {
							this.setId(Long.valueOf(value.toString()));
							this.log.debug("Recovered the identifier (in database) of the current c level (" + value + ").");
						} catch (NumberFormatException nfe) {
							this.log.error("Error parsing to long identifier (in database) of the c level (" + value + ").");
						}
					}
				}
			}

			// Check if exists the identifier of the EAD in DB.
			if (this.getFaId() == null) {
				if (faIdSet != null && !faIdSet.isEmpty()) {
					key = faIdSet.iterator().next();
					value = jsonObject.getString(key);
					if (value != null) {
						try {
							this.setFaId(Long.valueOf(value.toString()));
							this.log.debug("Recovered the identifier (in database) of the current EAD (" + value + ").");
						} catch (NumberFormatException nfe) {
							this.log.error("Error parsing to long identifier (in database) of the EAD (" + value + ").");
						}
					}
				}
			}

			// Add the rest of elements in the lists to the values map.
//			this.addValuesToMap(jsonObject, archdescLevelSet);
			this.addValuesToMap(jsonObject, cLevelSet);
			this.addValuesToMap(jsonObject, eadIdSet);
			this.addValuesToMap(jsonObject, eadIdCountryCodeSet);
			this.addValuesToMap(jsonObject, eadIdMainAgencyCodeSet);
			this.addValuesToMap(jsonObject, languageSet);
			this.addValuesToMap(jsonObject, languageLangCodeSet);
			this.addValuesToMap(jsonObject, titleProperSet);
			this.addValuesToMap(jsonObject, unitDateNormalSet);

			// Recover the value of the EADID.
			if (eadIdSet != null && !eadIdSet.isEmpty()) {
				Iterator<String> eadIdIt = eadIdSet.iterator();
				if (eadIdIt.hasNext()) {
					String eadIdValue = jsonObject.getString(eadIdIt.next());
		    		// Unescape char '.
		    		if (eadIdValue.contains("%27")) { 
		    			eadIdValue = eadIdValue.replaceAll("%27", "'");
		    		}
		    		// Unescape char <.
		    		if (eadIdValue.contains("%3C")) { 
		    			eadIdValue = eadIdValue.replaceAll("%3C", "<");
		    		}
		    		// Unescape char >.
		    		if (eadIdValue.contains("%3E")) { 
		    			eadIdValue = eadIdValue.replaceAll("%3E", ">");
		    		}
					this.setChagedEADID(eadIdValue);
				}
			}

    		this.log.debug("Ending process to recover values.");
		} catch (JSONException e) {
			this.log.error("Error processing JSON to recover form values.");
			if (e.getCause() != null) {
				this.log.error(e.getCause());
			}
			result = Action.ERROR;

			this.log.debug("Leaving the method \"recoverMapFormValues\" when an error occurs trying to recover the data.");
		}

		return result;
    }

    /**
     * <p>
     * This recursive method is called from itself and from the method
     * <i>"{@link EditEadAction#recoverMapFormValues()}"</i>.
     * </p>
     * <p>
     * It is used to ensure the elements in a {@link Set}{@code <}{@link String}{@code >}
     * are correctly sorted in ascending mode.
     * </p>
     *
     * @param elementSet {@link Set}{@code <}{@link String}{@code >} of
     * unsorted elements.
     *
     * @return {@link Set}{@code <}{@link String}{@code >} of sorted elements.
     */
    private Set<String> ensureOrder(Set<String> elementSet) {
    	log.debug("Starting the process to order the list.");
    	// New set with values correctly ordered.
    	Set<String> orderedSet = new LinkedHashSet<String>();

    	if (elementSet != null && !elementSet.isEmpty()) {
    		List<String> elementList = new ArrayList<String>(elementSet);
    		int size = elementList.size();
    		// Process the list.
    		for (int i = 0; i < size; i++) {
    			String current = elementList.get(0);
    			String compare;
    			if (elementList.size() > 1) {
    				// Compare the elements in the list with the current one.
    				for (int j = 1; j < elementList.size(); j++) {
        				compare = elementList.get(j);

        				int currentValue = Integer.parseInt(current.substring((current.lastIndexOf("_") + 1)));
        				int compareValue = Integer.parseInt(compare.substring((compare.lastIndexOf("_") + 1)));
        				// Replace the element if necessary.
        				if (currentValue > compareValue) {
        					log.debug("Replace current (" + current + ") with: " + compare);
        					current = compare;
        				}    					
    				}
    				// Remove the element from the list.
    				elementList.remove(current);
    			}
    			// Add the element to the ordered set.
    			orderedSet.add(current);
    		}
    	}

    	log.debug("Ending the process to order the list.");
    	return orderedSet;
    }

    /**
     * <p>
     * This recursive method is called from itself and from the method
     * <i>"{@link EditEadAction#recoverMapFormValues()}"</i>.
     * </p>
     * <p>
     * It is used to ensure the language elements in a
     * {@link Set}{@code <}{@link String}{@code >} are correctly sorted in
     * ascending mode.
     * </p>
     *
     * @param elementSet {@link Set}{@code <}{@link String}{@code >} of
     * unsorted language elements.
     *
     * @return {@link Set}{@code <}{@link String}{@code >} of sorted language
     * elements.
     */
    private Set<String> ensureOrderLanguage(Set<String> elementSet) {
    	log.debug("Starting the process to order the language list.");
    	// New set with values correctly ordered.
    	Set<String> orderedSet = new LinkedHashSet<String>();

    	if (elementSet != null && !elementSet.isEmpty()) {
    		List<String> elementList = new ArrayList<String>(elementSet);
    		int size = elementList.size();

    		if (size > 1) {
    			Map<Integer, Set<String>> valuesMap = new LinkedHashMap<Integer, Set<String>>();
				// Add the elements to the map.
				for (int i = 0; i < size; i++) {
					String currentElement = elementList.get(i);
					String tempCurrentElement = currentElement.substring(0, currentElement.lastIndexOf("_"));
					if (!currentElement.contains("hidden")) {
	    				int currentValue = Integer.parseInt(tempCurrentElement.substring((tempCurrentElement.lastIndexOf("_") + 1)));
	
						// Add the current element to the map.
						if (!valuesMap.isEmpty()) {
							Set<String> stringSet = valuesMap.get(currentValue);
							if (stringSet == null) {
								stringSet = new LinkedHashSet<String>();
							}
							stringSet.add(currentElement);
							valuesMap.put(currentValue, stringSet);
						} else {
							Set<String> stringSet = new LinkedHashSet<String>();
							stringSet.add(currentElement);
							valuesMap.put(currentValue, stringSet);
						}
					}
				}

				// The map has the values ordered by location.
				// Process the entries of the map.
				if (valuesMap != null && !valuesMap.isEmpty()) {
					Set<Integer> mapKeysSet = valuesMap.keySet();
					Iterator<Integer> mapKeysIt = mapKeysSet.iterator();
					while (mapKeysIt.hasNext()) {
						Integer currentKey = mapKeysIt.next();
						Set<String> currentSet = valuesMap.get(currentKey);
						if (currentSet != null && !currentSet.isEmpty()) {
							currentSet = this.ensureOrder(new HashSet<String>(currentSet));
						}
						valuesMap.put(currentKey, currentSet);
					}

					// The map has the values ordered by location and ordered inside the location.
					// Recover the values of the map and add to the final ordered set.
					List<Integer> mapKeysList = new LinkedList<Integer>(mapKeysSet);
					Collections.sort(mapKeysList);
					for (int i = 0; i < mapKeysList.size(); i++) {
						Iterator<String> currentValues = valuesMap.get(mapKeysList.get(i)).iterator();
						while (currentValues.hasNext()) {
							orderedSet.add(currentValues.next());
						}
					}
				}
    		} else {
    			orderedSet.add(elementList.get(0));
    		}
    	}

    	log.debug("Ending the process to order the list.");
    	return orderedSet;
    }

    /**
     * <p>
     * This method is called from <i>"{@link EditEadAction#recoverMapFormValues()}"</i>.
     * </p>
     * <p>
     * It is used to fill the entries in <i>"{@link EditEadAction#formValues}"</i>
     * map from the values in the <i>"{@link JSONObject}"</i> using the keys
     * passed in the parameter elementSet.
     * </p>
     *
     * @param jsonObject {@link JSONObject} object received from the client
     * which contains all the information and should be used to recover the
     * values using the keys passed in parameter elementSet.
     * @param elementSet {@link Set}{@code <}{@link String}{@code >} ordered
     * set of elements which represents the keys to recover the values in the
     * parameter jsonObject.
     *
     * @throws JSONException Exception while working with {@link JSONObject}.
     */
    private void addValuesToMap(JSONObject jsonObject, Set<String> elementSet) throws JSONException {
    	this.log.debug("Entering method \"addValuesToMap\".");

    	if (elementSet != null && !elementSet.isEmpty()) {
			Iterator<String> elementIt = elementSet.iterator();
			while (elementIt.hasNext()) {
				String key = elementIt.next();
				String value = jsonObject.getString(key);
				if (value != null) {
					this.getFormValues().put(key, value);
					this.log.debug("Recovered the new value (" + value + ") with key: \"" + key + "\".");
				}
			}
		}

    	this.log.debug("Leaving method \"addValuesToMap\".");
    }

    /**
     * <p>
     * This method is called thought the Struts action <i>"editEadXmlDeleteEntries"</i>.
     * </p>
     * <p>
     * It is used to deletes the entries of this Finding Aid in the EadContent and the CLevel tables.
     * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
     *
     * @return null since we use the HttpResponse to write JSON data directly
     * to the page.
     */
    public String deleteDatabaseEntries(){
//        Long faId = new Long(getServletRequest().getParameter("faId"));
//        Long hgId = new Long(getServletRequest().getParameter("hgId"));
//
//        ArchivalInstitution archivalInstitution;
//        if(faId != null)
//            archivalInstitution = DAOFactory.instance().getFindingAidDAO().findById(faId.intValue()).getArchivalInstitution();
//        else
//            archivalInstitution = DAOFactory.instance().getHoldingsGuideDAO().findById(hgId.intValue()).getArchivalInstitution();
//
//        try {
//            SecurityContext.get().checkAuthorized(archivalInstitution);
//            EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
//            EadContent oldEadContent;
//            if(faId != null)
//                oldEadContent = eadContentDAO.getEadContentByFindingAidId(faId.intValue());
//            else
//                oldEadContent = eadContentDAO.getEadContentByHoldingsGuideId(hgId.intValue());
//            if(oldEadContent != null)
//                eadContentDAO.delete(oldEadContent);
//            LOG.trace("DB entries erased!");
//        } catch (NotAuthorizedException e){
//            LOG.error("Not authorized to delete this FA...");
//        }
        return null;
    }

    /**
     * <p>
     * This method is called thought the Struts action <i>"editEadXmlSaveAll"</i>.
     * Also is called from <i>"{@link EditEadAction#saveXmlData()}"</i>.
     * </p>
     * <p>
     * It is used to save the new information of the EAD, which is currently
     * stored in database on <i>"ead_content"</i> and <i>"c_level"</i> tables,
     * to the file.
     * </p>
     * <p>
     * Also revert the file to the <b>not validated</b> status.
     * </p>
     * <p>
     * If any error is detected during the process, all the changes will be
     * reverted and an appropriate error message will be shown to the user.
     * </p>
     *
     * @return
     * <p>
     * {@link String} which represent the result of the process.
     * </p>
     * <p>
     * Could be one of the follows:
     * </p>
     * <p>
     *  <ul>
     *   <li><b>{@link Action#SUCCESS}</b> - If all the data is correctly
     * 		saved in the file and it is reverted to <b>not validated</b>
     * 		status.</li>
     *   <li><b>{@link Action#ERROR}</b> - If there is some problem in the
     * 		process of saving the file or changing the status.</li>
     *  </ul>
     * </p>
     *
     * @see SecurityContext#checkAuthorized(ArchivalInstitution)
     * @see ReconstructEadFile#reconstructEadFile(EadContent, String)
     */
    public String saveAllXmlData(){
    	this.log.debug("Entering method \"saveAllXmlData\".");

    	String result = Action.ERROR;
        ArchivalInstitution archivalInstitution = null;
        FindingAid findingAid = null;
        EadContent eadContent = null;
        XmlType xmlType = XmlType.getType(xmlTypeId);
        if(xmlType == XmlType.EAD_FA){
        	if(faId==null && fileId!=null){ //in case which faId is null the target value is into fileId
        		faId = fileId.longValue();
        	}
            findingAid = DAOFactory.instance().getFindingAidDAO().findById(faId.intValue());
            archivalInstitution = findingAid.getArchivalInstitution();
            eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
        } else {
        	return result;
        }

        Writer writer = null;
        try {
        	SecurityContext.get().checkAuthorized(archivalInstitution);

            try {
                writer = openOutputWriter();
                String filePath;
                filePath = APEnetUtilities.getConfig().getRepoDirPath() + findingAid.getPathApenetead();

                LOG.trace("FilePath of the FA/HG (faId: " + faId + ", hgId: " + hgId + ") that we are saving after editing: " + filePath);

                ReconstructEadFile.reconstructEadFile(eadContent, filePath);

                JpaUtil.beginDatabaseTransaction();
                if(ValidatedState.VALIDATED.equals(findingAid.getValidated())) {
                    findingAid.setValidated(ValidatedState.NOT_VALIDATED);
                	findingAid.setDynamic(false);
                }
                JpaUtil.commitDatabaseTransaction();

                result = Action.SUCCESS;

//                writer.append(new JSONObject().put("saved", true).toString());
//                writer.close();

    			this.log.debug("Leaving the method \"saveAllXmlData\" when no error occurs.");
            } catch (Exception e){
                LOG.error("ERROR", e);
                try {
                    if(writer != null){
                    	JSONObject jsonObject = new JSONObject();
                    	jsonObject.put("saved", false);
                    	jsonObject.put("savedText", getText("dashboard.editead.errorSavingToXML"));
                    	writer.append(jsonObject.toString());
                        writer.close();
                    }
                } catch (Exception ex){
                    LOG.error("Error closing the streams", ex);
                }

    			this.log.debug("Leaving the method \"saveAllXmlData\" when an error occurs saving the new information in the file or in database.");
            }
        } catch (NotAuthorizedException e){
            LOG.error("Not authorized...", e);

			this.log.debug("Leaving the method \"saveAllXmlData\" when an error occurs due to the user is not autorized to change the current file.");
        }
        return result;
    }

    /**
     * <p>
     * This method is called thought the Struts action <i>"editEadGetFields"</i>.
     * </p>
     * <p>
     * It is used to recover the possible editable fields and send it to the
     * client.
     * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
     *
     * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
     */
    public String getPossibleFieldEntries(){
    	this.log.debug("Entering method \"getPossibleFieldEntries\".");

        Writer writer = null;
        try {
            writer = openOutputWriter();

            if((id != null || faId != null || hgId != null) && type != null){
//                CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(id);
                if(type.equals("thisLevel")){
                    JSONObject child = new JSONObject();

                    for(AddableFields field : AddableFields.values()){
                        if((id != null && id != -1 && field.type.equals("c")) || ((faId != null || hgId != null) && field.type.equals("ead")))
                            child.put(field.name, field.xpath);
                    }

                    writer.append(child.toString());
                } else if(type.equals("lowLevels")){
                    JSONObject child = new JSONObject();

                    for(AddableFields field : AddableFields.values()){
                        if(field.type.equals("c"))
                            child.put(field.name, field.xpath);
                    }

                    writer.append(child.toString());
                    LOG.info(child.toString());
                } else if(type.equals("allLevels")){
                    JSONObject child = new JSONObject();

                    for(AddableFields field : AddableFields.values()){
                        if(field.type.equals("c"))
                            child.put(field.name, field.xpath);
                    }

                    writer.append(child.toString());
                    LOG.info(child.toString());
                }
                writer.close();
            }

        	this.log.debug("Leaving method \"getPossibleFieldEntries\" when no error occurs.");
        } catch (Exception e){
            LOG.error("Error getting possible field entries", e);

        	this.log.debug("Leaving method \"getPossibleFieldEntries\" when error occurs processing the possible field entries.");
        }
        return null;
    }

    /**
     * <p>
     * This method is called thought the Struts action <i>"editEadAddField"</i>.
     * </p>
     * <p>
     * It is used to define the level in which is needed to add a new field.
     * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
     *
     * @return null since we use the HttpResponse to write JSON data directly
	 * to the page.
	 *
	 * @see EditEadAction#writeNewCLevelXmlAndChildren(AddableFields, CLevel, String, boolean)
	 * @see EditParser#addInLevel(AddableFields, String, String)
     */
    public String addFieldEntry(){
    	this.log.debug("Entering method \"addFieldEntry\".");

        Writer writer = null;

        try {
            writer = openOutputWriter();

            if((id != null || faId != null || hgId != null) && type != null){
                if(formValues != null){
                    for(String key : formValues.keySet()){
                        for(AddableFields field : AddableFields.values()){

                                if(field.name.equals(key)){
                                    LOG.info(key + " found");

                                    if(type.equals("allLevels")){
                                        if(faId != null){
                                            try {
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
                                                List<CLevel> cLevels = DAOFactory.instance().getCLevelDAO().findTopCLevels(eadContent.getEcId());
                                                for(CLevel cLevel : cLevels)
                                                    writeNewCLevelXmlAndChildren(field, cLevel, key, false);
                                                writer.append(new JSONObject().put("saved", true).toString());
                                            } catch (Exception e){
                                                LOG.error("Could not save cLevel new XML");
                                                writer.append(new JSONObject().put("saved", false).toString());
                                            }
                                        } else if(hgId != null){
                                            try {
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
                                                List<CLevel> cLevels = DAOFactory.instance().getCLevelDAO().findTopCLevels(eadContent.getEcId());
                                                for(CLevel cLevel : cLevels)
                                                    writeNewCLevelXmlAndChildren(field, cLevel, key, false);
                                                writer.append(new JSONObject().put("saved", true).toString());
                                            } catch (Exception e){
                                                LOG.error("Could not save cLevel new XML");
                                                writer.append(new JSONObject().put("saved", false).toString());
                                            }
                                        }
                                    } else if(type.equals("lowLevels")){
                                        if(faId != null){
                                            try {
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
                                                List<CLevel> cLevels = DAOFactory.instance().getCLevelDAO().findTopCLevels(eadContent.getEcId());
                                                for(CLevel cLevel : cLevels)
                                                    writeNewCLevelXmlAndChildren(field, cLevel, key, true);
                                                writer.append(new JSONObject().put("saved", true).toString());
                                            } catch (Exception e){
                                                LOG.error("Could not save cLevel new XML");
                                                writer.append(new JSONObject().put("saved", false).toString());
                                            }
                                        } else if(hgId != null){
                                            try {
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
                                                List<CLevel> cLevels = DAOFactory.instance().getCLevelDAO().findTopCLevels(eadContent.getEcId());
                                                for(CLevel cLevel : cLevels)
                                                    writeNewCLevelXmlAndChildren(field, cLevel, key, true);
                                                writer.append(new JSONObject().put("saved", true).toString());
                                            } catch (Exception e){
                                                LOG.error("Could not save cLevel new XML");
                                                writer.append(new JSONObject().put("saved", false).toString());
                                            }
                                        }
                                    } else {
                                        if((id != null && id != -1 && field.type.equals("c")) || ((faId != null || hgId != null) && field.type.equals("ead"))){
                                            if(id != null){
                                                CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(id);
                                                String newXml = new EditParser().addInLevel(field, cLevel.getXml(), formValues.get(key));
                                                try {
                                                	JpaUtil.beginDatabaseTransaction();
                                                    cLevel.setXml(newXml);
                                                    JpaUtil.commitDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", true).toString());
                                                } catch (Exception e){
                                                    LOG.error("Could not save cLevel new XML");
                                                    JpaUtil.rollbackDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", false).toString());
                                                } finally {
                                                	JpaUtil.closeDatabaseSession();
                                                }
                                            } else if (faId != null){
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
                                                String newXml = new EditParser().addInLevel(field, eadContent.getXml(), formValues.get(key));
                                                try {
                                                	JpaUtil.beginDatabaseTransaction();
                                                    eadContent.setXml(newXml);
                                                    JpaUtil.commitDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", true).toString());
                                                } catch (Exception e){
                                                    LOG.error("Could not save eadContent new XML");
                                                    JpaUtil.rollbackDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", false).toString());
                                                } finally {
                                                	JpaUtil.closeDatabaseSession();
                                                }
                                            } else if (hgId != null){
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
                                                String newXml = new EditParser().addInLevel(field, eadContent.getXml(), formValues.get(key));
                                                try {
                                                	JpaUtil.beginDatabaseTransaction();
                                                    eadContent.setXml(newXml);
                                                    JpaUtil.commitDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", true).toString());
                                                } catch (Exception e){
                                                    LOG.error("Could not save eadContent new XML");
                                                    JpaUtil.rollbackDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", false).toString());
                                                } finally {
                                                	JpaUtil.closeDatabaseSession();
                                                }
                                            }
                                        }
                                    }
                                    break;
                                }

                        }
                        LOG.info(key + ": " + formValues.get(key));
                    }
                }
            }
            writer.close();
        } catch (Exception e){
            LOG.error("Could not save the field entry", e);
        }

    	this.log.debug("Leaving method \"addFieldEntry\".");

        return null;
    }

    /**
     * <p>
     * This method is called from <i>"{@link EditEadAction#addFieldEntry()}"</i>.
     * </p>
     * <p>
     * It is used to define the level in which is needed to add a new field.
     * </p>
     * <p>
     * <b>Note: This method is currently unused.</b>
     * </p>
     *
     * @param field {@link AddableFields} filed which should be added to the
     * level. Could be one of the enumeration.
     * @param cLevel {@link CLevel} level in which the field should be added.
     * @param key {@link String} which contains the value of the key to recover
     * the value, from the map, that will be used for the new field added.
     * @param onlyLowLevels boolean specifying if the field should be
     * added in the current level or in the others too.
     *
     * @throws XMLStreamException Exception when processing the XML information
     * in memory.
     * @throws IOException Exception when saving the information to the file.
     *
     * @see EditParser#addInLevel(AddableFields, String, String)
     */
    private void writeNewCLevelXmlAndChildren(AddableFields field, CLevel cLevel, String key, boolean onlyLowLevels) throws XMLStreamException, IOException {
        LOG.info("Adding in " + key + ", field " + field.name + "for cLevel id " + cLevel.getId());
        if(!onlyLowLevels){
            String newXml = new EditParser().addInLevel(field, cLevel.getXml(), formValues.get(key));
            JpaUtil.beginDatabaseTransaction();
            cLevel.setXml(newXml);
            JpaUtil.commitDatabaseTransaction();
        }
        List<CLevel> children = DAOFactory.instance().getCLevelDAO().findChilds(cLevel.getId());
        for(CLevel child : children){
            writeNewCLevelXmlAndChildren(field, child, key, onlyLowLevels);
        }
    }

    /**
     * <p>
     * Enumeration which contains the list of fields that can be <b>editable</b>
     * during the edition of the apeEAD files.
     * </p>
     */
    public enum EditableFields {
    	/**
    	 * <p>
    	 * Element which represent the field <b>{@code <titleproper>}</b>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#TITLEPROPER}.
    	 * <p>
    	 */
    	TITLEPROPER(EditEadAction.TITLEPROPER),
//    	ARCHDESC_LEVEL(EditEadAction.ARCHDESC_LEVEL),
    	/**
    	 * <p>
    	 * Element which represent the field <b>{@code <eadid>}</b>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#EADID}.
    	 * <p>
    	 */
    	EADID(EditEadAction.EADID),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @countrycode}</b>
    	 * for the element <i>{@code <eadid>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#EADID_COUNTRYCODE}.
    	 * <p>
    	 */
    	EADID_COUNTRYCODE(EditEadAction.EADID_COUNTRYCODE),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @mainagencycode}</b>
    	 * for the element <i>{@code <eadid>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#EADID_MAINAGENCYCODE}.
    	 * <p>
    	 */
    	EADID_MAINAGENCYCODE(EditEadAction.EADID_MAINAGENCYCODE),
    	/**
    	 * <p>
    	 * Element which represent the field <b>{@code <language>}</b>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#LANGUAGE}.
    	 * <p>
    	 */
    	LANGUAGE(EditEadAction.LANGUAGE),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @langcode}</b>
    	 * for the element <i>{@code <language>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#LANGUAGE_LANGCODE}.
    	 * <p>
    	 */
    	LANGUAGE_LANGCODE(EditEadAction.LANGUAGE_LANGCODE),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @normal}</b>
    	 * for the element <i>{@code <unitdate>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by the name which is the value of {@link EditEadAction#UNITDATE_NORMAL}.
    	 * <p>
    	 */
    	UNITDATE_NORMAL(EditEadAction.UNITDATE_NORMAL);

    	/**
    	 * <p>
    	 * Variable to store the name of the editable field.
    	 * </p>
    	 */
        String name;

        /**
         * <p>
         * Constructor.
         * </p>
         *
         * @param name {@link String} which contains the current field name.
         */
        EditableFields(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }

        /**
         * <p>
         * This method is used to check if the passed field name is contained
         * in the list of fields that can be <b>editable</b>.
         * </p>
         *
         * @param name {@link String} which contains the current field name.
         *
         * @return
	     * <p>
	     * {@code boolean} which represent the result of the check.
	     * </p>
	     * <p>
	     * Could be one of the follows:
	     * </p>
	     * <p>
	     *  <ul>
	     *   <li><b>true</b> - The passed field name is in the list of fields
	     *   	that can be <b>editable</b>.</li>
	     *   <li><b>false</b> - The passed field name is not in the list of
	     *   	fields that can be <b>editable</b>.</li>
	     *  </ul>
	     * </p>
         */
        public static boolean isEditable(String name){
            for(EditableFields editableFields : EditableFields.values()){
                if(editableFields.getName().equals(name))
                    return true;
            }
            return false;
        }
    }

    /**
     * <p>
     * Enumeration which contains the list of fields that won't be <b>displayed</b>
     * (and therefore not editable) during the edition of the apeEAD files.
     * </p>
     */
    public enum UndisplayableFields {
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @encodinganalog}</b>
    	 * for <i>any</i> element.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        ENCODINGANALOG("encodinganalog"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @level}</b>
    	 * for the element <i>{@code <c>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        LEVEL("level"),
    	/**
    	 * <p>
    	 * Element which represent the field <b>{@code <did>}</b>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        DID("did"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @id}</b>
    	 * for <i>any</i> element.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        ID("id"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @era}</b>
    	 * for the element <i>{@code <unitdate>}</i> or <i>{@code <date>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        ERA("era"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @calendar}</b>
    	 * for the element <i>{@code <unitdate>}</i> or <i>{@code <date>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        CALENDAR("calendar"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @countryencoding}</b>
    	 * for the element <i>{@code <eadheader>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        COUNTRYENCODING("countryencoding"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @dateencoding}</b>
    	 * for the element <i>{@code <eadheader>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        DATEENCODING("dateencoding"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @langencoding}</b>
    	 * for the element <i>{@code <eadheader>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        LANGENCODING("langencoding"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @repositoryencoding}</b>
    	 * for the element <i>{@code <eadheader>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        REPOSITORYENCODING("repositoryencoding"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @scriptencoding}</b>
    	 * for the element <i>{@code <eadheader>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        SCRIPTENCODING("scriptencoding"),
    	/**
    	 * <p>
    	 * Element which represent the field attribute <b>{@code @relatedencoding}</b>
    	 * for the element <i>{@code <eadheader>}</i> or <i>{@code <archdesc>}</i>.
    	 * </p>
    	 * <p>
    	 * Is only described by its name.
    	 * <p>
    	 */
        RELATEDENCODING("relatedencoding");

    	/**
    	 * <p>
    	 * Variable to store the name of the non-displayed field.
    	 * </p>
    	 */
        String name;

        /**
         * <p>
         * Constructor.
         * </p>
         *
         * @param name {@link String} which contains the current field name.
         */
        UndisplayableFields(String name){
            this.name = name;
        }

        public String getName(){
            return name;
        }

        /**
         * <p>
         * This method is used to check if the passed field name is contained
         * in the list of fields that shoudn't be <b>displayed</b>.
         * </p>
         *
         * @param name {@link String} which contains the current field name.
         *
         * @return
	     * <p>
	     * {@code boolean} which represent the result of the check.
	     * </p>
	     * <p>
	     * Could be one of the follows:
	     * </p>
	     * <p>
	     *  <ul>
	     *   <li><b>true</b> - The passed field name is not in the list of
	     *   	fields that shoudn't be <b>editable</b>.</li>
	     *   <li><b>false</b> - The passed field name is in the list of fields
	     *   	that shoudn't be <b>displayed</b>.</li>
	     *  </ul>
	     * </p>
         */
        public static boolean isDisplayable(String name){
            if(StringUtils.isEmpty(name))
                return false;

            for(UndisplayableFields undisplayableFields : UndisplayableFields.values()){
                if(undisplayableFields.getName().equals(name))
                    return false;
            }
            return true;
        }
    }

    /**
     * <p>
     * Enumeration which contains the list of fields that can be <b>addable</b>
     * during the edition of the apeEAD files.
     * </p>
     */
    public enum AddableFields {
    	/**
    	 * <p>
    	 * Element which represent the field <b>{@code <language>}</b> as child
    	 * of element <i>{@code <ead>}</i>.
    	 * </p>
    	 * <p>
    	 * Is described by its name, full path to locate it, and type.
    	 * <p>
    	 */
        LANGUSAGE_EAD("langusage_ead", "ead/eadheader/profiledesc/langusage/language", "ead"),
    	/**
    	 * <p>
    	 * Element which represent the field <b>{@code <language>}</b> as child
    	 * of element <i>{@code <c>}</i>.
    	 * </p>
    	 * <p>
    	 * Is described by its name, full path to locate it, and type.
    	 * <p>
    	 */
        LANGUSAGE_C("langusage_c", "c/did/langusage/language", "c");

    	/**
    	 * <p>
    	 * Variable to store the name of the addable field.
    	 * </p>
    	 */
        String name;
    	/**
    	 * <p>
    	 * Variable to store the path of the addable field.
    	 * </p>
    	 */
        String xpath;
    	/**
    	 * <p>
    	 * Variable to store the type of the addable field.
    	 * </p>
    	 * <p>
    	 * Could be one of the follows:
    	 * </p>
    	 * <p>
	     *  <ul>
	     *   <li><b>ead</b> - Represents the <b>addable</b> field <i>{@code <language>}</i>
	     *   	as child of element <i>{@code <ead>}</i>.</li>
	     *   <li><b>c</b> - Represents the <b>addable</b> field <i>{@code <language>}</i>
	     *   	as child of element <i>{@code <c>}</i>.</li>
	     *  </ul>
    	 * </p>
    	 */
        String type;

        /**
         * <p>
         * Constructor.
         * </p>
         *
         * @param name {@link String} which contains the current field name.
         * @param xpath {@link String} which contains the current field path.
         * @param type {@link String} which contains the current field type.
         */
        AddableFields(String name, String xpath, String type){
            this.name = name;
            this.xpath = xpath;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getXpath() {
            return xpath;
        }

        public String getType() {
            return type;
        }
    }
}
