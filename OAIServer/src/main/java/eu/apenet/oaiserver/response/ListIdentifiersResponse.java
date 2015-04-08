package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.vo.MetadataObject;
import eu.apenet.oaiserver.config.vo.ResumptionTokens;
import org.apache.log4j.Logger;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListIdentifiersResponse extends AbstractResponse {
    private static final Logger LOGGER = Logger.getLogger(ListIdentifiersResponse.class);
	private List<MetadataObject> metadataObjects;
	private ResumptionTokens resumptionToken;

	public ListIdentifiersResponse(List<MetadataObject> metadataObjects, ResumptionTokens resumptionToken) {
		this.metadataObjects = metadataObjects;
		this.resumptionToken = resumptionToken;
	}
	protected ListIdentifiersResponse(MetadataObject metadataObject) {
		metadataObjects = new ArrayList<MetadataObject>();
		metadataObjects.add(metadataObject);
	}
	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
			throws XMLStreamException, IOException {
		writer.writeStartElement(getVerb());
		for (MetadataObject metadataObject : metadataObjects) {
			writer.writeStartElement("record");
			writer.writeStartElement("header");
			if (MetadataObject.State.REMOVED.equals(metadataObject.getState())) {
				writer.writeAttribute("status", "deleted");
			}
			writer.writeTextElement("identifier", metadataObject.getOaiIdentifier());
			writer.writeTextElement("datestamp", OAIUtils.parseDateToISO8601(metadataObject.getModificationDate()));
			writer.writeTextElement("setSpec", metadataObject.getSet());
			writer.closeElement();
			if (MetadataObject.State.PUBLISHED.equals(metadataObject.getState())) {
				if (resumptionToken == null) {
					LOGGER.info("XML '" + metadataObject.getOaiIdentifier() + "' LAST ITEM");
				} else {
					LOGGER.info("XML '" + metadataObject.getOaiIdentifier() + "'");
				}
				writeMetadataObjectFile(writer, metadataObject);
			}
			writer.closeElement();
		}
		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}

	protected void writeMetadataObjectFile(XMLStreamWriterHolder writer, MetadataObject metadataObject) throws IOException, XMLStreamException {

	}

	protected String getVerb(){
		return RequestProcessor.VERB_LIST_IDENTIFIERS;
	}

}
