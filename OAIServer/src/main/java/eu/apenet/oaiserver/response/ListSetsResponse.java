package eu.apenet.oaiserver.response;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.stream.XMLStreamException;

import eu.apenet.oaiserver.request.RequestProcessor;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.Country;
import eu.apenet.persistence.vo.ResumptionToken;

public class ListSetsResponse extends AbstractResponse {


	private static final String SEPARATOR = "-";
	private List<String> eSets;
	private ResumptionToken resumptionToken;

	public ListSetsResponse(List<String> eSets, ResumptionToken resumptionToken) {
		this.eSets = eSets;
		this.resumptionToken = resumptionToken;
	}
	@Override
	protected void generateResponseInternal(XMLStreamWriterHolder writer, Map<String, String> params)
			throws XMLStreamException, IOException {
		writer.writeStartElement(getVerb());
		Set<String> writedSets = new HashSet<String>();
		for (String eSet: eSets){
			writer.writeStartElement("set");
			writer.writeTextElement("setSpec", eSet);
			String institutionName = null;
			String countryName = null;
			String couIsoname = null;
			if(!eSet.isEmpty() && eSet.contains(SEPARATOR)){
				ArchivalInstitution example = new ArchivalInstitution();
				example.setRepositorycode(eSet);
				List<ArchivalInstitution> archivalInstitutions = DAOFactory.instance().getArchivalInstitutionDAO().findByExample(example);
				if (archivalInstitutions.size() > 0) {
					institutionName = archivalInstitutions.get(0).getAiname();
				}
				couIsoname = eSet.substring(0,eSet.indexOf(SEPARATOR));
				Country country = DAOFactory.instance().getCountryDAO().getCountries(couIsoname).get(0); 
				if (country != null) {
					countryName = country.getCname();
				}
				writer.writeTextElement("setName", institutionName);
			}
			if(institutionName!=null && countryName!=null){
				writer.writeStartElement("setDescription");
				writer.writeStartElementNS(OAI_DC_PREFIX, DC_PREFIX, OAIDC_NAMESPACE);
				writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, OAI_DC_PREFIX, "", OAIDC_NAMESPACE);
				writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, DC_PREFIX, "", DC_METADATA_NAMESPACE);
				writer.writeAttributeNS(XMLStreamWriterHolder.XSI_PREFIX, XMLStreamWriterHolder.SCHEMA_LOCATION, XMLStreamWriterHolder.XSI_NAMESPACE, OAIDC_NAMESPACE + " "  + OAIDC_SCHEMA_LOCATION_FILE);
				writer.writeTextElementNS(DC_PREFIX, "title", DC_METADATA_NAMESPACE, "EDM Records for "+institutionName+", "+countryName);
				writer.writeTextElementNS(DC_PREFIX, "creator", DC_METADATA_NAMESPACE, ARCHIVES_PORTAL_EUROPE);
				writer.writeTextElementNS(DC_PREFIX, "description", DC_METADATA_NAMESPACE, "This set contains the EDM records created via "+ARCHIVES_PORTAL_EUROPE+" by "+institutionName+", "+countryName);
				writer.writeTextElementNS(DC_PREFIX, "contributor", DC_METADATA_NAMESPACE, institutionName);
				writer.closeElement();
				writer.closeElement();
			}
			writer.closeElement();
			writedSets.add(eSet);
		}
		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}



	protected String getVerb(){
		return RequestProcessor.VERB_LIST_SETS;
	}

}
