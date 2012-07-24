package eu.apenet.dashboard.archivallandscape;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ChangeAlIdentifiers {

	private final Logger log = Logger.getLogger(getClass());
	
	//Checking of the unique identifiers					
	public String checkIdentifierAvailability(List<ArchivalInstitution> institutionList, String identifier, ArchivalInstitution ai){
		String result ="success";
		try{
			for (ArchivalInstitution aiCheck: institutionList)
			{
				if (aiCheck.getInternalAlId().equals(identifier) && (aiCheck.getAiId() != ai.getAiId()))
					{
						log.debug("al.message.changeIdentifier.alreadyUsed");
						result= "input";			
					}
			}
		}catch(Exception e)
		{
			result = "error";
		}
		return result;
	}
	
	public String changeIdentifierinDDBB(ArchivalInstitution ai, String newIdentifier)
	{
		String result = null;
		log.debug("Changing the AL identifier of the " + ai.getAiname());
		try{
			ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
			ai.setInternalAlId(newIdentifier.trim());
			aiDao.updateSimple(ai);
			result = "success";
		}catch(Exception e){
			result = "error";
		}		
		return result;
	}
	
	public String changeIdentifierinAlNode(String oldIdentifier, String newIdentifier, String path) {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Boolean changed = false;		
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
	            
	            resultValue = "success";
        	}else
        	{
        		log.error("Item with this id " + oldIdentifier + " not found in " +file.getName());
        		log.error("Therefore, the identifier could not be changed in " + file.getName());
        		resultValue = "error";
        	}
        	//Always success
        	resultValue = "success";
        	
		}catch(Exception e){
			log.error("Error in changing the identifier process in the " + path);
			log.error(e.getMessage());
			log.error(e.getStackTrace());
			resultValue = "error";
		}
		return resultValue;		
		
	}

	
	
}
