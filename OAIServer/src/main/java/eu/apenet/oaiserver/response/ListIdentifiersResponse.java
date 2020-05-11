package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListIdentifiersResponse extends AbstractResponse {

    private static final Logger LOGGER = Logger.getLogger(ListIdentifiersResponse.class);
    private List<Ese> eses;
    private ResumptionToken resumptionToken;

    public ListIdentifiersResponse(List<Ese> eses, ResumptionToken resumptionToken) {
        this.eses = eses;
        this.resumptionToken = resumptionToken;
    }

    protected ListIdentifiersResponse(Ese ese) {
        eses = new ArrayList<>();
        eses.add(ese);
    }

    @Override
    protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
            throws XMLStreamException, IOException {
        writer.writeStartElement(getVerb());
        for (Ese ese : eses) {
            writer.writeStartElement("record");
            writer.writeStartElement("header");
            if (EseState.REMOVED.equalsIgnoreCase(ese.getEseState().getState())) {
                writer.writeAttribute("status", "deleted");
            }
            writer.writeTextElement("identifier", ese.getOaiIdentifier());
            writer.writeTextElement("datestamp", OAIUtils.parseDateToISO8601(ese.getModificationDate()));
            writer.writeTextElement("setSpec", ese.getEset());
            writer.closeElement();
            if (EseState.PUBLISHED.equalsIgnoreCase(ese.getEseState().getState())) {
                if (resumptionToken == null) {
                    LOGGER.info("ESE '" + ese.getOaiIdentifier() + "' LAST ITEM");
                } else {
                    LOGGER.info("ESE '" + ese.getOaiIdentifier() + "'");
                }
                writeEseFile(writer, ese);
            }
            writer.closeElement();
        }
        writeResumptionToken(writer, resumptionToken);
        writer.closeElement();
    }

    protected void writeEseFile(XMLStreamWriterHolder writer, Ese ese) throws IOException, XMLStreamException {

    }

    protected String getVerb() {
        return RequestProcessor.VERB_LIST_IDENTIFIERS;
    }

}
