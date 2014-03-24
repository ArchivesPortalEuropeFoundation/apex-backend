package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.infraestructure.ArchivalLandscapeNode;
import eu.apenet.dashboard.infraestructure.ArchivalLandscapeStructure;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.dashboard.utils.ChangeControl;
import eu.apenet.dashboard.utils.ContentUtils;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.EadDAO;
import eu.apenet.persistence.dao.EadSearchOptions;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.Lang;
import eu.apenet.persistence.vo.SentMailRegister;
import eu.apenet.persistence.vo.SourceGuide;

/**
 * @author Jara Alvarez
 */
public class ArchivalLandscape extends ActionSupport{

    /**
	 * Serializable.
	 */
	private static final long serialVersionUID = 7141674075748597835L;
	//private Integer partnerId; 
    private String countryName;
    public List<String> Structure; //All structure in archival landscape for this partner
    public List<String> Archives; //Archival institutions for this partner    
	private Country country;
	private ArchivalInstitution ai;
	private ArchivalInstitution ai_parent;
	private ArchivalInstitutionDAO aiDao;
	private static Logger log = Logger.getLogger(ArchivalLandscape.class);
	private List<ArchivalInstitution> archivalInstitutionsToDelete= new ArrayList<ArchivalInstitution>();
	private List<ArchivalInstitution> archivalInstitutionsToInsert= new ArrayList<ArchivalInstitution>();
	private List<AiAlternativeName> archivalInstitutionsNameNotChanged= new ArrayList<AiAlternativeName>();
	private List<AiAlternativeName> archivalInstitutionsNameChanged= new ArrayList<AiAlternativeName>();
	private List<ArchivalInstitution> archivalInstitutionsParentNotChanged= new ArrayList<ArchivalInstitution>();
	private List<ArchivalInstitution> archivalInstitutionsParentChanged= new ArrayList<ArchivalInstitution>();
	private List<SentMailRegister> sentMailRegisterList = new ArrayList<SentMailRegister>();

	static Semaphore sem = new Semaphore(1,true) ;
    
	public int j = 0;

	// Set for the duplicate identifiers.
	private static Set<String> duplicateIdentifiers;
	
	//Constructor. Set the partner logged
	public ArchivalLandscape() {
    		
		//Obtain partnerId from session.
		SecurityContext securityContext = SecurityContext.get();
        this.country = DAOFactory.instance().getCountryDAO().findById(securityContext.getCountryId());
        this.countryName = this.country.getCname().toLowerCase();
    }
	public ArchivalLandscape(Country country) {
		
		//Obtain partnerId from session.
        this.country = country;
        this.countryName = this.country.getCname().toLowerCase();
    }
     
//    public Integer getPartnerId() {
//        return partnerId;
//    }
//
//	public void setPartnerId(Integer partnerId) {
//		this.partnerId = partnerId;
//	}

	//Returns the ISO name of the country
	public String getmyCountry() {
        return this.country.getIsoname();
    }
	//Returns the name of the country
	public String getmyCountryName() {
        return this.country.getCname();
    }
    public int getCountryId() {
        return this.country.getId();
    }
	
	public List<ArchivalInstitution> getArchivalInstitutionsToDelete() {
		return archivalInstitutionsToDelete;
	}

	public void setArchivalInstitutionsToDelete(
			List<ArchivalInstitution> archivalInstitutionsToDelete) {
		this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsToInsert() {
		return archivalInstitutionsToInsert;
	}

	public void setArchivalInstitutionsToInsert(
			List<ArchivalInstitution> archivalInstitutionsToInsert) {
		this.archivalInstitutionsToInsert = archivalInstitutionsToInsert;
	}

	public List<SentMailRegister> getSentMailRegisterList() {
		return sentMailRegisterList;
	}

	public void setSentMailRegisterList(List<SentMailRegister> sentMailRegisterList) {
		this.sentMailRegisterList = sentMailRegisterList;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameNotChanged() {
		return archivalInstitutionsNameNotChanged;
	}

	public void setArchivalInstitutionsNameNotChanged(
			List<AiAlternativeName> archivalInstitutionsNameNotChanged) {
		this.archivalInstitutionsNameNotChanged = archivalInstitutionsNameNotChanged;
	}

	public List<AiAlternativeName> getArchivalInstitutionsNameChanged() {
		return archivalInstitutionsNameChanged;
	}

	public void setArchivalInstitutionsNameChanged(
			List<AiAlternativeName> archivalInstitutionsNameChanged) {
		this.archivalInstitutionsNameChanged = archivalInstitutionsNameChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentNotChanged() {
		return archivalInstitutionsParentNotChanged;
	}

	public void setArchivalInstitutionsParentNotChanged(
			List<ArchivalInstitution> archivalInstitutionsParentNotChanged) {
		this.archivalInstitutionsParentNotChanged = archivalInstitutionsParentNotChanged;
	}

	public List<ArchivalInstitution> getArchivalInstitutionsParentChanged() {
		return archivalInstitutionsParentChanged;
	}

	public void setArchivalInstitutionsParentChanged(
			List<ArchivalInstitution> archivalInstitutionsParentChanged) {
		this.archivalInstitutionsParentChanged = archivalInstitutionsParentChanged;
	}

	public static Set<String> getDuplicateIdentifiers() {
		return ArchivalLandscape.duplicateIdentifiers;
	}

	public static void addDuplicateIdentifiers(String duplicateIdentifier) {
		if (ArchivalLandscape.getDuplicateIdentifiers() == null) {
			ArchivalLandscape.duplicateIdentifiers = new LinkedHashSet<String>();
		}

		ArchivalLandscape.getDuplicateIdentifiers().add(duplicateIdentifier);
	}

	public static void setDuplicateIdentifiers(Set<String> duplicateIdentifiers) {
		ArchivalLandscape.duplicateIdentifiers = duplicateIdentifiers;
	}

	//Returns the path of the AL of each country
	public String getmyPath(String country) {

		String path = APEnetUtilities.getConfig().getRepoDirPath() + File.separatorChar;
		path = path + country + File.separatorChar + "AL" + File.separatorChar;
		
		File file=new File(path);
        if(!file.exists())
		    file.mkdir();
		
    	return path;
    }

	//Read the file and check if it is well formed
    public String read(File file, String typeProcess) {

    	String result="";
        int country = -1;
        Boolean checkTranslation= true;
        
        try {
            //Open the file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputStream sfile = new FileInputStream(file);
            if(sfile.available()<=0){
            	sfile.close();
            	log.error("There isn't information to be readed! - "+file.getName());
            	return null;
            }
            Document doc = dBuilder.parse(sfile);
            sfile.close();
            doc.getDocumentElement().normalize();
            log.info("The file " + file.getName() + " of the country " +this.countryName + " is being reading");

            //Get country nodes
            NodeList a = doc.getElementsByTagName("c");

            for (int i = 0; i < a.getLength(); i++) {
                NamedNodeMap attributes = a.item(i).getAttributes();
                Node attribute = attributes.getNamedItem("level");
                if ((attribute.getNodeValue().equals("fonds"))) {
                    NodeList listDid = a.item(i).getChildNodes();
                    for (int l = 0; l < listDid.getLength(); l++) {
                        if (listDid.item(l).getNodeName() == "did") {
                            NodeList listUnitTitle = listDid.item(l).getChildNodes();
                          
                            //Check if the 14 country name translations are included in the xml
//                            if (checkTranslation(listUnitTitle)) //todo: Why do we care about this? Why do we need all country names?
//                            	checkTranslation = true;
                            
                            	for (int j = 0; j < listUnitTitle.getLength(); j++) {
                                if (listUnitTitle.item(j).getNodeName().equals("unittitle")) {
                                    NamedNodeMap attributesUnitTitle = listUnitTitle.item(j).getAttributes();
                                    Node attributeUnitTitle = attributesUnitTitle.getNamedItem("type");
                                    if (attributeUnitTitle.getNodeValue().equals("eng")) {
                                        if (listUnitTitle.item(j).getTextContent().toLowerCase().equals(countryName))
                                            country = i; //The country node of the partner logged
                                    }
                                   
                                }
                            }
                        }
                    }
                }
            }
           
            String pathCountry = this.getmyPath(this.getmyCountry());
            
          //The country is not in the AL
    		if ((country == -1) || (!checkTranslation)){
    			log.error("The file " + file.getName() + " of the country " +this.countryName + " does not have the country unittitle's");
    			result = "ERROR";
    		}
    		else 
    		{            	            
    			if (!typeProcess.equals("upload"))
    			{
    				//If the xml of this country does not exist, create it     			
		            File files = new File(pathCountry + this.getmyCountry() + "AL.xml");
		        	if (!files.exists())
		        	{
		        		//Everything is OK. The node of this country is located
		        		Document docCountry = createXML(a.item(country), doc, null);
			            doc = docCountry;
			            result="SUCCESS";
		        	}
    			}
        	    result = "SUCCESS";    	         
	        	//showTree(doc.getElementsByTagName("unittitle"),doc);
	        	log.debug("The file " + file.getName() + " of the country " +this.countryName + " has been read");
    		}

        } catch (Exception e) {
            log.error("The file " + file.getName() + " of the country " +this.countryName + " has not been read. " + e.getMessage());
            result = "ERROR";
        }
        return result;
    }


    //Create the AL of the country from the node given in the parameter  
	private Document createXML(Node item, Document doc, String path) {
			
		log.debug("Creating the archival landscape of the country " + this.getmyCountry() + "...");
        try {
            //Create document in which the new archival landscape will be
            //DocumentBuilder docCountry2;
            //docCountry2 = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        	DocumentBuilderFactory docCountry2;
            docCountry2 = DocumentBuilderFactory.newInstance();
            
            docCountry2.setValidating(false);
            docCountry2.setNamespaceAware(true);
            docCountry2.setAttribute("http://apache.org/xml/features/validation/schema",false);
            docCountry2.setAttribute("http://apache.org/xml/features/validation/schema-full-checking",false);
            docCountry2.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage","http://www.w3.org/2001/XMLSchema");
            docCountry2.setIgnoringElementContentWhitespace(true);            
            
            Document docNew = docCountry2.newDocumentBuilder().newDocument();
            Result result;
            Source source;
            
            //The element from the general AL to copy
            Element nodetocopy = (Element) item;
            nodetocopy = (Element) item;
            Node finalItem = null;                        
            
            //Create general elements to copy in the archival landscape per country
            Element eadheader=null;
            Element archdesc=null;
            Element archdesdid= null;
            Element dsc=null;
            
            //Temporary nodes to copy the above ones
            Node nodewherecopy2=null;
            Node newnode=null;
            Node newnode2=null;
            Node newnode3=null;
            
            //Establish a root with the node ead and the node of the country

            Element a = doc.getDocumentElement();
            Element root = docNew.createElement(a.getNodeName());
            
            for (int i=0;i<a.getAttributes().getLength();i++)
            {
            	root.setAttribute(a.getAttributes().item(i).getNodeName(), a.getAttributes().item(i).getNodeValue());
            }
	        docNew.appendChild(root);
	            
	        NodeList eadchild = a.getChildNodes();
	            for (int i= 0;i<eadchild.getLength();i++)	            	
	            {
	            	//If the user is using the template generated by dashboard with 'root' tag at the beginning
	            	if (eadchild.item(i).getNodeName().equals("ead"))
	            	{
	            		eadchild = eadchild.item(i).getChildNodes();
	            	}
	            	if (eadchild.item(i).getNodeName().equals("eadheader"))
	            		eadheader = (Element) eadchild.item(i);
	            	else {
	            		if (eadchild.item(i).getNodeName().equals("archdesc"))
	            		{	
	            			archdesc = (Element) eadchild.item(i);
	            			NodeList archdeschild =  eadchild.item(i).getChildNodes();
	            			for(int j= 0;j<archdeschild.getLength();j++)
	            			{
	            				if (archdeschild.item(j).getNodeName().equals("did"))
	            					archdesdid = (Element) archdeschild.item(j);
	            				else
	            				{
	            					if (archdeschild.item(j).getNodeName().equals("dsc"))
	            						dsc = (Element) archdeschild.item(j);
	            				}
	            			}
	            		}
	            	}
	            }    
    	            
	            //Import the nodes if they are filled in in the temporary ones
	            //Put the nodes in the right order
	            if (eadheader!= null)
	            {
	            	nodewherecopy2= docNew.importNode(eadheader,true);
		            root.appendChild(nodewherecopy2);
	            }
	            if (archdesc != null)
	            {
	            	newnode = docNew.importNode(archdesc,false);
	            	root.appendChild(newnode);
	            }
	            if (archdesdid!= null)
	            {
	            	newnode2= docNew.importNode(archdesdid,true);
	            	newnode.appendChild(newnode2);
	            }
	            if (dsc!=null)
	            {
	            	newnode3 = docNew.importNode(dsc,false);
	            	newnode.appendChild(newnode3);
	            }
	            if (nodetocopy!=null)
	            {
	            	finalItem = docNew.importNode(nodetocopy,true);
	            	newnode3.appendChild(finalItem);
	            }
	            
	            //Set the eadid to a generic one	            
	            Node eadid = docNew.getElementsByTagName("eadid").item(0);
	            if (eadid.getTextContent()== null || eadid.getTextContent().isEmpty())
	            	eadid.setTextContent("AL-" + this.getmyCountry());
	            
	            //Set the countrycode within element eadid (Just in case is finally needed)
	            /*if (eadid.hasAttributes())
	            {
	            	NamedNodeMap attributesEadid = eadid.getAttributes();
                    Node attributeUnitTitle = attributesEadid.getNamedItem("countrycode");
                    attributeUnitTitle.setTextContent(this.getmyCountry());
	            }*/
	            
	            //Create the new file with the doc created 
	            source = new DOMSource(docNew);
	                        
	        	String pathCountry = this.getmyPath(this.getmyCountry());	
	            pathCountry = pathCountry + this.getmyCountry() + "AL.xml";
	            result = new StreamResult(new java.io.File(pathCountry));
	            
	            //Write the XML
	            Transformer transformer;
	            try {
	                transformer = TransformerFactory.newInstance().newTransformer();
	                transformer.transform(source, result);
	            } catch (Exception e) {
	            	log.error("The archival landscape of the country " + this.getmyCountry() + " might not have the general mandatory elements. " + e.getMessage());
	            }	        
	       
	            doc = docNew;
	            log.debug("The archival landscape of the country " + this.getmyCountry() + "has been created");
	            
        } catch (ParserConfigurationException e1) {
        	log.error("The archival landscape of the country " + this.getmyCountry() + " could not be created. " + e1.getMessage());
        }
        return doc;
    }

    public List<String> returnTree() {
        return Structure;
    }
    

	public List<ArchivalInstitution> showArchives() {
		this.aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		SecurityContext securityContext = SecurityContext.get();
		if (securityContext.isCountryManager()){
			return this.aiDao.getArchivalInstitutionsByCountryIdForAL(securityContext.getCountryId(), true);
		}else if (securityContext.isInstitutionManager()) {
			return this.aiDao.getArchivalInstitutionsByPartnerId(securityContext.getPartnerId());
		}
		return new ArrayList<ArchivalInstitution>();
		
	}
	
	//Check if an institution has alternatives name in a node in archival landscape. 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void checkAlternativeNames(ArchivalInstitution arch_inst, ArchivalLandscapeNode archivalLandscapeNode){
		
		//Check if the languages considered for this item (group or institution) are stored in database
		Boolean found= null;
		AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO(); 
		List<AiAlternativeName> aiNames = ai_alNameDAO.findByAIId(arch_inst); //Alternative names stored in database
		Map<String, String> namesToUpload = new HashMap<String, String>(); 
		Iterator iterator = archivalLandscapeNode.getNames().entrySet().iterator(); //Alternative names in the new AL
		Iterator iterator2 = archivalLandscapeNode.getPrimaryName().entrySet().iterator(); //Alternative names in the new AL with primary info
        while(iterator.hasNext()){
        	found = false;
        	Map.Entry<String, String> temp = (Entry<String, String>) iterator.next();
        	Map.Entry<String, Boolean> primaryNames = null;
        	if (iterator2.hasNext())
        		primaryNames = (Entry<String, Boolean>) iterator2.next();
        	
        	for (int m=0;m<aiNames.size();m++)
        	{
        		//The language is the same
        		if (aiNames.get(m).getLang().getIsoname().toLowerCase().equals(temp.getKey().toLowerCase()))
        		{
        			found = true;
        			//But the name is NOT the same, we update it 
        			if (!aiNames.get(m).getAiAName().equals(temp.getValue()))
        			{
        				if (primaryNames!= null)
        				{
        					Boolean toChange= false;
        					//If it's NOT the primary name (therefore this name is not indexed), the alternative name will be updated 
        					if (!primaryNames.getValue())
     						{
        						toChange = true;        						
     						}
        					//If it's the primary name, we have to check if this item is indexed somewhere 
        					else{
        						// If yes, update the list to inform the user that the name can't be changed
        						boolean containsPublishedFiles = ContentUtils.containsPublishedFiles(arch_inst);
        						if (containsPublishedFiles)
        							this.getArchivalInstitutionsNameNotChanged().add(aiNames.get(m));        						
        						//Change the name in archival institution name too.
        						else
        						{
        							toChange=true;
        							ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
        							arch_inst.setAiname(temp.getValue());
        							aiDao.updateSimple(arch_inst);
        							this.getArchivalInstitutionsNameChanged().add(aiNames.get(m));
        						}	
        					}
        					if (toChange)
        					{
        						aiNames.get(m).setAiAName(temp.getValue());
                				AiAlternativeName ai_altName = new AiAlternativeName();
                				ai_altName = ai_alNameDAO.findById(aiNames.get(m).getAiAnId());
                				ai_altName.setAiAName(temp.getValue());
                				ai_alNameDAO.updateSimple(ai_altName);
        					}
        				}
        			}
        			aiNames.remove(m);
        		}
        		//The language is not the same
        		else
        		{
        			//But the name is the same, we change the language
        			if (aiNames.get(m).getAiAName().equals(temp.getValue()))
        			{       
        				found = true;
        				List<AiAlternativeName> ai_altName = new ArrayList<AiAlternativeName>();		            	
        	        	ai_altName = ai_alNameDAO.findByAiAName(aiNames.get(m).getAiAName().toString());
        	        	
        	        	for (int j=0;j<ai_altName.size();j++)
        	        	{
        	        		if (ai_altName.get(j).getArchivalInstitution().getAiId() == arch_inst.getAiId())
        	        		{
        	        			LangDAO langDao = DAOFactory.instance().getLangDAO();
                	        	Lang lang = new Lang();		            	
                	        	lang = langDao.getLangByIsoname(temp.getKey());
        	        			ai_altName.get(j).setLang(lang);        	        			
        	        			log.debug("Updating in database the language of the alternative name "+ temp.getValue() +" of the institution "+ arch_inst.getAiname());
        	                	ai_alNameDAO.updateSimple(ai_altName.get(j));
        	        		}
        	        	}
        	        	aiNames.remove(m);
        			}
        		}
        	}
        	if (!found)
        		namesToUpload.put(temp.getKey(), temp.getValue());        	
        }
        iterator = namesToUpload.entrySet().iterator();
        //All the languages that are still in the file list must be stored in database because they are new ones
        while (iterator.hasNext())
        {        	
			Map.Entry<String, String> temp = (Entry<String, String>) iterator.next();
        	        	
        	AiAlternativeName ai_altName = new AiAlternativeName();		            	
        	ai_altName.setAiAName(temp.getValue());		            	
        	ai_altName.setArchivalInstitution(arch_inst);
        	LangDAO langDao = DAOFactory.instance().getLangDAO();
        	Lang lang = new Lang();		            	
        	lang = langDao.getLangByIsoname(temp.getKey());
        	if (lang == null)
        	{
        		log.error("The lang " + temp.getKey() + " does not exist");
        		log.error("It was not possible to upload the alternative name: " + temp.getValue() + " of the institution: " + arch_inst);
        	}
        	else
        	{
        		ai_altName.setLang(lang);
        		//At this time, the new alternative names are not primary ones, so, false.
        		ai_altName.setPrimaryName(false);
        		log.debug("Storing in database the alternative name "+ temp.getValue() +" of the institution "+ arch_inst);
        		ai_alNameDAO.insertSimple(ai_altName);
        	}
        }
       //All the languages that are still in the database list must be deleted in database because they are old ones
        for (int i=0;i<aiNames.size();i++)        	
        {
        	log.debug("Deleting from database the alternative name "+ aiNames.get(i).getAiAName()+" of the institution "+ arch_inst);
        	ai_alNameDAO.deleteSimple(aiNames.get(i));
        }
	}

	/**
	 * This method checks if a file contains unique identifiers 
	 * @param file
	 * @return boolean
	 */
	public static Boolean checkIdentifiers(File file){
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.normalizeDocument();
			doc.normalize();
			NodeList listTemp = doc.getElementsByTagName("c");
			boolean changes = false;
			List<String> identifiers = new ArrayList<String>(); //list of unique identifiers
			// Delete the content of "DuplicateIdentifiers".
			ArchivalLandscape.setDuplicateIdentifiers(null);
			for(int i=0;i<listTemp.getLength();i++){
				Element cTemp = (Element)listTemp.item(i);
				if(cTemp.getAttributes()!=null && cTemp.getAttributes().getNamedItem("id")==null){
					String identifier = generateNewRandomIdentifier();
					cTemp.setAttribute("id",identifier); //This identifier can be repeated
					changes = true;
				}else if(cTemp.getAttributes()!=null && cTemp.getAttributes().getNamedItem("id")!=null){
					String identifier = cTemp.getAttributes().getNamedItem("id").getNodeValue();
					if(identifiers.contains(identifier)){ //internal identifier is repeated?
						// Add the repeated identifiers to the set.
						ArchivalLandscape.addDuplicateIdentifiers(identifier);
					}else if(!isValidIdentifier(identifier)){ //is a valid identifier?
						// return a new state for personalized layout
						return null;
					}
					identifiers.add(identifier);
				}
			}

			if (ArchivalLandscape.duplicateIdentifiers != null
					&& !ArchivalLandscape.duplicateIdentifiers.isEmpty()) {
				return false;
			}

			if(changes){
				TransformerFactory tf = TransformerFactory.newInstance();
		        Transformer transformer = tf.newTransformer();
		        transformer.transform(new DOMSource(doc), new StreamResult(file));
			}
		} catch (Exception e) {
			log.error("Checking c identifiers in makeTemporal():"+  APEnetUtilities.generateThrowableLog(e));
		}

		return true;
	}	

	
	private static String generateNewRandomIdentifier() {
		return "A"+System.currentTimeMillis()+"-"+(new Float(+Math.random()*1000000).toString());
	}
	public static boolean isValidIdentifier(String identifier) {
		if(identifier.length()>0){
			char firstCharacter = identifier.charAt(0);
			if(Character.isLetter(firstCharacter)){
				return true;
			}
		}
		return false;
	}
	//Store in the database the name of the NEW archival institutions uploaded and delete the ones removed
	public String storeArchives(File file, boolean execute) {
		
		String result = SUCCESS;
				
		//Get archival institution by this partner
		LangDAO langDao = DAOFactory.instance().getLangDAO();
		Lang lang = null;		
		this.ai_parent = new ArchivalInstitution();
		this.aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		List <ArchivalInstitution> ais = this.aiDao.getGroupsAndArchivalInstitutionsByCountryId(this.country.getId(), "alorder", false);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        log.debug("Storing in database the institutions of the country " + this.getmyCountry() + "...");
           
        try {
    		
			dBuilder = dbFactory.newDocumentBuilder();
			Reader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
			InputSource sfile = new InputSource(reader);
	        Document doc = dBuilder.parse(sfile);
	        doc.getDocumentElement().normalize();
	        
	        //Get the whole structure of the archival landscape
            NodeList a = doc.getElementsByTagName("c");            
            ArchivalLandscapeStructure al = new ArchivalLandscapeStructure();
            List <ArchivalLandscapeNode> alNodes = al.archivalLandscapeStructure(a);
            
            for (int h = 0; h < alNodes.size(); h++) {
                ArchivalLandscapeNode archivalLandscapeNode = alNodes.get(h);
	            this.ai = new ArchivalInstitution();
		        if (ais.size()==0) { //There's no (or no more) archival institutions for this partner
                    for (Entry<String, Boolean> item : archivalLandscapeNode.getPrimaryName().entrySet()) {
                        if (item.getValue())
                            this.ai.setAiname(archivalLandscapeNode.getNames().get(item.getKey()));
                    }

		            this.ai.setGroup(archivalLandscapeNode.getIs_group());
		            
		            if (archivalLandscapeNode.getParent_name()== null)
		            	this.ai.setParent(null);
		            else {
		            	ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId(), archivalLandscapeNode.getParent_internal_al_id());
		            	this.ai.setParent(ai_parent);
		            }
		            this.ai.setCountryId(this.country.getId());
		            this.ai.setCountry(this.country);
		            this.ai.setAlorder(archivalLandscapeNode.getNodeId());
		            this.ai.setInternalAlId(archivalLandscapeNode.getInternal_al_id());
		            //StoreAI
		            log.debug("Storing in database the institution "+ this.ai.getAiname());
		            this.aiDao.insertSimple(this.ai);
		            ContentUtils.updateContainsSearchableItemsInAiGroups(this.ai);
		            this.archivalInstitutionsToInsert.add(this.ai);
		            
		            //Store the alternative names of the archival institution
                    for (Entry<String, String> temp : archivalLandscapeNode.getNames().entrySet()) {
                        AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO();
                        AiAlternativeName ai_altName = new AiAlternativeName();
                        ai_altName.setAiAName(temp.getValue());
                        ai_altName.setArchivalInstitution(this.ai);

                        if(lang == null || !lang.getIsoname().equalsIgnoreCase(temp.getKey()))
                            lang = langDao.getLangByIsoname(temp.getKey());

                        ai_altName.setLang(lang);
                        if (archivalLandscapeNode.getPrimaryName().get(temp.getKey().toLowerCase()) == null)
                            ai_altName.setPrimaryName(false);
                        else
                            ai_altName.setPrimaryName(true);
                        log.debug("Storing in database the alternative name " + temp.getValue() + " of the institution " + this.ai.getAiname());
                        ai_alNameDAO.insertSimple(ai_altName);
                    }
		        } else {//There are already archival institutions for this partner
		        	Boolean upload=true;
		        	for (int i= 0; i< ais.size();i++) {
                        ArchivalInstitution archivalInstitution = ais.get(i);
		         		//If the institution already exists, don't store in database
		            	if (archivalLandscapeNode.getInternal_al_id().equals(archivalInstitution.getInternalAlId())) {
		            		this.ai = archivalInstitution;
		            		//If the item stored in database has the same parent in the archival landscape, check the languages
		            		if ((archivalLandscapeNode.getParent_internal_al_id()== null) && (archivalInstitution.getParent() ==null))
		            		{
		            			checkAlternativeNames(archivalInstitution, archivalLandscapeNode);
		            		}
		            		else if  ((archivalLandscapeNode.getParent_internal_al_id()!= null) && (archivalInstitution.getParent() != null))
		            		{
				            	if ((archivalLandscapeNode.getParent_internal_al_id().equals(archivalInstitution.getParent().getInternalAlId())))
				            	{
				            		checkAlternativeNames(archivalInstitution, archivalLandscapeNode);
				            	}
				            	//If not, change the parent of the item for the new one
				            	else 
				            	{
//				            		//Check if the item is in the index
//				            		Long numFilesIndexed = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(archivalInstitution.getInternalAlId(), archivalInstitution.isGroup());
//	        						if ((numFilesIndexed != null) && (numFilesIndexed > 0))
//	        						{
//	        							this.getArchivalInstitutionsParentNotChanged().add(ais.get(h));
//	        						}
//	        						else{
	        							if (archivalLandscapeNode.getParent_internal_al_id()== null)
				    		            	this.ai.setParent(null);
				    		            else {
				    		            	ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId() , archivalLandscapeNode.getParent_internal_al_id());
				    		            	this.ai.setParent(ai_parent);
				    		            }
	        							this.getArchivalInstitutionsParentChanged().add(this.ai);
//	        						}
				            	}
		            		}
		            		//Discrepancies in parents to solve (unless the parent is the node of the country=fonds)
		            		else if ((archivalInstitution.getParent() == null) && (archivalLandscapeNode.getParent_internal_al_id()!= null))
		            		{
//		            			//Check if the item is in the index
//			            		Long numFilesIndexed = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(archivalInstitution.getInternalAlId(), archivalInstitution.isGroup());
//        						if ((numFilesIndexed != null) && (numFilesIndexed > 0)) {
//		            				this.getArchivalInstitutionsParentNotChanged().add(this.ai);
//        						} else {
        							ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId() , archivalLandscapeNode.getParent_internal_al_id());
		    		            	this.ai.setParent(ai_parent);
		    		            	this.getArchivalInstitutionsParentChanged().add(this.ai);
//        						}
		            		}else if (((archivalInstitution.getParent() != null) && (archivalLandscapeNode.getParent_internal_al_id()== null)))
		            		{
//		            			//Check if the item is in the index
//			            		Long numFilesIndexed = EditArchivalLandscapeLogic.countIndexedContentByInstitutionGroupId(archivalInstitution.getInternalAlId(), archivalInstitution.isGroup());
//        						if ((numFilesIndexed != null) && (numFilesIndexed > 0)) {
//        							this.getArchivalInstitutionsParentNotChanged().add(this.ai);
//        						} else {
        							this.ai.setParent(null);
        							this.getArchivalInstitutionsParentChanged().add(this.ai);
//        						}
		            		}	
					         //Update always the order of the archival landscape
		            		this.ai.setAlorder(archivalLandscapeNode.getNodeId());
		            		this.ai.setInternalAlId(archivalLandscapeNode.getInternal_al_id());
		            		log.debug("Updating the parent in database of the institution "+ this.ai.getAiname());
	            			this.aiDao.updateSimple(this.ai);
	            			ContentUtils.updateContainsSearchableItemsInAiGroups(this.ai);
			            	ais.remove(i);
			            	upload = false;
		            	}
		        	}
			         //New files to upload
			         if (upload) {
                         for (Entry<String, Boolean> item : archivalLandscapeNode.getPrimaryName().entrySet()) {
                             if (item.getValue())
                                 this.ai.setAiname(archivalLandscapeNode.getNames().get(item.getKey()));
                         }
				        	
				         //this.ai.setPartnerId(this.partnerId);		            
				         this.ai.setGroup(archivalLandscapeNode.getIs_group());
				            
				         if (archivalLandscapeNode.getParent_name()== null)
				            this.ai.setParent(null);
				         else {
				        	 ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(this.country.getId() , archivalLandscapeNode.getParent_internal_al_id());
				             this.ai.setParent(ai_parent);
				         }
				         this.ai.setCountryId(this.country.getId());
				         this.ai.setCountry(this.country);
				         this.ai.setAlorder(archivalLandscapeNode.getNodeId());
				         this.ai.setInternalAlId(archivalLandscapeNode.getInternal_al_id());
				         //StoreAI
				         log.debug("Storing in database the institution "+ this.ai.getAiname());
				         this.aiDao.insertSimple(this.ai);
				         ContentUtils.updateContainsSearchableItemsInAiGroups(this.ai);
				         this.archivalInstitutionsToInsert.add(this.ai);
		         		
				         //Store the alternative names of the archival institution
                         for (Entry<String, String> temp : archivalLandscapeNode.getNames().entrySet()) {
                             AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO();
                             AiAlternativeName ai_altName = new AiAlternativeName();
                             ai_altName.setAiAName(temp.getValue());
                             ai_altName.setArchivalInstitution(this.ai);
                             if (lang != null) {
                                 if (!lang.getIsoname().toLowerCase().equals(temp.getKey().toLowerCase()))
                                     lang = langDao.getLangByIsoname(temp.getKey());
                             } else {
                                 lang = langDao.getLangByIsoname(temp.getKey());
                             }
                             ai_altName.setLang(lang);
                             if (archivalLandscapeNode.getPrimaryName().get(temp.getKey().toLowerCase()) == null)
                                 ai_altName.setPrimaryName(false);
                             else
                                 ai_altName.setPrimaryName(true);
                             log.debug("Storing in database the alternative name " + temp.getValue() + " of the institution " + this.ai.getAiname());
                             ai_alNameDAO.insertSimple(ai_altName);
                         }
			         }
		        }
	        }//End for        
		    //Files to delete 
		    if (ais.size() > 0) {
                for (ArchivalInstitution ai1 : ais) {
                    log.debug("Deleting from database the institution " + ai1.getAiname() + "...");
                    ContentUtils cu = new ContentUtils();
                    String resultRemoveAI = cu.deleteArchivalInstitution(ai1, execute);
                    archivalInstitutionsToDelete.add(ai1);
                    this.sentMailRegisterList.addAll(cu.getSentMailRegisterList());
                    if (resultRemoveAI.equals("error")) {
                        log.error("The institution " + ai1.getAiname() + " could not be removed. ");
                        result = "error";
                    }
                }
		    }
		    alNodes.clear();
		    ais.clear();
        }catch(IOException e)		{
        	log.error("The file " + file.getName() + " could not be read " + APEnetUtilities.generateThrowableLog(e));
        	result = "error";								
 		}catch (Exception e) {
			log.error("Some institutions of the country " + this.getmyCountry() +" could not be stored or removed in database and repository " + APEnetUtilities.generateThrowableLog(e));
			result = "error";
		}
		return result;
	}
	
	public void storeOperation(String op){
		ChangeControl.logOperation(op);
	}

    //Check if the file has eadid
	public Boolean checkEadid(File file){		
    
		Boolean bResult;
        Source source;
        Result result;
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Reader reader = new InputStreamReader(new FileInputStream(file),"UTF-8");
			InputSource sfile = new InputSource(reader);
	        Document doc = dBuilder.parse(sfile);
	        doc.getDocumentElement().normalize();
	        
	        Node eadid = doc.getElementsByTagName("eadid").item(0);
	        
	        if (eadid != null) {
		        if (eadid.getTextContent().isEmpty()) {
		        	eadid.setTextContent("AL-" + this.getmyCountry());
		            source = new DOMSource(doc);
		        	String pathCountry = this.getmyPath(this.getmyCountry());	
		            pathCountry = pathCountry + this.getmyCountry() + "AL.xml";
		            result = new StreamResult(new File(pathCountry));

		            Transformer transformer = TransformerFactory.newInstance().newTransformer();
	                transformer.transform(source, result);
		        	
	                bResult = true;
		        } else
		        	bResult = true;		        
	        } else {
                log.debug("The process could not find the eadid of the AL file: " + this.getmyCountry());
	        	bResult = false;
            }
		} catch (Exception e) {
			log.error("checkEadid: The addition of an EADID in the archival landscape of the country " + this.getmyCountry() + " has had errors. " + e.getMessage());
			bResult = false;
		}
        return bResult;
	}

	protected static String deleteContent(ArchivalInstitution ai) {
		String path = null;
		ContentUtils cu = new ContentUtils();
		String resultRemoveAI = "";
        try {
        	EadDAO eadDAO = DAOFactory.instance().getEadDAO();
			EadSearchOptions eadSearchOptions = new EadSearchOptions();
			eadSearchOptions.setArchivalInstitionId(ai.getAiId());
			eadSearchOptions.setEadClass(FindingAid.class);
			boolean hasEads = eadDAO.existEads(eadSearchOptions);
			if (!hasEads) {
				eadSearchOptions.setEadClass(HoldingsGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if (!hasEads) {
				eadSearchOptions.setEadClass(SourceGuide.class);
				hasEads = hasEads || eadDAO.existEads(eadSearchOptions);
			}
			if(!hasEads){
				path = ai.getEagPath();
				resultRemoveAI = cu.deleteArchivalInstitution(ai,true);
				if(path!=null && path.length()>0){ //there are files to be removed
					log.debug("There are somethings to be removed, checking...");
					path = path.substring(0,path.indexOf(File.separatorChar+"EAG"));
					path = path+"_old";
					String subDir = APEnetUtilities.getConfig().getRepoDirPath();
					if(!(resultRemoveAI!=null && resultRemoveAI.equals("ok"))){
						log.debug("Rollback detected, reverting _old to original path...");
						FileUtils.moveDirectory(new File(subDir+path),new File(subDir+path.substring(0,path.length()-"_old".length())));
						log.debug("Revert done!");
					}
				}else{
					log.debug("Nothing to be removed.");
				}
			}
		} catch (Exception e) {
			log.error( APEnetUtilities.generateThrowableLog(e));
		}
        return path;
	}
}


