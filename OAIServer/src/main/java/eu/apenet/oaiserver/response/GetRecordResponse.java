package eu.apenet.oaiserver.response;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.vo.Ese;

public class GetRecordResponse extends ListRecordsResponse {

    public GetRecordResponse(Ese ese) {
        super(ese);
    }

    @Override
    protected String getVerb() {
        return RequestProcessor.VERB_GET_RECORD;
    }
}
