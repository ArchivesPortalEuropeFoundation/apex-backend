package eu.apenet.dashboard.actions.ajax;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

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
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.HoldingsGuide;
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
    /**
	 * 
	 */
	private static final long serialVersionUID = -1796389317724915629L;
	private static final String TYPE_EAD_CONTENT = "ec";
    private static final String TYPE_C_LEVEL = "cl";
    private String cId;
    private String hgId;




    @Override
    public String execute() {
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
            ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitution(getAiId());
            String eadid = "HG_" + archivalInstitution.getRepositorycode() + "_"+System.currentTimeMillis();
            CLevelTreeNode levelTreeNode = createCLevelTreeNode();
            levelTreeNode.setUnitid(eadid);
            StringWriter eadContentXml = createEadContentData(archivalInstitution, levelTreeNode);
            JpaUtil.beginDatabaseTransaction();
            EadContent eadContent;
            if(StringUtils.isEmpty(getServletRequest().getParameter("dataToEdit"))){
            	Ead holdingsGuide = new HoldingsGuide();

            	holdingsGuide.setEadid(eadid);
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
            	holdingsGuide.setPathApenetead(startPath+ APEnetUtilities.convertToFilename(eadid)+ ".xml");
            	holdingsGuide = DAOFactory.instance().getEadDAO().store(holdingsGuide);
                eadContent = createDummyEadContent();
                eadContent.setHgId(holdingsGuide.getId());
                eadContent.setEadid(eadid);
                eadContent.setTitleproper(levelTreeNode.getUnittitle());
                eadContent.setUnittitle(levelTreeNode.getUnittitle());
                eadContent.setXml(eadContentXml.toString());
            } else { //We just edit the one with DB ID is key
                LOG.info("Edit key: " + getServletRequest().getParameter("key"));
                String fullStr = getServletRequest().getParameter("key");
                String keyString = fullStr.substring(3);

                eadContent = DAOFactory.instance().getEadContentDAO().findById(Long.parseLong(keyString));
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
        
        qName = new QName(AbstractParser.APENET_EAD, "revisiondesc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(AbstractParser.APENET_EAD, "change");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(AbstractParser.APENET_EAD, "date");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeEndElement();
        qName = new QName(AbstractParser.APENET_EAD, "item");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeCharacters(TransformationTool.getFullVersion());
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
