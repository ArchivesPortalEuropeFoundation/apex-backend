package eu.apenet.dashboard.actions.ajax;

import java.io.IOException;
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
 * User: Yoann Moranville
 * Date: 3/10/11
 *
 * @author Yoann Moranville
 */
public class EditEadAction extends AjaxControllerAbstractAction {
	private static final long serialVersionUID = 4831971826309950250L;
    // Log.
    private final Logger log = Logger.getLogger(EditEadAction.class);

	// Constants for the names of only editable elements.
//	private static final String ARCHDESC_LEVEL = "archdesc_level"; // Name of attibute "@level" in element <archdesc>.
	private static final String TITLEPROPER = "titleproper"; // Name of element <titleproper>.

	// Constants for the names of editable and appendable elements.
	private static final String EADID = "eadid"; // Name of element <eadid>.
	private static final String EADID_COUNTRYCODE = "eadid_countrycode"; // Name of attibute "@countrycode" in element <eadid>.
	private static final String EADID_MAINAGENCYCODE = "eadid_mainagencycode"; // Name of attibute "@mainagencycode" in element <eadid>.
	private static final String LANGUAGE = "language"; // Name of element <language>.
	private static final String LANGUAGE_LANGCODE = "language_langcode"; // Name of attibute "@langcode" in element <language>.
	private static final String UNITDATE_NORMAL = "unitdate_normal"; // Name of attibute "@normal" in element <unitdate>.

	// Constants for the names of the buttons for the appendable elements.
	private static final String BTN_UNITDATE_NORMAL = "btn_unitdate_normal"; // Name of the button to add attibute "@normal" in element <unitdate>.

	// Constants for the names used.
	private static final String C_LEVEL_ID = "id"; // Type of XML.
	private static final String FILEID = "fileId"; // Key for the identifier of the EAD in DB.
	private static final String FORM_NAME = "'formValues'"; // Name of the map with all the values.
	private static final String XMLTYPEID = "xmlTypeId"; // Type of XML.

	// Variables.
	private Long id;
	private Long faId;
	private Long hgId;
	private Integer fileId;
	private int xmlTypeId;
	private Map<String, String> formValues;
	private String type;
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
	 * Override the method to build the correct breadcrumb.
	 */
	@Override
	protected void buildBreadcrumbs() {
		super.buildBreadcrumbs();
		addBreadcrumb("contentmanager.action", getText("breadcrumb.section.contentmanager"));
		addBreadcrumb(getText("breadcrumb.section.editionofEAD"));
	}

	/**
	 * Override the method to execute the action.
	 */
	@Override
	public String execute() {
		return SUCCESS;
	}

	public String createDbEntries(){
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
        return null;
    }

    public String getXmlData() {
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
        return null;
    }

    //todo before production: Sanitize user input
    /**
     * Method to check the data in the form and save it. 
     * 
     * @return The result of the process.
     */
    public String saveXmlData() {
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
            	} else {
                    jsonObject.put("saved", true);
                    jsonObject.put("savedText", getText("dashboard.editead.correctSaved"));
            		
            	}
            } else {
            	jsonObject.put("saved", true);
                jsonObject.put("savedText", getText("dashboard.editead.notChanged"));
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
        }
        return null;
    }

    /**
     * Method to recover and add the correct values to the map "formValues".
     * This is derived from issue 180 and bug in Struts2 (see:
     * https://issues.apache.org/jira/browse/WW-4176).
     */
    private String recoverMapFormValues() {
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
		}

		return result;
    }

    /**
     * Method to ensure the elements in the set are correctly ordered.
     *
     * @param elementSet Set of elements.
     * @return Ordered set of elements.
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
     * Method to ensure the elements in the language sets are correctly ordered.
     *
     * @param elementSet Set of elements.
     * @return Ordered set of elements.
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
     * Method to add the entries in FormValues map from the values in jsonObject usign
     * the keys in elementSet.
     *
     * @param jsonObject Object to recover the values using the keys passed in elementSet.
     * @param elementSet Ordered set of elements.
     * @throws JSONException Exception processing JSONObject.
     */
    private void addValuesToMap(JSONObject jsonObject, Set<String> elementSet) throws JSONException {
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
    }

    /**
     * Deletes the entries of this Finding Aid in the EadContent and the CLevel tables
     * @return null since we use the HttpResponse to write JSON data directly to the page
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

    public String saveAllXmlData(){
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
            }
        } catch (NotAuthorizedException e){
            LOG.error("Not authorized...", e);
        }
        return result;
    }

    public String getPossibleFieldEntries(){
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
        } catch (Exception e){
            LOG.error("Error getting possible field entries", e);
        }
        return null;
    }

    /**
     * type can be "thisLevel", "allLevels" or "lowLevels"
     * @return null - the data is being sent directly to the response pipe
     */
    public String addFieldEntry(){
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
        return null;
    }

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
     * Fields that are Editable on this Edit page
     */
    public enum EditableFields {
    	TITLEPROPER(EditEadAction.TITLEPROPER),
//    	ARCHDESC_LEVEL(EditEadAction.ARCHDESC_LEVEL),
    	EADID(EditEadAction.EADID),
    	EADID_COUNTRYCODE(EditEadAction.EADID_COUNTRYCODE),
    	EADID_MAINAGENCYCODE(EditEadAction.EADID_MAINAGENCYCODE),
    	LANGUAGE(EditEadAction.LANGUAGE),
    	LANGUAGE_LANGCODE(EditEadAction.LANGUAGE_LANGCODE),
    	UNITDATE_NORMAL(EditEadAction.UNITDATE_NORMAL);

        String name;
        EditableFields(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }

        public static boolean isEditable(String name){
            for(EditableFields editableFields : EditableFields.values()){
                if(editableFields.getName().equals(name))
                    return true;
            }
            return false;
        }
    }

    /**
     * Fields that we do not display (and therefor not edit) on the Edit page
     */
    public enum UndisplayableFields {
        ENCODINGANALOG("encodinganalog"),
        LEVEL("level"),
        DID("did"),
        ID("id"),
        ERA("era"),
        CALENDAR("calendar"),
        COUNTRYENCODING("countryencoding"),
        DATEENCODING("dateencoding"),
        LANGENCODING("langencoding"),
        REPOSITORYENCODING("repositoryencoding"),
        SCRIPTENCODING("scriptencoding"),
        RELATEDENCODING("relatedencoding");

        String name;
        UndisplayableFields(String name){
            this.name = name;
        }
        public String getName(){
            return name;
        }

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

    public enum AddableFields {
        LANGUSAGE_EAD("langusage_ead", "ead/eadheader/profiledesc/langusage/language", "ead"),
        LANGUSAGE_C("langusage_c", "c/did/langusage/language", "c");

        String name;
        String xpath;
        String type;

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
