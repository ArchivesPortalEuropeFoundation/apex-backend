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

import eu.apenet.dashboard.services.eaccpf.xml.stream.publish.EacCpfPublishData;
import eu.apenet.persistence.vo.EacCpf;
import eu.archivesportaleurope.xml.ApeXMLConstants;
import eu.archivesportaleurope.xml.xpath.AttributeXpathHandler;
import eu.archivesportaleurope.xml.xpath.CountXpathHandler;
import eu.archivesportaleurope.xml.xpath.NestedXpathHandler;
import eu.archivesportaleurope.xml.xpath.StringXpathHandler;
import eu.archivesportaleurope.xml.xpath.TextXpathHandler;
import eu.archivesportaleurope.xml.xpath.XmlStreamHandler;

public class EacCpfPublishDataFiller {
	/*
	 * unitids
	 */
	private AttributeXpathHandler languageHandler;
	private TextXpathHandler entityTypeHandler;
	private TextXpathHandler entityIdHandler;
	private TextXpathHandler placesHandler;
	private TextXpathHandler functionsHandler;
	private TextXpathHandler mandatesHandler;
	private TextXpathHandler occupationsHandler;

	/*
	 * description
	 */
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
	private NestedXpathHandler relationsHandler;
	
	private CountXpathHandler countArchivalMaterialRelationsHandler;
	private CountXpathHandler countNameRelationsHandler;
	private AttributeXpathHandler institutionsRelationsHandler;

	private List<XmlStreamHandler> eacCpfHandlers = new ArrayList<XmlStreamHandler>();

	public EacCpfPublishDataFiller() {
		/*
		 * name
		 */
		languageHandler = new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "eac-cpf",
				"control", "languageDeclaration", "language" }, "languageCode");
		entityTypeHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "eac-cpf",
				"cpfDescription", "identity", "entityType" });
		entityIdHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "eac-cpf",
				"cpfDescription", "identity", "entityId" });
		placesHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "places", "place", "placeEntry" });
		occupationsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "occupations", "occupation", "term" });
		mandatesHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "mandates", "mandate", "term" });
		functionsHandler = new TextXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] { "functions", "function", "term" });
		
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
		institutionsRelationsHandler =  new AttributeXpathHandler(ApeXMLConstants.APE_EAC_CPF_NAMESPACE, new String[] {"resourceRelation", "relationEntry"},"agencyCode");
//		private CountXpathHandler countNameRelationsHandler;
//		private TextXpathHandler institutionsRelationsHandler;	
//		countArchivalMaterialRelationsExpression = XPATH.compile("count(//eac:resourceRelation)");
//		countNameRelationsExpression = XPATH.compile("count(//eac:cpfRelation[not(@cpfRelationType='identity')])");
//		institutionsRelationsExpression = XPATH.compile("//eac:resourceRelation/eac:relationEntry[@localType='agencyCode']");
		
		
		eacCpfHandlers.add(languageHandler);
		eacCpfHandlers.add(entityTypeHandler);
		eacCpfHandlers.add(entityIdHandler);
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
		eacCpfHandlers.add(descriptionHandler);
		eacCpfHandlers.add(relationsHandler);
		


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
		publishData.setNumberOfArchivalMaterialRelations(countArchivalMaterialRelationsHandler.getCount());
		publishData.setNumberOfNameRelations(countNameRelationsHandler.getCount());
		publishData.setNumberOfInstitutionsRelations(institutionsRelationsHandler.getResultSet().size());
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
