package eu.apenet.oaiserver.verb;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import eu.apenet.oaiserver.util.OAIResponse;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.dao.EseDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;

public class ListMetadataFormats extends OAIVerb{
	private static Logger LOG = Logger.getLogger(ListMetadataFormats.class);
	private InputStream inputStream;
	private String identifier;
	
	public ListMetadataFormats(Map<String, String> params) {
		this.identifier = params.get("identifier");
	}

	public String getIdentifier(){
		return this.identifier;
	}
	
	public void setIdentifier(String identifier){
		this.identifier = identifier;
	}
	
	public InputStream getInputStream(){
		return this.inputStream;
	}
	
	public boolean execute(){
		try{
			Map<String,String> map = new HashMap<String, String>();
			if(this.identifier!=null && !this.identifier.isEmpty()){
				map.put("identifier", this.identifier);
			}
			this.inputStream = OAIResponse.getVerbInfo("ListMetadataFormats",map,this.getUrl());
			if(this.inputStream!=null){
				return true;
			}
			Document emptyDocument = OAIResponse.getVerbResponse("ListMetadataFormats",new HashMap<String, String>(),this.getUrl());
			this.inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(emptyDocument,"badArgument"));
		}catch(Exception e){
			LOG.error("Params are NOT right in URL: ",e.getCause());
		}
		return false;
	}
	
	public static Document getListMetadataFormatsLogic(Map<String, String> arguments, String requestURL) throws Exception {
		Document doc = OAIResponse.getVerbResponse("ListMetadataFormats",arguments,requestURL);
		if(!(doc.getElementsByTagName("error")!=null && doc.getElementsByTagName("error").getLength()>0)){
			String identifier = arguments.get("identifier");
			if(identifier!=null && !identifier.isEmpty()){
				if(arguments!=null && !arguments.isEmpty()){
					try{
						Ese ese = null;
						MetadataFormat metadataFormat = null;
						// The identifier retrieved for a record in the request is a specific one. It is necessary to remove the last sufix to obtain
						// the general identifier for the ESE created from the specific FA which contains the record asked
						if (identifier.contains("-")) {
							identifier = identifier.substring(0, identifier.lastIndexOf("-"));
							ese = DAOFactory.instance().getEseDAO().getEseByIdentifierAndFormat(identifier,null);							
						}

						if(ese!=null){
							metadataFormat = ese.getMetadataFormat();
							Node verbNode = doc.createElementNS(OAIResponse.NAMESPACE, "ListMetadataFormats");
							Node metadataFormatNode = buildMetadataFormatNode(doc,null,metadataFormat);
							verbNode.appendChild(metadataFormatNode);
							doc.getDocumentElement().appendChild(verbNode);
						}else{
							doc = OAIUtils.getResponseError(doc,"idDoesNotExist");
						}
					} catch (Exception e) {
						LOG.error("Error while trying to obtain an ESE with identifier "+identifier+" from DDBB -- "+e.getCause());
					}
				}else{
					doc = OAIUtils.getResponseError(doc,"idDoesNotExist");
				}
			}else{
				EseDAO eseDAO = DAOFactory.instance().getEseDAO();
				Iterator<MetadataFormat> metadataFormatIterator = DAOFactory.instance().getMetadataFormatDAO().findAll().iterator();
				Node verbNode = doc.createElementNS(OAIResponse.NAMESPACE, "ListMetadataFormats");
				boolean changes = false;
				while(metadataFormatIterator.hasNext()){
					MetadataFormat metadataFormat = metadataFormatIterator.next();
					List<Ese> results = eseDAO.getEsesByArguments(null, null,metadataFormat, null, 0, OAIResponse.LIMIT_PER_RESPONSE);
					if(!results.isEmpty()){
						Node metadataFormatNode = buildMetadataFormatNode(doc,null,metadataFormat);
						verbNode.appendChild(metadataFormatNode);
						changes = true;
					}
				}
				if(changes){
					doc.getDocumentElement().appendChild(verbNode);
				}
			}/*else{
				doc = OAIUtils.getResponseError(doc,"badArgument");
			}*/
		}
		return doc;
	}
	
	public static Node buildMetadataFormatNode(Document doc,String object,MetadataFormat metadataFormat) {
		Node metadataFormatNode  = doc.createElementNS(OAIResponse.NAMESPACE, "metadataFormat");
		Node metadataPrefixNode = doc.createElementNS(OAIResponse.NAMESPACE, "metadataPrefix");
		metadataPrefixNode.setTextContent(metadataFormat.getFormat());
		metadataFormatNode.appendChild(metadataPrefixNode);
		Node schemaNode = doc.createElementNS(OAIResponse.NAMESPACE, "schema");
		schemaNode.setTextContent(OAIUtils.ESE_SCHEMA_LOCATION_FILE);
		metadataFormatNode.appendChild(schemaNode);
		Node metadataNamespaceNode = doc.createElementNS(OAIResponse.NAMESPACE, "metadataNamespace");
		metadataNamespaceNode.setTextContent(OAIUtils.ESE_METADATA_NAMESPACE);
		metadataFormatNode.appendChild(metadataNamespaceNode);
		return metadataFormatNode;
	}
}
