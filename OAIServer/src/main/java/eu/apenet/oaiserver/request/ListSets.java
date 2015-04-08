package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.response.ListSetsResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;

public class ListSets {
	public static boolean execute(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException {
		new ListSetsResponse(null).generateResponse(writer, params);
		return true;
	}
}
