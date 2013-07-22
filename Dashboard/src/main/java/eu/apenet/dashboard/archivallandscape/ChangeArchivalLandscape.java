package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.commons.utils.APEnetUtilities;

public class ChangeArchivalLandscape extends Thread {

	static Semaphore sem1;
	int partnerId;
	private String countryName;
	String pathCountry;
	private final Logger log = Logger.getLogger(getClass());
	
	double i;



	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getPathCountry() {
		return pathCountry;
	}

	public void setPathCountry(String pathCountry) {
		this.pathCountry = pathCountry;
	}

	public ChangeArchivalLandscape(Semaphore sem, String countryName, String path) {

        this.sem1 = sem;
        this.setCountryName(countryName);
        this.setPathCountry(path);
    } 
		
	/*public ChangeArchivalLandscape(double d) {
		super();
		this.i = d;
	}*/

	public void run()  {  
		
		try {

				this.sem1.acquire();				
				
				//CODE				
				int country = -1;
				int position = -1;
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
				try {
					dBuilder = dbFactory.newDocumentBuilder();
					String path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";
			    	File file = new File  (path);
					InputStream sfile = new FileInputStream(file);
			        Document doc = dBuilder.parse(sfile);
			        doc.getDocumentElement().normalize();	        
		        
			        NodeList prea = doc.getElementsByTagName("dsc");
			       	       
			        //To select the node to overwrite
			        for(int p= 0; p< prea.getLength();p++)
			        {
			        	NodeList a = prea.item(p).getChildNodes();
			            for (int i = 0; i < a.getLength(); i++) 
			            {
				        	if (a.item(i).getNodeName().equals("c"))
				        	{
					                NamedNodeMap attributes = a.item(i).getAttributes();
					                Node attribute = attributes.getNamedItem("level");
					                if ((attribute.getNodeValue().equals("fonds"))) {
					                    NodeList listDid = a.item(i).getChildNodes();
					                    for (int l = 0; l < listDid.getLength(); l++) {
					                        if (listDid.item(l).getNodeName() == "did") {
					                            NodeList listUnitTitle = listDid.item(l).getChildNodes();
					                            for (int j = 0; j < listUnitTitle.getLength(); j++) {
					                                if (listUnitTitle.item(j).getNodeName().equals("unittitle")) {
					                                    NamedNodeMap attributesUnitTitle = listUnitTitle.item(j).getAttributes();
					                                    Node attributeUnitTitle = attributesUnitTitle.getNamedItem("type");
					                                    if (attributeUnitTitle.getNodeValue().equals("eng")) {
					                                        if (listUnitTitle.item(j).getTextContent().toLowerCase().equals(this.getCountryName()))
					                                            country = i;     
					                                        	position = p;
					                                    }
					                                }
					                            }
					                        }
					                    }
					                }                
					            }
				        	}
			        }

		            //Delete the node to overwrite if exists
		            Node thisNode;
		            
		            if (country>0)
		            {       
		            	thisNode = prea.item(position).getChildNodes().item(country);
		            }
		            else
		            	thisNode = doc.getElementsByTagName("dsc").item(0);
		            
		            //Get the new node to insert in the archival landscape
		            DocumentBuilderFactory dbFactory2 = DocumentBuilderFactory.newInstance();
		            DocumentBuilder dBuilder2 = dbFactory2.newDocumentBuilder();
		            //String pathCountry = this.getmyPath(this.getmyCountry());	
			        //pathCountry = pathCountry + this.getmyCountry() + "AL.xml";
		            InputStream sfile2 = new FileInputStream(this.getPathCountry());
		            Document doc2 = dBuilder2.parse(sfile2);
		            doc2.getDocumentElement().normalize();
		             
		            NodeList b = doc2.getElementsByTagName("c");
		            NamedNodeMap attributes = b.item(0).getAttributes();
		            Node attribute = attributes.getNamedItem("level");                
		            if (attribute.getNodeValue().equals("fonds")) 
		            {
		                Node nodeToInsert = b.item(0);                
		                Node nodewherecopy = doc.importNode(nodeToInsert, true);
		                //Replace the node
		                thisNode.getParentNode().replaceChild(nodewherecopy, thisNode);
		             }                        	            
		         	  
			        //Write the right node      
			        Source source = new DOMSource(doc);
		        	String path2 = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR;
		            path2 = path2 + "AL.xml";
		            Result result = new StreamResult(new java.io.File(path2));

		            //Write the XML
		            Transformer transformer;
		            transformer = TransformerFactory.newInstance().newTransformer();
		            transformer.transform(source, result);    

				} catch (Exception e) {
					log.error(e.getMessage());
				}finally {
					this.sem1.release();
				}
				//CODE				
		    	
		    	log.debug("The general AL with the changes in the country " + this.getCountryName() + " has been changed");
		    	
		} catch (InterruptedException e1) {
			log.error(e1.getMessage());
			this.sem1.release();
		}
 

	  
	 }


}
