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
			Integer aiId = null;
			String countryName = null;
			String couIsoname = null;
			if(!eSet.isEmpty() && eSet.contains(":")){
				aiId = Integer.valueOf(eSet.substring(eSet.lastIndexOf(":")+1));
				ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(aiId);
				if (archivalInstitution != null) {
					institutionName = archivalInstitution.getAiname();
				}
				couIsoname = eSet.substring(0,eSet.indexOf(":"));
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
			appendVirtualSets(writer,eSet,writedSets);
		}
		writeResumptionToken(writer, resumptionToken);
		writer.closeElement();
	}
	/**
	 * This method is a very temporal solution to apply OAI recommendations,
	 * related to ticket #309. In a near future it should be changed. First look forward
	 * if virtual target set has been appended, next apply this target set to the document. 
	 * 
	 * @param doc
	 * @param eset
	 * @return Document
	 * @throws XMLStreamException 
	 */
	private static void appendVirtualSets(XMLStreamWriterHolder writer, String eset, Set<String> writedSets) throws XMLStreamException {
		String temporalHierarchy = eset.substring(0,eset.lastIndexOf(":")); //Remove the lastest institution
		String[] hierarchy = temporalHierarchy.split(":");
		String hierarchyName = null;
		for(int i=0;i<hierarchy.length;i++){
			String target = hierarchy[i];
			if (i==0) {
				// The node is a country
				Country country = DAOFactory.instance().getCountryDAO().getCountries(target).get(0);
				if (country != null) {
					hierarchyName = country.getCname();
				}
			}
			else {
				// The node is an archival institution
				ArchivalInstitution archivalInstitution = DAOFactory.instance().getArchivalInstitutionDAO().findById(Integer.valueOf(target)); 
				hierarchyName = archivalInstitution.getAiname();
			}
			if(!writedSets.contains(hierarchyName)){ //build a virtual set
				writer.writeStartElement("set");
				
				String targetName = eset.substring(0,eset.indexOf(target));
				if(targetName.length()>1){
					targetName+=target;
				}else{
					targetName = target;
				}
				writer.writeTextElement("setSpec", targetName);
				writer.writeTextElement("setName", hierarchyName);
				writer.writeStartElement("setDescription");
				writer.writeStartElementNS(OAI_DC_PREFIX, DC_PREFIX, OAIDC_NAMESPACE);
				writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, OAI_DC_PREFIX, "", OAIDC_NAMESPACE);
				writer.writeAttributeNS(XMLStreamWriterHolder.XMLNS, DC_PREFIX, "", DC_METADATA_NAMESPACE);
				writer.writeAttributeNS(XMLStreamWriterHolder.XSI_PREFIX, XMLStreamWriterHolder.SCHEMA_LOCATION, XMLStreamWriterHolder.XSI_NAMESPACE, OAIDC_NAMESPACE + " "  + OAIDC_SCHEMA_LOCATION_FILE);
				writer.writeTextElementNS(DC_PREFIX, "title", DC_METADATA_NAMESPACE, "EDM Records for "+hierarchyName);
				writer.writeTextElementNS(DC_PREFIX, "creator", DC_METADATA_NAMESPACE, ARCHIVES_PORTAL_EUROPE);
				writer.writeTextElementNS(DC_PREFIX, "description", DC_METADATA_NAMESPACE, "This set contains the EDM records created via "+ARCHIVES_PORTAL_EUROPE+" by " + hierarchyName);
				writer.writeTextElementNS(DC_PREFIX, "contributor", DC_METADATA_NAMESPACE, hierarchyName);
				writer.closeElement();
				writer.closeElement();
				writer.closeElement();
			}
		}
	}


	protected String getVerb(){
		return RequestProcessor.VERB_LIST_SETS;
	}

}
