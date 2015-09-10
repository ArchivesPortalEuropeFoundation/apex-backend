package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.config.main.vo.ResumptionTokens;
import eu.apenet.oaiserver.config.other.dao.DAOFactory;
import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class ListSetsResponse extends AbstractResponse {

	private ResumptionTokens resumptionToken;

	public ListSetsResponse(ResumptionTokens resumptionToken) {
		this.resumptionToken = resumptionToken;
	}

	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params) throws XMLStreamException, IOException {
		writer.writeStartElement(getVerb());
		List<String> oaiPmhSets = DAOFactory.instance().getEadObjectDAO().getSets();
//		List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByOaiPmhSets(oaiPmhSets);
		for (String oaiPmhSet : oaiPmhSets) {
			writer.writeStartElement("set");
			writer.writeTextElement("setSpec", oaiPmhSet);
			writer.writeTextElement("setName", oaiPmhSet);
			writer.closeElement();
		}

		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}

	protected String getVerb() {
		return RequestProcessor.VERB_LIST_SETS;
	}

}
