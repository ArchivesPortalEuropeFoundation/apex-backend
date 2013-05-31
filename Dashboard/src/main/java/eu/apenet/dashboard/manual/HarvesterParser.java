package eu.apenet.dashboard.manual;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLOutputFactory2;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import eu.apenet.commons.exceptions.APEnetException;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.ead.xml.AbstractParser;
import eu.apenet.dashboard.services.ead.xml.EADNamespaceContext;
import eu.apenet.dpt.utils.service.TransformationTool;
import eu.apenet.dpt.utils.util.SeparateFinnishFiles;
import eu.apenet.persistence.dao.CLevelDAO;
import eu.apenet.persistence.dao.EadContentDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CLevel;
import eu.apenet.persistence.vo.EadContent;
import eu.apenet.persistence.vo.FileType;
import eu.apenet.persistence.vo.UpFile;
import eu.apenet.persistence.vo.UploadMethod;

/**
 * User: Yoann Moranville
 * Date: 3/22/11
 *
 * @author Yoann Moranville
 */
public class HarvesterParser extends AbstractParser {
    private final static Logger LOG = Logger.getLogger(HarvesterParser.class);
    private final static String UTF8 = "utf-8";

    private File file;
    private LinkedList<File> files;
    private int ai_id;
    private Map<Integer, Boolean> eadIdMap;

    public HarvesterParser(File file, int ai_id){
        this.file = file;
        this.ai_id = ai_id;
    }

    public HarvesterParser(LinkedList<File> files, int ai_id){
        this.files = files;
        this.ai_id = ai_id;
    }

    /**
     * Function used to separate the FAs of an EAD harvesting
     * @return A list of the FAs that have been inserted in the UpFile table
     */
    public LinkedList<UpFile> analyzeStream(){
        LinkedList<UpFile> uploadedFiles = new LinkedList<UpFile>();
        try {
            if(file == null)
                throw new Exception("Variable file is null");

            LOG.info("Analyzing stream: " + file.getName());
            String directory = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("/")+1);
            LOG.info("Directory: " + directory);
            SeparateFinnishFiles separateFinnishFiles = new SeparateFinnishFiles();
            List<String> listPaths = separateFinnishFiles.separateFile(file, new File(directory));
            for(String path : listPaths)
                uploadedFiles.add(createDBentry(path));

            if(!file.delete())
                FileUtils.forceDelete(file);

            return uploadedFiles;
        } catch (Exception e){
            LOG.error("OUPSI", e);
            return null;
        }
    }

    public LinkedList<UpFile> dublinCoreToEad() throws APEnetException{
        LOG.info("Starting DC to EAD process (creation of all CLevels from the DC elements of the harvested files)");
        try {
            if(files == null)
                throw new Exception("Variable files is null");

            Iterator<File> fileIterator = files.iterator();
            int fullSize = files.size();
            int currentSize = 0;

            XMLInputFactory2 inputFactory = (XMLInputFactory2) XMLInputFactory2.newInstance();
            XMLStreamReader2 xmlReader = inputFactory.createXMLStreamReader(fileIterator.next()); //First iteration can never be empty

            XMLOutputFactory2 xmlOutputFactory = (XMLOutputFactory2)XMLOutputFactory2.newInstance();

            OutputStream findingAidOutputStream2 = null;
            XMLStreamWriter2 findingAidXmlWriter2 = null;

            boolean isInsideRecord = false;

            XPath xPath = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
            xPath.setNamespaceContext(new EADNamespaceContext());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            XPathExpression unitidExpr = xPath.compile("/ead:c/ead:did/ead:unitid/text()");
            XPathExpression unittitleExpr = xPath.compile("/ead:c/ead:did/ead:unittitle/text()");
            XPathExpression levelExpr = xPath.compile("/ead:c/@level");

            //We begin a creation of all the C Levels, but we do not know the "order" so we create a dummy eadcontent that will be their "father"
            HibernateUtil.beginDatabaseTransaction();
            EadContent eadContent = new EadContent();
            eadContent.setEadid("OAI_SET_" + ai_id);
            DAOFactory.instance().getEadContentDAO().insertSimple(eadContent);
            HibernateUtil.commitDatabaseTransaction();


            int event;
            int numberOfClevel = 0;
            while(xmlReader.hasNext()){
                event = xmlReader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if(xmlReader.getLocalName().equals("dc")) {
                        findingAidOutputStream2 = new ByteArrayOutputStream();
                        findingAidXmlWriter2 = (XMLStreamWriter2) xmlOutputFactory.createXMLStreamWriter(findingAidOutputStream2, UTF8);

                        findingAidXmlWriter2.writeStartElement(xmlReader.getPrefix(), xmlReader.getLocalName(), xmlReader.getNamespaceURI());
                        findingAidXmlWriter2.writeNamespace("ns2", "http://purl.org/dc/elements/1.1/");
                        findingAidXmlWriter2.writeNamespace("ns3", "http://www.openarchives.org/OAI/2.0/oai_dc/");

                        isInsideRecord = true;
                    } else if(isInsideRecord) {
                        findingAidXmlWriter2.writeStartElement(xmlReader.getPrefix(), xmlReader.getLocalName(), xmlReader.getNamespaceURI());
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if(isInsideRecord)
                        findingAidXmlWriter2.writeEndElement();

                    if(xmlReader.getLocalName().equals("dc")){
                        isInsideRecord = false;

                        findingAidXmlWriter2.close();
                        findingAidOutputStream2.close();

                        File outputFile = new File(APEnetUtilities.getDashboardConfig().getTempAndUpDirPath() + APEnetUtilities.FILESEPARATOR + ai_id + APEnetUtilities.FILESEPARATOR + "oai_" + ai_id + ".xml");
                        TransformationTool.createTransformation(IOUtils.toInputStream(findingAidOutputStream2.toString()), outputFile, HarvesterParser.class.getResourceAsStream("/dc2c.xsl"), null, true, true, null, true, null);

                        Document doc = builder.parse(outputFile);
                        doc.getDocumentElement().normalize();

                        String unittitleString = (String) unittitleExpr.evaluate(doc, XPathConstants.STRING);
                        String levelString = (String) levelExpr.evaluate(doc, XPathConstants.STRING);
                        String unitidString = "";
                        NodeList list = (NodeList) unitidExpr.evaluate(doc, XPathConstants.NODESET);
                        for (int i = 0; i < list.getLength(); i++) {
                            String val = list.item(i).getNodeValue().trim();
                            if(StringUtils.isNotEmpty(val))
                                unitidString = val;
                        }

                        CLevel cLevel = new CLevel();
                        cLevel.setLevel(levelString);
                        cLevel.setOrderId(numberOfClevel++);
                        cLevel.setUnittitle(unittitleString);
                        cLevel.setUnitid(unitidString.replaceAll("-", "/"));
                        cLevel.setXml(FileUtils.readFileToString(outputFile, UTF8));

                        outputFile.delete();

                        HibernateUtil.beginDatabaseTransaction();
                        try {
                            cLevel.setEcId(eadContent.getEcId());

                            DAOFactory.instance().getCLevelDAO().insertSimple(cLevel);
                            HibernateUtil.commitDatabaseTransaction();
                        } catch (Exception e){
                            LOG.error("Error", e);
                            HibernateUtil.rollbackDatabaseTransaction();
                        }
                        HibernateUtil.closeDatabaseSession();
                    }
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    if(isInsideRecord)
                        findingAidXmlWriter2.writeCharacters(xmlReader.getText());
                } else if (event == XMLStreamConstants.END_DOCUMENT){
                    if(fileIterator.hasNext()) {
                        LOG.info("Examined file: " + (++currentSize) + "/" + fullSize);
                        xmlReader = inputFactory.createXMLStreamReader(fileIterator.next());
                    }
                }
            }

            deleteAllHarvestedFiles(files);
            
            if(findingAidXmlWriter2 != null)
                findingAidXmlWriter2.close();
            if(findingAidOutputStream2 != null)
                findingAidOutputStream2.close();
        } catch (Exception e){
            LOG.error("Error", e);
            throw new APEnetException(e);
        }
        return null;
    }

    private void deleteAllHarvestedFiles(List<File> files) {
        for(File file : files)
            file.delete();
    }

    public int dbToEad(String tmpDirPath) {
        final int MAX_ELEMENTS = 100;
        LOG.info("Starting Database conversion into EAD files");
        EadContent eadContent = DAOFactory.instance().getEadContentDAO().getEadContentByEadid("OAI_SET_" + ai_id);
        ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(ai_id);
        String mainagencycode = archivalInstitution.getRepositorycode();
        int from = 0;
        CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
        List<CLevel> cLevels = cLevelDAO.findTopCLevels(eadContent.getEcId(), from, MAX_ELEMENTS);
        Map<String, String> idsWithTypes = new HashMap<String, String>();
        Map<String, Long> idsWithUnitids = new HashMap<String, Long>();
        while(cLevels.size() != 0) {
            from += MAX_ELEMENTS;
            for(CLevel cLevel : cLevels) {
                idsWithTypes.put(cLevel.getUnitid(), cLevel.getLevel());
                idsWithUnitids.put(cLevel.getUnitid(), cLevel.getClId());
            }
            cLevels = cLevelDAO.findTopCLevels(eadContent.getEcId(), from, MAX_ELEMENTS);
        }
        return dbToEad(eadContent, idsWithTypes, idsWithUnitids, mainagencycode, tmpDirPath);
    }
    
    public int dbToEad(EadContent eadContent, Map<String, String> idsWithTypes, Map<String, Long> idsWithUnitids, String mainagencycode, String tmpDirPath) {
        LOG.info("idsWithType map size: " + idsWithTypes.size());
        idsWithTypes = sortHashMapByKey(idsWithTypes);
        LOG.info("idsWithType map size after sorting: " + idsWithTypes.size());
//        printList_simple(idsWithTypes);
        Map<String, List<String>> organizedIds = createOrganizedMapOfId(idsWithTypes);
        LOG.info("organizedIds map size: " + organizedIds.size());
        LOG.info("idsWithType map size after sorting: " + idsWithTypes.size());
//        printList(organizedIds);
        try {
            return createHierarchyAndEadFile(organizedIds, idsWithUnitids, eadContent, mainagencycode, tmpDirPath);
        } catch (APEnetException e){
            LOG.error("ERROR", e); //To do
        }
        return -1;
    }

    public Map<String, String> sortHashMapByKey(Map<String, String> passedMap) {
        List<String> mapKeys = new ArrayList<String>(passedMap.keySet());
        Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);

        Map<String, String> sortedMap = new LinkedHashMap<String, String>(mapKeys.size());

        for(String orderedKey : mapKeys)
            sortedMap.put(orderedKey, passedMap.get(orderedKey));
        return sortedMap;
    }

    private CLevel removeClevel(String id, CLevelDAO cLevelDAO, Long eadContentId) throws APEnetException {
        try {
            CLevel cLevel = cLevelDAO.findByUnitid(id, eadContentId);
            DAOFactory.instance().getCLevelDAO().delete(cLevel);
            return cLevel;
        } catch (Exception e) {
            throw new APEnetException("CLevel (unitid: '" + id + "') was not found in the database");
        }
    }

    private EadContent createEadContent(CLevel cLevel, String mainagencycode, String tmpDirPath) throws Exception {
        EadContent eadContent = new EadContent();
        eadContent.setTitleproper(cLevel.getUnittitle());
        eadContent.setEadid(cLevel.getUnitid());
        eadContent.setUnittitle(cLevel.getUnittitle());
        eadContent.setVisible(false);

        File outputFile = new File(tmpDirPath + APEnetUtilities.FILESEPARATOR + ai_id + APEnetUtilities.FILESEPARATOR + "oai_" + ai_id + ".xml");
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mainagencycode", mainagencycode);
            TransformationTool.createTransformation(IOUtils.toInputStream(cLevel.getXml(), UTF8), outputFile, HarvesterParser.class.getResourceAsStream("/c2ead.xsl"), params, true, true, null, true, null);
            eadContent.setXml(FileUtils.readFileToString(outputFile, UTF8));

            FileUtils.forceDelete(outputFile);

            HibernateUtil.beginDatabaseTransaction();
            DAOFactory.instance().getEadContentDAO().insertSimple(eadContent);
            HibernateUtil.commitDatabaseTransaction();
            return eadContent;
        } catch (Exception e){
            LOG.error("Error", e);
            if(outputFile.exists())
                outputFile.delete();
            throw e;
        }
    }

    public static Map<String, List<String>> createOrganizedMapOfId(Map<String, String> idsWithTypes){
        LOG.info("createOrganizedMapOfId");
        Map<String, List<String>> listFas = new LinkedHashMap<String, List<String>>();

        List<String> keySet = new LinkedList<String>(idsWithTypes.keySet());
        for(int i = 0; i < keySet.size(); i++){
            String id = keySet.get(i);
            if(idsWithTypes.get(id).equals("fonds")){
                idsWithTypes.remove(id);
                listFas.put(id, new ArrayList<String>());
            }
        }

        for(String id : idsWithTypes.keySet()){
            if(listFas.get(id) != null && listFas.get(id).size() != 0)
                break;
            String smallId = id;
            while(listFas.get(smallId) == null && !smallId.equals("")){
                int i;
                if((i = smallId.lastIndexOf("/")) == -1)
                    break;
                smallId = smallId.substring(0, i);
            }
            if(listFas.get(smallId) != null)
                listFas.get(smallId).add(id);
        }

        LOG.info("createOrganizedMapOfId finished");
        return listFas;
    }

    private void createEadFile(Node node, String tmpDirPath){
        OutputStream outputStream = null;
        try {
            String filePath = "/" + ai_id;

            if(!new File(tmpDirPath + filePath).exists())
                new File(tmpDirPath + filePath).mkdir();
            String fileName = node.getId().replaceAll("/", "_") + ".xml";
            filePath += "/" + fileName;
            outputStream = new FileOutputStream(new File(tmpDirPath + filePath));

            CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
            EadContentDAO eadContentDAO = DAOFactory.instance().getEadContentDAO();
            EadContent eadContent = eadContentDAO.getEadContentByEadid(node.getId());

            EadCreator eadCreator = new EadCreator(outputStream);
            eadCreator.writeEadContent(eadContent.getXml());

            List<CLevel> cLevels = cLevelDAO.findTopCLevelsOrderUnitid(eadContent.getEcId());
            for(CLevel cLevel : cLevels){
                eadCreator.writeEadContent(cLevel.getXml(), cLevel.getLevel());
                writeChildren(cLevel.getClId(), eadCreator);
                eadCreator.closeCTag();
                cLevelDAO.delete(cLevel);
            }
            eadContentDAO.delete(eadContent);
            eadCreator.closeEndTags();
            eadCreator.closeWriter();
            outputStream.flush();
            outputStream.close();

            UpFile upFile = new UpFile();
            upFile.setAiId(ai_id);
            upFile.setFileType(FileType.XML);
            upFile.setFilename(fileName);
            upFile.setPath(filePath);
            upFile.setUploadMethod(DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(UploadMethod.OAI_PMH));

//            HibernateUtil.beginDatabaseTransaction();
            DAOFactory.instance().getUpFileDAO().insertSimple(upFile);
//            HibernateUtil.commitDatabaseTransaction();
//            HibernateUtil.closeDatabaseSession();
        } catch (Exception e){
            HibernateUtil.rollbackDatabaseTransaction();
            HibernateUtil.closeDatabaseSession();
            LOG.error("Error", e);
            if(outputStream != null){
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    public List<CLevel> writeChildren(Long parentId, EadCreator eadCreator){
        try {
            List<CLevel> children = DAOFactory.instance().getCLevelDAO().findChildrenOrderUnitId(parentId);
            for(CLevel child : children){
                eadCreator.writeEadContent(child.getXml(), child.getLevel());
                writeChildren(child.getClId(), eadCreator);
                eadCreator.closeCTag();
            }
        } catch (Exception e){
            LOG.error(e);
        }
        return null;
    }

    private void createLevelsInDB(Node node, int level, Long eadContentId, Long oldEadContentId){
        level++;
        for(Node child : node.getChildren()){
            if(level != 0){
                //1. Get the correct current CLevel from DB
                CLevel childCLevel = DAOFactory.instance().getCLevelDAO().findByUnitid(child.getId(), oldEadContentId);
                //2. Get the node parent (never null because it is not the ROOT)
                //3. Get the correct parent CLevel from DB
                Long parentCLevelId = DAOFactory.instance().getCLevelDAO().getClIdByUnitid(node.getId(), eadContentId);
                //4. Insert in the correct current CLevel the ID of the correct parent CLevel in the DB
                childCLevel.setParentClId(parentCLevelId);
                childCLevel.setEcId(eadContentId);
                //5. Save
//                HibernateUtil.beginDatabaseTransaction();
//                DAOFactory.instance().getCLevelDAO().update(childCLevel);
                HibernateUtil.getDatabaseSession().update(childCLevel);
//                HibernateUtil.commitDatabaseTransaction();
//                HibernateUtil.closeDatabaseSession();
            }
            createLevelsInDB(child, level, eadContentId, oldEadContentId);
        }
    }

    private UpFile createDBentry(String filename){
        try {
            HibernateUtil.beginDatabaseTransaction();
            UpFile upFile = new UpFile();
            upFile.setAiId(ai_id);
            upFile.setFileType(FileType.XML);
            upFile.setFilename(filename);
            upFile.setPath(APEnetUtilities.FILESEPARATOR + ai_id + APEnetUtilities.FILESEPARATOR + filename);
            upFile.setUploadMethod(DAOFactory.instance().getUploadMethodDAO().getUploadMethodByMethod(UploadMethod.OAI_PMH));
            DAOFactory.instance().getUpFileDAO().store(upFile);
            HibernateUtil.commitDatabaseTransaction();
            return upFile;
        } catch(Exception e){
            HibernateUtil.rollbackDatabaseTransaction();
            LOG.error("Could not insert file in DB, rollback DB transaction", e);
            return null;
        }
    }

    public int createHierarchyAndEadFile(Map<String, List<String>> map, Map<String, Long> idsWithTypes, EadContent eadContent, String mainagencycode, String tmpDirPath) throws APEnetException {
        CLevelDAO cLevelDAO = DAOFactory.instance().getCLevelDAO();
        int fullSize = map.size();
        int currentSize = 0;
        for(String key : map.keySet()){
            try {
                LOG.info("Create hierarchy for key: " + key + ", " + (++currentSize) + "/" + fullSize);
                CLevel oldCLevel = removeClevel(key, cLevelDAO, eadContent.getEcId());
                EadContent newEadContent = createEadContent(oldCLevel, mainagencycode, tmpDirPath);

                List<String> unitids = map.get(key);

                HibernateUtil.beginDatabaseTransaction();
//                for(String id : unitids)
//                    cLevelDAO.setEcIdHql(idsWithTypes.get(id), newEadContent.getEcId(), eadContent.getEcId()); //idsWithTypes: key=unitid, value=clId

                LOG.info("Size of the map with current key: " + unitids.size());
                Node root = new Node(null, newEadContent.getEadid());
                loopGoesInTree(root, unitids/*, 0*/);
                createLevelsInDB(root, -1, newEadContent.getEcId(), eadContent.getEcId());

                LOG.info("Create EAD file for EadContent id: " + newEadContent.getEcId());
                createEadFile(root, tmpDirPath);

                HibernateUtil.commitDatabaseTransaction();
            } catch (Exception e) {
                HibernateUtil.rollbackDatabaseTransaction();
                LOG.error("Error, we had to rollback the DB TX", e);
            }
        }
        try {
            HibernateUtil.beginDatabaseTransaction();
            DAOFactory.instance().getEadContentDAO().delete(eadContent);
            HibernateUtil.commitDatabaseTransaction();
        } catch (Exception e){
            LOG.error("Error", e);
            HibernateUtil.rollbackDatabaseTransaction();
        }
        HibernateUtil.closeDatabaseSession();
        return fullSize;
    }

    private void loopGoesInTree(Node node, List<String> cLevelUnitids/*, int start*/){
        for(int i = 0/*start*/; i < cLevelUnitids.size(); i++){
            String unitid = cLevelUnitids.get(i);
            if(unitid.length() == node.getId().length() || unitid.equals(node.getId()))
                break;
            String smallId = unitid.replace(node.getId() + "/", "");
            if(!smallId.contains("/")){
                Node child = new Node(node, unitid);
                node.addChild(child);
                cLevelUnitids.remove(i);
                i--;
                if(i < cLevelUnitids.size() && i >= 0)
                    loopGoesInTree(child, cLevelUnitids/*, i*/);
            }
        }
    }

    // 2 next functions: Print helps for some debugging
    public static void printList(Map<String, List<String>> map){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Start...");
        buffer.append("\n");
        for(String key : map.keySet()){
            buffer.append("KEY (FONDS): ").append(key);
            buffer.append("\n");
            for(String list : map.get(key)) {
                buffer.append("-- is in: ").append(list);
                buffer.append("\n");
            }
        }
        buffer.append("Finish...");
        try {
            FileUtils.writeStringToFile(new File("/tmp/list_ids_map.txt"), buffer.toString());
        } catch (Exception e){

        }
    }

    public static void printList_simple(Map<String, String> map){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Start...");
        buffer.append("\n");
        for(String key : map.keySet()){
            buffer.append("KEY: ").append(key).append(", LEVEL: ").append(map.get(key));
            buffer.append("\n");
        }
        buffer.append("Finish...");
        try {
            FileUtils.writeStringToFile(new File("/tmp/list_ids_simple.txt"), buffer.toString());
        } catch (Exception e){

        }
    }

    /* Getters */
    public int getNumberOfFAs() {
        return eadIdMap.size();
    }


    public class Node {
        public String id;
        public List<Node> children;
        public Node parent;

        public Node(Node parent, String id){
            this.parent = parent;
            this.id = id;
            this.children = null;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }

        public void setChildren(List<Node> children){
            this.children = children;
        }
        public List<Node> getChildren(){
            if(children == null)
                return new LinkedList<Node>();
            return children;
        }

        public void addChild(Node child){
            if(children == null)
                children = new LinkedList<Node>();
            children.add(child);
        }

        public Node getParent() {
            return parent;
        }
        public void setParent(Node parent) {
            this.parent = parent;
        }
    }
}
