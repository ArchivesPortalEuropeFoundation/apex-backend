package eu.apenet.oaiserver.verb;

import java.io.InputStream;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.oaiserver.util.OAIResponse;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;

public class GetRecord extends OAIVerb{
	
	private static Logger LOG = Logger.getLogger(GetRecord.class);
	private String identifier;
	private String metadataPrefix;
	private InputStream inputStream;
	
	public GetRecord(Map<String, String> params) {
		this.identifier = params.get("identifier");
		this.metadataPrefix = params.get("metadataPrefix");
	}

	public InputStream getInputStream(){
		return this.inputStream;
	}
	
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}

	public String getIdentifier(){
		return identifier;
	}

	public void setMetadataPrefix(String metadataPrefix){
		this.metadataPrefix = metadataPrefix;
	}

	public String getMetadataPrefix(){
		return metadataPrefix;
	}

	public boolean execute(){
		try{
			if(this.metadataPrefix!=null && this.metadataPrefix.length()>0 && this.identifier!=null && this.identifier.length()>0){
				Map<String,String> attributes = new HashMap<String,String>();
				attributes.put("identifier", this.identifier);
				attributes.put("metadataPrefix", this.metadataPrefix);
				this.inputStream = OAIResponse.getVerbInfo("GetRecord",attributes,this.getUrl());
				if(this.inputStream!=null){
					return true;
				}
			}
			Document emptyDocument = OAIResponse.getVerbResponse("GetRecord",null,this.getUrl());
			this.inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(emptyDocument,"badArgument"));
		}catch(Exception e){
			LOG.error("Params are NOT right in URL: ",e.getCause());
		}
		return false;
	}
	
	public static Document getRecordLogic(String identifier,String metadataPrefix,FindingAid findingAid,String requestURL) throws DOMException, ParserConfigurationException, ParseException {
		Document doc = null;
		Map<String,String> attributesMap = new HashMap<String, String>();
		if(identifier!=null){
			attributesMap.put("identifier", identifier);
		}if(metadataPrefix!=null){
			attributesMap.put("metadataPrefix",metadataPrefix);
		}
		Ese ese = null;
		NodeList errorNode = null;
		try {
			doc = OAIResponse.getVerbResponse("GetRecord",attributesMap,requestURL);
			MetadataFormat metadataFormat = DAOFactory.instance().getMetadataFormatDAO().getMetadataFormatByName(metadataPrefix);
			ese = DAOFactory.instance().getEseDAO().getEseByIdentifierAndFormat(identifier.substring(0,identifier.lastIndexOf(OAIUtils.SPECIAL_KEY)),metadataFormat);
			errorNode = doc.getElementsByTagName("error");
			if(errorNode==null || errorNode.getLength()>0){
				return doc;
			}else if(ese==null){
				return OAIUtils.getResponseError(doc,"idDoesNotExist");
			}
		} catch (Exception ex) {
			LOG.error("Error trying to get the object identified with "+identifier+": "+ex.getCause());
		}
		boolean found = false;
		boolean valid = false;
		if(ese!=null){ //Get the content (record nodes)
			Node verbNode = null;
			if (verbNode == null) {
				verbNode = doc.createElementNS(OAIResponse.NAMESPACE, "GetRecord");
			}
			String path = null;
			try {
				//Check metadataPrefix
				if(ese.getMetadataFormat().getFormat().equals(metadataPrefix)){
					valid = true;
				}else{
					valid = false;
				}
				Node tempNode = null;
				if(valid){
					Set<Ese> eses = new HashSet<Ese>();
					eses.add(ese);
					tempNode = OAIUtils.getRecordOfEse(eses,identifier,metadataPrefix,doc);
					if(tempNode!=null){
						found = true;
					}else{
						found = false;
					}
				}else{
					return OAIUtils.getResponseError(OAIResponse.getVerbResponse("GetRecord", attributesMap,requestURL), "cannotDisseminateFormat");
				}
				if(tempNode!=null){
					found = true;
					verbNode.appendChild(tempNode);
				}
			} catch (Exception e) {
				LOG.error("Error when trying to get records of an Ese file: "+ path + ". " + e.getCause());
			}
			Node oldNode = doc.getElementsByTagName("OAI-PMH").item(0);
			oldNode.appendChild(verbNode);
		}
		if(!found){
			try {
				doc = null;
				return OAIUtils.getResponseError(OAIResponse.getVerbResponse("GetRecord", attributesMap,requestURL), "idDoesNotExist");
			} catch (Exception e) {
				LOG.error("Error trying to build the error response document. "+e.getCause());
			}
		}
		return doc;
	}
}
