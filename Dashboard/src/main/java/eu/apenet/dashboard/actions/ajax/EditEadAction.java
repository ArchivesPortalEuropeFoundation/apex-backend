package eu.apenet.dashboard.actions.ajax;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.exception.NotAuthorizedException;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.dashboard.manual.EditParser;
import eu.apenet.dashboard.manual.ReconstructEadFile;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.EuropeanaState;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.ValidatedState;

/**
 * User: Yoann Moranville
 * Date: 3/10/11
 *
 * @author Yoann Moranville
 */
public class EditEadAction extends AjaxControllerAbstractAction {

    private static final long serialVersionUID = 4831971826309950250L;

    private Long id;
    private Long faId;
    private Long hgId;
    private int xmlTypeId;
    private Map<String, String> formValues;
    private String type;
	
	@Override
	public String execute() {
		return SUCCESS;
	}
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFaId() {
        return faId;
    }

    public void setFaId(Long faId) {
        this.faId = faId;
    }

    public Long getHgId() {
        return hgId;
    }

    public void setHgId(Long hgId) {
        this.hgId = hgId;
    }

    public int getXmlTypeId() {
        return xmlTypeId;
    }

    public void setXmlTypeId(int xmlTypeId) {
        this.xmlTypeId = xmlTypeId;
    }

    public Map<String, String> getFormValues() {
        return formValues;
    }

    public void setFormValues(Map<String, String> formValues) {
        this.formValues = formValues;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String createDbEntries(){
        XmlType xmlType = XmlType.getType(xmlTypeId);
        LOG.trace("Identifier of EAD: " + id + ", is XmlType: " + xmlType.getName());
        Writer writer = null;
        try {
            writer = openOutputWriter();

            if(xmlType != XmlType.EAD_FA && xmlType != xmlType.EAD_HG)
                throw new APEnetException(getText("editEadAction.noXmlTypedefinedOrWrongType"));

            Ead ead = DAOFactory.instance().getEadDAO().findById(id.intValue(), xmlType.getClazz());

            if(ead.isPublished())
                throw new APEnetException(getText("editEadAction.faStateNotCompliant"));
            if (ead instanceof FindingAid){
            	if (!EuropeanaState.NOT_CONVERTED.equals(((FindingAid) ead).getEuropeana())){
            		throw new APEnetException(getText("editEadAction.faStateNotCompliant"));
            	}
            }
            EADParser.parseEad(ead);

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
            } else if(faId != null && faId != -1 && xmlType == XmlType.EAD_FA){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
                if(eadContent != null)
                    obj.put("xml", new EditParser().xmlToHtml(null, eadContent));
            } else if(hgId != null && hgId != -1 && xmlType == XmlType.EAD_HG){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
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
        XmlType xmlType = XmlType.getType(xmlTypeId);
        Writer writer = null;
        try {
            writer = openOutputWriter();

            if(id != null){
                CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(id);
                String newXml = new EditParser().getNewXmlString(cLevel.getXml(), formValues);
                cLevel.setXml(newXml);

                //Check if unittitle and @level have been changed
                if(!StringUtils.isEmpty(formValues.get("unittitle"))){
                    if(!formValues.get("unittitle").equals(cLevel.getUnittitle()))
                        cLevel.setUnittitle(formValues.get("unittitle"));
                }
                if(!StringUtils.isEmpty(formValues.get("c_level"))){
                    if(!formValues.get("c_level").equals(cLevel.getLevel()))
                        cLevel.setLevel(formValues.get("c_level"));
                }
                HibernateUtil.beginDatabaseTransaction();
                DAOFactory.instance().getCLevelDAO().update(cLevel);
                HibernateUtil.commitDatabaseTransaction();
            } else if(xmlType == XmlType.EAD_FA){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
                String newXml = new EditParser().getNewXmlString(eadContent.getXml(), formValues);
                eadContent.setXml(newXml);
                //Check if eadid and unittitle have been changed
                if(!StringUtils.isEmpty(formValues.get("eadid"))){
                    if(!formValues.get("eadid").equals(eadContent.getEadid()))
                        eadContent.setEadid(formValues.get("eadid"));
                }
                if(!StringUtils.isEmpty(formValues.get("unittitle"))){
                    if(!formValues.get("unittitle").equals(eadContent.getUnittitle()))
                        eadContent.setUnittitle(formValues.get("unittitle"));
                }
                HibernateUtil.beginDatabaseTransaction();
                DAOFactory.instance().getEadContentDAO().update(eadContent);
                HibernateUtil.commitDatabaseTransaction();
            } else if(xmlType == XmlType.EAD_HG){
                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
                String newXml = new EditParser().getNewXmlString(eadContent.getXml(), formValues);
                eadContent.setXml(newXml);
                //Check if eadid and unittitle have been changed
                if(!StringUtils.isEmpty(formValues.get("eadid"))){
                    if(!formValues.get("eadid").equals(eadContent.getEadid()))
                        eadContent.setEadid(formValues.get("eadid"));
                }
                if(!StringUtils.isEmpty(formValues.get("unittitle"))){
                    if(!formValues.get("unittitle").equals(eadContent.getUnittitle()))
                        eadContent.setUnittitle(formValues.get("unittitle"));
                }
                HibernateUtil.beginDatabaseTransaction();
                DAOFactory.instance().getEadContentDAO().update(eadContent);
                HibernateUtil.commitDatabaseTransaction();
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

            HibernateUtil.rollbackDatabaseTransaction();
            LOG.error("EXCEPTION", e);
        }
        return null;
    }

    /**
     * Deletes the entries of this Finding Aid in the EadContent and the CLevel tables
     * @return null since we use the HttpResponse to write JSON data directly to the page
     */
    public String deleteDatabaseEntries(){
        Long faId = new Long(request.getParameter("faId"));
        Long hgId = new Long(request.getParameter("hgId"));

        ArchivalInstitution archivalInstitution;
        if(faId != null)
            archivalInstitution = DAOFactory.instance().getFindingAidDAO().findById(faId.intValue()).getArchivalInstitution();
        else
            archivalInstitution = DAOFactory.instance().getHoldingsGuideDAO().findById(hgId.intValue()).getArchivalInstitution();

        try {
            SecurityContext.get().checkAuthorized(archivalInstitution);
            EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
            EadContent oldEadContent;
            if(faId != null)
                oldEadContent = eadContentDAO.getEadContentByFindingAidId(faId.intValue());
            else
                oldEadContent = eadContentDAO.getEadContentByHoldingsGuideId(hgId.intValue());
            if(oldEadContent != null)
                eadContentDAO.delete(oldEadContent);
            LOG.trace("DB entries erased!");
        } catch (NotAuthorizedException e){
            LOG.error("Not authorized to delete this FA...");
        }
        return null;
    }

    public String saveAllXmlData(){

        ArchivalInstitution archivalInstitution = null;
        HoldingsGuide holdingsGuide = null;
        FindingAid findingAid = null;
        EadContent eadContent = null;
        XmlType xmlType = XmlType.getType(xmlTypeId);
        if(xmlType == XmlType.EAD_FA){
            findingAid = DAOFactory.instance().getFindingAidDAO().findById(faId.intValue());
            archivalInstitution = findingAid.getArchivalInstitution();
            eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
        } else if (xmlType == XmlType.EAD_HG){
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

                HibernateUtil.beginDatabaseTransaction();
                if(findingAid != null){
                    if(ValidatedState.VALIDATED.equals(findingAid.getValidated()))
                        findingAid.setValidated(ValidatedState.NOT_VALIDATED);
                } else {
                    if(ValidatedState.VALIDATED.equals(holdingsGuide.getValidated()))
                    	holdingsGuide.setValidated(ValidatedState.NOT_VALIDATED);
                }
                HibernateUtil.commitDatabaseTransaction();

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
                                                    HibernateUtil.beginDatabaseTransaction();
                                                    cLevel.setXml(newXml);
                                                    HibernateUtil.commitDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", true).toString());
                                                } catch (Exception e){
                                                    LOG.error("Could not save cLevel new XML");
                                                    HibernateUtil.rollbackDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", false).toString());
                                                } finally {
                                                    HibernateUtil.closeDatabaseSession();
                                                }
                                            } else if (faId != null){
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByFindingAidId(faId.intValue());
                                                String newXml = new EditParser().addInLevel(field, eadContent.getXml(), formValues.get(key));
                                                try {
                                                    HibernateUtil.beginDatabaseTransaction();
                                                    eadContent.setXml(newXml);
                                                    HibernateUtil.commitDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", true).toString());
                                                } catch (Exception e){
                                                    LOG.error("Could not save eadContent new XML");
                                                    HibernateUtil.rollbackDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", false).toString());
                                                } finally {
                                                    HibernateUtil.closeDatabaseSession();
                                                }
                                            } else if (hgId != null){
                                                EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByHoldingsGuideId(hgId.intValue());
                                                String newXml = new EditParser().addInLevel(field, eadContent.getXml(), formValues.get(key));
                                                try {
                                                    HibernateUtil.beginDatabaseTransaction();
                                                    eadContent.setXml(newXml);
                                                    HibernateUtil.commitDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", true).toString());
                                                } catch (Exception e){
                                                    LOG.error("Could not save eadContent new XML");
                                                    HibernateUtil.rollbackDatabaseTransaction();
                                                    writer.append(new JSONObject().put("saved", false).toString());
                                                } finally {
                                                    HibernateUtil.closeDatabaseSession();
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
            HibernateUtil.beginDatabaseTransaction();
            cLevel.setXml(newXml);
            HibernateUtil.commitDatabaseTransaction();
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
        UNITTITLE("unittitle"),
        UNITID("unitid"),
        UNITDATE("unitdate"),
        TYPE("unittitle_type"),
        NORMAL("unitdate_normal"),
        P("p"),
        C_LEVEL("c_level"),
        EADID("eadid"),
        IDENTIFIER("c_identifier");

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
