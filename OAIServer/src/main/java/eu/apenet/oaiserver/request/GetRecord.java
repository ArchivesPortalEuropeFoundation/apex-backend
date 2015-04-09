package eu.apenet.oaiserver.request;

import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.MetadataFormats;
import eu.apenet.oaiserver.config.dao.MetadataObjectDAOFront;
import eu.apenet.oaiserver.config.ape.dao.impl.MetadataObjectDAOFrontImpl;
import eu.apenet.oaiserver.config.vo.MetadataObject;
import eu.apenet.oaiserver.response.ErrorResponse;
import eu.apenet.oaiserver.response.GetRecordResponse;
import eu.apenet.oaiserver.response.XMLStreamWriterHolder;

public class GetRecord {
	public static boolean execute(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException {
        MetadataObjectDAOFront metadataObjectDAOFront = new MetadataObjectDAOFrontImpl();
		String identifier = params.get("identifier");
		MetadataFormats metadataFormats = MetadataFormats.getMetadataFormats(params.get(RequestProcessor.METADATA_PREFIX));
        MetadataObject metadataObject = metadataObjectDAOFront.getMetadataObject(identifier, metadataFormats);
		if (metadataObject == null){
			new ErrorResponse(ErrorResponse.ErrorCode.ID_DOES_NOT_EXIST).generateResponse(writer, params);
			return false;
		}else{
			new GetRecordResponse(metadataObject).generateResponse(writer, params);
		}
		return true;
	}

}
