package eu.apenet.dashboard.archivallandscape;

import java.io.File;

import java.io.FileInputStream;
import java.io.InputStream;
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

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.CouAlternativeNameDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.CouAlternativeName;
import eu.apenet.persistence.vo.Country;

public class InsertNodeinLA extends Thread {

	static Semaphore sem1;
	private final Logger log = Logger.getLogger(getClass());

	private String langIsoName;
	private String node;	
	private Country country;
	private String altName;
	
	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getLangIsoName() {
		return langIsoName;
	}

	public void setLangIsoName(String langIsoName) {
		this.langIsoName = langIsoName;
	}

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getAltName() {
		return altName;
	}

	public void setAltName(String altName) {
		this.altName = altName;
	}

	public InsertNodeinLA(Semaphore sem, String node, Country country, String langIsoName, String altName) {

        this.sem1 = sem;
        this.setCountry(country);
        this.setLangIsoName(langIsoName);
        this.setNode(node);        
        this.setAltName(altName);
    } 
		
	public void run()  {  
		
		try {

				this.sem1.acquire();				

		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
		        Element did = null;
		        String nameToInsert;		        
		        Boolean stored = false;
		        Document doc;
		        String path;
		        
				try {
					
					if (altName == null)
			        	nameToInsert= country.getCname();
			        else
			        	nameToInsert = altName;
					
					//To include the new country node in AL general
			        if (node.equals("dsc"))
			        {
			        	dBuilder = dbFactory.newDocumentBuilder();
						path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";		        	
				    	File file = new File  (path);
						InputStream sfile = new FileInputStream(file);
				        doc = dBuilder.parse(sfile);
				        doc.getDocumentElement().normalize();	
			        	
			        	Element newC = doc.createElement("c");
				        newC.setAttribute("encodinganalog", "3.1.4");
				        newC.setAttribute("id" , "1");
				        newC.setAttribute("level", "fonds");
				        
				        did = doc.createElement("did");
				        NodeList dsc = doc.getElementsByTagName("dsc");			        
				        dsc.item(0).appendChild(newC);	
				        newC.appendChild(did);
				        
				        Element unittitle = doc.createElement("unittitle");
				        unittitle.setAttribute("encodinganalog", "3.1.2");
				        unittitle.setAttribute("type", langIsoName.toLowerCase());
			        	unittitle.setTextContent(nameToInsert);
				        
				        if (did != null)
				        	did.appendChild(unittitle);
				        
				       //Write the right node      
				        Source source = new DOMSource(doc);
			            Result result = new StreamResult(new java.io.File(path));
	
			            //Write the XML
			            Transformer transformer;
			            transformer = TransformerFactory.newInstance().newTransformer();
			            transformer.transform(source, result);  
			        
			        //To include the country translation
			        }else
			        {
			        	Boolean changed=false;
			        	dBuilder = dbFactory.newDocumentBuilder();
			        	//First in specific AL of the country
						path = APEnetUtilities.getConfig().getRepoDirPath() + APEnetUtilities.FILESEPARATOR + country.getIsoname() + APEnetUtilities.FILESEPARATOR + "AL" + APEnetUtilities.FILESEPARATOR + country.getIsoname()+ "AL.xml";		        	
				    	File file = new File  (path);
				    	if (!file.exists())
				    	{
				    		path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";		        	
					    	file = new File  (path);
					    	changed = true;
				    	}
						InputStream sfile = new FileInputStream(file);
				        doc = dBuilder.parse(sfile);
				        doc.getDocumentElement().normalize();	
			        	
			        	CouAlternativeNameDAO couAltNamesDAO = DAOFactory.instance().getCouAlternativeNameDAO();
			    		List<CouAlternativeName> altNames = couAltNamesDAO.getAltNamesbyCountry(country);
			        
			        	NodeList cNodes = doc.getElementsByTagName("c");
			        	for (int i=0; i<cNodes.getLength();i++)
			        	{	
			        		NamedNodeMap attributes = cNodes.item(i).getAttributes();
			                Node attribute = attributes.getNamedItem("level");
			                if ((attribute.getNodeValue().equals("fonds"))) {
			                	if (cNodes.item(i).getTextContent().toString().toLowerCase().contains(altNames.get(0).getCouAnName().toLowerCase()))
			                	{
			                		NodeList listDid = cNodes.item(i).getChildNodes();
				                    for (int l = 0; l < listDid.getLength(); l++) {
				                        if (listDid.item(l).getNodeName().equals("did"))
				                        {
				                        	did = (Element) listDid.item(l);
				                        	NodeList listUnittitle = listDid.item(l).getChildNodes();
				                        	for (int m=0;m<listUnittitle.getLength();m++)
				                        	{				                        		
				                        		if (listUnittitle.item(m).getNodeName().equals("unittitle"))
				                        		{
				                        			NamedNodeMap attributesUnittile = listUnittitle.item(m).getAttributes();
				                        			Node attributeLang = attributesUnittile.getNamedItem("type");
				                        				
				                        			if(attributeLang.getNodeValue().toString().toLowerCase().equals(langIsoName.toLowerCase()))				                        			
				                        				stored = true;
				                        		}
				                        	}
				                        }
				                    }
			                	}
			                }
			        	}
			        	if (!stored)
				        {
			        		stored=true;
					        Element unittitle = doc.createElement("unittitle");
					        unittitle.setAttribute("encodinganalog", "3.1.2");
					        unittitle.setAttribute("type", langIsoName.toLowerCase());
				        	unittitle.setTextContent(nameToInsert);
					        
					        if (did != null)
					        	did.appendChild(unittitle);
					        else
					        {
					        	log.error("Node for " + country.getCname() + " not found in AL.xml");
					        	this.sem1.release();
					        }
		         	  
					        //Write the right node      
					        Source source = new DOMSource(doc);
				            Result result = new StreamResult(new java.io.File(path));
		
				            //Write the XML
				            Transformer transformer;
				            transformer = TransformerFactory.newInstance().newTransformer();
				            transformer.transform(source, result);
				        }
			        	
			        	//Second, in general AL  
				        if (!changed)
				        {
				        	path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";		        	
					    	file = new File  (path);
					    	stored = false;
							sfile = new FileInputStream(file);
					        doc = dBuilder.parse(sfile);
					        doc.getDocumentElement().normalize();	
				        	
				        	couAltNamesDAO = DAOFactory.instance().getCouAlternativeNameDAO();
				    		altNames = couAltNamesDAO.getAltNamesbyCountry(country);
				        
				        	cNodes = doc.getElementsByTagName("c");
				        	for (int i=0; i<cNodes.getLength();i++)
				        	{	
				        		NamedNodeMap attributes = cNodes.item(i).getAttributes();
				                Node attribute = attributes.getNamedItem("level");
				                if ((attribute.getNodeValue().equals("fonds"))) {
				                	if (cNodes.item(i).getTextContent().toString().toLowerCase().contains(altNames.get(0).getCouAnName().toLowerCase()))
				                	{
				                		NodeList listDid = cNodes.item(i).getChildNodes();
					                    for (int l = 0; l < listDid.getLength(); l++) {
					                        if (listDid.item(l).getNodeName().equals("did"))
					                        {
					                        	did = (Element) listDid.item(l);
					                        	NodeList listUnittitle = listDid.item(l).getChildNodes();
					                        	for (int m=0;m<listUnittitle.getLength();m++)
					                        	{				                        		
					                        		if (listUnittitle.item(m).getNodeName().equals("unittitle"))
					                        		{
					                        			NamedNodeMap attributesUnittile = listUnittitle.item(m).getAttributes();
					                        			Node attributeLang = attributesUnittile.getNamedItem("type");
					                        				
					                        			if(attributeLang.getNodeValue().toString().toLowerCase().equals(langIsoName.toLowerCase()))				                        			
					                        				stored = true;
					                        		}
					                        	}
					                        }
					                    }
				                	}
				                }
				        	}
				        }
			        
				        if (!stored)
				        {
					        Element unittitle = doc.createElement("unittitle");
					        unittitle.setAttribute("encodinganalog", "3.1.2");
					        unittitle.setAttribute("type", langIsoName.toLowerCase());
				        	unittitle.setTextContent(nameToInsert);
					        
					        if (did != null)
					        	did.appendChild(unittitle);
					        else
					        {
					        	log.error("Node for " + country.getCname() + " not found in AL.xml");
					        	this.sem1.release();
					        }
		         	  
					        //Write the right node      
					        Source source = new DOMSource(doc);
				            Result result = new StreamResult(new java.io.File(path));
		
				            //Write the XML
				            Transformer transformer;
				            transformer = TransformerFactory.newInstance().newTransformer();
				            transformer.transform(source, result);  
				        }else
				        	log.debug("Name: "+ nameToInsert +" not stored in AL.xml for: " +country.getCname()+" because is already stored.");
	
					}
				}catch (Exception e) {
					log.error(e.getMessage());
				}finally {
					this.sem1.release();
				}
	    	
		    	log.debug("The general AL with the new country " + this.getCountry().getCname() + " has been inserted");
		    	
		} catch (InterruptedException e1) {
			log.error(e1.getMessage());	
			this.sem1.release();
		}
 

	  
	 }


}
