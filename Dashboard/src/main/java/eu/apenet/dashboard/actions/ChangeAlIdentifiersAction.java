package eu.apenet.dashboard.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.archivallandscape.ChangeAlIdentifiers;
import eu.apenet.dashboard.security.SecurityContext;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.hibernate.HibernateUtil;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;

/**
 * Changing the identifiers of the archival landscape items
 * 
 *  1- Change in DDBB archival_institution entity
 *  2- Change the al of the country
 *  3- Change the AL.xml general
 *    
 * @author jara
 */

@SuppressWarnings("serial")
public class ChangeAlIdentifiersAction extends ActionSupport implements Preparable {

	private List<ArchivalInstitution> institutionList = new ArrayList<ArchivalInstitution>();	
	private Integer institutionSelected;
	private final Logger log = Logger.getLogger(getClass());
	private Country country = new Country();
	private String identifier;
	private String identifierOld;
	
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<ArchivalInstitution> getInstitutionList() {
		return institutionList;
	}

	public void setInstitutionList(List<ArchivalInstitution> institutionList) {
		this.institutionList = institutionList;
	}

	public Integer getInstitutionSelected() {
		return institutionSelected;
	}

	public void setInstitutionSelected(Integer institutionSelected) {
		this.institutionSelected = institutionSelected;
	}

	public String getIdentifierOld() {
		return identifierOld;
	}

	public void setIdentifierOld(String identifierOld) {
		this.identifierOld = identifierOld;
	}

	public void validate(){
		
		log.debug("Validating textfields in changing AL identifiers process...");
		
		if (this.getIdentifier()!= null)
		{
			if (this.getIdentifier().length() == 0)
				addFieldError("identifier", "Identifier required");
		}
	}
	
	public String execute()   {
		return SUCCESS;
	}

	@Override
	public void prepare() throws Exception {
		
		CountryDAO countryDao = DAOFactory.instance().getCountryDAO();
		this.setCountry(countryDao.findById(SecurityContext.get().getCountryId()));
		ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
		
		this.setInstitutionList(aiDao.getGroupsAndArchivalInstitutionsByCountryId(this.getCountry().getId(),"alorder", true ));
	}
	
	public String storeIdentifier(){
		
		String result = null;
		ArchivalLandscape al = new ArchivalLandscape();
		ChangeAlIdentifiers cAlId = new ChangeAlIdentifiers();
		
		if ((this.getIdentifier()!= null)) 
		{
			try{
				
				HibernateUtil.beginDatabaseTransaction();
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution ai = new ArchivalInstitution();
				
				ai = aiDao.findById(this.getInstitutionSelected());	
				this.setIdentifierOld(ai.getInternalAlId());				
				if (!(this.getIdentifier().trim().equals(this.getIdentifierOld().trim())))
				{
					//Checking of the unique identifiers		
					
					String available = cAlId.checkIdentifierAvailability(this.getInstitutionList(), this.getIdentifier(), ai);
					if (!(available.equals("success")))
					{
						addActionMessage(getText("al.message.changeIdentifier.alreadyUsed"));
						result= INPUT;
					}else 
					{
						String ddbbChanged = cAlId.changeIdentifierinDDBB(ai, this.getIdentifier());
						if (!ddbbChanged.equals("success"))
						{
							addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.error"));
							result = ERROR;
						}
						else 
						{
							String resultChangeXml = changeIdentifierinAlNode(this.getIdentifierOld(),this.getIdentifier());				
							if (resultChangeXml.equals(SUCCESS)){				
								al.changeAL();
								result = SUCCESS;
								addActionMessage(getText("al.message.changeIdentifier.identifierChanged"));
								HibernateUtil.commitDatabaseTransaction();
								HibernateUtil.closeDatabaseSession();
							}
							else{
								addActionMessage(ai.getAiname() + ":   " + getText("al.message.changeIdentifier.error"));
								result = ERROR;
							}
						}
					}
				}
				else
				{
					addActionMessage(getText("al.message.changeIdentifier.identifierEqual"));
					result = INPUT;	
				}
				
			}catch(Exception e){
				log.error(e.getMessage());
				addActionMessage(getText("al.message.changeIdentifier.error"));
				result = ERROR;				
			}finally{
				if (result.equals(ERROR)){
					try{
						log.debug("Rollbacking the changing AL identifiers process");
	
						HibernateUtil.rollbackDatabaseTransaction();
						HibernateUtil.closeDatabaseSession();
						
						//Rollback the changing of the xml files
						String resultChangeXml = changeIdentifierinAlNode(this.getIdentifier(),this.getIdentifierOld());
						if (resultChangeXml.equals(SUCCESS))
							al.changeAL();
						/*else
							log.warn("The changing identifier " + this.getIdentifierOld() + " into " + this.getIdentifier() + " in the storeIdentifier() rollback could not be done properly. Please review them manually.");*/
					}
					catch(Exception e){						
						log.error("Error in rollbacking the changing archival landscape identifiers process. Please review manually.");
						log.error(e.getMessage());
						log.error(e.getStackTrace());						
					}
				}
			}
		
		}else
			result = INPUT;		
		
		return result;
	}

	//To change the id attribute in the correspondent c element node, in the xml files.
	private String changeIdentifierinAlNode(String oldIdentifier, String newIdentifier) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Boolean changed = false;		
        ArchivalLandscape al = new ArchivalLandscape();
		String path = al.getmyPath(al.getmyCountry()) + al.getmyCountry() + "AL.xml";
        String resultValue= null;
        
		try {

			dBuilder = dbFactory.newDocumentBuilder();				        	
	    	File file = new File  (path);
			InputStream sfile = new FileInputStream(file);
	        Document doc = dBuilder.parse(sfile);
	        doc.getDocumentElement().normalize();	
	        
        	NodeList cNodes = doc.getElementsByTagName("c");
        	for (int i =0;i<cNodes.getLength();i++)
        	{
        		NamedNodeMap attributes = cNodes.item(i).getAttributes();
                Node attribute = attributes.getNamedItem("id");
                if (attribute.getNodeValue().equals(oldIdentifier)){
                	attribute.setNodeValue(newIdentifier);
                	changed = true;
                }
        	}
        	
        	if (changed)
        	{
        		//Write the right node      
		        Source source = new DOMSource(doc);
	            Result result = new StreamResult(new java.io.File(path));

	            //Write the XML
	            Transformer transformer;
	            transformer = TransformerFactory.newInstance().newTransformer();
	            transformer.transform(source, result);    
	            
	            resultValue = SUCCESS;
        	}else
        	{
        		log.error("Item with this id " + oldIdentifier + " not found in " +file.getName());
        		log.error("The identifier could not be changed");
        		resultValue = ERROR;
        	}
        	
		}catch(Exception e){
			log.error("Error in changing the identifier process in the " + path);
			log.error(e.getMessage());
			log.error(e.getStackTrace());
			resultValue = ERROR;
		}
		return resultValue;		
		
	}
}
