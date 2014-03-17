package eu.apenet.dashboard.actions.ajax;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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
import eu.apenet.dashboard.services.ead.xml.ReconstructEadFile;
import eu.apenet.dashboard.services.ead.xml.XmlEadParser;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
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

    // Constants for the names.
	private static final String FORM_NAME = "'formValues'"; // Name of the map with all the values.
	private static final String C_IDENTIFIER = "c_identifier"; // Name of element <c>.
	private static final String C_LEVEL = "c_level"; // Name of element <c>.
	private static final String C_LEVEL_ID = "id"; // Type of XML.
	private static final String EADID = "eadid"; // Name of element <eadid>.
	private static final String FILEID = "fileId"; // Key for the identifier of the EAD in DB.
	private static final String P_ELEMENT = "p"; // Name of element <p>.
	private static final String UNITDATE = "unitdate"; // Name of element <unitdate>.
	private static final String UNITDATE_NORMAL = "unitdate_normal"; // Name of element <unitdate_normal>.
	private static final String UNITID = "unitid"; // Name of element <unitid>.
	private static final String UNITTITLE = "unittitle"; // Name of element <unittitle>.
	private static final String UNITTITLE_TYPE = "unittitle_type"; // Name of element <unittitle_type>.
	private static final String XMLTYPEID = "xmlTypeId"; // Type of XML.

    // Log.
    private final Logger log = Logger.getLogger(getClass());

	private Long id;
	private Long faId;
	private Long hgId;
	private Integer fileId;
	private int xmlTypeId;
	private Map<String, String> formValues;
	private String type;

	@Override
	public String execute() {
		return SUCCESS;
	}

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

	public void setFileId(Integer fileId) {
		this.fileId = fileId;
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
    public String saveXmlData() {
        Writer writer = null;
        try {
            XmlType xmlType = XmlType.getType(this.getXmlTypeId());
            writer = openOutputWriter();

        	// Check if its necessary to recover formValues.
        	if (this.getFormValues() == null || this.getFormValues().isEmpty()) {
        		if (this.recoverMapFormValues().equalsIgnoreCase(Action.ERROR)) {
        			try {
                        if(writer != null){
                            writer.append(new JSONObject().put("saved", false).toString());
                            writer.close();
                        }
                    } catch (Exception ex){
                        LOG.error("Could not send the JSON to the page...");
                    }
        		}
        	}

        	boolean dataChanged = false;

            if(this.getId() != null){
                CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(this.getId());
                String newXml = new EditParser().getNewXmlString(cLevel.getXml(), this.getFormValues());
                cLevel.setXml(newXml);

                //Check if unittitle and @level have been changed
                if(!StringUtils.isEmpty(this.getFormValues().get(EditEadAction.UNITTITLE))){
                    if(!this.getFormValues().get(EditEadAction.UNITTITLE).equals(cLevel.getUnittitle())) {
                        cLevel.setUnittitle(this.getFormValues().get(EditEadAction.UNITTITLE));
                    	dataChanged = true;
                    }
                }
                if(!StringUtils.isEmpty(this.getFormValues().get(EditEadAction.C_LEVEL))){
                    if(!this.getFormValues().get(EditEadAction.C_LEVEL).equals(cLevel.getLevel())) {
                        cLevel.setLevel(this.getFormValues().get(EditEadAction.C_LEVEL));
                		dataChanged = true;
                    }
                }
                JpaUtil.beginDatabaseTransaction();
                DAOFactory.instance().getCLevelDAO().update(cLevel);
                JpaUtil.commitDatabaseTransaction();
            } else if(xmlType == XmlType.EAD_FA){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(this.getFaId().intValue());
                String newXml = new EditParser().getNewXmlString(eadContent.getXml(), this.getFormValues());
                eadContent.setXml(newXml);
                //Check if eadid and unittitle have been changed
                if(!StringUtils.isEmpty(this.getFormValues().get(EditEadAction.EADID))){
                    if(!this.getFormValues().get(EditEadAction.EADID).equals(eadContent.getEadid())) {
                        eadContent.setEadid(this.getFormValues().get(EditEadAction.EADID));
                        dataChanged = true;
                    }
                }
                if(!StringUtils.isEmpty(this.getFormValues().get(EditEadAction.UNITTITLE))){
                    if(!this.getFormValues().get(EditEadAction.UNITTITLE).equals(eadContent.getUnittitle())) {
                        eadContent.setUnittitle(this.getFormValues().get(EditEadAction.UNITTITLE));
                        dataChanged = true;
                    }
                }
                JpaUtil.beginDatabaseTransaction();
                DAOFactory.instance().getEadContentDAO().update(eadContent);
                JpaUtil.commitDatabaseTransaction();
            }
            // TODO: Commented this part due to comment in revision 1596:
            // "IssueID 693: HG and SG should not be editable."
//            else if(xmlType == XmlType.EAD_HG){
//                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
//                String newXml = new EditParser().getNewXmlString(eadContent.getXml(), formValues);
//                eadContent.setXml(newXml);
//                //Check if eadid and unittitle have been changed
//                if(!StringUtils.isEmpty(formValues.get(EditEadAction.EADID))){
//                    if(!formValues.get(EditEadAction.EADID).equals(eadContent.getEadid()))
//                        eadContent.setEadid(formValues.get(EditEadAction.EADID));
//                }
//                if(!StringUtils.isEmpty(formValues.get(EditEadAction.UNITTITLE))){
//                    if(!formValues.get(EditEadAction.UNITTITLE).equals(eadContent.getUnittitle()))
//                        eadContent.setUnittitle(formValues.get(EditEadAction.UNITTITLE));
//                }
//                JpaUtil.beginDatabaseTransaction();
//                DAOFactory.instance().getEadContentDAO().update(eadContent);
//                JpaUtil.commitDatabaseTransaction();
//            }

            // Try to persist the changes in file (if needed).
            if (dataChanged) {
            	this.saveAllXmlData();
            }

            writer.append(new JSONObject().put("saved", true).toString());
            writer.close();
        } catch (Exception e){
            try {
                if(writer != null){
                    writer.append(new JSONObject().put("saved", false).toString());
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
			Set<String> eadIdSet = new LinkedHashSet<String>();
			Set<String> unitidSet = new LinkedHashSet<String>();
			Set<String> unittitleSet = new LinkedHashSet<String>();
			Set<String> unittitleTypeSet = new LinkedHashSet<String>();
			Set<String> unitdateSet = new LinkedHashSet<String>();
			Set<String> unitdateNormalSet = new LinkedHashSet<String>();
			Set<String> cLevelSet = new LinkedHashSet<String>();
			Set<String> cIdentifierSet = new LinkedHashSet<String>();
			Set<String> pSet = new LinkedHashSet<String>();

			Iterator<String> keysIt = jsonObject.keys();

			// Check iterator values.
			while (keysIt.hasNext()) {
				String key = keysIt.next();
				if (key.startsWith(EditEadAction.XMLTYPEID)) {
					xmlTypeSet.add(key);
				} else if (key.startsWith(EditEadAction.C_LEVEL_ID)) {
					idSet.add(key);
				} else if (key.startsWith(EditEadAction.FILEID)) {
					faIdSet.add(key);
				} else if (key.startsWith(EditEadAction.EADID)) {
					eadIdSet.add(key);
				} else if (key.startsWith(EditEadAction.UNITID)) {
					unitidSet.add(key);
				} else if (key.startsWith(EditEadAction.UNITTITLE)) {
					if (key.startsWith(EditEadAction.UNITTITLE_TYPE)) {
						unittitleTypeSet.add(key);
					} else {
						unittitleSet.add(key);
					}
				} else if (key.startsWith(EditEadAction.UNITDATE)) {
					if (key.startsWith(EditEadAction.UNITDATE_NORMAL)) {
						unitdateNormalSet.add(key);
					} else {
						unitdateSet.add(key);
					}
				} else if (key.startsWith(EditEadAction.C_LEVEL)) {
					cLevelSet.add(key);
				} else if (key.startsWith(EditEadAction.C_IDENTIFIER)) {
					cIdentifierSet.add(key);
				} else if (key.startsWith(EditEadAction.P_ELEMENT)) {
					pSet.add(key);
				}
			}

			String value = "";
			String key = "";
			int count = 0;

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

			// Check if exists value for element "<eadid>".
			if (eadIdSet != null && !eadIdSet.isEmpty()) {
				count = 0;
				key = eadIdSet.iterator().next();
				value = jsonObject.getString(key);
				if (value != null) {
					count++;
					this.getFormValues().put(EditEadAction.EADID + "_" + count, value);
					this.log.debug("Recovered the EADID (" + value + ").");
				}
			}

			// Check if exists value for element "<unitid>".
			if (unitidSet != null && !unitidSet.isEmpty()) {
				count = 0;
				Iterator<String> unitidIt = unitidSet.iterator();
				while (unitidIt.hasNext()) {
					key = unitidIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.UNITID + "_" + count, value);
						this.log.debug("Recovered the new value for element <unitid> (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<unittitle>".
			if (unittitleSet != null && !unittitleSet.isEmpty()) {
				count = 0;
				Iterator<String> unittitleIt = unittitleSet.iterator();
				while (unittitleIt.hasNext()) {
					key = unittitleIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.UNITTITLE + "_" + count, value);
						this.log.debug("Recovered the new value for element <unittitle> (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<unittitle>" attribute "type".
			if (unittitleTypeSet != null && !unittitleTypeSet.isEmpty()) {
				count = 0;
				Iterator<String> unittitleTypeIt = unittitleTypeSet.iterator();
				while (unittitleTypeIt.hasNext()) {
					key = unittitleTypeIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.UNITTITLE_TYPE + "_" + count, value);
						this.log.debug("Recovered the new value for element <unittitle> attribute type (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<unitdate>".
			if (unitdateSet != null && !unitdateSet.isEmpty()) {
				count = 0;
				Iterator<String> unitdateIt = unitdateSet.iterator();
				while (unitdateIt.hasNext()) {
					key = unitdateIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.UNITDATE + "_" + count, value);
						this.log.debug("Recovered the new value for element <unitdate> (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<unitdate>" attribute "normal".
			if (unitdateNormalSet != null && !unitdateNormalSet.isEmpty()) {
				count = 0;
				Iterator<String> unitdateNormalIt = unitdateNormalSet.iterator();
				while (unitdateNormalIt.hasNext()) {
					key = unitdateNormalIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.UNITDATE_NORMAL + "_" + count, value);
						this.log.debug("Recovered the new value for element <unitdate> attribute normal (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<c>" attribute "level".
			if (cLevelSet != null && !cLevelSet.isEmpty()) {
				count = 0;
				Iterator<String> cLevelIt = cLevelSet.iterator();
				while (cLevelIt.hasNext()) {
					key = cLevelIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.C_LEVEL + "_" + count, value);
						this.log.debug("Recovered the new value for element <c>  attribute level (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<c>" attribute "id".
			if (cIdentifierSet != null && !cIdentifierSet.isEmpty()) {
				count = 0;
				Iterator<String> cIdentifierIt = cIdentifierSet.iterator();
				while (cIdentifierIt.hasNext()) {
					key = cIdentifierIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.C_IDENTIFIER + "_" + count, value);
						this.log.debug("Recovered the new value for element <c>  attribute id (" + value + ").");
					}
				}
			}

			// Check if exists value for element "<p>".
			if (pSet != null && !pSet.isEmpty()) {
				count = 0;
				Iterator<String> pIt = pSet.iterator();
				while (pIt.hasNext()) {
					key = pIt.next();
					value = jsonObject.getString(key);
					if (value != null) {
						count++;
						this.getFormValues().put(EditEadAction.P_ELEMENT + "_" + count, value);
						this.log.debug("Recovered the new value for element <p> (" + value + ").");
					}
				}
			}

    		System.out.println(requestedData);
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

        ArchivalInstitution archivalInstitution = null;
        HoldingsGuide holdingsGuide = null;
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
        } else if (xmlType == XmlType.EAD_HG){
        	if(hgId==null && fileId!=null){ //in case that hgId is null the target value is into fileId
        		hgId = fileId.longValue();
        	}
            holdingsGuide = DAOFactory.instance().getHoldingsGuideDAO().findById(hgId.intValue());
            archivalInstitution = holdingsGuide.getArchivalInstitution();
            eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
        }

        Writer writer = null;
        try {
        	SecurityContext.get().checkAuthorized(archivalInstitution);

            try {
                writer = openOutputWriter();
                String filePath;
                if(findingAid != null) {
                    filePath = APEnetUtilities.getConfig().getRepoDirPath() + findingAid.getPathApenetead();
                } else {
                    filePath = APEnetUtilities.getConfig().getRepoDirPath() + holdingsGuide.getPathApenetead();
                }
                LOG.trace("FilePath of the FA/HG (faId: " + faId + ", hgId: " + hgId + ") that we are saving after editing: " + filePath);

                ReconstructEadFile.reconstructEadFile(eadContent, filePath);

                JpaUtil.beginDatabaseTransaction();
                if(findingAid != null){
                    if(ValidatedState.VALIDATED.equals(findingAid.getValidated()))
                        findingAid.setValidated(ValidatedState.NOT_VALIDATED);
                    	findingAid.setDynamic(false);
                } else {
                    if(ValidatedState.VALIDATED.equals(holdingsGuide.getValidated()))
                    	holdingsGuide.setValidated(ValidatedState.NOT_VALIDATED);
                    	holdingsGuide.setDynamic(false);
                }
                JpaUtil.commitDatabaseTransaction();

                writer.append(new JSONObject().put("saved", true).toString());
                writer.close();
            } catch (Exception e){
                LOG.error("ERROR", e);
                try {
                    if(writer != null){
                        writer.append(new JSONObject().put("saved", false).toString());
                        writer.close();
                    }
                } catch (Exception ex){
                    LOG.error("Error closing the streams", ex);
                }
            }
        } catch (NotAuthorizedException e){
            LOG.error("Not authorized...", e);
        }
        return null;
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
        LOG.info("Adding in " + key + ", field " + field.name + "for cLevel id " + cLevel.getClId());
        if((onlyLowLevels && (cLevel.getLevel().equals("item") || cLevel.getLevel().equals("file"))) || !onlyLowLevels){
            String newXml = new EditParser().addInLevel(field, cLevel.getXml(), formValues.get(key));
            JpaUtil.beginDatabaseTransaction();
            cLevel.setXml(newXml);
            JpaUtil.commitDatabaseTransaction();
        }
        List<CLevel> children = DAOFactory.instance().getCLevelDAO().findChilds(cLevel.getClId());
        for(CLevel child : children){
            writeNewCLevelXmlAndChildren(field, child, key, onlyLowLevels);
        }
    }

    /**
     * Fields that are Editable on this Edit page
     */
    public enum EditableFields {
        UNITTITLE(EditEadAction.UNITTITLE),
        UNITID(EditEadAction.UNITID),
        UNITDATE(EditEadAction.UNITDATE),
        TYPE(EditEadAction.UNITTITLE_TYPE),
        NORMAL(EditEadAction.UNITDATE_NORMAL),
        P(EditEadAction.P_ELEMENT),
        C_LEVEL(EditEadAction.C_LEVEL),
        EADID(EditEadAction.EADID),
        IDENTIFIER(EditEadAction.C_IDENTIFIER);

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

    public enum CElementLevel {
        FONDS(0, "fonds", new String[]{"fonds"}),
        SERIES(1, "series", new String[]{"fonds", "series"}),
        SUBSERIES(2, "subseries", new String[]{"series", "subseries"}),
        FILE(3, "file", new String[]{"subseries", "file"}),
        ITEM(4, "item", new String[]{"file", "item"});

        int level;
        String name;
        String[] possibleChanges;
        CElementLevel(int level, String name, String[] possibleChanges){
            this.level = level;
            this.name = name;
            this.possibleChanges = possibleChanges;
        }

        public static CElementLevel getCElementLevel(String name){
            for(CElementLevel cElementLevel : CElementLevel.values()){
                if(cElementLevel.name.equals(name))
                    return cElementLevel;
            }
            return null;
        }

        public static List<String> getPossibleLevels(CLevel parentCLevel, List<String> childLevels){
            List<String> possibleValues = new ArrayList<String>();
            CElementLevel parent;
            if(parentCLevel != null)
                parent = getCElementLevel(parentCLevel.getLevel());
            else
                parent = CElementLevel.FONDS;

            CElementLevel highestChild = null;
            for(String childLevel : childLevels){
                LOG.info("bef: " + childLevel);
                if(highestChild == null)
                    highestChild = getCElementLevel(childLevel);
                else {
                    if(highestChild.level > getCElementLevel(childLevel).level)
                        highestChild = getCElementLevel(childLevel);
                }
            }

            for(CElementLevel cElementLevel : CElementLevel.values()){
                if(cElementLevel.level >= parent.level && cElementLevel.level <= (highestChild==null?CElementLevel.ITEM.level+1:((highestChild == CElementLevel.FILE || highestChild == CElementLevel.ITEM)?highestChild.level-1:highestChild.level))){
                    possibleValues.add(cElementLevel.name);
                }
            }
            return possibleValues;
        }
    }

    /**
     * Fields that we do not display (and therefor not edit) on the Edit page
     */
    public enum UndisplayableFields {
        ENCODINGANALOG("encodinganalog"),
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
