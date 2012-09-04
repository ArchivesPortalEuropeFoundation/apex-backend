package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.opensymphony.xwork2.ActionSupport;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.AiAlternativeNameDAO;
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.dao.CountryDAO;
import eu.apenet.persistence.dao.LangDAO;
import eu.apenet.persistence.dao.UserDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.AiAlternativeName;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.Lang;
import eu.apenet.persistence.vo.User;

public class FillPrimaryAltInstNameAction extends ActionSupport {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3580578617260303246L;

	private final Logger log = Logger.getLogger(getClass());


	public String execute()  {  
		
		AiAlternativeNameDAO ai_alNameDAO = DAOFactory.instance().getAiAlternativeNameDAO();
    	AiAlternativeName ai_altName = new AiAlternativeName();    	  	
    	ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
    	ArchivalInstitution ai = new ArchivalInstitution();  
    	LangDAO langDao = DAOFactory.instance().getLangDAO();
    	Lang lang = new Lang();
    	Boolean primary = false;
		        
    	log.info("Starting FillPrimaryAction...");
    	
				try {

					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			        DocumentBuilder dBuilder;
					
					dBuilder = dbFactory.newDocumentBuilder();
					String path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";		
					log.info("Getting AL.xml : " + path);
			    	File file = new File  (path);
					InputStream sfile = new FileInputStream(file);
			        Document doc = dBuilder.parse(sfile);
			        doc.getDocumentElement().normalize();	
			        
		        	NodeList cNodes = doc.getElementsByTagName("c");		        	
		        	log.info("Nodes size to check : " + cNodes.getLength());
		        	
		        	for (int i=0; i<cNodes.getLength();i++){	
		        		Integer	countryId = null;
		        			log.info("Starting the for...");
			        		NamedNodeMap attributes = cNodes.item(i).getAttributes();
			                Node attribute = attributes.getNamedItem("level");
			                if ((attribute.getNodeValue().equals("fonds"))){
			                	NodeList listDid = cNodes.item(i).getChildNodes();
			                	for (int l = 0; l < listDid.getLength(); l++) {
			                        if (listDid.item(l).getNodeName() == "did") {
			                        	NodeList listUnitTitle = listDid.item(l).getChildNodes();
			                            for (int j = 0; j < listUnitTitle.getLength(); j++) {
			                                if (listUnitTitle.item(j).getNodeName().equals("unittitle")) {
			                                	log.info("Unitittle to check : " + listUnitTitle.item(j).getNodeName());
			                                	NamedNodeMap attributesUnitTitle = listUnitTitle.item(j).getAttributes();
			                                	Node attributeLang = attributesUnitTitle.getNamedItem("type");
			                                	if (attributeLang.getNodeValue().equals("eng")) {
			                                		CountryDAO couDao = DAOFactory.instance().getCountryDAO();
			                                		Country cou = couDao.getCountryByCname(listUnitTitle.item(j).getTextContent().toUpperCase());
			                                		countryId = cou.getId();
			                                	}
			                                }
			                            }
			                        }
			                	}
			                }			                
			                else{
			                	
			                	NamedNodeMap attributesFonds = cNodes.item(i).getAttributes();
                                Node idAttribute  = attributesFonds.getNamedItem("id");	

			                	NodeList listDid = cNodes.item(i).getChildNodes();
			                    for (int l = 0; l < listDid.getLength(); l++) {
			                        if (listDid.item(l).getNodeName() == "did") {
			                        	primary= false;
			                            NodeList listUnitTitle = listDid.item(l).getChildNodes();
			                            for (int j = 0; j < listUnitTitle.getLength(); j++) {
			                                if (listUnitTitle.item(j).getNodeName().equals("unittitle")) {
			                                   
			                                	log.info("Unitittle to check : " + listUnitTitle.item(j).getNodeName());
			                                    if ((idAttribute != null)&&(countryId != null))
			                                    {
			                                    	NamedNodeMap attributesUnitTitle = listUnitTitle.item(j).getAttributes();
				                                    ai= aiDao.getArchivalInstitutionsByCountryIdandAlIdentifier(countryId, idAttribute.getNodeValue().toString());			                                   
				                                    Node attributeLang = attributesUnitTitle.getNamedItem("type");
				                                    lang = langDao.getLangByIsoname(attributeLang.getNodeValue().toUpperCase().toString());
				                                    ai_altName = ai_alNameDAO.findByAIIdandLang(ai, lang);
				                                    if (ai_altName!= null)
				                                    {
					                                    if ((!primary)){			                                    	
					    			                    	primary = true;
					    			                    	ai_altName.setPrimaryName(primary);
					    			                    }				                                    
					                                    ai_alNameDAO.update(ai_altName);	
					                                   log.info(ai_altName.getAiAName() + "  with language "+ lang.getIsoname() +" updated to primary " +primary);
				                                    }
			                                    }
			                                }
			                            }}}			                	
			                }
			        	}
		        	addActionMessage("OK");
		        	return SUCCESS;

				} catch (Exception e) {
					addActionMessage("ERROR");
					log.error(e.getMessage());
					e.printStackTrace();
					return ERROR;
				}	    	
	 }
}
