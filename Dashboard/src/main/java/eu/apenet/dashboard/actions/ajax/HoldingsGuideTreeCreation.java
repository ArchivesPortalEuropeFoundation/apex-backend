package eu.apenet.dashboard.actions.ajax;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import eu.archivesportaleurope.persistence.jpa.JpaUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.indexing.AbstractParser;
import eu.apenet.dashboard.indexing.EADNamespaceContext;
import eu.apenet.dashboard.indexing.EADParser;
import eu.apenet.dashboard.manual.ReconstructEadFile;
import eu.apenet.dashboard.manual.hgTreeCreation.CLevelTreeNode;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;
import eu.apenet.persistence.vo.ValidatedState;

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
        try {
        	Integer aiId = getAiId();
            if(hgId != null){
                int hgIdInt = Integer.parseInt(hgId);
                HoldingsGuide holdingsGuide = DAOFactory.instance().getHoldingsGuideDAO().findById(hgIdInt);
                String stateOfHG = holdingsGuideIsNotIndexed(holdingsGuide);
                LOG.info("State of HG: " + stateOfHG);
                if(stateOfHG.equals(ERROR))
                    return ERROR;
                if(stateOfHG.equals(SUCCESS))
                    EADParser.parseEad(holdingsGuide);
                getAllFindingAids(holdingsGuide.getArchivalInstitution()); //In order to retrieve it a load time and have the data in the session
            } else if(aiId != null){
                getAllFindingAids(DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId));
            }
            return SUCCESS;
        } catch (Exception e){
            LOG.error("Error executing HG tree creation page", e);
            return ERROR;
        }
    }

    public String getPossibleFAs() throws Exception {
        Writer writer = openOutputWriter();

        List<FindingAid> findingAids = getAllFindingAids(DAOFactory.instance().getArchivalInstitutionDAO().findById(getAiId()));

        JSONArray possibleFAs = new JSONArray();
        JSONObject simpleFA;
        for(FindingAid findingAid : findingAids){
            simpleFA = new JSONObject();
            simpleFA.put("id", findingAid.getId());
            simpleFA.put("eadId", findingAid.getEadid());
            simpleFA.put("title", findingAid.getTitle());
            possibleFAs.put(simpleFA);
        }
        writer.append(possibleFAs.toString());
        writer.close();

        return null;
    }

    private List<FindingAid> getAllFindingAids(ArchivalInstitution archivalInstitution){
        if(session == null)
            session = request.getSession();

        List<FindingAid> findingAids;

        if(session.getAttribute(ALL_FINDING_AIDS_SESSION) == null)
            findingAids = DAOFactory.instance().getFindingAidDAO().getFindingAidsNotLinked(archivalInstitution.getAiId());
        else {
            findingAids = (List<FindingAid>) session.getAttribute(ALL_FINDING_AIDS_SESSION);
        }

        session.setAttribute(ALL_FINDING_AIDS_SESSION, findingAids);

        return findingAids;
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
        LOG.info("name: " + request.getParameter("name"));
        LOG.info("identifier: " + request.getParameter("identifier"));
        LOG.info("desc: " + request.getParameter("desc"));
        return new CLevelTreeNode(request.getParameter("identifier"), request.getParameter("name")).setDescription(request.getParameter("desc"));
    }

    /**
     * First AJAX request, we create the EAD CONTENT data with a dummy file and save it in the DB
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

            EadContent eadContent;
            if(StringUtils.isEmpty(request.getParameter("dataToEdit"))){
                eadContent = createDummyEadContent();
                eadContent.setEadid(levelTreeNode.getUnitid());
                eadContent.setTitleproper(levelTreeNode.getUnittitle());
                eadContent.setUnittitle(levelTreeNode.getUnittitle());
                eadContent.setXml(eadContentXml.toString());
            } else { //We just edit the one with DB ID is key
                LOG.info("Edit key: " + request.getParameter("key"));
                String fullStr = request.getParameter("key");
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


            JpaUtil.beginDatabaseTransaction();
            eadContent = DAOFactory.instance().getEadContentDAO().store(eadContent);
            JpaUtil.commitDatabaseTransaction();
            JpaUtil.closeDatabaseSession();

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

            if(StringUtils.isEmpty(request.getParameter("parentId")))
                throw new APEnetException("In addCLevelData(), the parameter parentId can not be null or empty");

            String fullStr = request.getParameter("parentId");
            String parentId = fullStr.substring(3);
            String type = fullStr.substring(0, 2);
            LOG.info("The request parameter parentId is '" + fullStr + "', so the parentId is '" + parentId + "' and the type is '" + type + "'");

            CLevel cLevel;
            if(StringUtils.isEmpty(request.getParameter("dataToEdit"))){ //If empty then it is a new to create (new CLEVEL)
                cLevel = createDummyCLevel();
                cLevel.setXml(cLevelXml.toString());
                cLevel.setUnitid(levelTreeNode.getUnitid());
                cLevel.setUnittitle(levelTreeNode.getUnittitle());
            } else { //We just edit the one with DB ID is key
                LOG.info("Edit key: " + request.getParameter("key"));
                fullStr = request.getParameter("key");
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

            JpaUtil.beginDatabaseTransaction();
            cLevel = DAOFactory.instance().getCLevelDAO().store(cLevel);
            JpaUtil.commitDatabaseTransaction();
            JpaUtil.closeDatabaseSession();

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
            if(StringUtils.isEmpty(request.getParameter("key"))){
                throw new APEnetException("The key parameter is empty and should not be...");
            }
            if(aiId==null){
                throw new APEnetException("The aiId parameter could not be readed from session...");
            }
            String fullStr = request.getParameter("key");
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

    private CLevel createDummyCLevelItem() {
        CLevel cLevel = new CLevel();
        cLevel.setLeaf(true);
        cLevel.setLevel("item");
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

    public String addFAsToCurrentLevel() {
        try {
            Writer writer = openOutputWriter();
            Integer aiId = getAiId();
            //To get the FA to be a <c> part of HG
            String[] faIdentifiers = request.getParameter("selectedFAs").replace("[", "").replace("]", "").split(",");

            XPath xPath = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
            xPath.setNamespaceContext(new EADNamespaceContext());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc;
            XPathExpression unittitleExpr = xPath.compile("/ead:c/ead:did/ead:unittitle/text()");
            XPathExpression unitidExpr = xPath.compile("/ead:c/ead:did/ead:unitid/text()");

            if(StringUtils.isEmpty(request.getParameter("key"))){
                throw new APEnetException("The parameter key can not be null or empty");
            }
            String fullStr = request.getParameter("key");
            String keyId = fullStr.substring(3);
            String type = fullStr.substring(0, 2);

            int sizeChildren = 0;
            if(type.equals(TYPE_C_LEVEL))
                sizeChildren = DAOFactory.instance().getCLevelDAO().countChildCLevels(Long.parseLong(keyId)).intValue();
            else if(type.equals(TYPE_EAD_CONTENT))
                sizeChildren = DAOFactory.instance().getCLevelDAO().countTopCLevels(Long.parseLong(keyId)).intValue();

            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject;

            for(String faIdentifier : faIdentifiers){
                FindingAid findingAid = DAOFactory.instance().getFindingAidDAO().findById(Integer.parseInt(faIdentifier));
                InputStream xslIs = TransformationTool.class.getResourceAsStream("/xsl/fa2hg.xsl");

                String filePath = APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + findingAid.getPathApenetead();
                File faFile;
                if(!(faFile = new File(filePath)).exists())
                    faFile = new File(APEnetUtilities.getConfig().getRepoDirPath() + findingAid.getPathApenetead());
                InputStream fileIs = FileUtils.openInputStream(faFile);

                File outputTemp = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + aiId + APEnetUtilities.FILESEPARATOR + ".temp_file.xml");
                Map<String, String> params = new HashMap<String, String>();
                params.put("addXMLNS", "true");
                TransformationTool.createTransformation(fileIs, outputTemp, xslIs, params, false, false, null, true);
                String cLevelXml = FileUtils.readFileToString(outputTemp, UTF8); //UTF8 important!

                doc = builder.parse(new InputSource(new StringReader(cLevelXml)));
                doc.getDocumentElement().normalize();

                String unittitleString = (String) unittitleExpr.evaluate(doc, XPathConstants.STRING);
                String unitidString = (String) unitidExpr.evaluate(doc, XPathConstants.STRING);

                CLevel cLevel = createDummyCLevelItem();
                cLevel.setXml(cLevelXml);
                cLevel.setUnitid(unitidString);
                cLevel.setUnittitle(unittitleString);
                cLevel.setOrderId(sizeChildren++);

                if(type.equals(TYPE_C_LEVEL))
                    cLevel.setParentClId(Long.parseLong(keyId));
                else if(type.equals(TYPE_EAD_CONTENT))
                    cLevel.setEcId(Long.parseLong(keyId));

                JpaUtil.beginDatabaseTransaction();
                cLevel = DAOFactory.instance().getCLevelDAO().store(cLevel);
                JpaUtil.commitDatabaseTransaction();
                JpaUtil.closeDatabaseSession();

                jsonObject = new JSONObject();
                jsonObject.put("identifier", cLevel.getUnitid()).put("title", cLevel.getUnittitle()).put("isFolder", false).put("key", TYPE_C_LEVEL + "_" + cLevel.getClId());
                jsonArray.put(jsonObject);
            }

            deleteFAsFromList(faIdentifiers);

            jsonObject = new JSONObject();
            jsonObject.put("success", true).put("data", jsonArray);
            writer.append(jsonObject.toString());
            LOG.info("Sending jsonObj: " + jsonObject.toString());
            writer.close();
        } catch (Exception e) {
            LOG.error("Error", e);
        }
        return null;
    }

    private void deleteFAsFromList(String[] faIdentifiers){
        List<String> faIdentifierStr = Arrays.asList(faIdentifiers);

        if(session == null)
            session = request.getSession();

        if(session.getAttribute(ALL_FINDING_AIDS_SESSION) != null){
            List<FindingAid> findingAids = (List<FindingAid>) session.getAttribute(ALL_FINDING_AIDS_SESSION);
            for(int i = 0; i < findingAids.size(); i++){
                FindingAid fa = findingAids.get(i);
                if(faIdentifierStr.contains(fa.getId() + "")){
                    findingAids.remove(fa);
                    i--;
                }
            }
            session.setAttribute(ALL_FINDING_AIDS_SESSION, findingAids);
        }
    }

    public String deleteLevelHG(){
        Writer writer = null;
        try {
            writer = openOutputWriter();
            if(StringUtils.isEmpty(request.getParameter("key")))
                throw new APEnetException("The parameter key can not be null or empty");
            String fullStr = request.getParameter("key");
            String keyString = fullStr.substring(3);
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
