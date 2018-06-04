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
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.Ead;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.SourceGuide;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.ValidatedState;
import eu.archivesportaleurope.xml.ApeXMLConstants;

/**
 * User: Yoann Moranville
 * Date: 06/07/2011
 *
 * @author Yoann Moranville
 */
public class HgSgTreeCreation extends AjaxControllerAbstractAction {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1796389317724915629L;
	private static final String TYPE_EAD_CONTENT = "ec";
    private static final String TYPE_C_LEVEL = "cl";
    protected static final String EAD_XML_TYPE = "eadXmlTypeId";
    private String cId;
    private String id;
    protected Integer eadXmlTypeId;



    @Override
    public String execute() {
        return SUCCESS;
    }




    public CLevelTreeNode createCLevelTreeNode(){
        return new CLevelTreeNode(getServletRequest().getParameter("identifier"), getServletRequest().getParameter("name")).setDescription(getServletRequest().getParameter("desc"));
    }
    
    public Ead getEad(){
    	XmlType type = null;
    	if (eadXmlTypeId != null){
    		type = XmlType.getType(eadXmlTypeId);
    	}
    	if (XmlType.EAD_SG.equals(type)){
    		return new SourceGuide();
    	}else {
    		return new HoldingsGuide();
    	}
    }
    public String getPrefix(){
    	XmlType type = null;
    	if (eadXmlTypeId != null){
    		type = XmlType.getType(eadXmlTypeId);
    	}
    	if (XmlType.EAD_SG.equals(type)){
    		return "SG_";
    	}else {
    		return "HG_";
    	}
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

            CLevelTreeNode levelTreeNode = createCLevelTreeNode();
            EadContent eadContent;
            if(StringUtils.isEmpty(getServletRequest().getParameter("key"))){
                String eadid = getPrefix() +System.currentTimeMillis();
                eadid = eadid.substring(0,eadid.length()-4);
                StringWriter eadContentXml = createEadContentData(archivalInstitution, levelTreeNode, eadid);
            	Ead hgOrSg = getEad();

            	hgOrSg.setEadid(eadid);
            	hgOrSg.setTitle(levelTreeNode.getUnittitle());
            	hgOrSg.setDynamic(true);
            	hgOrSg.setValidated(ValidatedState.VALIDATED);
            	hgOrSg.setConverted(true);
            	hgOrSg.setUploadDate(new Date());
            	hgOrSg.setAiId(getAiId());
            	hgOrSg.setArchivalInstitution(archivalInstitution);
                UploadMethod uploadMethod = DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(UploadMethod.HTTP);
            	hgOrSg.setUploadMethod(uploadMethod);
            	XmlType type = XmlType.getType(eadXmlTypeId);
            	String startPath = CreateEadTask.getPath(type, archivalInstitution);
            	hgOrSg.setPath(startPath+ APEnetUtilities.convertToFilename(eadid)+ ".xml");
            	hgOrSg = DAOFactory.instance().getEadDAO().store(hgOrSg);
                eadContent = createDummyEadContent();
                if (hgOrSg instanceof SourceGuide){
                	eadContent.setSgId(hgOrSg.getId());
                }else {
                	eadContent.setHgId(hgOrSg.getId());
                }
                eadContent.setEadid(eadid);
                eadContent.setTitleproper(levelTreeNode.getUnittitle());
                eadContent.setUnittitle(levelTreeNode.getUnittitle());
                eadContent.setXml(eadContentXml.toString());
            } else { //We just edit the one with DB ID is key
                String fullStr = getServletRequest().getParameter("key");
                String keyString = fullStr.substring(3);

                eadContent = DAOFactory.instance().getEadContentDAO().findById(Long.parseLong(keyString));
                Ead ead = eadContent.getEad();
                StringWriter eadContentXml = createEadContentData(archivalInstitution, levelTreeNode, ead.getEadid());
                if(!eadContent.getTitleproper().equals(levelTreeNode.getUnittitle()))
                    eadContent.setTitleproper(levelTreeNode.getUnittitle());
                if(!eadContent.getUnittitle().equals(levelTreeNode.getUnittitle())){
                	eadContent.setUnittitle(levelTreeNode.getUnittitle());
                	ead.setTitle(levelTreeNode.getUnittitle());
                }
                    
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
            boolean addLevel = StringUtils.isEmpty(getServletRequest().getParameter("key"));
            if(addLevel){ 
            	//If empty then it is a new to create (new CLEVEL)
                cLevel = createDummyCLevel();
                cLevel.setXml(cLevelXml.toString());
                cLevel.setUnitid(levelTreeNode.getUnitid());
                cLevel.setUnittitle(levelTreeNode.getUnittitle());

            } else { 
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
                cLevel.setEcId(ecId);
                cLevel.setParentId(Long.parseLong(parentId));
                if (addLevel){
	                Long sizeChildren = DAOFactory.instance().getCLevelDAO().countChildCLevels(Long.parseLong(parentId));
	                cLevel.setOrderId(sizeChildren.intValue());
                }
            } else if(type.equals(TYPE_EAD_CONTENT)){
                cLevel.setEcId(Long.parseLong(parentId));
                if (addLevel){
                	Long sizeChildren = DAOFactory.instance().getCLevelDAO().countTopCLevels(Long.parseLong(parentId));
                	cLevel.setOrderId(sizeChildren.intValue());
                }
            }

            cLevel = DAOFactory.instance().getCLevelDAO().store(cLevel);

            String cLevelNewId = "cl_" + cLevel.getId();
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
        return cLevel;
    }

    public StringWriter createEadContentData(ArchivalInstitution archivalInstitution, CLevelTreeNode levelTreeNode, String eadid) throws XMLStreamException, IOException {
        XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
        StringWriter archdesc = new StringWriter();
        XMLStreamWriter writer = xmlOutputFactory.createXMLStreamWriter(archdesc);

        writer.writeStartDocument();

        QName qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "ead");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeDefaultNamespace(ApeXMLConstants.APE_EAD_NAMESPACE);
        writer.writeNamespace("xlink", ApeXMLConstants.XLINK_NAMESPACE);
        writer.writeNamespace("xsi", ApeXMLConstants.XSI_NAMESPACE);
        writer.writeAttribute("audience", "external");

        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "eadheader");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("countryencoding", "iso3166-1");
        writer.writeAttribute("dateencoding", "iso8601");
        writer.writeAttribute("langencoding", "iso639-2b");
        writer.writeAttribute("repositoryencoding", "iso15511");
        writer.writeAttribute("scriptencoding", "iso15924");
        writer.writeAttribute("relatedencoding", "MARC21");

        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "eadid");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("countrycode", archivalInstitution.getCountry().getIsoname());
        writer.writeAttribute("mainagencycode", archivalInstitution.getRepositorycode());
        writer.writeAttribute("identifier", archivalInstitution.getCountry().getIsoname() + "_" + archivalInstitution.getRepositorycode());
        writer.writeCharacters(eadid);
        writer.writeEndElement();

        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "filedesc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "titlestmt");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "titleproper");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeCharacters(levelTreeNode.getUnittitle());
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "revisiondesc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "change");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "date");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeEndElement();
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "item");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeCharacters(TransformationTool.getFullEADVersion());
        writer.writeEndElement();
        writer.writeEndElement();
        writer.writeEndElement();
        
        writer.writeEndElement();
        
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "archdesc");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("level", "fonds");
        writer.writeAttribute("type", "holdings_guide");
        writer.writeAttribute("encodinganalog", "3.1.4");
        writer.writeAttribute("relatedencoding", "ISAD(G)v2");

        writeDidData(writer, levelTreeNode);

        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "dsc");
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


        QName qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "c");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeDefaultNamespace(ApeXMLConstants.APE_EAD_NAMESPACE);
        writer.writeNamespace("xlink", ApeXMLConstants.XLINK_NAMESPACE);
        writer.writeNamespace("xsi", ApeXMLConstants.XSI_NAMESPACE);
        writer.writeAttribute("encodinganalog", "3.1.4");

        writeDidData(writer, levelTreeNode);

        writer.writeEndElement();

        writer.writeEndDocument();
        writer.close();
        cLevel.close();

        return cLevel;
    }

    private void writeDidData(XMLStreamWriter writer, CLevelTreeNode levelTreeNode) throws XMLStreamException {
        QName qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "did");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "unitid");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("encodinganalog", "3.1.1");
        if(StringUtils.isNotEmpty(levelTreeNode.getUnitid()))
            writer.writeCharacters(levelTreeNode.getUnitid());
        writer.writeEndElement();
        qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "unittitle");
        writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
        writer.writeAttribute("encodinganalog", "3.1.2");
        if(StringUtils.isNotEmpty(levelTreeNode.getUnittitle()))
            writer.writeCharacters(levelTreeNode.getUnittitle());
        writer.writeEndElement();
        writer.writeEndElement();
        if(StringUtils.isNotEmpty(levelTreeNode.getDescription())){
            qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "scopecontent");
            writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
            writer.writeAttribute("encodinganalog", "summary");
            qName = new QName(ApeXMLConstants.APE_EAD_NAMESPACE, "p");
            writer.writeStartElement(qName.getPrefix(), qName.getLocalPart(), qName.getNamespaceURI());
            writer.writeCharacters(levelTreeNode.getDescription());
            writer.writeEndElement();
            writer.writeEndElement();
        }
    }


    public String deleteCLevel(){
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




	public String getId() {
		return id;
	}




	public void setId(String id) {
		this.id = id;
	}




	public Integer getEadXmlTypeId() {
		return eadXmlTypeId;
	}




	public void setEadXmlTypeId(Integer eadXmlTypeId) {
		this.eadXmlTypeId = eadXmlTypeId;
	}


}
