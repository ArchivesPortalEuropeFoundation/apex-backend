package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.main.vo.ResumptionTokens;
import eu.apenet.oaiserver.request.RequestProcessor;

public class ListSetsResponse extends AbstractResponse {

	private ResumptionTokens resumptionToken;

	public ListSetsResponse(ResumptionTokens resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException {
		writer.writeStartElement(getVerb());
//		List<String> oaiPmhSets = DAOFactory.instance().getEseDAO().getSets();
//		List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByOaiPmhSets(oaiPmhSets);
//		for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
			writer.writeStartElement("set");
			writer.writeTextElement("setSpec", "exemple1");
			String institutionName = "exemple1Name";
			writer.writeTextElement("setName", institutionName);
			writer.closeElement();
//		}

		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}

	protected String getVerb() {
		return RequestProcessor.VERB_LIST_SETS;
	}

}
