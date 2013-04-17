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
import eu.apenet.persistence.dao.ArchivalInstitutionDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;

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
	/**
	 * 
	 * Through this procedure can be ensured that an institution within the Dashboard
	 * will have its unique ISO 15511 compliant identifier.
	 * 
	 * @param archivalInstitutionId
	 * @param fullFilePath
	 * @return boolean
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public boolean checkAndFixRepositorId(Integer archivalInstitutionId,String fullFilePath) throws TransformerException, ParserConfigurationException, SAXException, IOException{
		boolean changed = false;
		
        APEnetEAGDashboard eag = new APEnetEAGDashboard(archivalInstitutionId, fullFilePath);
        eag.setEagPath(fullFilePath);
        
        String otherRepositorId = eag.lookingForwardElementContent("/eag/archguide/repositorid");
        
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
		Document tempDoc = docBuilder.parse(fullFilePath);
        
		Element currentNode = null;
		
		if(otherRepositorId!=null && !otherRepositorId.isEmpty()){  
			//in case it isn’t, a code compliant with ISO 15511 will be created automatically 
			//for @repositorycode, using the country code plus an ascending counter
			if(!isWrongRepositorId(otherRepositorId) && otherRepositorId.charAt(2)!='-' ){
				int zeroes = 11-archivalInstitutionId.toString().length();
				otherRepositorId = new ArchivalLandscape().getmyCountry()+"-";
            	for(int x=0;x<zeroes;x++){
            		otherRepositorId+="0";
            	}
            	otherRepositorId+=archivalInstitutionId.toString();
			}
			//at this case the code provided is compliant with ISO 15511, it will 
			//be copied to the attribute @repositorycode coming with <repositorid>;
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
	/**
	 * Checks if it's a valid code.
	 * The code provided must be compliant with ISO 15511. 
	 * In case it isn’t or the code provided could not be used
	 * returns false 
	 * 
	 * @param repositorId
	 * @return boolean
	 */
	private boolean isWrongRepositorId(String repositorId){
		String isoCountry = new ArchivalLandscape().getmyCountry();
		if(repositorId.length()==14 && repositorId.substring(0,2).toLowerCase().equals(isoCountry) && StringUtils.isNumeric(repositorId.substring(3))){
			//TODO could be helpful store all identifiers or check all existings eags to get all ingested ISO-codes into repositorycode attribute
			Integer archivalInstitutionId = new Integer(repositorId.substring(3));
			if(archivalInstitutionId!=null){
				ArchivalInstitutionDAO aiDao = DAOFactory.instance().getArchivalInstitutionDAO();
				ArchivalInstitution archivalInstitution = aiDao.getArchivalInstitution(archivalInstitutionId);
				if(archivalInstitution==null || archivalInstitution.getCountry().getIsoname().equals(isoCountry)){  
					return true; //the ISO code used could not be unique because it's reserved to existing other institution of this country
				}
			}
		}
		return false;
	}
}
