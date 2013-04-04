package eu.apenet.dashboard.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.infraestructure.ArchivalLandscapeNode;
import eu.apenet.dashboard.infraestructure.ArchivalLandscapeStructure;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.HoldingsGuide;
import eu.apenet.persistence.vo.Lang;

/**
 * @author Jara Alvarez
 * 
 * Temporary action for fill in the alorder attribute in the archival_institution entity for the first time
 */
public class FillOrderAL extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 153733697705282061L;
	static Semaphore sem = new Semaphore(1,true) ;
    private ArchivalInstitution ai_parent;
    private ArchivalInstitutionDAO aiDao;
    private ArchivalInstitution ai;
    private AiAlternativeNameDAO AiAltNameDao;    
    
    public List<String> Structure; //All structure in archival landscape for this partner
    public List<String> Archives; //Archival institutions for this partner    

	private final Logger log = Logger.getLogger(getClass());
	private List<Integer> archivalInstitutionsToDelete= new ArrayList<Integer>();
	private List<FindingAid> fasDeleted = new ArrayList<FindingAid>();
	private List<HoldingsGuide> hgsDeleted = new ArrayList<HoldingsGuide>();
	
	public List<FindingAid> getFasDeleted() {
		return fasDeleted;
	}

	public void setFasDeleted(List<FindingAid> fasDeleted) {
		this.fasDeleted = fasDeleted;
	}

	public List<HoldingsGuide> getHgsDeleted() {
		return hgsDeleted;
	}

	public void setHgsDeleted(List<HoldingsGuide> hgsDeleted) {
		this.hgsDeleted = hgsDeleted;
	}


	public List<Integer> getArchivalInstitutionsToDelete() {
		return archivalInstitutionsToDelete;
	}

    public ArchivalInstitution getAi_parent() {
		return ai_parent;
	}

	public void setAi_parent(ArchivalInstitution ai_parent) {
		this.ai_parent = ai_parent;
	}

	public ArchivalInstitutionDAO getAiDao() {
		return aiDao;
	}

	public void setAiDao(ArchivalInstitutionDAO aiDao) {
		this.aiDao = aiDao;
	}

	public ArchivalInstitution getAi() {
		return ai;
	}

	public void setAi(ArchivalInstitution ai) {
		this.ai = ai;
	}

	public AiAlternativeNameDAO getAiAltNameDao() {
		return AiAltNameDao;
	}

	public void setAiAltNameDao(AiAlternativeNameDAO aiAltNameDao) {
		AiAltNameDao = aiAltNameDao;
	}

	public void setArchivalInstitutionsToDelete(
			List<Integer> archivalInstitutionsToDelete) {
		this.archivalInstitutionsToDelete = archivalInstitutionsToDelete;
	}
	
	public String execute() throws Exception{

		Boolean ddbbResult=null;
		String resultStoreArchives = null;
		String result=null;
		String pathCountryAL = null;
		String pathCountryALOld = null;	
		
		
		CountryDAO countryDAO = DAOFactory.instance().getCountryDAO();
		List<Country> ListCountries = countryDAO.findAll();
		UserDAO partnerDao = DAOFactory.instance().getUserDAO();
						
		for (Country country : ListCountries)
		{
			ddbbResult=true;
			if (!country.getIsoname().equals("EU"))
			{
			try{
				String path = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;
				path = path + country.getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR;
				pathCountryAL = path + country.getIsoname() + "AL.xml";
				//pathCountryALOld = path + country.getIsoname() + "AL_old.xml";
				File file = new File(pathCountryAL);
				
				if (file.exists())
				{
					if (checkIdentifiers(file))				
					{
						//Store in data base the operation, the archival institutions
						HibernateUtil.beginDatabaseTransaction();	
						changeAL(country);
						resultStoreArchives=storeArchives(file, country);
						
						if (resultStoreArchives.equals("success")){		
							
							//The final commits			
						    HibernateUtil.commitDatabaseTransaction();	
							result= SUCCESS;
						}
						else{
							String errorMessage = getText("al.message.someErrors");
							addActionMessage(getText("al.message.someErrors"));
							ddbbResult = false;
							log.error(errorMessage);
							result= ERROR;
						}
					}
					else{						
						addActionMessage(getText("al.message.errAssignXMLId"));
						ddbbResult = false;						
						result= ERROR;
					}
				}
			} catch (HibernateException e) {
				ddbbResult=false;
				log.error("The new archival landscape of " +country.getIsoname() + " could not be overwrited requested by edition process", e);
			}
			catch (Exception e) {
				ddbbResult=false;
				log.error("The new archival landscape of " +country.getIsoname() + " could not be overwrited requested by edition process", e);	
			}finally{		
				if (!ddbbResult)
				{
					log.error("Some operation was not correct in overwriting the AL of the " + country.getIsoname()+ " requested by edition process. Rollbacking the whole transaction process");
					try {
						//this.setArchivalInstitutionsToDelete(a.getArchivalInstitutionsToDelete());
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						
						if (new File(pathCountryALOld).exists())
						{
							new File(pathCountryAL).delete();
							new File(pathCountryALOld).renameTo(new File(pathCountryAL));	
						}
						
					}catch (Exception e) {
						log.error("FATAL ERROR. The rollback of index or in repository could not be done successfully. A manually review of the AL of: " + country.getIsoname() + " must be done. Error: " + e);	
					}
					addActionMessage(getText("al.message.errorOverwritting") + " of the country: " + country.getCname());
					result = ERROR;
				}
			}			
		}
		}
		
		if (result == SUCCESS)
			 addActionMessage("OK");
		else
		 addActionMessage("error");
		
		return result;		
	}
	
	/**
	 * This method checks if a file contains unique identifiers 
	 * @param file
	 * @return boolean
	 */
	public boolean checkIdentifiers(File file){
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.normalizeDocument();
			doc.normalize();
			NodeList listTemp = doc.getElementsByTagName("c");
			boolean changes = false;
			List<String> identifiers = new ArrayList<String>(); //list of unique identifiers
			for(int i=0;i<listTemp.getLength();i++){
				Element cTemp = (Element)listTemp.item(i);
				if(cTemp.getAttributes()!=null && cTemp.getAttributes().getNamedItem("id")==null){
					String identifier = "A"+System.currentTimeMillis()+"-"+(new Float(+Math.random()*1000000).toString());
					cTemp.setAttribute("id",identifier); //This identifier can be repeated
					changes = true;
				}else if(cTemp.getAttributes()!=null && cTemp.getAttributes().getNamedItem("id")!=null){
					String identifier = cTemp.getAttributes().getNamedItem("id").getNodeValue();
					if(identifiers.contains(identifier)){
						return false;
					}
					identifiers.add(identifier);
				}
			}
			if(changes){
				TransformerFactory tf = TransformerFactory.newInstance();
		        Transformer transformer = tf.newTransformer();
		        transformer.transform(new DOMSource(doc), new StreamResult(file));
			}
		} catch (Exception e) {
			log.error("Checking c identifiers in makeTemporal():"+e.getCause(),e);
		}
		return true;
	}	
	
	public String storeArchives(File file, Country country) {
		
		String result="success";	
				
		//Get archival institution by this partner
		List <ArchivalInstitution> ais = new ArrayList<ArchivalInstitution>();
		AiAltNameDao = DAOFactory.instance().getAiAlternativeNameDAO();
		this.ai_parent = new ArchivalInstitution();
		this.aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		ais = this.aiDao.getGroupsAndArchivalInstitutionsByCountryId(country.getId(), "alorder", false);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        log.debug("Storing in database the institutions of the country " +country.getIsoname() + "...");
           
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
            
            for (int h = 0; h < alNodes.size(); h++) 
	        {
	            this.ai = new ArchivalInstitution();	            
		        //There's no (or no more) archival institutions for this partner 
		        if (ais.size()==0) 
		        {
		        	//Do not make any insertion 
		        }
		        //There're already archival institutions for this partner
		        else
		        {
		        	for (int i= 0; i< ais.size();i++)
		        	{            			
		         		//If the institution already exists, don't store in database		        		
		        		if (alNodes.get(h).getInternal_al_id().equals(ais.get(i).getInternalAlId()))
			            	{	
		            		this.ai = ais.get(i);
		            		//If the item stored in database has the same parent in the archival landscape, check the languages
		            		if ((alNodes.get(h).getParent_internal_al_id()== null) && (ais.get(i).getParent() ==null))
		            		{
		            			checkAlternativeNames(ais.get(i), alNodes.get(h));
		            		}		            		
		            		else if  ((alNodes.get(h).getParent_internal_al_id()!= null) && (ais.get(i).getParent() != null))
		            		{
		            			if ((alNodes.get(h).getParent_internal_al_id().equals(ais.get(i).getParent().getInternalAlId())))
					            	{
				            		checkAlternativeNames(ais.get(i), alNodes.get(h));
				            	}
		            		
		            			//If not, change the parent of the item for the new one
		            			else
		            			{		  
		            			
			            			if (alNodes.get(h).getParent_internal_al_id()== null)
			    		            	this.ai.setParent(null);
			    		            else {
			    		            	ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier( country.getId() , alNodes.get(h).getParent_internal_al_id());
			    		            	this.ai.setParent(ai_parent);			    		            	
			    		            }
		            			}
			            	}
		            		//Discrepancies in parents to solve (unless the parent is the node of the country=fonds)
		            		else if ((ais.get(i).getParent() == null) && (alNodes.get(h).getParent_internal_al_id()!= null))	            				
		            		{
		            				ai_parent = aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier( country.getId() , alNodes.get(h).getParent_internal_al_id());
		    		            	this.ai.setParent(ai_parent);	
		            		}
		            		//Update always the order of the archival landscape
		            		this.ai.setAlorder(alNodes.get(h).getNodeId());
		            		this.ai.setInternalAlId(alNodes.get(h).getInternal_al_id());
		            		log.debug("Updating the parent in database of the institution "+ this.ai.getAiname());
	            			this.aiDao.updateSimple(this.ai);
			            	ais.remove(i);
		            	}
		        		  else{
		        		    	if ((ais.get(i).getInternalAlId()== null) && (alNodes.get(h).getNames().containsValue(ais.get(i).getAiname().trim())))
		        		    	{		        		    		
		        		    		this.ai = ais.get(i);
		        		    		//Update always the order of the archival landscape
				            		this.ai.setAlorder(alNodes.get(h).getNodeId());
				            		this.ai.setInternalAlId(alNodes.get(h).getInternal_al_id());				            		
			            			this.aiDao.updateSimple(this.ai);
					            	ais.remove(i);
		    		            }
		        		  }
		            }
		       }//End-for ai
        }//End-for alNodes
            
            alNodes.clear();
		    ais.clear();
        }catch(IOException e)		{
        	log.error("The file " + file.getName() + " could not be read ", e);
        	result = "error";								
 		}catch (Exception e) {
			log.error("Some institutions of the country " + country.getIsoname() +" could not be stored or removed in database and repository ", e);
			result = "error";
		}
		return result;
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
        while(iterator.hasNext()){
        	found = false;
        	Map.Entry<String, String> temp = (Entry<String, String>) iterator.next();        	
        	for (int m=0;m<aiNames.size();m++)
        	{
        		//The language is the same
        		if (aiNames.get(m).getLang().getIsoname().toLowerCase().equals(temp.getKey().toLowerCase()))
        		{
        			found = true;
        			//The name is NOT the same 
        			if (!aiNames.get(m).getAiAName().equals(temp.getValue()))
        			{
        				aiNames.get(m).setAiAName(temp.getValue());
        				AiAlternativeName ai_altName = new AiAlternativeName();
        				ai_altName = ai_alNameDAO.findById(aiNames.get(m).getAiAnId());
        				ai_altName.setAiAName(temp.getValue());
        				ai_alNameDAO.updateSimple(ai_altName);
        			}
        		}
        		else
        		{
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
        				
        			}
        		}
        	}
        	if (!found)
        		namesToUpload.put(temp.getKey(), temp.getValue());
        }
        iterator = namesToUpload.entrySet().iterator();
        //All the languages that are still in the list must be stored in database because they are new ones
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
	}
	
	public synchronized void changeAL(Country country)
	{
			log.debug("Changing the general AL with the changes in the country " + country.getCname() + "...");
		
			String path = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR;
			path = path + country.getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR;
			String pathCountry = path + country.getIsoname() + "AL.xml";

			new Thread( new eu.apenet.dashboard.archivallandscape.ChangeArchivalLandscape(this.sem, country.getCname().toLowerCase(), pathCountry)).start();

	}
	
}

