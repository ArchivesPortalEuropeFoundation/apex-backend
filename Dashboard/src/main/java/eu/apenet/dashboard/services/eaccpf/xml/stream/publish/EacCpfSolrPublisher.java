package eu.apenet.dashboard.services.eaccpf.xml.stream.publish;

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
import eu.apenet.dashboard.services.eaccpf.publish.EacCpfSolrObject;
import eu.apenet.dashboard.services.eaccpf.xml.EacCpfNamespaceContext;
import eu.apenet.persistence.vo.ArchivalInstitution;
import eu.apenet.persistence.vo.EacCpf;

public class EacCpfSolrPublisher  extends AbstractSolrPublisher{


	private static final Logger LOGGER = Logger.getLogger(EacCpfSolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");

	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();

	private static XPathExpression cpfDescriptionExpression;
	private static XPathExpression descriptionExpression;
	private static XPathExpression fromDateExpression;
	private static XPathExpression toDateExpression;
	private static XPathExpression oneDateExpression;
	private static XPathExpression oneDateNormalExpression;
	private static XPathExpression fromDateNormalExpression;
	private static XPathExpression toDateNormalExpression;





	private String recordId;
	static {
		try {
			XPATH.setNamespaceContext(new EacCpfNamespaceContext());
			cpfDescriptionExpression =  XPATH.compile("/eac:eac-cpf/eac:cpfDescription");

			descriptionExpression = XPATH.compile("./eac:description");
			fromDateExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:fromDate");
			toDateExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:toDate");
			oneDateExpression = XPATH.compile("./eac:existDates/eac:date");
			oneDateNormalExpression = XPATH.compile("./eac:existDates/eac:date/@standardDate");
			fromDateNormalExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:fromDate/@standardDate");
			toDateNormalExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:toDate/@standardDate");




		} catch (XPathExpressionException e) {
			LOGGER.info(e.getMessage(), e);
		}
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
		recordId = eacCpf.getIdentifier();
		Node cpfDescriptionNode = (Node) cpfDescriptionExpression.evaluate(doc, XPathConstants.NODE);
		parseCpfDescription(eacCpfSolrObject, cpfDescriptionNode);
		//publishEacCpf(eacCpfSolrObject);

	}
	private void parseCpfDescription(EacCpfSolrObject eacCpfSolrObject, Node cpfDescriptionNode) throws XPathExpressionException{
		Node descriptionNode = (Node) descriptionExpression.evaluate(cpfDescriptionNode, XPathConstants.NODE);


		String oneDate = removeUnusedCharacters((String) oneDateExpression.evaluate(descriptionNode, XPathConstants.STRING));
		String oneDateNormal = removeUnusedCharacters((String) oneDateNormalExpression.evaluate(descriptionNode, XPathConstants.STRING));
		if (StringUtils.isBlank(oneDate) && StringUtils.isBlank(oneDateNormal)){
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
//			if ("0001".equals(fromDate)){
//				fromDate = null;
//				eacCpfSolrObject.setFromDateExist(false);
//			}else {
				eacCpfSolrObject.setFromDateExist(true);
			//}
//			if ("2099".equals(toDate)){
//				toDate = null;
//				eacCpfSolrObject.setToDateExist(false);
//			}else {
				eacCpfSolrObject.setToDateExist(true);
			//}
			if (toDate == null && fromDate != null){
				toDate = fromDate;
			}
			if (fromDate == null && toDate != null){
				fromDate = toDate;
			}
			eacCpfSolrObject.setFromDate(obtainDate(fromDate, true));
			eacCpfSolrObject.setToDate(obtainDate(toDate, false));
		}else {
			eacCpfSolrObject.setDateDescription(oneDate);
			eacCpfSolrObject.setFromDate(obtainDate(oneDateNormal, true));
			eacCpfSolrObject.setToDate(obtainDate(oneDateNormal, false));		
			eacCpfSolrObject.setFromDateExist(true);
			eacCpfSolrObject.setToDateExist(true);
		}



	}

	public void publishEacCpf(EacCpf eacCpf, EacCpfPublishData eacCpfPublishData) throws MalformedURLException, SolrServerException, IOException {

		SolrInputDocument doc = new SolrInputDocument();
		doc.addField(SolrFields.ID, eacCpf.getId());
		add(doc,SolrFields.EAC_CPF_RECORD_ID, eacCpf.getIdentifier());
		addLowerCase(doc,SolrFields.EAC_CPF_FACET_ENTITY_TYPE, eacCpfPublishData.getEntityType());
		doc.addField(SolrFields.EAC_CPF_ENTITY_ID, eacCpfPublishData.getEntityIds());
		doc.addField(SolrFields.EAC_CPF_NAMES, eacCpfPublishData.getNames());
		doc.addField(SolrFields.EAC_CPF_PLACES, eacCpfPublishData.getPlaces());
		doc.addField(SolrFields.EAC_CPF_OCCUPATION,eacCpfPublishData.getOccupations());
		doc.addField(SolrFields.EAC_CPF_FACET_FUNCTION, eacCpfPublishData.getFunctions());
		doc.addField(SolrFields.EAC_CPF_FACET_MANDATE,eacCpfPublishData.getMandates());		
		add(doc, SolrFields.EAC_CPF_DESCRIPTION,eacCpfPublishData.getDescription());
//		add(doc, SolrFields.START_DATE, eacCpfPublishData.getFromDate());
//		add(doc, SolrFields.END_DATE, eacCpfPublishData.getToDate());
//		add(doc, SolrFields.EAC_CPF_DATE_DESCRIPTION, eacCpfPublishData.getDateDescription());
//		if (StringUtils.isBlank(eacCpfPublishData.getDateDescription())) {
//			add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
//		} else {
//			if (StringUtils.isBlank(eacCpfPublishData.getFromDate()) && StringUtils.isBlank(eacCpfPublishData.getToDate())) {
//				add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_OTHER_DATE);
//			} else {
//				if (!eacCpfPublishData.isToDateExist()){
//					add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED_UNKNOWN_ENDDATE);
//				}else if (!eacCpfPublishData.isFromDateExist()){
//					add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED_UNKNOWN_STARTDATE);
//				}else {
//					add(doc, SolrFields.DATE_TYPE, SolrValues.DATE_TYPE_NORMALIZED);
//				}
//			}
//		}
		ArchivalInstitution archivalInstitution = eacCpf.getArchivalInstitution();
		add(doc, SolrFields.COUNTRY, archivalInstitution.getCountry().getEncodedCname() + COLON + SolrValues.TYPE_GROUP + COLON + archivalInstitution.getCountry().getId());
		doc.addField(SolrFields.COUNTRY_ID, archivalInstitution.getCountry().getId());
		add(doc, SolrFields.LANGUAGE, eacCpfPublishData.getLanguage());

		add(doc, SolrFields.AI, archivalInstitution.getAiname() + COLON + archivalInstitution.getAiId());
		doc.addField(SolrFields.AI_ID, archivalInstitution.getAiId());
		add(doc, SolrFields.OTHER,eacCpfPublishData.getOther());
		doc.addField(SolrFields.REPOSITORY_CODE, archivalInstitution.getRepositorycode());
		doc.addField(SolrFields.EAC_CPF_NUMBER_OF_MATERIAL_RELATIONS, eacCpfPublishData.getNumberOfArchivalMaterialRelations());
		doc.addField(SolrFields.EAC_CPF_NUMBER_OF_NAME_RELATIONS, eacCpfPublishData.getNumberOfNameRelations());
		doc.addField(SolrFields.EAC_CPF_NUMBER_OF_INSTITUTIONS_RELATIONS, eacCpfPublishData.getNumberOfInstitutionsRelations());
		addSolrDocument(doc);
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
		return EacCpfSolrServerHolder.getInstance();
	}
	public long unpublish(EacCpf eacCpf) throws SolrServerException, IOException {
		return getSolrServerHolder().deleteByQuery("(" + SolrFields.AI_ID + ":" + eacCpf.getAiId() + " AND " + SolrFields.ID + ":\"" + eacCpf.getId() + "\")");
	}
}
