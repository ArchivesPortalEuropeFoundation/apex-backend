package eu.apenet.oaiserver.response;

import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.persistence.vo.MetadataFormat;

public class ListMetadataFormatsResponse extends AbstractResponse {

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
			throws XMLStreamException {
		writer.writeStartElement("ListMetadataFormats");
		writer.writeStartElement("metadataFormat");
		writer.writeTextElement("metadataPrefix", MetadataFormat.EDM.toString());
		writer.writeTextElement("schema", EDM_SCHEMA_LOCATION);
		writer.writeTextElement("metadataNamespace", EDM_NAMESPACE);
		writer.closeElement();
		writer.closeElement();

	}
}
