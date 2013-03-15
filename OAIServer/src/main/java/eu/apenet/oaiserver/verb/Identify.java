package eu.apenet.oaiserver.verb;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import eu.apenet.oaiserver.util.OAIResponse;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;

public class Identify extends OAIVerb{
	private static final String REPOSITORY_NAME = "Archives Portal Europe OAI-PMH Repository";
	private static final String ADMIN_EMAIL = "info@apex-project.eu";
	private static final String DELETED_RECORD_MANNER = "transient";
	private static final String GRANULARITY = "YYYY-MM-DDThh:mm:ssZ";
	private static final String PROTOCOL_VERSION = "2.0";
	private static final String COMPRESSION = "gzip";
	
	private InputStream inputStream;
	private Logger LOG = Logger.getLogger(Identify.class);
	
	public InputStream getInputStream(){
		return this.inputStream;
	}
	
	public boolean execute(){
		try {
			this.inputStream = OAIResponse.getVerbInfo("Identify",new HashMap<String, String>(),this.getUrl());
			if(this.inputStream!=null){
				return true;
			}
			Document emptyDocument = OAIResponse.getVerbResponse("Identify",new HashMap<String, String>(),this.getUrl());
			this.inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(emptyDocument,"badArgument"));
		} catch (Exception e) {
			LOG.error("Params are NOT right in URL: ", e.getCause());
		}
		return false;
	}
	
	/**
	 * This method returns a document with all the information which has to be offer by
	 * Identify verb in www.archivesportaleurope.eu OAI-PMH.
	 * 
	 * @param requestURL
	 * @return Document
	 * @throws Exception
	 */
	public static Document getIdentifyLogic(String requestURL) throws Exception {
		Document doc = OAIResponse.getVerbResponse("Identify",null,requestURL);
		Element identifyNode = doc.createElementNS(OAIResponse.NAMESPACE, "Identify");
		Element repositoryNameNode = doc.createElementNS(OAIResponse.NAMESPACE, "repositoryName");
		repositoryNameNode.setTextContent(Identify.REPOSITORY_NAME);
		identifyNode.appendChild(repositoryNameNode);
		Element baseUrlNode = doc.createElementNS(OAIResponse.NAMESPACE, "baseURL");
		baseUrlNode.setTextContent(requestURL);
		identifyNode.appendChild(baseUrlNode);
		Element protocolVersionNode = doc.createElementNS(OAIResponse.NAMESPACE, "protocolVersion");
		protocolVersionNode.setTextContent(Identify.PROTOCOL_VERSION);
		identifyNode.appendChild(protocolVersionNode);
		Node adminEmailNode = doc.createElementNS(OAIResponse.NAMESPACE, "adminEmail");
		adminEmailNode.setTextContent(Identify.ADMIN_EMAIL);
		identifyNode.appendChild(adminEmailNode);
		Element earliestDatestampNode = doc.createElementNS(OAIResponse.NAMESPACE, "earliestDatestamp");
		Date earliestDate = DAOFactory.instance().getEseDAO().getTheEarliestDatestamp();
		if(earliestDate==null){
			earliestDate = new Date();
		}
		earliestDatestampNode.setTextContent(OAIUtils.parseDateToISO8601(earliestDate));
		identifyNode.appendChild(earliestDatestampNode);
		Element deletedRecordNode = doc.createElementNS(OAIResponse.NAMESPACE, "deletedRecord");
		deletedRecordNode.setTextContent(Identify.DELETED_RECORD_MANNER);
		identifyNode.appendChild(deletedRecordNode);
		Node granularityNode = doc.createElementNS(OAIResponse.NAMESPACE, "granularity");
		granularityNode.setTextContent(Identify.GRANULARITY);
		identifyNode.appendChild(granularityNode);
		Node compressionNode = doc.createElementNS(OAIResponse.NAMESPACE, "compression");
		compressionNode.setTextContent(Identify.COMPRESSION);
		identifyNode.appendChild(compressionNode);
		doc.getElementsByTagName("OAI-PMH").item(0).appendChild(identifyNode);
		return appendBrandingAndRightInformation(doc);
	}
	/**
	 * This method has been built to 'solve' petitions proposed in ticket #309.
	 * This method is temporal and only has ticket implementations.
	 * 
	 * @param doc
	 * @return Document
	 */
	public static Document appendBrandingAndRightInformation(Document doc){
		
		//Branding part
		Node descriptionBranding = doc.createElementNS(OAIResponse.NAMESPACE, "description");
		Element brandings = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION, "branding");
		brandings.setAttribute("xmlns:xsi",OAIUtils.XMLNS_XSI);
		brandings.setAttribute("xsi:schemaLocation",OAIUtils.BRANDING_SCHEMA_LOCATION + " " + OAIUtils.BRANDING_SCHEMA_LOCATION_FILE);
		Node collectionIcon = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION , "collectionIcon");
		Node url = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION, "url");
		url.setTextContent("PLEASE CREATE DEFINED-SIZED (88 x 31) LOGO AND INSERT URL");
		collectionIcon.appendChild(url);
		Node link = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION, "link");
		link.setTextContent("http://www.archivesportaleurope.net");
		collectionIcon.appendChild(link);
		Node title = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION, "title");
		title.setTextContent("Archives Portal Europe");
		collectionIcon.appendChild(title);
		Node width = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION, "width");
		width.setTextContent("88");
		collectionIcon.appendChild(width);
		Node height = doc.createElementNS(OAIUtils.BRANDING_SCHEMA_LOCATION, "height");
		height.setTextContent("31");
		collectionIcon.appendChild(height);
		brandings.appendChild(collectionIcon);
		descriptionBranding.appendChild(brandings);

		//Rights information part
		Node descriptionRights = doc.createElementNS(OAIResponse.NAMESPACE, "description");
		Element rightsManifest = doc.createElementNS(OAIUtils.RIGHTS_SCHEMA_LOCATION, "rightsManifest");
		rightsManifest.setAttribute("xmlns:xsi",OAIUtils.XMLNS_XSI);
		rightsManifest.setAttribute("xsi:schemaLocation", OAIUtils.RIGHTS_SCHEMA_LOCATION + " " + OAIUtils.RIGHTS_SCHEMA_LOCATION_FILE);
		rightsManifest.setAttribute("appliesTo",OAIResponse.NAMESPACE+"entity#metadata");
		Node rights = doc.createElementNS(OAIUtils.RIGHTS_SCHEMA_LOCATION, "rights");
		Node rightsDefinition = doc.createElementNS(OAIUtils.RIGHTS_SCHEMA_LOCATION, "rightsDefinition");
		Element oaiDcDc = doc.createElementNS(OAIUtils.OAIDC_SCHEMA_LOCATION,"oai_dc:dc");
		oaiDcDc.setAttribute("xmlns:dc",OAIUtils.DC_METADATA_NAMESPACE);
		oaiDcDc.setAttribute("xmlns:xsi",OAIUtils.XMLNS_XSI);
		oaiDcDc.setAttribute("xsi:schemaLocation",OAIUtils.OAIDC_SCHEMA_LOCATION + " " + OAIUtils.OAIDC_SCHEMA_LOCATION_FILE);
		Node dcTitle = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:title");
		dcTitle.setTextContent("Archives Portal Europe OAI-PMH repository metadata rights description");
		oaiDcDc.appendChild(dcTitle);
		Node dcDate = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:date");
		dcDate.setTextContent("2011-05-02");
		oaiDcDc.appendChild(dcDate);
		Node dcCreator = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:creator");
		dcCreator.setTextContent("Archives Portal Europe");
		oaiDcDc.appendChild(dcCreator);
		Node dcDescription = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, "dc:description");
		dcDescription.setTextContent("PLEASE WRITE THE RIGHTS DESCRIPTION HERE");
		oaiDcDc.appendChild(dcDescription);
		rightsDefinition.appendChild(oaiDcDc);
		rights.appendChild(rightsDefinition);
		rightsManifest.appendChild(rights);
		descriptionRights.appendChild(rightsManifest);
		
		//Now append to doc branding and rights information
		doc.getElementsByTagName("Identify").item(0).appendChild(descriptionBranding);
		doc.getElementsByTagName("Identify").item(0).appendChild(descriptionRights);
		return doc;
	}
}