package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;

import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.vo.ResumptionToken;

public abstract class AbstractResponse {
	public static final String UTF_8 = "utf-8";
	public static final String REQUEST_URL = "url";
	protected static final String DC_PREFIX = "dc";
	protected static final String OAI_DC_PREFIX = "oai_dc";
	protected static final String DC_METADATA_NAMESPACE = "http://purl.org/dc/elements/1.1/";
	protected final static String ESE_SCHEMA_LOCATION_FILE = "http://www.europeana.eu/schemas/ese/ESE-V3.4.xsd";
	protected static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	protected static final String ESE_METADATA_NAMESPACE = "http://www.europeana.eu/schemas/ese/";
	protected static final String DCTERMS_SCHEMA_LOCATION = "http://purl.org/dc/terms/";
	protected static final String EDM_SCHEMA_LOCATION = "http://www.europeana.eu/schemas/edm/EDM.xsd";
	protected static final String EDM_NAMESPACE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String OAIDC_NAMESPACE = "http://www.openarchives.org/OAI/2.0/oai_dc/";
	protected static final String OAIDC_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/oai_dc.xsd";
	protected static final String ARCHIVES_PORTAL_EUROPE = "Archives Portal Europe";
	
	public void generateResponse(XMLStreamWriterHolder writerHolder, Map<String, String> params)
			throws XMLStreamException, IOException {
		writerHolder.writeOaiPmh();
		writerHolder.writeTextElement("responseDate", OAIUtils.parseDateToISO8601(new Date()));
		String verb = params.get(OAIUtils.VERB);
		if (StringUtils.isBlank(verb)) {
			writerHolder.writeTextElement("request", params.get(REQUEST_URL));
		} else {
			writerHolder.writeTextElementWithAttribute("request", params.get(REQUEST_URL), OAIUtils.VERB, verb);
		}
		generateResponseInternal(writerHolder, params);
		writerHolder.close();
	}

	protected abstract void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
			throws XMLStreamException, IOException;

	protected void writeResumptionToken(XMLStreamWriterHolder writerHolder, ResumptionToken resumptionToken)
			throws XMLStreamException {
		if (resumptionToken != null) {
			writerHolder.writeStartElement("resumptionToken");
			writerHolder.writeAttribute("expirationDate",
					OAIUtils.parseDateToISO8601(resumptionToken.getExpirationDate()));
			writerHolder.writeCharacters(resumptionToken.getRtId() + "");
			writerHolder.closeElement();
		}
	}
}
