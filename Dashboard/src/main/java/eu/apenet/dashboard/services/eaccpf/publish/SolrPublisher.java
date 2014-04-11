package eu.apenet.dashboard.services.eaccpf.publish;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import eu.apenet.commons.solr.AbstractSolrServerHolder;
import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.dashboard.services.eaccpf.xml.EacCpfNamespaceContext;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;

public class SolrPublisher  extends AbstractSolrPublisher{


	private static final Logger LOGGER = Logger.getLogger(SolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");

	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
	private static XPathExpression recordIdExpression;
	private static XPathExpression agencyCodeExpression;
	private static XPathExpression agencyNameExpression;
	private static XPathExpression nameExpression;
	private static XPathExpression nameParallelExpression;
	private static XPathExpression placeExpression;
	private static XPathExpression cpfDescriptionExpression;
	private static XPathExpression descriptionExpression;
	private static XPathExpression fromDateExpression;
	private static XPathExpression toDateExpression;
	private static XPathExpression fromDateNormalExpression;
	private static XPathExpression toDateNormalExpression;
	private static XPathExpression occupationExpression;
	private static XPathExpression occupationPlaceEntryExpression;
	
	private static XPathExpression bioghistExpression;
	private static XPathExpression generalContextExpression;
	private static XPathExpression descriptiveNoteExpression;
	private static XPathExpression structureOrGenealogyExpression;
	private static XPathExpression languageExpression;
	private static XPathExpression entityTypeExpression;
	private static XPathExpression entityIdExpression;
	private static XPathExpression mandateExpression;	
	private static XPathExpression functionExpression;	
	private static XPathExpression legalStatusTermExpression;;
	private static XPathExpression legalStatusPlaceEntryExpression;;
	private static XPathExpression mandatePlaceEntryExpression;;
	private static XPathExpression localDescriptionTermExpression;
	private static XPathExpression localDescriptionPlaceEntryExpression;
	private static XPathExpression placeRoleExpression;
	private static XPathExpression placeAddressLineExpression;
	private static XPathExpression relationsExpression;
	private static XPathExpression otherPlaceEntryExpression;
	private static XPathExpression otherRelationEntryExpression;	
	private String recordId;
	static {
		try {
			XPATH.setNamespaceContext(new EacCpfNamespaceContext());
			recordIdExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:recordId");
			agencyCodeExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyCode");
			agencyNameExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName");
			cpfDescriptionExpression =  XPATH.compile("/eac:eac-cpf/eac:cpfDescription");
			nameExpression = XPATH.compile("./eac:identity/eac:nameEntry/eac:part");
			nameParallelExpression = XPATH.compile("./eac:identity/eac:nameEntryParallel/eac:nameEntry/eac:part");
			descriptionExpression = XPATH.compile("./eac:description");
			relationsExpression = XPATH.compile("./eac:relations");
			placeExpression = XPATH.compile("./eac:places/eac:place/eac:placeEntry");
			fromDateExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:fromDate");
			toDateExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:toDate");
			fromDateNormalExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:fromDate/@standardDate");
			toDateNormalExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:toDate/@standardDate");
			occupationExpression = XPATH.compile("./eac:occupations/eac:occupation/eac:term");
			bioghistExpression = XPATH.compile("./eac:biogHist//text()");
			generalContextExpression = XPATH.compile("./eac:generalContext//text()");
			descriptiveNoteExpression = XPATH.compile("//eac:descriptiveNote//text()");
			structureOrGenealogyExpression = XPATH.compile("./eac:structureOrGenealogy//text()");
			languageExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:languageDeclaration/eac:language/@languageCode");
			entityTypeExpression = XPATH.compile("./eac:identity/eac:entityType");
			entityIdExpression = XPATH.compile("./eac:identity/eac:entityId");
			mandateExpression =  XPATH.compile("./eac:mandates/eac:mandate/eac:term");
			functionExpression = XPATH.compile("./eac:functions/eac:function/eac:term");	
			legalStatusTermExpression = XPATH.compile("./legalStatuses/legalStatus/term");
			legalStatusPlaceEntryExpression = XPATH.compile("./legalStatuses/legalStatus/placeEntry");
			occupationPlaceEntryExpression = XPATH.compile("./eac:occupations/eac:occupation/placeEntry");
			mandatePlaceEntryExpression = XPATH.compile("./mandates/mandate/placeEntry");
			localDescriptionTermExpression = XPATH.compile("./localDescriptions/localDescription/term");
			localDescriptionPlaceEntryExpression = XPATH.compile("./localDescriptions/localDescription/placeEntry");
			placeRoleExpression = XPATH.compile("./places/place/placeRole");
			placeAddressLineExpression = XPATH.compile("./places/place/address/addressline");
			otherPlaceEntryExpression = XPATH.compile("//eac:placeEntry");
			otherRelationEntryExpression = XPATH.compile("//eac:relationEntry");
		} catch (XPathExpressionException e) {
			LOGGER.info(e.getMessage(), e);
		}
	}

	private String getContent(List<XPathExpression> expressions, Node baseNode) throws XPathExpressionException{
		StringBuilder stringBuilder = new StringBuilder();
		for (XPathExpression expression: expressions){
			stringBuilder.append(getText((NodeList)expression.evaluate(baseNode, XPathConstants.NODESET)));
		}
		return stringBuilder.toString();
	}
	
	public void publish(EacCpf eacCpf) throws Exception{
		File file = new File (APEnetUtilities.getDashboardConfig().getRepoDirPath() + eacCpf.getPath());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);		
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new FileReader(file)));
		doc.getDocumentElement().normalize();
		EacCpfSolrObject eacCpfSolrObject = new EacCpfSolrObject(eacCpf);
		eacCpfSolrObject.setRecordId((eacCpf.getIdentifier()));
		NodeList entityIdNodeList = (NodeList) entityIdExpression.evaluate(doc, XPathConstants.NODESET);
		eacCpfSolrObject.setEntityIds(getTextsWithoutMultiplity(entityIdNodeList));
		eacCpfSolrObject.setEntityType((String) entityTypeExpression.evaluate(doc, XPathConstants.STRING));
		eacCpfSolrObject.setLanguage((String) languageExpression.evaluate(doc, XPathConstants.STRING));
		eacCpfSolrObject.setAgencyCode(eacCpf.getArchivalInstitution().getRepositorycode());
		eacCpfSolrObject.setAgencyName(removeUnusedCharacters((String) agencyNameExpression.evaluate(doc, XPathConstants.STRING)));
		Node cpfDescriptionNode = (Node) cpfDescriptionExpression.evaluate(doc, XPathConstants.NODE);
		parseCpfDescription(eacCpfSolrObject, cpfDescriptionNode);
		publishEacCpf(eacCpfSolrObject);

	}
	private void parseCpfDescription(EacCpfSolrObject eacCpfSolrObject, Node cpfDescriptionNode) throws XPathExpressionException{
		Node descriptionNode = (Node) descriptionExpression.evaluate(cpfDescriptionNode, XPathConstants.NODE);
		NodeList nameNodeList = (NodeList) nameExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		eacCpfSolrObject.setNames(getTextsWithoutMultiplity(nameNodeList));
		if (eacCpfSolrObject.getNames().size() ==0){
			nameNodeList = (NodeList) nameParallelExpression.evaluate(descriptionNode, XPathConstants.NODESET);
			eacCpfSolrObject.setNames(getTextsWithoutMultiplity(nameNodeList));
		}
		NodeList placesNodeList = (NodeList) placeExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		eacCpfSolrObject.setPlaces(getTextsWithoutMultiplity(placesNodeList));
		
		NodeList functionsNodeList = (NodeList) functionExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		eacCpfSolrObject.setFunctions(getTextsWithoutMultiplity(functionsNodeList));
		NodeList mandatesNodeList = (NodeList) mandateExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		eacCpfSolrObject.setMandates(getTextsWithoutMultiplity(mandatesNodeList));
		NodeList occupationsNodeList = (NodeList) occupationExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		
		eacCpfSolrObject.setOccupations(getTextsWithoutMultiplity(occupationsNodeList));
		List<XPathExpression> descriptionExpressions = new ArrayList<XPathExpression>();
		descriptionExpressions.add(bioghistExpression);
		descriptionExpressions.add(generalContextExpression);
		descriptionExpressions.add(structureOrGenealogyExpression);
		descriptionExpressions.add(descriptiveNoteExpression);
		descriptionExpressions.add(legalStatusTermExpression);
		descriptionExpressions.add(legalStatusPlaceEntryExpression);
		descriptionExpressions.add(mandatePlaceEntryExpression);
		descriptionExpressions.add(localDescriptionTermExpression);	
		descriptionExpressions.add(localDescriptionPlaceEntryExpression);
		descriptionExpressions.add(placeRoleExpression);		
		descriptionExpressions.add(placeAddressLineExpression);	
		eacCpfSolrObject.setDescription(getContent(descriptionExpressions, descriptionNode));
		Node relationsNode = (Node) relationsExpression.evaluate(cpfDescriptionNode, XPathConstants.NODE);
		List<XPathExpression> otherExpressions = new ArrayList<XPathExpression>();
		otherExpressions.add(descriptiveNoteExpression);
		otherExpressions.add(otherPlaceEntryExpression);
		otherExpressions.add(otherRelationEntryExpression);

		eacCpfSolrObject.setOther(getContent(otherExpressions, relationsNode));
		String fromDate = removeUnusedCharacters((String) fromDateExpression.evaluate(descriptionNode, XPathConstants.STRING));
		String toDate = removeUnusedCharacters((String) toDateExpression.evaluate(descriptionNode, XPathConstants.STRING));
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
		eacCpfSolrObject.setDateDescription(dateDescription);
		fromDate = (String) fromDateNormalExpression.evaluate(descriptionNode, XPathConstants.STRING);
		toDate = (String) toDateNormalExpression.evaluate(descriptionNode, XPathConstants.STRING);
		eacCpfSolrObject.setFromDate(obtainDate(fromDate, true));
		eacCpfSolrObject.setToDate(obtainDate(toDate, false));
	}

	private void publishEacCpf(EacCpfSolrObject eacCpfSolrObject) throws MalformedURLException, SolrServerException, IOException {

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrFields.ID, eacCpfSolrObject.getEacCpf().getId());
		add(doc,SolrFields.EAC_CPF_RECORD_ID, eacCpfSolrObject.getRecordId());
		addLowerCase(doc,SolrFields.EAC_CPF_FACET_ENTITY_TYPE, eacCpfSolrObject.getEntityType());
		doc.addField(SolrFields.EAC_CPF_ENTITY_ID, eacCpfSolrObject.getEntityIds());
		doc.addField(SolrFields.EAC_CPF_NAMES, eacCpfSolrObject.getNames());
		doc.addField(SolrFields.EAC_CPF_PLACES, eacCpfSolrObject.getPlaces());
		doc.addField(SolrFields.EAC_CPF_OCCUPATION,eacCpfSolrObject.getOccupations());
		doc.addField(SolrFields.EAC_CPF_FACET_FUNCTION, eacCpfSolrObject.getFunctions());
		doc.addField(SolrFields.EAC_CPF_FACET_MANDATE,eacCpfSolrObject.getMandates());		
		add(doc, SolrFields.EAC_CPF_DESCRIPTION,eacCpfSolrObject.getDescription());
		add(doc, SolrFields.START_DATE, eacCpfSolrObject.getFromDate());
		add(doc, SolrFields.END_DATE, eacCpfSolrObject.getToDate());
		add(doc, SolrFields.EAC_CPF_DATE_DESCRIPTION, eacCpfSolrObject.getDateDescription());
		if (StringUtils.isBlank(eacCpfSolrObject.getDateDescription())) {
			add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
		} else {
			if (StringUtils.isBlank(eacCpfSolrObject.getFromDate())) {
				add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_OTHER_DATE);
			} else {
				add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED);
			}
		}
		ArchivalInstitution archivalInstitution = eacCpfSolrObject.getEacCpf().getArchivalInstitution();
		add(doc, SolrFields.COUNTRY, archivalInstitution.getCountry().getCname().replace(" ", "_") + COLON + SolrValues.TYPE_GROUP + COLON + archivalInstitution.getCountry().getId());
		doc.addField(SolrFields.COUNTRY_ID, archivalInstitution.getCountry().getId());
		add(doc, SolrFields.LANGUAGE, eacCpfSolrObject.getLanguage());

		add(doc, SolrFields.AI, archivalInstitution.getAiname() + COLON + archivalInstitution.getAiId());
		doc.addField(SolrFields.AI_ID, archivalInstitution.getAiId());
		addSolrDocument(doc);
	}
	public void deleteEverything() throws SolrServerException{
		rollbackSolrDocuments("*:*");
	}
	@Override
	protected String getKey() {
		return recordId;
	}

	protected static Set<String> getTextsWithoutMultiplity(NodeList nodeList) {
		Set<String> results = new LinkedHashSet<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			String text = removeUnusedCharacters(nodeList.item(i).getTextContent());
			if (StringUtils.isNotBlank(text)) {
				int index = text.indexOf('(');
				if (index > 0){
					text  = text.substring(0, index).trim();
				}
				results.add(text);
			}
		}
		return results;
	}


	@Override
	protected AbstractSolrServerHolder getSolrServerHolder() {
		return EacCpfSolrServerHolder.getInstance();
	}
	public long unpublish(EacCpf eacCpf) throws SolrServerException, IOException {
		return getSolrServerHolder().deleteByQuery("(" + SolrFields.AI_ID + ":" + eacCpf.getAiId() + " AND " + SolrFields.ID + ":\"" + eacCpf.getId() + "\")");
	}
}
