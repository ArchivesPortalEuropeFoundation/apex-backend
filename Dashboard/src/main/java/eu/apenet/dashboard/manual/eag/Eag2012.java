package eu.apenet.dashboard.manual.eag;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.apenet.dashboard.archivallandscape.ArchivalLandscape;
import eu.apenet.dashboard.manual.APEnetEAGDashboard;

/**
 * This class has been created to manage all EAG2012 information
 * into one object.
 */
public class Eag2012 {
	
	private String maintenanceStatus;
	private String repositoryId;
	private String otherRepositorId;
	private String autform;
	private String parform;
	private String nonpreform;
	private String dateSet;
	private String repositoryName;
	private String citation;
	private String objectBinWrap;
	private String controlId;
	private String controlLanguage;
	private String recordIdId;
	private String recordIdValue;
	private String otherRecordIdLocalType;
	private String otherRecordIdId;
	private String otherRecordIdValue;
	private String sourcesId;
	private String sourcesLang;
	private String sourceHref;
	private String sourceId;
	private String sourceLastDateTimeVerified;

	public Eag2012() {
		// TODO put all parameters here
	}

	public String getMaintenanceStatus() {
		return this.maintenanceStatus;
	}

	public String getRepositoryId() {
		return this.repositoryId;
	}

	public String getOtherRepositorId() {
		return this.otherRepositorId;
	}

	public String getAutform() {
		return this.autform;
	}

	public String getParform() {
		return this.parform;
	}

	public String getNonpreform() {
		return this.nonpreform;
	}

	public String getDateSet() {
		return this.dateSet;
	}

	public String getRepositoryName() {
		return this.repositoryName;
	}

	public String getCitation() {
		return this.citation;
	}

	public String getObjectBinWrap() {
		return this.objectBinWrap;
	}

	public String getControlId() {
		return this.controlId;
	}

	public String getControlLanguage() {
		return this.controlLanguage;
	}

	public String getRecordIdId() {
		return this.recordIdId;
	}

	public String getRecordIdValue() {
		return this.recordIdValue;
	}

	public String getOtherRecordIdLocalType() {
		return this.otherRecordIdLocalType;
	}

	public String getOtherRecordIdId() {
		return this.otherRecordIdId;
	}

	public String getOtherRecordIdValue() {
		return this.otherRecordIdValue;
	}

	public String getSourcesId() {
		return this.sourcesId;
	}

	public String getSourcesLang() {
		return this.sourcesLang;
	}

	public String getSourceLastDateTimeVerified() {
		return sourceLastDateTimeVerified;
	}

	public String getSourceHref() {
		return sourceHref;
	}

	public String getSourceId() {
		return sourceId;
	}
	
	public boolean checkAndFixRepositorId(Integer archivalInstitutionId,String fullFilePath) throws TransformerException, ParserConfigurationException, SAXException, IOException{
        
		boolean changed = false;
        APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, fullFilePath);
        //eag.setEagPath(fullFilePath);
        String otherRepositorId = eag.lookingForwardElementContent("/eag/archguide/repositorid");
        
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document tempDoc = docBuilder.parse(fullFilePath);
        
		Element currentNode = null;
		//TODO it's needed store all identifiers or check all existings eags to get all ingested ISO-codes into repositorycode attribute
		if(otherRepositorId!=null && !otherRepositorId.isEmpty()){  
			//Fill with new code
			if(otherRepositorId.length()!=14 || otherRepositorId.charAt(2)!='-' || StringUtils.isAlphanumeric(otherRepositorId.substring(3))){//XY-###########.length()=14
				int zeroes = 11-archivalInstitutionId.toString().length();
				otherRepositorId = new ArchivalLandscape().getmyCountry()+"-";
            	for(int x=0;x<zeroes;x++){
            		otherRepositorId+="0";
            	}
            	otherRepositorId+=archivalInstitutionId.toString();
			}
			NodeList recordsIds = tempDoc.getElementsByTagName("repositorid");
    		for(int i=0;i<recordsIds.getLength() && !changed;i++){
    			currentNode = (Element) recordsIds.item(i);
    			Node parent = currentNode.getParentNode();
    			if(parent!=null && parent.getNodeName().equals("archguide")){
    				parent = parent.getParentNode();
    				if(parent!=null && parent.getNodeName().equals("eag")){
    					currentNode.setAttribute("repositorycode",otherRepositorId);
    					changed = true;
    				}
    			}
    		}
		}
        
        if(changed){
			TransformerFactory tf = TransformerFactory.newInstance(); // Save changes
			Transformer transformer = tf.newTransformer();
			transformer.transform(new DOMSource(tempDoc), new StreamResult(new File(fullFilePath)));
		}
        return changed;
	}
}
