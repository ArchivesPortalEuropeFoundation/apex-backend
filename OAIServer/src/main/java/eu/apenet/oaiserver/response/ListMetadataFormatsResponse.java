package eu.apenet.oaiserver.response;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.main.MetadataFormats;

public class ListMetadataFormatsResponse extends AbstractResponse {

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
			throws XMLStreamException {
		writer.writeStartElement("ListMetadataFormats");
        for(MetadataFormats metadataFormats : MetadataFormats.values()) {
            writer.writeStartElement("metadataFormat");
            writer.writeTextElement("metadataPrefix", metadataFormats.getName());
            writer.writeTextElement("schema", metadataFormats.getSchemaLocation());
            writer.writeTextElement("metadataNamespace", metadataFormats.getNamespace());
            writer.closeElement();
        }
		writer.closeElement();

	}
}
