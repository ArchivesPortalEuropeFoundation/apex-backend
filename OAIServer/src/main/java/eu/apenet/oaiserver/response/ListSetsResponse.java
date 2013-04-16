package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
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
		appendCountries(writer);
		List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO()
				.getArchivalInstitutionsWithRepositoryCode();
		for (ArchivalInstitution archivalInstitution : archivalInstitutions) {
			writer.writeStartElement("set");
			writer.writeTextElement("setSpec", archivalInstitution.getRepositorycode());
			String institutionName = archivalInstitution.getAiname();
			String countryName = archivalInstitution.getCountry().getCname();
			writer.writeTextElement("setName", institutionName);
			writer.writeStartElement("setDescription");
			writer.writeStartElementNS(OAI_DC_PREFIX, DC_PREFIX, OAIDC_NAMESPACE);
			writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, OAI_DC_PREFIX, "", OAIDC_NAMESPACE);
			writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, DC_PREFIX, "", DC_METADATA_NAMESPACE);
			writer.writeAttributeNS(XMLStreamWriterHolder.XSI_PREFIX, XMLStreamWriterHolder.SCHEMA_LOCATION,
					XMLStreamWriterHolder.XSI_NAMESPACE, OAIDC_NAMESPACE + " " + OAIDC_SCHEMA_LOCATION_FILE);
			writer.writeTextElementNS(DC_PREFIX, "title", DC_METADATA_NAMESPACE, "EDM Records for " + institutionName
					+ ", " + countryName);
			writer.writeTextElementNS(DC_PREFIX, "creator", DC_METADATA_NAMESPACE, ARCHIVES_PORTAL_EUROPE);
			writer.writeTextElementNS(DC_PREFIX, "description", DC_METADATA_NAMESPACE,
					"This set contains the EDM records created via " + ARCHIVES_PORTAL_EUROPE + " by "
							+ institutionName + ", " + countryName);
			writer.writeTextElementNS(DC_PREFIX, "contributor", DC_METADATA_NAMESPACE, institutionName);
			writer.closeElement();
			writer.closeElement();
			writer.closeElement();
		}

		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}

	private static void appendCountries(XMLStreamWriterHolder writer) throws XMLStreamException {
		List<Country> countries = DAOFactory.instance().getCountryDAO().findAll();
		for (Country country : countries) {
			writer.writeStartElement("set");
			writer.writeTextElement("setSpec", country.getIsoname());
			writer.writeTextElement("setName", country.getCname());
			writer.writeStartElement("setDescription");
			writer.writeStartElementNS(OAI_DC_PREFIX, DC_PREFIX, OAIDC_NAMESPACE);
			writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, OAI_DC_PREFIX, "", OAIDC_NAMESPACE);
			writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, DC_PREFIX, "", DC_METADATA_NAMESPACE);
			writer.writeAttributeNS(XMLStreamWriterHolder.XSI_PREFIX, XMLStreamWriterHolder.SCHEMA_LOCATION,
					XMLStreamWriterHolder.XSI_NAMESPACE, OAIDC_NAMESPACE + " " + OAIDC_SCHEMA_LOCATION_FILE);
			writer.writeTextElementNS(DC_PREFIX, "title", DC_METADATA_NAMESPACE,
					"EDM Records for " + country.getCname());
			writer.writeTextElementNS(DC_PREFIX, "creator", DC_METADATA_NAMESPACE, ARCHIVES_PORTAL_EUROPE);
			writer.writeTextElementNS(
					DC_PREFIX,
					"description",
					DC_METADATA_NAMESPACE,
					"This set contains the EDM records created via " + ARCHIVES_PORTAL_EUROPE + " by "
							+ country.getCname());
			writer.writeTextElementNS(DC_PREFIX, "contributor", DC_METADATA_NAMESPACE, country.getCname());
			writer.closeElement();
			writer.closeElement();
			writer.closeElement();
		}
	}

	protected String getVerb() {
		return RequestProcessor.VERB_LIST_SETS;
	}

}
