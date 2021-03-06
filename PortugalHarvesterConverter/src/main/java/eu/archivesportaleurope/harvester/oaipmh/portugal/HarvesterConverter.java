package eu.archivesportaleurope.harvester.oaipmh.portugal;

import java.io.*;
import java.sql.SQLException;
import java.util.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import eu.archivesportaleurope.harvester.oaipmh.portugal.database.DBUtil;
import eu.archivesportaleurope.harvester.oaipmh.portugal.objects.Node;
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

import eu.apenet.dpt.utils.service.TransformationTool;
import eu.archivesportaleurope.harvester.oaipmh.portugal.objects.CLevel;
import eu.archivesportaleurope.harvester.oaipmh.portugal.objects.EadContent;

/**
 * User: yoannmoranville
 * Date: 22/01/14
 *
 * @author yoannmoranville
 */
public class HarvesterConverter extends AbstractParser {
    private final static Logger LOG = Logger.getLogger(HarvesterConverter.class);
    private final static String UTF8 = "utf-8";

    private List<File> files;
    private File directoryDone;
    private DBUtil dbUtil;

    public HarvesterConverter(File directory){
        LOG.info("Directory used: " + directory.getAbsolutePath());
        this.directoryDone = new File(directory, "DONE");
        this.files = Arrays.asList(directory.listFiles(
                new FileFilter() {
                    @Override
                    public boolean accept(File file) {
                        return file.getName().endsWith(".xml");
                    }
                }
        ));
        dbUtil = new DBUtil();
    }

    public void dublinCoreToEad() throws Exception{
        LOG.info("Starting DC to EAD process (creation of all CLevels from the DC elements of the harvested files)");
        try {
            if(files == null)
                throw new Exception("Variable files is null");

            Iterator<File> fileIterator = files.iterator();
            int fullSize = files.size();
            LOG.info("There are " + fullSize + " files");
            if(fullSize == 0) {
                throw new Exception("No files harvested...");
            }
            int currentSize = 0;

            XMLInputFactory2 inputFactory = (XMLInputFactory2) XMLInputFactory2.newInstance();
            XMLStreamReader2 xmlReader = inputFactory.createXMLStreamReader(fileIterator.next()); //First iteration can never be empty

            XMLOutputFactory2 xmlOutputFactory = (XMLOutputFactory2)XMLOutputFactory2.newInstance();

            OutputStream findingAidOutputStream2 = null;
            XMLStreamWriter2 findingAidXmlWriter2 = null;

            boolean isInsideRecord = false;

            XPathFactory xpathFactory = XPathFactory.newInstance(XPathFactory.DEFAULT_OBJECT_MODEL_URI, "org.apache.xpath.jaxp.XPathFactoryImpl", this.getClass().getClassLoader());
            XPath xPath = xpathFactory.newXPath();
            xPath.setNamespaceContext(new EADNamespaceContext());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            XPathExpression unitidExpr = xPath.compile("/ead:c/ead:did/ead:unitid/text()");
            XPathExpression levelExpr = xPath.compile("/ead:c/@level");

            String query = "INSERT INTO ead_content (eadid) VALUES ('OAI_SET')";
            dbUtil.launchQuery(query);
            long eadcontentid = dbUtil.retrieveEadContentIdByEadid("OAI_SET");

            int numberOfClevel = 0;
            int event;
            while(xmlReader.hasNext()){
                event = xmlReader.next();
                if (event == XMLStreamConstants.START_ELEMENT) {
                    if(xmlReader.getLocalName().equals("dc")) {
                        findingAidOutputStream2 = new ByteArrayOutputStream();
                        findingAidXmlWriter2 = (XMLStreamWriter2) xmlOutputFactory.createXMLStreamWriter(findingAidOutputStream2, UTF8);

                        findingAidXmlWriter2.writeStartElement(xmlReader.getLocalName());
                        findingAidXmlWriter2.writeNamespace("ns2", "http://purl.org/dc/elements/1.1/");
                        findingAidXmlWriter2.writeNamespace("ns3", "http://www.openarchives.org/OAI/2.0/oai_dc/");

                        isInsideRecord = true;
                    } else if(isInsideRecord) {
                        findingAidXmlWriter2.writeStartElement(xmlReader.getLocalName());
                    }
                } else if (event == XMLStreamConstants.END_ELEMENT) {
                    if(isInsideRecord)
                        findingAidXmlWriter2.writeEndElement();

                    if(xmlReader.getLocalName().equals("dc")){
                        isInsideRecord = false;
                        if(findingAidXmlWriter2 != null)
                            findingAidXmlWriter2.close();
                        if(findingAidOutputStream2 != null)
                            findingAidOutputStream2.close();

                        File outputFileDir = directoryDone;
                        if(!outputFileDir.exists())
                            outputFileDir.mkdirs();
                        File outputFile = new File(outputFileDir, "oai_set.xml");
                        if(findingAidOutputStream2 != null)
                            TransformationTool.createTransformation(IOUtils.toInputStream(findingAidOutputStream2.toString()), outputFile, new File(HarvesterConverter.class.getResource("/dc2c.xsl").getFile()), null, true, true, null, true, null);
                        else
                            throw new Exception("findingAidOutputStream2 is null");

                        Document doc = builder.parse(outputFile);
                        doc.getDocumentElement().normalize();

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
                        cLevel.setUnitid(unitidString.replaceAll("-", "/"));
                        cLevel.setOrderId(numberOfClevel++);
                        cLevel.setXml(FileUtils.readFileToString(outputFile, UTF8));
                        cLevel.setEcId(eadcontentid);
                        dbUtil.insertCLevel(cLevel);
                        //for now, do not delete files, maybe we can use them again if it fails, no need to harvest again
//                        FileUtils.forceDelete(outputFile);
//                        if(outputFileDir.listFiles().length == 0)
//                            FileUtils.deleteDirectory(outputFileDir);
                        if(fileIterator.hasNext()) {
                            File file = fileIterator.next();
                            LOG.debug("Examining next file: " + (++currentSize + 1) + "/" + fullSize + " - " + "Name of file: " + file.getName());
                            xmlReader = inputFactory.createXMLStreamReader(file);
                        }
                    }
                } else if (event == XMLStreamConstants.CHARACTERS) {
                    if(isInsideRecord)
                        findingAidXmlWriter2.writeCharacters(xmlReader.getText());
                } else if (event == XMLStreamConstants.END_DOCUMENT){
                    if(fileIterator.hasNext()) {
                        LOG.debug("Examined file: " + (++currentSize) + "/" + fullSize);
                        xmlReader = inputFactory.createXMLStreamReader(fileIterator.next());
                    }
                }
            }

            if(findingAidXmlWriter2 != null)
                findingAidXmlWriter2.close();
            if(findingAidOutputStream2 != null)
                findingAidOutputStream2.close();
        } catch (Exception e){
            LOG.error("Error", e);
            throw new RuntimeException();
        }
    }

    public int dbToEadNew(String mainagencycode) throws Exception {
        final int MAX_ELEMENTS = 1000;
        LOG.info("Starting Database conversion into EAD files");
        EadContent eadContent = dbUtil.retrieveEadContentByEadid("OAI_SET");
        int from = 0;

        List<CLevel> cLevelsFonds = dbUtil.retrieveAllFonds(eadContent.getEcId());

        LinkedHashMap<String, Long> idsWithUnitids;
        LOG.info("Size of cLevelsFonds: " + cLevelsFonds.size());
        for(CLevel cLevel : cLevelsFonds) {
            LOG.info("====");
            idsWithUnitids = new LinkedHashMap<String, Long>();
            //Create ONE FA per fonds!!!
            List<CLevel> childrenOfFonds = dbUtil.retrieveChildrenOfFonds(cLevel.getUnitid());
            LOG.info("Size of childrenOfFonds: " + childrenOfFonds.size());
            for(CLevel child : childrenOfFonds) {
                idsWithUnitids.put(child.getUnitid(), child.getClId());
            }
            dbToEad(cLevel.getUnitid(), eadContent, idsWithUnitids, mainagencycode);
        }
        LOG.info("====");
        return cLevelsFonds.size();
    }

//    public int dbToEad(String mainagencycode) throws Exception {
//        final int MAX_ELEMENTS = 1000;
//        LOG.info("Starting Database conversion into EAD files");
//        EadContent eadContent = dbUtil.retrieveEadContentByEadid("OAI_SET");
//
//        int from = 0;
//
//        List<CLevel> cLevels = dbUtil.retrieveNextClevels(eadContent.getEcId(), from, MAX_ELEMENTS);
//        Map<String, String> idsWithTypes = new HashMap<String, String>();
//        Map<String, Long> idsWithUnitids = new HashMap<String, Long>();
//        while(cLevels.size() != 0) {
//            from += MAX_ELEMENTS;
//            for(CLevel cLevel : cLevels) {
//                idsWithTypes.put(cLevel.getUnitid(), cLevel.getLevel());
//                idsWithUnitids.put(cLevel.getUnitid(), cLevel.getClId());
//            }
//            cLevels = dbUtil.retrieveNextClevels(eadContent.getEcId(), from, MAX_ELEMENTS);
//        }
//        return dbToEad(eadContent, idsWithTypes, idsWithUnitids, mainagencycode);
//    }

    public int dbToEad(String fondsUnitid, EadContent eadContent, LinkedHashMap<String, Long> unitidsWithId, String mainagencycode) throws Exception {
//        printList_simple(unitidsWithId);
        unitidsWithId = sortHashMapByKey(unitidsWithId);
//        LOG.info("idsWithType map size after sorting: " + unitidsWithLevel.size());
//        printList_simple(unitidsWithId);
//        Map<String, List<String>> organizedIds = createOrganizedMapOfId(unitidsWithLevel);
//        LOG.info("organizedIds map size: " + organizedIds.size());
//        LOG.info("idsWithType map size after sorting: " + unitidsWithLevel.size());
//        printList(organizedIds);
        try {
            return createHierarchyAndEadFile(fondsUnitid, unitidsWithId, eadContent, mainagencycode);
        } catch (Exception e){
            LOG.error("ERROR", e); //To do
            throw e;
        }
    }

    public static LinkedHashMap<String, Long> sortHashMapByKey(LinkedHashMap<String, Long> passedMap) {
        LinkedList<String> mapKeys = new LinkedList<String>(passedMap.keySet());
//        Collections.sort(mapKeys, String.CASE_INSENSITIVE_ORDER);
        Collections.sort(mapKeys, new MyComparator());

        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<String, Long>(mapKeys.size());

        for(String orderedKey : mapKeys)
            sortedMap.put(orderedKey, passedMap.get(orderedKey));
        return sortedMap;
    }

    private CLevel removeClevel(String id, Long eadContentId) throws Exception {
        try {
            CLevel cLevel = dbUtil.selectCLevelByIdAndEcId(id, eadContentId);
            dbUtil.deleteCLevelById(cLevel.getClId());
            return cLevel;
        } catch (Exception e) {
            throw new Exception("CLevel (unitid: '" + id + "') was not found in the database", e);
        }
    }

    private EadContent createEadContent(CLevel cLevel, String mainagencycode) throws Exception {
        EadContent eadContent = new EadContent();
        eadContent.setEadid(cLevel.getUnitid());

        File outputFile = new File(directoryDone, "oai.xml");
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("mainagencycode", mainagencycode);
            TransformationTool.createTransformation(IOUtils.toInputStream(cLevel.getXml(), UTF8), outputFile, new File(HarvesterConverter.class.getResource("/c2ead.xsl").getFile()), params, true, true, null, true, null);
            eadContent.setXml(FileUtils.readFileToString(outputFile, UTF8));

            dbUtil.saveEadContentEadidAndXml(eadContent);
            eadContent = dbUtil.retrieveEadContentByEadid(cLevel.getUnitid());

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
        LOG.info("There will be " + listFas.size() + " FAs to create!");

        for(String id : idsWithTypes.keySet()){
            LOG.info("id:" + id + ", listFas null:" + (listFas.get(id) == null));
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

    private void createEadFile(Node node){
        OutputStream outputStream = null;
        try {
            String fileName = node.getId().replaceAll("/", "_") + ".xml";
            outputStream = new FileOutputStream(new File(directoryDone, fileName));

            EadContent eadContent = dbUtil.retrieveEadContentByEadid(node.getId());

            EadCreator eadCreator = new EadCreator(outputStream);
            eadCreator.writeEadContent(eadContent.getXml());

            List<CLevel> cLevels = dbUtil.retrieveAllParentClevelsOrdered(eadContent.getEcId());
            for(CLevel cLevel : cLevels){
                eadCreator.writeEadContent(cLevel.getXml(), cLevel.getLevel());
                writeChildren(cLevel.getClId(), eadCreator);
                eadCreator.closeCTag();
                dbUtil.deleteCLevelById(cLevel.getClId());
            }
            dbUtil.deleteEadContentById(eadContent.getEcId());

            eadCreator.closeEndTags();
            eadCreator.closeWriter();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e){
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
            List<CLevel> children = dbUtil.retrieveAllChildren(parentId);
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

    private void createLevelsInDB(Node node, int level, Long eadContentId, Long oldEadContentId) throws SQLException {
        level++;
        for(Node child : node.getChildren()){
            if(level != 0){
                //1. Get the correct current CLevel from DB
                CLevel childCLevel = dbUtil.retrieveCLevelByUnitid(child.getId(), eadContentId);
                //2. Get the node parent (never null because it is not the ROOT)
                //3. Get the correct parent CLevel from DB
                CLevel parentCLevel = dbUtil.retrieveCLevelByUnitid(node.getId(), eadContentId);
                Long parentCLevelId = parentCLevel.getClId();
                //4. Insert in the correct current CLevel the ID of the correct parent CLevel in the DB
                dbUtil.updateCLevelOther("parent_cl_id", parentCLevelId, childCLevel.getClId());
                dbUtil.updateCLevelOther("ec_id", eadContentId, childCLevel.getClId());
            }
            createLevelsInDB(child, level, eadContentId, oldEadContentId);
        }
    }

    public int createHierarchyAndEadFile(/*Map<String, List<String>> map, */String fondsUnitid, Map<String, Long> idsWithTypes, EadContent eadContent, String mainagencycode) throws Exception {
//        int fullSize = map.size();
//        int currentSize = 0;
//        for(String key : map.keySet()){
            try {
                LOG.info("Create hierarchy for key: " + fondsUnitid);
                CLevel oldCLevel = removeClevel(fondsUnitid, eadContent.getEcId());
                EadContent newEadContent = createEadContent(oldCLevel, mainagencycode);

                List<String> unitids = new ArrayList<String>(idsWithTypes.keySet());

                for(String id : unitids) {
                    //Update clId (idsWithTypes.get(id)) from oldEcId (eadContent.getEcId()) to newEcId (newEadContent.getEcId())
//                    cLevelDAO.setEcIdHql(idsWithTypes.get(id), newEadContent.getEcId(), eadContent.getEcId()); //idsWithTypes: key=unitid, value=clId
                    dbUtil.updateCLevelOther("ec_id", newEadContent.getEcId(), idsWithTypes.get(id));
                }

                LOG.info("Size of the map with current key: " + unitids.size());
                Node root = new Node(null, newEadContent.getEadid());
                loopGoesInTree(root, unitids, 0);
                createLevelsInDB(root, -1, newEadContent.getEcId(), eadContent.getEcId());

                LOG.info("Create EAD file for EadContent id: " + newEadContent.getEcId());
                createEadFile(root);

            } catch (Exception e) {
                LOG.error("Error", e);
                throw e;
            }
//        }
        try {
            dbUtil.deleteEadContentById(eadContent.getEcId());
        } catch (Exception e){
            LOG.error("Error", e);
            throw e;
        }
        return 0;
    }

    public static void loopGoesInTree(Node node, List<String> cLevelUnitids, int start){
        for(int i = start; i < cLevelUnitids.size(); i++) {
            String unitid = cLevelUnitids.get(i);
//            if(unitid.length() == node.getId().length() || unitid.equals(node.getId()))
//                break;
            String smallId = unitid.replace(node.getId() + "/", "");
            if(!smallId.contains("/")){
                Node child = new Node(node, unitid);
                node.addChild(child);
//                cLevelUnitids.remove(i);
//                i--;
//                if(i < 0)
//                    i = 0;
                if(i < cLevelUnitids.size() && i >= 0)
                    loopGoesInTree(child, cLevelUnitids, start);
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
            e.printStackTrace();
        }
    }

    public static void printList_simple(Map<String, Long> map){
        StringBuilder buffer = new StringBuilder();
        buffer.append("Start...");
        buffer.append("\n");
        for(String key : map.keySet()){
            buffer.append("KEY: ").append(key).append(", ID: ").append(map.get(key));
            buffer.append("\n");
        }
        buffer.append("Finish...");
        try {
            FileUtils.writeStringToFile(new File("/tmp/list_ids_simple.txt"), buffer.toString());
//            LOG.info(buffer.toString());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static class MyComparator implements Comparator<String> {
        public MyComparator(){}

        @Override
        public int compare(String o1, String o2) {

            String[] o1Split = o1.split("/");
            String[] o2Split = o2.split("/");
            int lengthSmaller = (o1Split.length >= o2Split.length ? o2Split.length : o1Split.length);

            for(int i = 0; i < lengthSmaller; i++) {
                String part1 = o1Split[i];
                String part2 = o2Split[i];


                if (part1.matches("[0-9]+") && part2.matches("[0-9]+")) {
                    Integer integer1 = Integer.valueOf(part1);
                    Integer integer2 = Integer.valueOf(part2);
                    if(!integer1.equals(integer2)) {
                        return integer1.compareTo(integer2);
                    }
                } else if (part1.matches("[a-zA-Z]+") && part2.matches("[a-zA-Z]+")) {
                    if(!part1.equals(part2)) {
                        return o1.compareTo(o2);
                    }
//                } else {
//                    if(o1.split("/").length > o2.split("/").length) {
//                        return 1;
//                    } else if(o1.split("/").length < o2.split("/").length) {
//                        return -1;
//                    } else {
//                        String lastPart1 = o1.substring(o1.lastIndexOf("/") + 1);
//                        String lastPart2 = o2.substring(o2.lastIndexOf("/") + 1);
//                        if (lastPart1.matches("[0-9]+") && lastPart2.matches("[0-9]+")) {
//                            Integer integer1 = Integer.valueOf(lastPart1);
//                            Integer integer2 = Integer.valueOf(lastPart2);
//                            return integer1.compareTo(integer2);
//                        } else if (lastPart1.matches("[a-zA-Z]+") && lastPart2.matches("[a-zA-Z]+")) {
//                            return lastPart1.compareTo(lastPart2);
//                        } else {
//                            return o1.compareTo(o2);
//                        }
//                    }
                }

            }

            if(o1Split.length > o2Split.length) {
                return 1;
            } else if(o1Split.length < o2Split.length) {
                return -1;
            } else {
                return 0;
            }

//            if (o1.matches("[0-9]+") && o2.matches("[0-9]+")) {
//                Integer integer1 = Integer.valueOf(o1);
//                Integer integer2 = Integer.valueOf(o2);
//                return integer1.compareTo(integer2);
//            } else if (o1.matches("[a-zA-Z]+") && o2.matches("[a-zA-Z]+")) {
//                return o1.compareTo(o2);
//            } else {
//                if(o1.split("/").length > o2.split("/").length) {
//                    return 1;
//                } else if(o1.split("/").length < o2.split("/").length) {
//                    return -1;
//                } else {
//                    String lastPart1 = o1.substring(o1.lastIndexOf("/") + 1);
//                    String lastPart2 = o2.substring(o2.lastIndexOf("/") + 1);
//                    if (lastPart1.matches("[0-9]+") && lastPart2.matches("[0-9]+")) {
//                        Integer integer1 = Integer.valueOf(lastPart1);
//                        Integer integer2 = Integer.valueOf(lastPart2);
//                        return integer1.compareTo(integer2);
//                    } else if (lastPart1.matches("[a-zA-Z]+") && lastPart2.matches("[a-zA-Z]+")) {
//                        return lastPart1.compareTo(lastPart2);
//                    } else {
//                        return o1.compareTo(o2);
//                    }
//                }
//            }
        }
    }
}
