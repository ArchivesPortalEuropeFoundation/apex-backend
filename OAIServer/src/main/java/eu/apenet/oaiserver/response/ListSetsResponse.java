package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
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
		List<String> oaiPmhSets = DAOFactory.instance().getEseDAO().getSetsWithPublicatedFiles();
		List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().getArchivalInstitutionsByOaiPmhSets(oaiPmhSets);
		for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
			writer.writeStartElement("set");
			writer.writeTextElement("setSpec", archivalInstitution.getRepositorycode());
			String institutionName = archivalInstitution.getAiname();
			writer.writeTextElement("setName", institutionName);
			writer.closeElement();
		}

		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}

	protected String getVerb() {
		return RequestProcessor.VERB_LIST_SETS;
	}

}
