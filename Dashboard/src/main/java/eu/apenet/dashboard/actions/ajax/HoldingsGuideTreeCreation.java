package eu.apenet.dashboard.actions.ajax;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.types.XmlType;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.manual.hgTreeCreation.CLevelTreeNode;
import eu.apenet.dashboard.services.ead.CreateEadTask;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.services.ead.xml.ReconstructEadFile;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.persistence.jpa.JpaUtil;

/**
 * User: Yoann Moranville
 * Date: 06/07/2011
 *
 * @author Yoann Moranville
 */
public class HoldingsGuideTreeCreation extends AjaxControllerAbstractAction {
    private static final String ALL_FINDING_AIDS_SESSION = "FAsSession";
    private static final String TYPE_EAD_CONTENT = "ec";
    private static final String TYPE_C_LEVEL = "cl";
    private String cId;
    private String hgId;
    private HttpSession session;




    @Override
    public String execute() {
//        try {
//        	Integer aiId = getAiId();
//            if(hgId != null){
//                int hgIdInt = Integer.parseInt(hgId);
//                HoldingsGuide holdingsGuide = DAOFactory.instance().getHoldingsGuideDAO().findById(hgIdInt);
//                String stateOfHG = holdingsGuideIsNotIndexed(holdingsGuide);
//                LOG.info("State of HG: " + stateOfHG);
//                if(stateOfHG.equals(ERROR))
//                    return ERROR;
//                if(stateOfHG.equals(SUCCESS))
//                    XmlEadParser.parseEad(holdingsGuide);
//                getAllFindingAids(holdingsGuide.getArchivalInstitution()); //In order to retrieve it a load time and have the data in the session
//            } else if(aiId != null){
//                getAllFindingAids(DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId));
//            }
//            return SUCCESS;
//        } catch (Exception e){
//            LOG.error("Error executing HG tree creation page", e);
//            return ERROR;
//        }
        return SUCCESS;
    }



    public String holdingsGuideIsNotIndexed(HoldingsGuide holdingsGuide){
        if(holdingsGuide.isPublished()){
            return ERROR;
        }else if(ValidatedState.VALIDATED.equals(holdingsGuide.getValidated())){
            return SUCCESS;
        }else
            return NONE;
    }

    public CLevelTreeNode createCLevelTreeNode(){
        LOG.info("name: " + getServletRequest().getParameter("name"));
        LOG.info("identifier: " + getServletRequest().getParameter("identifier"));
        LOG.info("desc: " + getServletRequest().getParameter("desc"));
        return new CLevelTreeNode(getServletRequest().getParameter("identifier"), getServletRequest().getParameter("name")).setDescription(getServletRequest().getParameter("desc"));
    }

    /**
     * First AJAX getServletRequest(), we create the EAD CONTENT data with a dummy file and save it in the DB
     * while we send this data to the page.
     * The data is send back directly via the http response writer.
     * @return A null String
     */
    public String addEadContentData(){
        try {
            Writer writer = openOutputWriter();

            CLevelTreeNode levelTreeNode = createCLevelTreeNode();
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
            StringWriter eadContentXml = createEadContentData(archivalInstitution, levelTreeNode);
            JpaUtil.beginDatabaseTransaction();
            EadContent eadContent;
            if(StringUtils.isEmpty(getServletRequest().getParameter("dataToEdit"))){
            	Ead holdingsGuide = new HoldingsGuide();
            	holdingsGuide.setEadid(levelTreeNode.getUnitid());
            	holdingsGuide.setTitle(levelTreeNode.getUnittitle());
            	holdingsGuide.setDynamic(true);
            	holdingsGuide.setValidated(ValidatedState.VALIDATED);
            	holdingsGuide.setConverted(true);
            	holdingsGuide.setUploadDate(new Date());
            	holdingsGuide.setAiId(getAiId());
            	holdingsGuide.setArchivalInstitution(archivalInstitution);
                UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(UploadMethod.HTTP);
            	holdingsGuide.setUploadMethod(uploadMethod);
            	String startPath = CreateEadTask.getPath(XmlType.EAD_HG, archivalInstitution);
            	holdingsGuide.setPathApenetead(APEnetUtilities.getDashboardConfig().getRepoDirPath() + startPath+ APEnetUtilities.convertToFilename(levelTreeNode.getUnitid())+ ".xml");
            	holdingsGuide = DAOFactory.instance().getEadDAO().store(holdingsGuide);
                eadContent = createDummyEadContent();
                eadContent.setHgId(holdingsGuide.getId());
                eadContent.setEadid(levelTreeNode.getUnitid());
                eadContent.setTitleproper(levelTreeNode.getUnittitle());
                eadContent.setUnittitle(levelTreeNode.getUnittitle());
                eadContent.setXml(eadContentXml.toString());
            } else { //We just edit the one with DB ID is key
                LOG.info("Edit key: " + getServletRequest().getParameter("key"));
                String fullStr = getServletRequest().getParameter("key");
                String keyString = fullStr.substring(3);

                eadContent = DAOFactory.instance().getEadContentDAO().findById(Long.parseLong(keyString));
                if(!eadContent.getEadid().equals(levelTreeNode.getUnitid()))
                    eadContent.setEadid(levelTreeNode.getUnitid());
                if(!eadContent.getTitleproper().equals(levelTreeNode.getUnittitle()))
                    eadContent.setTitleproper(levelTreeNode.getUnittitle());
                if(!eadContent.getUnittitle().equals(levelTreeNode.getUnittitle()))
                    eadContent.setUnittitle(levelTreeNode.getUnittitle());
                if(!eadContent.getXml().equals(eadContentXml.toString()))
                    eadContent.setXml(eadContentXml.toString());
            }


           
            eadContent = DAOFactory.instance().getEadContentDAO().store(eadContent);
            String eadContentNewId = "ec_" + eadContent.getEcId();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true).put("newId", eadContentNewId).put("dataToEdit", true);
            writer.append(jsonObject.toString());
            writer.close();
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return null;
    }

    private EadContent createDummyEadContent() {
        EadContent eadContent = new EadContent();
        eadContent.setDisplayDid(false);
        eadContent.setDisplayIntro(false);
        eadContent.setVisible(false);
        return eadContent;
    }

    public String addCLevelData(){
        try {
            Writer writer = openOutputWriter();

            CLevelTreeNode levelTreeNode = createCLevelTreeNode();
            StringWriter cLevelXml = createCLevelData(levelTreeNode);

            if(StringUtils.isEmpty(getServletRequest().getParameter("parentId")))
                throw new APEnetException(getText("holdingsGuideTreeCreation.parentIdCanNotBeNullOrEmpty"));

            String fullStr = getServletRequest().getParameter("parentId");
            String parentId = fullStr.substring(3);
            String type = fullStr.substring(0, 2);
            LOG.info("The getServletRequest() parameter parentId is '" + fullStr + "', so the parentId is '" + parentId + "' and the type is '" + type + "'");

            CLevel cLevel;
            if(StringUtils.isEmpty(getServletRequest().getParameter("dataToEdit"))){ //If empty then it is a new to create (new CLEVEL)
                cLevel = createDummyCLevel();
                cLevel.setXml(cLevelXml.toString());
                cLevel.setUnitid(levelTreeNode.getUnitid());
                cLevel.setUnittitle(levelTreeNode.getUnittitle());
            } else { //We just edit the one with DB ID is key
                LOG.info("Edit key: " + getServletRequest().getParameter("key"));
                fullStr = getServletRequest().getParameter("key");
                String keyString = fullStr.substring(3);

                cLevel = DAOFactory.instance().getCLevelDAO().findById(Long.parseLong(keyString));
                if(!cLevel.getUnitid().equals(levelTreeNode.getUnitid()))
                    cLevel.setUnitid(levelTreeNode.getUnitid());
                if(!cLevel.getUnittitle().equals(levelTreeNode.getUnittitle()))
                    cLevel.setUnittitle(levelTreeNode.getUnittitle());
                if(!cLevel.getXml().equals(cLevelXml.toString()))
                    cLevel.setXml(cLevelXml.toString());
            }

            if(type.equals(TYPE_C_LEVEL)){
                Long ecId = DAOFactory.instance().getCLevelDAO().findById(Long.parseLong(parentId)).getEcId();
                Long sizeChildren = DAOFactory.instance().getCLevelDAO().countChildCLevels(Long.parseLong(parentId));

                cLevel.setEcId(ecId);
                cLevel.setParentClId(Long.parseLong(parentId));
                cLevel.setOrderId(sizeChildren.intValue());
            } else if(type.equals(TYPE_EAD_CONTENT)){
                cLevel.setEcId(Long.parseLong(parentId));
                Long sizeChildren = DAOFactory.instance().getCLevelDAO().countTopCLevels(Long.parseLong(parentId));
                cLevel.setOrderId(sizeChildren.intValue());
            }

            cLevel = DAOFactory.instance().getCLevelDAO().store(cLevel);

            String cLevelNewId = "cl_" + cLevel.getClId();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", true).put("newId", cLevelNewId).put("dataToEdit", true);
            writer.append(jsonObject.toString());
            writer.close();
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return null;
    }

    public String createHoldingsGuide() {
        Writer writer = null;
        try {
        	Integer aiId = getAiId();
            writer = openOutputWriter();
            if(StringUtils.isEmpty(getServletRequest().getParameter("key"))){
                throw new APEnetException(getText("holdingsGuideTreeCreation.keyParameterNotBeEmpty"));
            }
            if(aiId==null){
                throw new APEnetException(getText("holdingsGuideTreeCreation.aiIdParameterCouldNotBeReadedFromSession"));
            }
            String fullStr = getServletRequest().getParameter("key");
            String keyString = fullStr.substring(3);

            EadContent eadContent = DAOFactory.instance().getEadContentDAO().findById(Long.parseLong(keyString));

            String filePath = "/" + aiId + "/";
            if(!new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + filePath).exists())
                new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + filePath).mkdir();
            String fileName = keyString + ".xml";
            filePath += fileName;

            ReconstructEadFile.reconstructEadFile(eadContent, APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + filePath);
            UpFile upFile = new UpFile();

            upFile.setAiId(aiId);
            upFile.setFileType(FileType.XML);
            upFile.setFilename(fileName);
            upFile.setPath(filePath);
            upFile.setUploadMethod(DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(UploadMethod.HTTP));

            HibernateUtil.beginDatabaseTransaction();
            DAOFactory.instance().getUpFileDAO().insertSimple(upFile);
            HibernateUtil.commitDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();

            writer.append(new JSONObject().put("success", true).toString());
            writer.close();
        } catch (Exception e){
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
            LOG.error("Error", e);
            try {
                if(writer != null){
                    writer.append(new JSONObject().put("success", false).toString());
                    writer.close();
                }
            } catch(Exception ex){
                LOG.error("Error", ex);
            }
        }
        return null;
    }

    private CLevel createDummyCLevel() {
        CLevel cLevel = new CLevel();
        cLevel.setLeaf(false);
        cLevel.setLevel("series");
        return cLevel;
    }

    public StringWriter createEadContentData(ArchivalInstitution archivalInstitution, CLevelTreeNode levelTreeNode) throws XMLStreamException, IOException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        StringWriter archdesc = new StringWriter();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(archdesc);

        writer.writeStartDocument();

        QName qName = new QName(AbstractParser.APENET_EAD, "ead");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeDefaultNamespace(AbstractParser.APENET_EAD);
        writer.writeNamespace("xlink", AbstractParser.XLINK);
        writer.writeNamespace("xsi", AbstractParser.XSI);
        writer.writeAttribute("audience", "external");

        qName = new QName(AbstractParser.APENET_EAD, "eadheader");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("countryencoding", "iso3166-1");
        writer.writeAttribute("dateencoding", "iso8601");
        writer.writeAttribute("langencoding", "iso639-2b");
        writer.writeAttribute("repositoryencoding", "iso15511");
        writer.writeAttribute("scriptencoding", "iso15924");
        writer.writeAttribute("relatedencoding", "MARC21");

        qName = new QName(AbstractParser.APENET_EAD, "eadid");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("countrycode", archivalInstitution.getCountry().getIsoname());
        writer.writeAttribute("mainagencycode", archivalInstitution.getRepositorycode());
        writer.writeAttribute("identifier", archivalInstitution.getCountry().getIsoname() + "_" + archivalInstitution.getRepositorycode());
        writer.writeCharacters(levelTreeNode.getUnitid());
        writer.writeEndElement();

        qName = new QName(AbstractParser.APENET_EAD, "filedesc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(AbstractParser.APENET_EAD, "titlestmt");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(AbstractParser.APENET_EAD, "titleproper");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeCharacters(levelTreeNode.getUnittitle());
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();

        qName = new QName(AbstractParser.APENET_EAD, "archdesc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("level", "fonds");
        writer.writeAttribute("type", "holdings_guide");
        writer.writeAttribute("encodinganalog", "3.1.4");
        writer.writeAttribute("relatedencoding", "ISAD(G)v2");

        writeDidData(writer, levelTreeNode);

        qName = new QName(AbstractParser.APENET_EAD, "dsc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();

        writer.writeEndDocument();
        writer.close();
        archdesc.close();

        return archdesc;
    }

    public StringWriter createCLevelData(CLevelTreeNode levelTreeNode) throws XMLStreamException, IOException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        StringWriter cLevel = new StringWriter();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(cLevel);

        writer.writeStartDocument();


        QName qName = new QName(AbstractParser.APENET_EAD, "c");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeDefaultNamespace(AbstractParser.APENET_EAD);
        writer.writeNamespace("xlink", AbstractParser.XLINK);
        writer.writeNamespace("xsi", AbstractParser.XSI);
        writer.writeAttribute("level", "series");
        writer.writeAttribute("encodinganalog", "3.1.4");

        writeDidData(writer, levelTreeNode);

        writer.writeEndElement();

        writer.writeEndDocument();
        writer.close();
        cLevel.close();

        return cLevel;
    }

    private void writeDidData(XMLStreamWriter writer, CLevelTreeNode levelTreeNode) throws XMLStreamException {
        QName qName = new QName(AbstractParser.APENET_EAD, "did");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(AbstractParser.APENET_EAD, "unitid");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("encodinganalog", "3.1.1");
        if(StringUtils.isNotEmpty(levelTreeNode.getUnitid()))
            writer.writeCharacters(levelTreeNode.getUnitid());
        writer.writeEndElement();
        qName = new QName(AbstractParser.APENET_EAD, "unittitle");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("encodinganalog", "3.1.2");
        if(StringUtils.isNotEmpty(levelTreeNode.getUnittitle()))
            writer.writeCharacters(levelTreeNode.getUnittitle());
        writer.writeEndElement();
        writer.writeEndElement();
        if(StringUtils.isNotEmpty(levelTreeNode.getDescription())){
            qName = new QName(AbstractParser.APENET_EAD, "scopecontent");
            writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
            writer.writeAttribute("encodinganalog", "summary");
            qName = new QName(AbstractParser.APENET_EAD, "p");
            writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
            writer.writeCharacters(levelTreeNode.getDescription());
            writer.writeEndElement();
            writer.writeEndElement();
        }
    }


    public String deleteLevelHG(){
        Writer writer = null;
        try {
            writer = openOutputWriter();
            String keyString = null;
            if (StringUtils.isNotBlank(getServletRequest().getParameter("id"))){
            	keyString = getServletRequest().getParameter("id");
            }else if(StringUtils.isNotBlank(getServletRequest().getParameter("key"))){
                String fullStr = getServletRequest().getParameter("key");
                keyString = fullStr.substring(3);
            }else {
                throw new APEnetException(getText("holdingsGuideTreeCreation.keyNotBeNullOrEmpty"));
            }

            CLevel cLevel = DAOFactory.instance().getCLevelDAO().findById(Long.parseLong(keyString));
            DAOFactory.instance().getCLevelDAO().delete(cLevel);
            writer.append(new JSONObject().put("success", true).toString());
            writer.close();
        } catch (Exception e){
            try {
                if(writer != null){
                    writer.append(new JSONObject().put("success", false).toString());
                    writer.close();
                }
            } catch(Exception ex){
                LOG.error("Error", ex);
            }
        }
        return null;
    }

    public String getCId() {
        return cId;
    }

    public void setCId(String cId) {
        this.cId = cId;
    }

    public String getHgId() {
        return hgId;
    }

    public void setHgId(String hgId) {
        this.hgId = hgId;
    }
}
