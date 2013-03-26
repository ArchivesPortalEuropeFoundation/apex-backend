package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.response.ErrorResponse;
import eu.apenet.oaiserver.response.ListSetsResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;
import eu.apenet.persistence.factory.DAOFactory;

public class ListSets {


	public static boolean execute(XMLStreamWriterHolder writer, Map<String, String> params)
		throws XMLStreamException, IOException {
		List<String> eSets = DAOFactory.instance().getEseDAO().getSetsOfEses(null, null, null, null, null);
		if (eSets.size() == 0){
			new ErrorResponse(ErrorResponse.ErrorCode.NO_SET_HIERARCHY).generateResponse(writer, params);
		}else {
			new ListSetsResponse(eSets, null).generateResponse(writer, params);
		}
		return true;

	}

}
