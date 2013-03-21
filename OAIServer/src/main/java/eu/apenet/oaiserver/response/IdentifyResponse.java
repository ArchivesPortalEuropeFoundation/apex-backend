package eu.apenet.oaiserver.response;

import java.util.Date;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.factory.DAOFactory;

public class IdentifyResponse extends AbstractResponse {
	private static final String DC_PREFIX = "dc";
	private static final String REPOSITORY_NAME = "Archives Portal Europe OAI-PMH Repository";
	private static final String ADMIN_EMAIL = "info@apex-project.eu";
	private static final String DELETED_RECORD_MANNER = "transient";
	private static final String GRANULARITY = "YYYY-MM-DDThh:mm:ssZ";
	private static final String PROTOCOL_VERSION = "2.0";
	private static final String COMPRESSION = "gzip";
	private static final String BRANDING_NAMESPACE = "http://www.openarchives.org/OAI/2.0/branding/";
	private static final String RIGHTS_NAMESPACE = "http://www.openarchives.org/OAI/2.0/rights/";
	private static final String OAIDC_NAMESPACE = "http://www.openarchives.org/OAI/2.0/oai_dc/";

	private static final String BRANDING_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/branding.xsd";
	private static final String RIGHTS_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/rightsManifest.xsd";
	private static final String OAIDC_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/oai_dc.xsd";

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException {
		writer.writeStartElement("Identify");
		writer.writeTextElement("repositoryName",REPOSITORY_NAME );
		writer.writeTextElement("baseURL",AbstractResponse.REQUEST_URL );
		writer.writeTextElement("protocolVersion",PROTOCOL_VERSION );
		writer.writeTextElement("adminEmail",ADMIN_EMAIL);
		Date earliestDate = DAOFactory.instance().getEseDAO().getTheEarliestDatestamp();
		if(earliestDate==null){
			earliestDate = new Date();
		}
		writer.writeTextElement("earliestDatestamp",OAIUtils.parseDateToISO8601(earliestDate));
		writer.writeTextElement("deletedRecord",DELETED_RECORD_MANNER);
		writer.writeTextElement("granularity",GRANULARITY);
		writer.writeTextElement("compression",COMPRESSION);
		appendBrandingAndRightInformation(writer, params);
		writer.closeElement();
		
		
	}
	/**
	 * This method has been built to 'solve' petitions proposed in ticket #309.
	 * This method is temporal and only has ticket implementations.
	 * 
	 * @param doc
	 * @return Document
	 */
	protected void appendBrandingAndRightInformation(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException {
		writer.writeStartElement("description");
		writer.writeStartElementWithNewDefaultNamespace("branding", BRANDING_NAMESPACE, BRANDING_SCHEMA_LOCATION_FILE);
		writer.writeStartElement("collectionIcon");
		writer.writeTextElement("url", "PLEASE CREATE DEFINED-SIZED (88 x 31) LOGO AND INSERT URL");
		writer.writeTextElement("link", "http://www.archivesportaleurope.net");
		writer.writeTextElement("title", "Archives Portal Europe");
		writer.writeTextElement("width", "88");
		writer.writeTextElement("height", "31");
		writer.closeElement();
		writer.closeElement();
		writer.closeElement();
		writer.writeStartElement("description");
		writer.writeStartElementWithNewDefaultNamespace("rightsManifest", RIGHTS_NAMESPACE, RIGHTS_SCHEMA_LOCATION_FILE);
		writer.writeAttribute("appliesTo", "http://www.openarchives.org/OAI/2.0/entity#metadata");
		writer.writeStartElement("rights");
		writer.writeStartElement("rightsDefinition");
		writer.writeStartElementNS("oai_dc", DC_PREFIX, OAIDC_NAMESPACE);
		writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, "oai_dc", "", OAIDC_NAMESPACE);
		writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, DC_PREFIX, "", DC_METADATA_NAMESPACE);
		writer.writeAttributeNS(XMLStreamWriterHolder.XSI_PREFIX, XMLStreamWriterHolder.SCHEMA_LOCATION, XMLStreamWriterHolder.XSI_NAMESPACE, OAIDC_NAMESPACE + " "  + OAIDC_SCHEMA_LOCATION_FILE);
		writer.writeTextElementNS(DC_PREFIX, "title", DC_METADATA_NAMESPACE, "Archives Portal Europe OAI-PMH repository metadata rights description");
		writer.writeTextElementNS(DC_PREFIX, "date", DC_METADATA_NAMESPACE, "2011-05-02");
		writer.writeTextElementNS(DC_PREFIX, "creator", DC_METADATA_NAMESPACE, "Archives Portal Europe");
		writer.writeTextElementNS(DC_PREFIX, "description", DC_METADATA_NAMESPACE, "PLEASE WRITE THE RIGHTS DESCRIPTION HERE");
		writer.closeElement();
		writer.closeElement();
		writer.closeElement();		
		writer.closeElement();

	}
}
