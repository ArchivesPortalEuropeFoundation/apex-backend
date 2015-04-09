package eu.apenet.oaiserver.response;

import eu.apenet.oaiserver.config.main.vo.MetadataObject;
import eu.apenet.oaiserver.request.RequestProcessor;

public class GetRecordResponse extends ListRecordsResponse {


	public GetRecordResponse(MetadataObject metadataObject) {
		super(metadataObject);
	}


	protected String getVerb(){
		return RequestProcessor.VERB_GET_RECORD;
	}
}
