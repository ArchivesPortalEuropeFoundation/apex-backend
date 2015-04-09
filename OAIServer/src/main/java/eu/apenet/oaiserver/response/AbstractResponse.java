package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.main.vo.ResumptionTokens;
import org.apache.commons.lang.StringUtils;

import eu.apenet.oaiserver.util.OAIUtils;

public abstract class AbstractResponse {
	public static final String UTF_8 = "utf-8";
	public static final String REQUEST_URL = "url";
	protected static final String ARCHIVES_PORTAL_EUROPE = "Archives Portal Europe";
	
	public void generateResponse(XMLStreamWriterHolder writerHolder, Map<String, String> params) throws XMLStreamException, IOException {
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

	protected abstract void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException;

	protected void writeResumptionToken(XMLStreamWriterHolder writerHolder, ResumptionTokens resumptionToken) throws XMLStreamException {
		if (resumptionToken != null) {
			writerHolder.writeStartElement("resumptionToken");
			writerHolder.writeAttribute("expirationDate", OAIUtils.parseDateToISO8601(resumptionToken.getExpirationDate()));
			writerHolder.writeCharacters(resumptionToken.getId() + "");
			writerHolder.closeElement();
		}
	}
}
