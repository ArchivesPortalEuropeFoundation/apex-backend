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

public class deleteNodeinLA extends Thread {

	static Semaphore sem1;
	private final Logger log = Logger.getLogger(getClass());

	private String country;
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public deleteNodeinLA(Semaphore sem, String country) {

        this.sem1 = sem;
        this.setCountry(country);
    } 
		
	public void run()  {  
		
		try {

				this.sem1.acquire();				

		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
		        Boolean countryFound=false;
		        
				try {

					dBuilder = dbFactory.newDocumentBuilder();
					String path = APEnetUtilities.getDashboardConfig().getArchivalLandscapeDirPath() + APEnetUtilities.FILESEPARATOR + "AL.xml";		        	
			    	File file = new File  (path);
					InputStream sfile = new FileInputStream(file);
			        Document doc = dBuilder.parse(sfile);
			        doc.getDocumentElement().normalize();	
			        
		        	NodeList cNodes = doc.getElementsByTagName("c");
			        	for (int i=0; i<cNodes.getLength();i++)
			        	{	
			        		NamedNodeMap attributes = cNodes.item(i).getAttributes();
			                Node attribute = attributes.getNamedItem("level");
			                if ((attribute.getNodeValue().equals("fonds"))) {
			                	if (cNodes.item(i).getTextContent().toLowerCase().toString().contains(this.getCountry().toLowerCase()))
			                	{
			                		countryFound=true;
			                		cNodes.item(i).getParentNode().removeChild(cNodes.item(i));
			                	}
			                }
			        	}
			       
			        if (!countryFound)
			        {
			        	log.error("Node for " + this.getCountry() + " not found in AL.xml");
			        	this.sem1.release();
			        }
	         	  
			        //Write the right node      
			        Source source = new DOMSource(doc);
		            Result result = new StreamResult(new java.io.File(path));

		            //Write the XML
		            Transformer transformer;
		            transformer = TransformerFactory.newInstance().newTransformer();
		            transformer.transform(source, result);    

				} catch (Exception e) {
					log.error(e.getMessage());
				}finally {
					this.sem1.release();
				}
	    	
		    	log.debug("The country" +this.getCountry()+" has been deleted from the general AL.xml");
		    	
		} catch (InterruptedException e1) {
			log.error(e1.getMessage());
			this.sem1.release();
		}
 

	  
	 }


}
