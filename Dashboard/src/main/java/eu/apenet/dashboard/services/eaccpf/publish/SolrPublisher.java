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

import eu.apenet.commons.solr.eads.EadSolrFields;
import eu.apenet.commons.solr.eads.EadSolrValues;
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
		Set<String> names = getTextsWithoutMultiplity(nameNodeList);
		eacCpfSolrObject.setNames(names);
		NodeList placesNodeList = (NodeList) placeExpression.evaluate(descriptionNode, XPathConstants.NODESET);
		Set<String>  places = getTextsWithoutMultiplity(placesNodeList);
		eacCpfSolrObject.setPlaces(places);
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
		add(doc, EadSolrFields.ID, eacCpfSolrObject.getRecordId());
		doc.addField("names", eacCpfSolrObject.getNames());
		doc.addField("places", eacCpfSolrObject.getPlaces());
		doc.addField("aiRefinement", eacCpfSolrObject.getAgencyName());
		doc.addField("placesRefinement",eacCpfSolrObject.getPlaces());
		add(doc, EadSolrFields.START_DATE, eacCpfSolrObject.getFromDate());
		add(doc, EadSolrFields.END_DATE, eacCpfSolrObject.getToDate());
		add(doc, "dateDescription", eacCpfSolrObject.getDateDescription());
		if (StringUtils.isBlank(eacCpfSolrObject.getDateDescription())) {
			add(doc, EadSolrFields.DATE_TYPE, EadSolrValues.DATE_TYPE_NO_DATE_SPECIFIED);
		} else {
			if (StringUtils.isBlank(eacCpfSolrObject.getFromDate())) {
				add(doc, EadSolrFields.DATE_TYPE, EadSolrValues.DATE_TYPE_OTHER_DATE);
			} else {
				add(doc, EadSolrFields.DATE_TYPE, EadSolrValues.DATE_TYPE_NORMALIZED);
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
