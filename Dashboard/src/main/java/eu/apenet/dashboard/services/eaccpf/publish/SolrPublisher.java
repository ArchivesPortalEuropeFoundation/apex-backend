package eu.apenet.dashboard.services.eaccpf.publish;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
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

import eu.apenet.commons.solr.SolrFields;
import eu.apenet.commons.solr.SolrValues;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.dashboard.services.AbstractSolrPublisher;
import eu.apenet.dashboard.services.eaccpf.xml.EacCpfNamespaceContext;

public class SolrPublisher  extends AbstractSolrPublisher{



	private static final Logger LOGGER = Logger.getLogger(SolrPublisher.class);
	public static final DecimalFormat NUMBERFORMAT = new DecimalFormat("00000000");

	private final static XPath XPATH = APEnetUtilities.getDashboardConfig().getXpathFactory().newXPath();
	private static XPathExpression recordIdExpression;
	private static XPathExpression agencyCodeExpression;
	private static XPathExpression agencyNameExpression;
	private static XPathExpression nameExpression;
	private static XPathExpression placeExpression;
	private static XPathExpression descriptionExpression;
	private static XPathExpression fromDateExpression;
	private static XPathExpression toDateExpression;
	private static XPathExpression fromDateNormalExpression;
	private static XPathExpression toDateNormalExpression;
	private static XPathExpression occupationExpression;
	private static XPathExpression bioghistExpression;
	private String recordId;
	static {
		try {
			XPATH.setNamespaceContext(new EacCpfNamespaceContext());
			recordIdExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:recordId");
			agencyCodeExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyCode");
			agencyNameExpression = XPATH.compile("/eac:eac-cpf/eac:control/eac:maintenanceAgency/eac:agencyName");
			nameExpression = XPATH.compile("/eac:eac-cpf/eac:cpfDescription/eac:identity/eac:nameEntry/eac:part");
			descriptionExpression = XPATH.compile("/eac:eac-cpf/eac:cpfDescription/eac:description");
			placeExpression = XPATH.compile("./eac:places/eac:place/eac:placeEntry");
			fromDateExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:fromDate");
			toDateExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:toDate");
			fromDateNormalExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:fromDate/@standardDate");
			toDateNormalExpression = XPATH.compile("./eac:existDates/eac:dateRange/eac:toDate/@standardDate");
			occupationExpression = XPATH.compile("./eac:occupations/eac:occupation/eac:term");
			bioghistExpression = XPATH.compile("./eac:biogHist//text()");
		} catch (XPathExpressionException e) {
			LOGGER.info(e.getMessage(), e);
		}
	}

	public void parse(File file) throws Exception{
		LOGGER.info(file.getName());
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);		
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new InputSource(new FileReader(file)));
		doc.getDocumentElement().normalize();
		EacCpfSolrObject eacCpfSolrObject = new EacCpfSolrObject();
		eacCpfSolrObject.setRecordId((String) recordIdExpression.evaluate(doc, XPathConstants.STRING));
		eacCpfSolrObject.setAgencyCode((String) agencyCodeExpression.evaluate(doc, XPathConstants.STRING));
		eacCpfSolrObject.setAgencyName(removeUnusedCharacters((String) agencyNameExpression.evaluate(doc, XPathConstants.STRING)));
		Node descriptionNode = (Node) descriptionExpression.evaluate(doc, XPathConstants.NODE);
		NodeList nameNodeList = (NodeList) nameExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		eacCpfSolrObject.setNames(getTextsWithoutMultiplity(nameNodeList));
		NodeList placesNodeList = (NodeList) placeExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		eacCpfSolrObject.setPlaces(getTextsWithoutMultiplity(placesNodeList));
		NodeList occupationsNodeList = (NodeList) occupationExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		
		eacCpfSolrObject.setOccupations(getTextsWithoutMultiplity(occupationsNodeList));
		
		String description = getText((NodeList) bioghistExpression.evaluate(descriptionNode, XPathConstants.NODESET));
		eacCpfSolrObject.setDescription(description);
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
		publishEacCpf(eacCpfSolrObject);

	}
	

	private void publishEacCpf(EacCpfSolrObject eacCpfSolrObject) throws MalformedURLException, SolrServerException, IOException {

		SolrInputDocument doc = new SolrInputDocument();
		add(doc, SolrFields.ID, eacCpfSolrObject.getRecordId());
		doc.addField(SolrFields.EAC_CPF_NAMES, eacCpfSolrObject.getNames());
		doc.addField(SolrFields.EAC_CPF_PLACES, eacCpfSolrObject.getPlaces());
		doc.addField(SolrFields.AI, eacCpfSolrObject.getAgencyName());
		doc.addField(SolrFields.EAC_CPF_FACET_PLACES,eacCpfSolrObject.getPlaces());
		doc.addField(SolrFields.EAC_CPF_FACET_OCCUPATION,eacCpfSolrObject.getOccupations());
		doc.addField(SolrFields.EAC_CPF_OCCUPATION,eacCpfSolrObject.getOccupations());
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
		addSolrDocument(doc);
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
				results.add(text);
			}
		}
		return results;
	}
}
