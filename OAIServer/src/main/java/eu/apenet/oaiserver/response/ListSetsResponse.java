package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListSetsResponse extends AbstractResponse {

    private ResumptionToken resumptionToken;

    public ListSetsResponse(ResumptionToken resumptionToken) {
        this.resumptionToken = resumptionToken;
    }

    @Override
    protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
            throws XMLStreamException, IOException {
        writer.writeStartElement(getVerb());
        List<String> oaiPmhSets = DAOFactory.instance().getEseDAO().getSets();
        for (String oaiPmhSet : oaiPmhSets) {
            String institutionName = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionByRepositoryCode(oaiPmhSet.substring(0, oaiPmhSet.indexOf(":"))).getAiname();
            String setName = oaiPmhSet.substring(oaiPmhSet.indexOf(":") + 1);

            writer.writeStartElement("set");
            writer.writeTextElement("setSpec", oaiPmhSet);
            writer.writeTextElement("setName", institutionName + " – " + setName);
            writer.closeElement();
        }

        writeResumptionToken(writer, resumptionToken);
        writer.closeElement();
    }

    protected String getVerb() {
        return RequestProcessor.VERB_LIST_SETS;
    }

}
