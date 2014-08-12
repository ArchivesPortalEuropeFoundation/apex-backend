package eu.apenet.dashboard.services.eaccpf.xml.stream;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import eu.apenet.commons.solr.SolrValues;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfPublishData;
import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.AttributeXpathHandler;
import eu.archivesportaleurope.xml.xpath.CountXpathHandler;
import eu.archivesportaleurope.xml.xpath.NestedXpathHandler;
import eu.archivesportaleurope.xml.xpath.StringXpathHandler;
import eu.archivesportaleurope.xml.xpath.TextMapXpathHandler;
import eu.archivesportaleurope.xml.xpath.TextXpathHandler;
import eu.archivesportaleurope.xml.xpath.XmlStreamHandler;

public class EacCpfPublishDataFiller {
	private static final String DATE_UNKNOWN_START = "unknownStart";
	private static final String DATE_UNKNOWN_END = "unknownEnd";
	private static final String DATE_UNKNOWN = "unknown";
	/*
	 * unitids
	 */
	private AttributeXpathHandler languageHandler;
	private TextXpathHandler entityTypeHandler;
	private TextXpathHandler entityIdHandler;

	private TextMapXpathHandler namesHandler;
	private TextMapXpathHandler namesParallelHandler;
	private NestedXpathHandler identityHandler;
	/*
	 * dates
	 */
	private TextXpathHandler fromDateHandler;
	private TextXpathHandler toDateHandler;
	private TextXpathHandler oneDateHandler;
	private AttributeXpathHandler oneDateNormalHandler;
	private AttributeXpathHandler fromDateNormalHandler;
	private AttributeXpathHandler toDateNormalHandler;
	private AttributeXpathHandler dateRangeLocalTypeHandler;
	private AttributeXpathHandler oneDateLocalTypeHandler;
	/*
	 * description
	 */
	private TextXpathHandler placesHandler;
	private TextXpathHandler functionsHandler;
	private TextXpathHandler mandatesHandler;
	private TextXpathHandler occupationsHandler;
	private TextXpathHandler bioghistHandler;
	private TextXpathHandler generalContextHandler;
	private TextXpathHandler structureOrGenealogyHandler;
	private TextXpathHandler legalStatusHandler;
	private TextXpathHandler occupationPlaceEntryHandler;
	private TextXpathHandler mandatePlaceEntryHandler;
	private TextXpathHandler localDescriptionHandler;

	private TextXpathHandler placeRoleHandler;
	private TextXpathHandler placeAddressLineHandler;
	
	private TextXpathHandler descriptionDescriptiveNoteHandler;
	private NestedXpathHandler descriptionHandler;
	/*
	 * relations
	 */
	private NestedXpathHandler relationsHandler;

	private CountXpathHandler countArchivalMaterialRelationsHandler;
	private CountXpathHandler countNameRelationsHandler;
	private TextXpathHandler institutionsRelationsHandler;
	
	private TextXpathHandler relationsPlaceEntryHandler;
	private TextXpathHandler relationsRelationEntryHandler;
	private TextXpathHandler relationsDescriptiveNoteHandler;

	/*
	 * alternativeSet
	 */
	private NestedXpathHandler alternativeSetHandler;	
	private TextXpathHandler alternativeSetDescriptiveNoteHandler;	
	private TextXpathHandler alternativeSetComponentEntryHandler;	

	private List<XmlStreamHandler> eacCpfHandlers = new ArrayList<XmlStreamHandler>();

	public EacCpfPublishDataFiller() {
		/*
		 * name
		 */
		languageHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "eac-cpf",
				"control", "languageDeclaration", "language" }, "languageCode");
		identityHandler  = new NestedXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"eac-cpf",
				"cpfDescription", "identity"});
		entityTypeHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "entityType" });
		entityIdHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "entityId" });
		
		namesHandler = new TextMapXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "nameEntry" });
		namesHandler.setAttributeValueAsKey("localType");
		namesParallelHandler = new TextMapXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {  "nameEntryParallel",  "nameEntry" });
		
		placesHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "places", "place", "placeEntry" });
		occupationsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "occupations", "occupation", "term" });
		mandatesHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "mandates", "mandate", "term" });
		functionsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "functions", "function", "term" });
		/*
		 * dates
		 */
		fromDateHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "dateRange", "fromDate" });
		toDateHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "dateRange", "toDate" });
		oneDateHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "date" });

		fromDateNormalHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "dateRange", "fromDate" }, "standardDate");
		toDateNormalHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "dateRange", "toDate" }, "standardDate");
		oneDateNormalHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "date" }, "standardDate");
		
		dateRangeLocalTypeHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "dateRange" }, "localType");
		oneDateLocalTypeHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "existDates", "date" }, "localType");

		/*
		 * description
		 */
		bioghistHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "biogHist" }, true);
		bioghistHandler.setAllTextBelow(true);
		generalContextHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "generalContext" }, true);
		generalContextHandler.setAllTextBelow(true);
		structureOrGenealogyHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "structureOrGenealogy" }, true);
		structureOrGenealogyHandler.setAllTextBelow(true);
		
		legalStatusHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "legalStatuses", "legalStatus", "term | placeEntry" });
		occupationPlaceEntryHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "occupations", "occupation", "placeEntry" });
		mandatePlaceEntryHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"mandates", "mandate", "placeEntry" });
		localDescriptionHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {  "localDescriptions", "localDescription", "term | placeEntry" });
		placeRoleHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "places", "place", "placeRole" });
		placeAddressLineHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"places", "place", "address", "addressline"});

		descriptionDescriptiveNoteHandler   = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"descriptiveNote", "p"}, true);
		descriptionDescriptiveNoteHandler.setAllTextBelow(true);
		descriptionDescriptiveNoteHandler.setRelative(true);
		descriptionHandler  = new NestedXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"eac-cpf",
				"cpfDescription", "description"});
		relationsHandler  = new NestedXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"eac-cpf",
				"cpfDescription", "relations"});
		countArchivalMaterialRelationsHandler = new CountXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"resourceRelation"});
		countNameRelationsHandler = new CountXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"cpfRelation"});
		countNameRelationsHandler.setAttribute("cpfRelationType", "identity", true);
		institutionsRelationsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"resourceRelation", "relationEntry"});
		institutionsRelationsHandler.setAttribute("localType", "agencyCode", false);
		relationsDescriptiveNoteHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"descriptiveNote", "p"}, true);
		relationsDescriptiveNoteHandler.setAllTextBelow(true);
		relationsDescriptiveNoteHandler.setRelative(true);
		
		relationsPlaceEntryHandler   = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"placeEntry"});
		relationsPlaceEntryHandler.setRelative(true);
		
		relationsRelationEntryHandler   = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"relationEntry"});
		relationsRelationEntryHandler.setRelative(true);

		alternativeSetHandler  = new NestedXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"eac-cpf",
				"cpfDescription", "alternativeSet"});
		alternativeSetDescriptiveNoteHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"descriptiveNote", "p"}, true);
		alternativeSetDescriptiveNoteHandler.setAllTextBelow(true);
		alternativeSetDescriptiveNoteHandler.setRelative(true);
		
		alternativeSetComponentEntryHandler   = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"setComponent", "componentEntry"});
		

		eacCpfHandlers.add(languageHandler);
		identityHandler.getHandlers().add(entityTypeHandler);
		identityHandler.getHandlers().add(entityIdHandler);
		identityHandler.getHandlers().add(namesHandler);
		identityHandler.getHandlers().add(namesParallelHandler);
		/*
		 * dates
		 */
		/*
		 * dates
		 */
		descriptionHandler.getHandlers().add(fromDateHandler);
		descriptionHandler.getHandlers().add(toDateHandler);
		descriptionHandler.getHandlers().add(oneDateHandler);
		descriptionHandler.getHandlers().add(fromDateNormalHandler);
		descriptionHandler.getHandlers().add(toDateNormalHandler);
		descriptionHandler.getHandlers().add(oneDateNormalHandler);
		descriptionHandler.getHandlers().add(dateRangeLocalTypeHandler);
		descriptionHandler.getHandlers().add(oneDateLocalTypeHandler);		
		
		descriptionHandler.getHandlers().add(placesHandler);
		descriptionHandler.getHandlers().add(occupationsHandler);

		descriptionHandler.getHandlers().add(mandatesHandler);
		descriptionHandler.getHandlers().add(functionsHandler);
		
		descriptionHandler.getHandlers().add(bioghistHandler);
		descriptionHandler.getHandlers().add(generalContextHandler);
		descriptionHandler.getHandlers().add(structureOrGenealogyHandler);
		
		descriptionHandler.getHandlers().add(legalStatusHandler);
		descriptionHandler.getHandlers().add(occupationPlaceEntryHandler);
		descriptionHandler.getHandlers().add(mandatePlaceEntryHandler);
		descriptionHandler.getHandlers().add(localDescriptionHandler);
		descriptionHandler.getHandlers().add(placeRoleHandler);
		descriptionHandler.getHandlers().add(placeAddressLineHandler);
		descriptionHandler.getHandlers().add(descriptionDescriptiveNoteHandler);
		
		relationsHandler.getHandlers().add(countArchivalMaterialRelationsHandler);
		relationsHandler.getHandlers().add(countNameRelationsHandler);
		relationsHandler.getHandlers().add(institutionsRelationsHandler);
		relationsHandler.getHandlers().add(relationsDescriptiveNoteHandler);
		relationsHandler.getHandlers().add(relationsPlaceEntryHandler);
		relationsHandler.getHandlers().add(relationsRelationEntryHandler);
		
		alternativeSetHandler.getHandlers().add(alternativeSetDescriptiveNoteHandler);
		alternativeSetHandler.getHandlers().add(alternativeSetComponentEntryHandler);
		eacCpfHandlers.add(descriptionHandler);
		eacCpfHandlers.add(relationsHandler);
		eacCpfHandlers.add(alternativeSetHandler);
		eacCpfHandlers.add(identityHandler);
		


	}

	public void processCharacters(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		for (XmlStreamHandler handler : eacCpfHandlers) {
			handler.processCharacters(xpathPosition, xmlReader);
		}

	}

	public void processStartElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		for (XmlStreamHandler handler : eacCpfHandlers) {
			handler.processStartElement(xpathPosition, xmlReader);
		}

	}

	public void processEndElement(LinkedList<QName> xpathPosition, XMLStreamReader xmlReader) throws Exception {
		for (XmlStreamHandler handler : eacCpfHandlers) {
			handler.processEndElement(xpathPosition, xmlReader);
		}

	}

	public void fillData(EacCpfPublishData publishData, EacCpf eacCpf) {
		publishData.setEntityType(entityTypeHandler.getFirstResult());
		publishData.setLanguage(languageHandler.getResultAsString());
		publishData.setEntityIds(entityIdHandler.getResultSet());
//		publishData.setNames(namesHandler.getResultSet("part"));
//		if (publishData.getNames().size() == 0){
//			publishData.setNames(namesParallelHandler.getResultSet("part"));
//		}
		if (namesHandler.getResults().size() > 0){
			List<String> names = new ArrayList<String>();
			for (Map<String, List<String>> tempResults: namesHandler.getResults()){
				String name = TextMapXpathHandler.getResultAsStringWithWhitespace(tempResults, new String[] {"part@surname","part@birthname", "part@prefix", "part@firstname", "part@suffix", "part@title", "part@alias"},", ");
				if (StringUtils.isBlank(name)){
					name = TextMapXpathHandler.getResultAsStringWithWhitespace(tempResults, ", ");
				}
				names.add(name);
			}
			//"Surname (Birthname), Prefix, Firstname Patronym, Suffix, Title (alias: Alias)"."
			publishData.setNames(names);
		}
		
		publishData.setPlaces(strip(placesHandler.getResultSet()));
		publishData.setFunctions(strip(functionsHandler.getResultSet()));
		publishData.setOccupations(strip(occupationsHandler.getResultSet()));
		publishData.setMandates(strip(mandatesHandler.getResultSet()));

		StringBuilder description = new StringBuilder();
		add(description, bioghistHandler.getResultAsStringWithWhitespace());
		add(description, generalContextHandler.getResultAsStringWithWhitespace());
		add(description, structureOrGenealogyHandler.getResultAsStringWithWhitespace());
		add(description, legalStatusHandler.getResultAsStringWithWhitespace());
		add(description, occupationPlaceEntryHandler.getResultAsStringWithWhitespace());
		add(description, mandatePlaceEntryHandler.getResultAsStringWithWhitespace());		
		add(description, localDescriptionHandler.getResultAsStringWithWhitespace());
		add(description, placeRoleHandler.getResultAsStringWithWhitespace());
		add(description, placeAddressLineHandler.getResultAsStringWithWhitespace());		
		add(description, descriptionDescriptiveNoteHandler.getResultAsStringWithWhitespace());
		publishData.setDescription(description.toString());
		StringBuilder other = new StringBuilder();
		add(other, relationsDescriptiveNoteHandler.getResultAsStringWithWhitespace());
		add(other, relationsPlaceEntryHandler.getResultAsStringWithWhitespace());
		add(other, relationsRelationEntryHandler.getResultAsStringWithWhitespace());
		add(other, alternativeSetDescriptiveNoteHandler.getResultAsStringWithWhitespace());
		add(other, alternativeSetComponentEntryHandler.getResultAsStringWithWhitespace());		
		publishData.setOther(other.toString());
		publishData.setNumberOfArchivalMaterialRelations(countArchivalMaterialRelationsHandler.getCount());
		publishData.setNumberOfNameRelations(countNameRelationsHandler.getCount());
		publishData.setNumberOfInstitutionsRelations(institutionsRelationsHandler.getResultSet().size());
		
		/*
		 * dates
		 */
		String oneDate = oneDateHandler.getFirstResult();
		String oneDateNormal = oneDateNormalHandler.getFirstResult();
		String dateLocalType = oneDateLocalTypeHandler.getFirstResult();
		if (StringUtils.isBlank(oneDate) && StringUtils.isBlank(oneDateNormal)){
			String fromDate = fromDateHandler.getFirstResult();
			String toDate = toDateHandler.getFirstResult();
			String fromDateNormal = fromDateNormalHandler.getFirstResult();
			String toDateNormal = toDateNormalHandler.getFirstResult();
			dateLocalType = dateRangeLocalTypeHandler.getFirstResult();
			if (StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) &&  StringUtils.isBlank(fromDateNormal) 
					&& StringUtils.isBlank(toDateNormal) && StringUtils.isBlank(dateLocalType)){
				publishData.setDateType(SolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
			}else {
				String dateDescription = null;
				if (StringUtils.isNotBlank(fromDate)){
					dateDescription = fromDate;
				}
				if (StringUtils.isNotBlank(toDate)){
					if (dateDescription == null){
						dateDescription = toDate;
					}else {
						dateDescription += " - " + toDate;
					}
				}
				publishData.setDateDescription(dateDescription);
				fromDateNormal = AbstractSolrPublisher.obtainDate(fromDateNormal, true);
				toDateNormal = AbstractSolrPublisher.obtainDate(toDateNormal, false);
				if (StringUtils.isBlank(fromDateNormal) && StringUtils.isBlank(toDateNormal)){
					if (StringUtils.isNotBlank(dateLocalType) && dateLocalType.startsWith(DATE_UNKNOWN)){
						publishData.setDateType(SolrValues.DATE_TYPE_UNKNOWN_DATE);
					}else {
						publishData.setDateType(SolrValues.DATE_TYPE_OTHER_DATE);
					}
				}else {
					if (StringUtils.isNotBlank(fromDateNormal) && StringUtils.isNotBlank(toDateNormal)){
						publishData.setFromDate(AbstractSolrPublisher.obtainDate(fromDateNormal, true));
						publishData.setToDate(AbstractSolrPublisher.obtainDate(toDateNormal, false));		
						publishData.setDateType(SolrValues.DATE_TYPE_NORMALIZED);
					}else if (StringUtils.isNotBlank(fromDateNormal)){
						publishData.setFromDate(AbstractSolrPublisher.obtainDate(fromDateNormal, true));
						publishData.setDateType(SolrValues.DATE_TYPE_NORMALIZED_UNKNOWN_STARTDATE);
					}else {
						publishData.setToDate(AbstractSolrPublisher.obtainDate(toDateNormal, false));
						publishData.setDateType(SolrValues.DATE_TYPE_NORMALIZED_UNKNOWN_ENDDATE);
					}
				}
			}
		}else {
			publishData.setDateDescription(oneDate);
			if (StringUtils.isBlank(oneDateNormal)){
				if (StringUtils.isNotBlank(dateLocalType) && dateLocalType.startsWith(DATE_UNKNOWN)){
					publishData.setDateType(SolrValues.DATE_TYPE_UNKNOWN_DATE);
				}else {
					publishData.setDateType(SolrValues.DATE_TYPE_OTHER_DATE);
				}
			}else {
				if (DATE_UNKNOWN_START.equals(dateLocalType)){
					publishData.setDateType(SolrValues.DATE_TYPE_NORMALIZED_UNKNOWN_STARTDATE);
					publishData.setToDate(AbstractSolrPublisher.obtainDate(oneDateNormal, false));	
				}else if (DATE_UNKNOWN_END.equals(dateLocalType)){
					publishData.setDateType(SolrValues.DATE_TYPE_NORMALIZED_UNKNOWN_ENDDATE);
					publishData.setFromDate(AbstractSolrPublisher.obtainDate(oneDateNormal, true));
				}else {
					publishData.setFromDate(AbstractSolrPublisher.obtainDate(oneDateNormal, true));
					publishData.setToDate(AbstractSolrPublisher.obtainDate(oneDateNormal, false));			
					publishData.setDateType(SolrValues.DATE_TYPE_NORMALIZED);
				}
			}

		}
	}

	protected static Set<String> strip(Set<String> source) {
		Set<String> results = new LinkedHashSet<String>();
		for (String temp : source) {
			String text = temp;
			int index = text.indexOf('(');
			if (index > 0) {
				text = text.substring(0, index).trim();
			}

			results.add(text);

		}
		return results;
	}

	private void add(StringBuilder other, String item) {
		if (StringUtils.isNotBlank(item)) {
			other.append(StringXpathHandler.WHITE_SPACE + item);
		}
	}

	public static Map<String, ResourceBundle> getResourceBundles(String baseName) {

		Map<String, ResourceBundle> resourceBundles = new HashMap<String, ResourceBundle>();

		for (String isoLanguage : Locale.getISOLanguages()) {
			try {
				URL url = EacCpfPublishDataFiller.class.getClassLoader().getResource(
						baseName + "_" + isoLanguage + ".properties");
				if (url != null) {
					resourceBundles.put(isoLanguage, ResourceBundle.getBundle(baseName, new Locale(isoLanguage)));
				}
			} catch (MissingResourceException ex) {
				// ...
			}
		}

		return resourceBundles;
	}
}
