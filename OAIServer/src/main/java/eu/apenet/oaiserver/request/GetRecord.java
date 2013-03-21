package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.response.ErrorResponse;
import eu.apenet.oaiserver.response.GetRecordResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.MetadataFormat;

public class GetRecord {


	public static boolean execute(XMLStreamWriterHolder writer, Map<String, String> params)
			throws XMLStreamException, IOException {
		String identifier = params.get("identifier");
		MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(params.get("metadataPrefix"));
		Ese ese = DAOFactory.instance().getEseDAO().getEseByIdentifierAndFormat(identifier,metadataFormat);
		if (ese == null){
			new ErrorResponse(ErrorResponse.ErrorCode.ID_DOES_NOT_EXIST).generateResponse(writer, params);
			return false;
		}else{
			new GetRecordResponse(ese).generateResponse(writer, params);
		}
		return true;
	}

}
