package eu.apenet.dashboard.services.eag.publish;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
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
import eu.apenet.commons.solr.EagSolrServerHolder;
import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.dashboard.services.eaccpf.xml.EacCpfNamespaceContext;
import eu.apenet.persistence.vo.ArchivalInstitution;

public class EagSolrPublisher  extends AbstractSolrPublisher{


	private static final Logger LOGGER = Logger.getLogger(EagSolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");

	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
	private static XPathExpression nameExpression;
	private static XPathExpression nameParallelExpression;
	private static XPathExpression placeExpression;
	private static XPathExpression cpfDescriptionExpression;
	private static XPathExpression descriptionExpression;
	private static XPathExpression fromDateExpression;
	private static XPathExpression toDateExpression;
	private static XPathExpression oneDateExpression;
	private static XPathExpression oneDateNormalExpression;
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
	private static XPathExpression alternativeSetExpression;
	private static XPathExpression otherPlaceEntryExpression;
	private static XPathExpression otherRelationEntryExpression;	
	private static XPathExpression otherComponentEntryExpression;
	private static XPathExpression countArchivalMaterialRelationsExpression;
	private static XPathExpression countNameRelationsExpression;
	private static XPathExpression institutionsRelationsExpression;
	private String recordId;
	static {
		try {
			XPATH.setNamespaceContext(new EacCpfNamespaceContext());
			cpfDescriptionExpression =  XPATH.compile("/eag:eac-cpf/eag:cpfDescription");
			nameExpression = XPATH.compile("./eag:identity/eag:nameEntry/eag:part");
			nameParallelExpression = XPATH.compile("./eag:identity/eag:nameEntryParallel/eag:nameEntry/eag:part");
			descriptionExpression = XPATH.compile("./eag:description");
			relationsExpression = XPATH.compile("./eag:relations");
			placeExpression = XPATH.compile("./eag:places/eag:place/eag:placeEntry");
			fromDateExpression = XPATH.compile("./eag:existDates/eag:dateRange/eag:fromDate");
			toDateExpression = XPATH.compile("./eag:existDates/eag:dateRange/eag:toDate");
			oneDateExpression = XPATH.compile("./eag:existDates/eag:date");
			oneDateNormalExpression = XPATH.compile("./eag:existDates/eag:date/@standardDate");
			fromDateNormalExpression = XPATH.compile("./eag:existDates/eag:dateRange/eag:fromDate/@standardDate");
			toDateNormalExpression = XPATH.compile("./eag:existDates/eag:dateRange/eag:toDate/@standardDate");
			occupationExpression = XPATH.compile("./eag:occupations/eag:occupation/eag:term");
			bioghistExpression = XPATH.compile("./eag:biogHist//text()");
			generalContextExpression = XPATH.compile("./eag:generalContext//text()");
			descriptiveNoteExpression = XPATH.compile("//eag:descriptiveNote//text()");
			structureOrGenealogyExpression = XPATH.compile("./eag:structureOrGenealogy//text()");
			languageExpression = XPATH.compile("/eag:eac-cpf/eag:control/eag:languageDeclaration/eag:language/@languageCode");
			entityTypeExpression = XPATH.compile("./eag:identity/eag:entityType");
			entityIdExpression = XPATH.compile("./eag:identity/eag:entityId");
			mandateExpression =  XPATH.compile("./eag:mandates/eag:mandate/eag:term");
			functionExpression = XPATH.compile("./eag:functions/eag:function/eag:term");	
			legalStatusTermExpression = XPATH.compile("./legalStatuses/legalStatus/term");
			legalStatusPlaceEntryExpression = XPATH.compile("./legalStatuses/legalStatus/placeEntry");
			occupationPlaceEntryExpression = XPATH.compile("./eag:occupations/eag:occupation/placeEntry");
			mandatePlaceEntryExpression = XPATH.compile("./mandates/mandate/placeEntry");
			localDescriptionTermExpression = XPATH.compile("./localDescriptions/localDescription/term");
			localDescriptionPlaceEntryExpression = XPATH.compile("./localDescriptions/localDescription/placeEntry");
			placeRoleExpression = XPATH.compile("./places/place/placeRole");
			placeAddressLineExpression = XPATH.compile("./places/place/address/addressline");
			otherPlaceEntryExpression = XPATH.compile("//eag:placeEntry");
			otherRelationEntryExpression = XPATH.compile("//eag:relationEntry");
			otherComponentEntryExpression = XPATH.compile("//eag:componentEntry");
			alternativeSetExpression = XPATH.compile("./eag:alternativeSet");
			countArchivalMaterialRelationsExpression = XPATH.compile("count(//eag:resourceRelation)");
			countNameRelationsExpression = XPATH.compile("count(//eag:cpfRelation[not(@cpfRelationType='identity')])");
			institutionsRelationsExpression = XPATH.compile("//eag:resourceRelation/eag:relationEntry[@localType='agencyCode']");

		} catch (XPathExpressionException e) {
			LOGGER.info(e.getMessage(), e);
		}
	}

	private StringBuilder getContent(List<XPathExpression> expressions, Node baseNode) throws XPathExpressionException{
		StringBuilder stringBuilder = new StringBuilder();
		for (XPathExpression expression: expressions){
			stringBuilder.append(getText((NodeList)expression.evaluate(baseNode, XPathConstants.NODESET))+ WHITE_SPACE);
		}
		return stringBuilder;
	}
	
	public void publish(ArchivalInstitution archivalInstitution) throws Exception{
		File file = new File (APEnetUtilities.getDashboardConfig().getRepoDirPath() + archivalInstitution.getEagPath());
		if (file.exists()){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);		
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new FileReader(file)));
		doc.getDocumentElement().normalize();
		EagSolrObject eacCpfSolrObject = new EagSolrObject(archivalInstitution);
//		eacCpfSolrObject.setRecordId((eacCpf.getIdentifier()));
//		recordId = eacCpf.getIdentifier();
//		eacCpfSolrObject.setLanguage((String) languageExpression.evaluate(doc, XPathConstants.STRING));
//		Node cpfDescriptionNode = (Node) cpfDescriptionExpression.evaluate(doc, XPathConstants.NODE);
//		NodeList entityIdNodeList = (NodeList) entityIdExpression.evaluate(cpfDescriptionNode, XPathConstants.NODESET);
//		eacCpfSolrObject.setEntityIds(getTextsWithoutMultiplity(entityIdNodeList,false));
//		eacCpfSolrObject.setEntityType((String) entityTypeExpression.evaluate(cpfDescriptionNode, XPathConstants.STRING));
//		parseCpfDescription(eacCpfSolrObject, cpfDescriptionNode);
		publishEacCpf(eacCpfSolrObject);
		}else {
			LOGGER.error("Could not find: " + file.getAbsolutePath());
		}

	}
//	private void parseCpfDescription(EagSolrObject eacCpfSolrObject, Node cpfDescriptionNode) throws XPathExpressionException{
//		Node descriptionNode = (Node) descriptionExpression.evaluate(cpfDescriptionNode, XPathConstants.NODE);
//		NodeList nameNodeList = (NodeList) nameExpression.evaluate(cpfDescriptionNode, XPathConstants.NODESET);
//		eacCpfSolrObject.setNames(getTextsWithoutMultiplity(nameNodeList, true));
//		if (eacCpfSolrObject.getNames().size() ==0){
//			nameNodeList = (NodeList) nameParallelExpression.evaluate(cpfDescriptionNode, XPathConstants.NODESET);
//			eacCpfSolrObject.setNames(getTextsWithoutMultiplity(nameNodeList, true));
//		}
//		NodeList placesNodeList = (NodeList) placeExpression.evaluate(descriptionNode, XPathConstants.NODESET);
//		eacCpfSolrObject.setPlaces(getTextsWithoutMultiplity(placesNodeList, true));
//		
//		NodeList functionsNodeList = (NodeList) functionExpression.evaluate(descriptionNode, XPathConstants.NODESET);
//		eacCpfSolrObject.setFunctions(getTextsWithoutMultiplity(functionsNodeList, true));
//		NodeList mandatesNodeList = (NodeList) mandateExpression.evaluate(descriptionNode, XPathConstants.NODESET);
//		eacCpfSolrObject.setMandates(getTextsWithoutMultiplity(mandatesNodeList, true));
//		NodeList occupationsNodeList = (NodeList) occupationExpression.evaluate(descriptionNode, XPathConstants.NODESET);
//		
//		eacCpfSolrObject.setOccupations(getTextsWithoutMultiplity(occupationsNodeList, true));
//		List<XPathExpression> descriptionExpressions = new ArrayList<XPathExpression>();
//		descriptionExpressions.add(bioghistExpression);
//		descriptionExpressions.add(generalContextExpression);
//		descriptionExpressions.add(structureOrGenealogyExpression);
//		descriptionExpressions.add(descriptiveNoteExpression);
//		descriptionExpressions.add(legalStatusTermExpression);
//		descriptionExpressions.add(legalStatusPlaceEntryExpression);
//		descriptionExpressions.add(mandatePlaceEntryExpression);
//		descriptionExpressions.add(localDescriptionTermExpression);	
//		descriptionExpressions.add(localDescriptionPlaceEntryExpression);
//		descriptionExpressions.add(placeRoleExpression);		
//		descriptionExpressions.add(placeAddressLineExpression);	
//		descriptionExpressions.add(occupationPlaceEntryExpression);	
//		eacCpfSolrObject.setDescription(getContent(descriptionExpressions, descriptionNode).toString());
//		Node relationsNode = (Node) relationsExpression.evaluate(cpfDescriptionNode, XPathConstants.NODE);
//		List<XPathExpression> otherExpressions = new ArrayList<XPathExpression>();
//		otherExpressions.add(descriptiveNoteExpression);
//		otherExpressions.add(otherPlaceEntryExpression);
//		otherExpressions.add(otherRelationEntryExpression);
//		StringBuilder other = getContent(otherExpressions, relationsNode);
//		otherExpressions.clear();
//		Node alternativeSetNode = (Node) alternativeSetExpression.evaluate(cpfDescriptionNode, XPathConstants.NODE);
//		otherExpressions.add(descriptiveNoteExpression);
//		otherExpressions.add(otherComponentEntryExpression);
//		other.append(getContent(otherExpressions, alternativeSetNode));
//		eacCpfSolrObject.setOther(other.toString());
//		String oneDate = removeUnusedCharacters((String) oneDateExpression.evaluate(descriptionNode, XPathConstants.STRING));
//		String oneDateNormal = removeUnusedCharacters((String) oneDateNormalExpression.evaluate(descriptionNode, XPathConstants.STRING));
//		if (StringUtils.isBlank(oneDate) && StringUtils.isBlank(oneDateNormal)){
//			String fromDate = removeUnusedCharacters((String) fromDateExpression.evaluate(descriptionNode, XPathConstants.STRING));
//			String toDate = removeUnusedCharacters((String) toDateExpression.evaluate(descriptionNode, XPathConstants.STRING));
//			String dateDescription = null;
//			if (StringUtils.isNotBlank(fromDate)){
//				dateDescription = fromDate;
//			}
//			if (StringUtils.isNotBlank(toDate)){
//				if (dateDescription == null){
//					dateDescription = toDate;
//				}else {
//					dateDescription += " - " + toDate;
//				}
//			}
//			eacCpfSolrObject.setDateDescription(dateDescription);
//			fromDate = (String) fromDateNormalExpression.evaluate(descriptionNode, XPathConstants.STRING);
//			toDate = (String) toDateNormalExpression.evaluate(descriptionNode, XPathConstants.STRING);
//			if ("0001".equals(fromDate)){
//				fromDate = null;
//				eacCpfSolrObject.setFromDateExist(false);
//			}else {
//				eacCpfSolrObject.setFromDateExist(true);
//			}
//			if ("2099".equals(toDate)){
//				toDate = null;
//				eacCpfSolrObject.setToDateExist(false);
//			}else {
//				eacCpfSolrObject.setToDateExist(true);
//			}
//			if (toDate == null && fromDate != null){
//				toDate = fromDate;
//			}
//			if (fromDate == null && toDate != null){
//				fromDate = toDate;
//			}
//			eacCpfSolrObject.setFromDate(obtainDate(fromDate, true));
//			eacCpfSolrObject.setToDate(obtainDate(toDate, false));
//		}else {
//			eacCpfSolrObject.setDateDescription(oneDate);
//			eacCpfSolrObject.setFromDate(obtainDate(oneDateNormal, true));
//			eacCpfSolrObject.setToDate(obtainDate(oneDateNormal, false));		
//			eacCpfSolrObject.setFromDateExist(true);
//			eacCpfSolrObject.setToDateExist(true);
//		}
//
//		eacCpfSolrObject.setNumberOfArchivalMaterialRelations(((Double) countArchivalMaterialRelationsExpression.evaluate(relationsNode, XPathConstants.NUMBER)).intValue());
//		eacCpfSolrObject.setNumberOfNameRelations(((Double) countNameRelationsExpression.evaluate(relationsNode, XPathConstants.NUMBER)).intValue());
//		Set<String> institutions = getTextsWithoutMultiplity((NodeList) institutionsRelationsExpression.evaluate(relationsNode, XPathConstants.NODESET), false);
//		eacCpfSolrObject.setNumberOfInstitutionsRelations(institutions.size());
//
//	}

	private void publishEacCpf(EagSolrObject eagSolrObject) throws MalformedURLException, SolrServerException, IOException {
		ArchivalInstitution archivalInstitution = eagSolrObject.getArchivalInstitution();
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrFields.ID, archivalInstitution.getAiId());
		doc.addField(SolrFields.EAG_REPOSITORY_CODE, archivalInstitution.getRepositorycode());
		doc.addField(SolrFields.EAG_NAME, archivalInstitution.getAiname());		
//		add(doc,SolrFields.EAC_CPF_RECORD_ID, eagSolrObject.getRecordId());
//		addLowerCase(doc,SolrFields.EAC_CPF_FACET_ENTITY_TYPE, eagSolrObject.getEntityType());
//		doc.addField(SolrFields.EAC_CPF_ENTITY_ID, eagSolrObject.getEntityIds());
//		doc.addField(SolrFields.EAC_CPF_NAMES, eagSolrObject.getNames());
//		doc.addField(SolrFields.EAC_CPF_PLACES, eagSolrObject.getPlaces());
//		doc.addField(SolrFields.EAC_CPF_OCCUPATION,eagSolrObject.getOccupations());
//		doc.addField(SolrFields.EAC_CPF_FACET_FUNCTION, eagSolrObject.getFunctions());
//		doc.addField(SolrFields.EAC_CPF_FACET_MANDATE,eagSolrObject.getMandates());		
//		add(doc, SolrFields.EAC_CPF_DESCRIPTION,eagSolrObject.getDescription());
		
		add(doc, SolrFields.COUNTRY, archivalInstitution.getCountry().getCname().replace(" ", "_") + COLON + SolrValues.TYPE_GROUP + COLON + archivalInstitution.getCountry().getId());
		doc.addField(SolrFields.COUNTRY_ID, archivalInstitution.getCountry().getId());

		addSolrDocument(doc);
	}
	public void deleteEverything() throws SolrServerException{
		rollbackSolrDocuments("*:*");
	}
	@Override
	protected String getKey() {
		return recordId;
	}


	protected static Set<String> getTextsWithoutMultiplity(NodeList nodeList, boolean strip) {
		Set<String> results = new LinkedHashSet<String>();
		for (int i = 0; i < nodeList.getLength(); i++) {
			String text = removeUnusedCharacters(nodeList.item(i).getTextContent());
			if (StringUtils.isNotBlank(text)) {
				if (strip){
					int index = text.indexOf('(');
					if (index > 0){
						text  = text.substring(0, index).trim();
					}
				}
				results.add(text);
			}
		}
		return results;
	}


	@Override
	protected AbstractSolrServerHolder getSolrServerHolder() {
		return EagSolrServerHolder.getInstance();
	}
	public long unpublish(ArchivalInstitution archivalInstitution) throws SolrServerException, IOException {
		return getSolrServerHolder().deleteByQuery("(" + SolrFields.ID + ":" + archivalInstitution.getAiId()+ "\")");
	}
}
