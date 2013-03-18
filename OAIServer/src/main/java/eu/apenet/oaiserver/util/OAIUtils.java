package eu.apenet.oaiserver.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.EseState;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class OAIUtils {
	//This part has the standard response description and never should be change. 
	private static final String BAD_VERB = "Value of the verb argument is not a legal OAI-PMH verb, the verb argument is missing, or the verb argument is repeated.";
	private static final String BAD_ARGUMENT = "The request includes illegal arguments, is missing required arguments, includes a repeated argument, or values for arguments have an illegal syntax.";
	private static final String BAD_RESUMPTION_TOKEN = "The value of the resumptionToken argument is invalid or expired.";
	private static final String CANNOT_DISSEMINATE_FORMAT = "The metadata format identified by the value given for the metadataPrefix argument is not supported by the item or by the repository.";
	private static final String ID_DOES_NOT_EXIST = "The value of the identifier argument is unknown or illegal in this repository.";
	private static final String NO_RECORDS_MATCH = "The combination of the values of the from, until, set and metadataPrefix arguments results in an empty list.";
	private static final String NO_METADATA_FORMATS = "There are no metadata formats available for the specified item.";
	private static final String NO_SET_HIERARCHY = "The repository does not support sets.";
	private static final String DC_PREFIX = "dc";
	private static final String DCTERMS_PREFIX = "dcterms";
	private static final String EUROPEANA_PREFIX = "europeana";
	private static final String IDENTIFIER_ATTRIBUTE = "identifier";
	private static final String METADATAPREFIX_ATTRIBUTE = "metadataPrefix";
	private static final String FROM_ATTRIBUTE = "from";
	private static final String UNTIL_ATTRIBUTE = "until";
	private static final String SET_ATTRIBUTE = "set";
	public static final String VERB = "verb";
	private static final String RESUMPTIONTOKEN_ATTRIBUTE = "resumptionToken";
	private static final long EXPIRATION_TIME_IN_MILLISECONDS = 1000*60*10; //10 minutes
	public static final String SPECIAL_KEY = "-";
	private static Logger LOG = Logger.getLogger(OAIUtils.class);
	public final static String XSI_SCHEMALOCATION = "http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd";
	public final static String ESE_SCHEMA_LOCATION_FILE = "http://www.europeana.eu/schemas/ese/ESE-V3.3.xsd";
	public static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	private static final String ESE_SCHEMA_LOCATION = "http://www.europeana.eu/schemas/ese/";
	public static final String ESE_METADATA_NAMESPACE = "http://www.europeana.eu/schemas/ese/";
	public static final String DC_METADATA_NAMESPACE = "http://purl.org/dc/elements/1.1/";
	public static final String DCTERMS_SCHEMA_LOCATION = "http://purl.org/dc/terms/";
	public static final String BRANDING_SCHEMA_LOCATION = "http://www.openarchives.org/OAI/2.0/branding/";
	public static final String RIGHTS_SCHEMA_LOCATION = "http://www.openarchives.org/OAI/2.0/rights/";
	public static final String OAIDC_SCHEMA_LOCATION = "http://www.openarchives.org/OAI/2.0/oai_dc/";
	public static final String DUBLINCORE_SCHEMA_LOCATION_FILE = "http://www.dublincore.org/schemas/xmls/qdc/dc.xsd";
	public static final String DCTERMS_SCHEMA_LOCATION_FILE = "http://www.dublincore.org/schemas/xmls/qdc/dcterms.xsd";
	public static final String BRANDING_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/branding.xsd";
	public static final String RIGHTS_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/rightsManifest.xsd";
	public static final String OAIDC_SCHEMA_LOCATION_FILE = "http://www.openarchives.org/OAI/2.0/oai_dc.xsd";
	
	
	
	/**
	 * Provides the node filling that has to returns the error which has happened in
	 * the system. The possibles error are: badArgument, badResumptionToken, badVerb, cannotDisseminateFormat,
	 * idDoesNotExist, noRecordMatch, noMetadataFormats and noSetHierarchy.
	 * The node structure is similar to:  
	 * <error code="ERROR_CODE">ERROR_MESSAGE</error>
	 * 
	 * @param doc 
	 * @param doc
	 * @return Document
	 * @throws ParseException 
	 * @throws ParserConfigurationException 
	 * @throws DOMException 
	 */
	public static Document getResponseError(Document doc,String code) throws DOMException, ParserConfigurationException, ParseException {
		Element errorNode = doc.createElementNS(OAIResponse.NAMESPACE,"error");
		errorNode.setAttribute("code",code);
		if(code.equals("badVerb")){
			errorNode.setTextContent(OAIUtils.BAD_VERB);
		}else if(code.equals("badArgument")){
			errorNode.setTextContent(OAIUtils.BAD_ARGUMENT);
		}else if(code.equals("badResumptionToken")){
			errorNode.setTextContent(OAIUtils.BAD_RESUMPTION_TOKEN);
		}else if(code.equals("cannotDisseminateFormat")){
			errorNode.setTextContent(OAIUtils.CANNOT_DISSEMINATE_FORMAT);
		}else if(code.equals("idDoesNotExist")){
			errorNode.setTextContent(OAIUtils.ID_DOES_NOT_EXIST);
		}else if(code.equals("noRecordsMatch")){
			errorNode.setTextContent(OAIUtils.NO_RECORDS_MATCH);
		}else if(code.equals("noMetadataFormats")){
			errorNode.setTextContent(OAIUtils.NO_METADATA_FORMATS);
		}else if(code.equals("noSetHierarchy")){
			errorNode.setTextContent(OAIUtils.NO_SET_HIERARCHY);
		}
		Element mainElement = (Element) doc.getElementsByTagName("OAI-PMH").item(0);
		mainElement.appendChild(errorNode);
		return doc;
	}
	
	/**
	 * Parses a DOM document to an inputStream to be returned without stores it in a 
	 * physical file.
	 * 
	 * @param doc
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream getInputStreamOfDocument(Document doc) throws Exception{	
		try {
			DOMSource source = new DOMSource(doc);
	        StringWriter sw = new StringWriter();
	        StreamResult result = new StreamResult(sw);
	        TransformerFactory tFactory = TransformerFactory.newInstance();
	        Transformer transformer = tFactory.newTransformer(); 
	        transformer.transform(source, result);
			return new ByteArrayInputStream(sw.toString().getBytes("UTF-8"));
		}catch(Exception e){
			LOG.error("Error trying to parse DOM document to InputStream: "+e.getCause());
			throw e;
		}
	}

	
	/**
	 * Builds a resumptionToken Node to be used in the next petition, it has to have this structure:
	 * from/until/set/metadata_prefix/last_number_sent
	 * ResumptionToken Node is added to a main Document. 
	 * 
	 * @param verb
	 * @param doc
	 * @param arguments
	 * @return Document
	 */
	public static Document buildResumptionToken(Document doc,Map<String,String> arguments,String limit){
		Element resumptionTokenNode = doc.createElementNS(OAIResponse.NAMESPACE, "resumptionToken");
		Date expirationDate = new Date();
		expirationDate = new Date(expirationDate.getTime()+OAIUtils.EXPIRATION_TIME_IN_MILLISECONDS);
		resumptionTokenNode.setAttribute("expirationDate",OAIUtils.parseDateToISO8601(expirationDate));
		String resumptionToken = arguments.get("resumptionToken");
		String from = null;
		String until = null;
		String set = null;
		String metadataPrefix = null;
		if(resumptionToken==null || resumptionToken.length()<=5){
			from = arguments.get("from");
			until = arguments.get("until");
			set = arguments.get("set");
			metadataPrefix = arguments.get("metadataPrefix");
		}else{
			String[] params = resumptionToken.split("/");
			from = params[0];
			until = params[1];
			set = params[2];
			metadataPrefix = params[3];
		}
		if(from==null){
			from = "0001-01-01T00:00:00Z";
		}
		if(until==null){
			until = "9999-12-31T23:59:59Z";
		}
		if(set==null){
			set = "";
		}
		if(metadataPrefix==null){
			metadataPrefix = "";
		}
		resumptionToken = null;
		try {
			try{//DDBB part
				ResumptionTokenDAO daoResumptionToken = DAOFactory.instance().getResumptionTokenDAO();
				ResumptionToken resToken = new ResumptionToken();
				resToken.setFromDate(OAIUtils.parseStringToISO8601Date(from));
				resToken.setUntilDate(OAIUtils.parseStringToISO8601Date(until));
				if(set!=null && !set.isEmpty()){
					resToken.setSet(set.trim());
				}
				resToken.setLastRecordHarvested(limit.toString());
				resToken.setExpirationDate(expirationDate); 
				MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(metadataPrefix);
				resToken.setMetadataFormat(metadataFormat);
				daoResumptionToken.store(resToken);//End DDBB part
			}catch(Exception e){
				LOG.error("Error trying to save the resumptionToken value in DDBB:",e);
			}
			resumptionToken = from+"/"+until+"/"+set+"/"+metadataPrefix+"/"+limit.toString();
			resumptionTokenNode.setTextContent(resumptionToken);
			doc.getDocumentElement().getLastChild().appendChild(resumptionTokenNode);
		} catch (Exception e) {
			LOG.error("Error trying to obtain the last identifier to be stored in ddbb (building resumptionToken):"+e.getCause());
		} finally{
			OAIUtils.removeOldResumptionTokens();
		}
		return doc;
	}

	/**
	 * Deletes all values in DDBB that has never been used again.
	 */
	public static void removeOldResumptionTokens() {
		ResumptionTokenDAO resumptionTokenDao = DAOFactory.instance().getResumptionTokenDAO();
		List<ResumptionToken> resumptionTokenList = resumptionTokenDao.getOldResumptionTokensThan(new Date());
		Iterator<ResumptionToken> iterator = resumptionTokenList.iterator();
		while(iterator.hasNext()){
			resumptionTokenDao.delete(iterator.next());
		}
	}
	
	/**
	 * This method parse a ISO8601 date to a Date Object
	 * @param isoStringDate
	 * @return Date
	 * @throws ParseException
	 */
	public static Date parseStringToISO8601Date(String isoStringDate) throws ParseException {
		SimpleDateFormat df = null;
		if (isoStringDate.contains("Z")) {
			//Time granularity
			df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");			
		}
		else {
			//Only day granularity
			df = new SimpleDateFormat("yyyy-MM-dd");						
		}
		TimeZone tz = TimeZone.getTimeZone("Zulu");
        df.setTimeZone(tz);
        Date date = df.parse(isoStringDate);
        return date;
	}
	
	/**
	 * It is a toString method which parses a date to ISO8601 format
	 * @param date
	 * @return String
	 */
	public static String parseDateToISO8601(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        TimeZone tz = TimeZone.getTimeZone("Zulu");
        df.setTimeZone(tz);
        String output = df.format(date);
        return output;
    }
	
	/**
	 * This function generates a new instance of DocumentBuilder 
	 * and returns it
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException
	 */
	public static DocumentBuilder buildDocument() throws ParserConfigurationException{
		DocumentBuilderFactory factory = null;
		try{
			factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			factory.setAttribute("http://apache.org/xml/features/validation/schema",false);
			factory.setAttribute("http://apache.org/xml/features/validation/schema-full-checking",false);
			factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage","http://www.w3.org/2001/XMLSchema");
			factory.setIgnoringElementContentWhitespace(true);
		}catch(Exception e){
			LOG.error("Error trying to instance a new Document",e);
		}
		return factory.newDocumentBuilder();
	}
	
	/**
	 * Returns the response with empty document with only OAI-PMH and responseDate nodes
	 * @throws ParserConfigurationException
	 * @throws DOMException
	 * @throws ParseException
	 */
	public static Document getEmptyDocument() throws ParserConfigurationException, DOMException, ParseException{
		Document doc = OAIUtils.buildDocument().newDocument();
		Element mainNode = doc.createElementNS(OAIResponse.NAMESPACE,"OAI-PMH");
		doc.setXmlVersion("1.0");
		mainNode.setAttribute("xmlns:xsi",OAIUtils.XMLNS_XSI);
		mainNode.setAttribute("xsi:schemaLocation",OAIUtils.XSI_SCHEMALOCATION);
		Node responseDate = doc.createElementNS(OAIResponse.NAMESPACE, "responseDate");
		responseDate.setTextContent(OAIUtils.parseDateToISO8601(new Date()));
		mainNode.appendChild(responseDate);
		doc.appendChild(mainNode);
		return doc;
	}
	
	/**
	 * Returns records elements from an Set<Ese> and parse them to a real format 
	 * OAI-PMH metadataFormat="VALID_METADATA_FORMAT"; builded all in the node record.
	 * It has two uses which are controlled with identifier parameter; When identifier 
	 * has a value it's used in the getRecord verb logic and when identifier is 'null'
	 * is used in ListRecords verb logic.
	 * 
	 * @param esePath
	 * @return NodeList
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 */
	public static Node getRecordOfEse(Set<Ese> eses,String identifier,String metadataPrefix,Document doc) 
			throws SAXException, IOException, ParserConfigurationException{
		
		Node listIdentifierNode = null;
		boolean found = false;
		Iterator<Ese> eseIterator = eses.iterator();
		if(!found && eseIterator.hasNext()){
			Ese ese = eseIterator.next();
			if(ese.getMetadataFormat().equals(MetadataFormat.getMetadataFormat(metadataPrefix)) && ese.getEseState().getState().equals(EseState.PUBLISHED)){
				try{
					XMLInputFactory factory = XMLInputFactory.newFactory();
					//XMLStreamReader r = factory.createXMLStreamReader(new FileReader(APEnetUtilities.getConfig().getRepoDirPath()+ese.getPath()));
					InputStream inputStream= new FileInputStream(new File(APEnetUtilities.getConfig().getRepoDirPath()+ese.getPath()));
					XMLStreamReader r = factory.createXMLStreamReader(new InputStreamReader(inputStream,"UTF-8"));
					if(identifier==null){
						listIdentifierNode = doc.createElementNS(OAIResponse.NAMESPACE,"ListRecords");
					}
					String nodeName = null;
					String prefix = null;
					String text = null;
					Node recordNode = doc.createElementNS(OAIResponse.NAMESPACE, "record");
					Element nodeHeadPart = null;
					Element nodeBodyPart = null;
					nodeBodyPart = doc.createElementNS(OAIUtils.ESE_SCHEMA_LOCATION,"europeana:record");
					nodeBodyPart.setAttribute("xmlns:dc",OAIUtils.DC_METADATA_NAMESPACE);
					nodeBodyPart.setAttribute("xmlns:dcterms",OAIUtils.DCTERMS_SCHEMA_LOCATION);
					int i=0;
					while(r.hasNext() || listIdentifierNode==null ) {
						Integer event = r.next();
						Node nodeBodyPartialPart = null;
						switch (event) {
							case XMLStreamConstants.START_ELEMENT:
								nodeName = r.getLocalName();
								if(nodeName.equals("identifier")){
									nodeHeadPart = doc.createElementNS(OAIResponse.NAMESPACE, "header");
									if(ese.getEseState().getState().equals(EseState.REMOVED)){
										nodeHeadPart.setAttribute("status", "deleted");
									}
									i++;
								}
								prefix = r.getName().getPrefix();
								text = "";
								break;
							case XMLStreamConstants.END_ELEMENT:
								if (r.getLocalName().trim().equals("record")){
									if(identifier==null || identifier.trim().equals((ese.getOaiIdentifier()+SPECIAL_KEY+i))) {
										if(nodeBodyPart!=null && nodeBodyPart.hasChildNodes()){
											recordNode.appendChild(nodeHeadPart);
											Element metadataNode = doc.createElementNS(OAIResponse.NAMESPACE, "metadata");
											metadataNode.setAttribute("xmlns",OAIUtils.ESE_SCHEMA_LOCATION);
											metadataNode.setAttribute("xmlns:xsi",OAIUtils.XMLNS_XSI);											
											metadataNode.setAttribute("xmlns:dc",OAIUtils.DC_METADATA_NAMESPACE);
											metadataNode.setAttribute("xmlns:europeana",OAIUtils.ESE_SCHEMA_LOCATION);
											metadataNode.setAttribute("xmlns:dcterms",OAIUtils.DCTERMS_SCHEMA_LOCATION);
											metadataNode.setAttribute("xsi:schemaLocation",OAIUtils.ESE_SCHEMA_LOCATION + " " + ESE_SCHEMA_LOCATION_FILE + " " + DC_METADATA_NAMESPACE + " " + DUBLINCORE_SCHEMA_LOCATION_FILE + " " + DCTERMS_SCHEMA_LOCATION + " " + DCTERMS_SCHEMA_LOCATION_FILE);
											if(listIdentifierNode!=null && nodeBodyPart!=null && nodeBodyPart.hasChildNodes() && !ese.getEseState().getState().equals(EseState.REMOVED)){
												if(!ese.getEseState().getState().equals(EseState.REMOVED)){
													metadataNode.appendChild(nodeBodyPart);
													recordNode.appendChild(metadataNode);
												}
											}
										}
										if(listIdentifierNode!=null && recordNode.hasChildNodes() && (nodeBodyPart.hasChildNodes() || ese.getEseState().getState().equals(EseState.REMOVED))){
											listIdentifierNode.appendChild(recordNode);
											recordNode = doc.createElementNS(OAIResponse.NAMESPACE, "record");
											if(!ese.getEseState().getState().equals(EseState.REMOVED)){
												nodeBodyPart = doc.createElementNS(OAIUtils.ESE_SCHEMA_LOCATION, "europeana:record");
												nodeBodyPart.setAttribute("xmlns:dc",OAIUtils.DC_METADATA_NAMESPACE);
												nodeBodyPart.setAttribute("xmlns:dcterms",OAIUtils.DCTERMS_SCHEMA_LOCATION);
											}
										}
									}
									if(found){ //GetRecord has finished
										if(!ese.getEseState().getState().equals(EseState.REMOVED)){
											Node metadataNode = doc.createElementNS(OAIResponse.NAMESPACE, "metadata");
											metadataNode.appendChild(nodeBodyPart);
											recordNode.appendChild(metadataNode);
										}
										r.close();
										return recordNode;
									}
								}else if(nodeName!=null && !nodeName.trim().isEmpty()){
									nodeBodyPartialPart = null;
									if(prefix!=null && !prefix.isEmpty()){
										if (prefix.equals(DC_PREFIX)) {
											nodeBodyPartialPart = doc.createElementNS(OAIUtils.DC_METADATA_NAMESPACE, prefix+":"+nodeName);
										}
										else if (prefix.equals(DCTERMS_PREFIX)) {
											nodeBodyPartialPart = doc.createElementNS(OAIUtils.DCTERMS_SCHEMA_LOCATION, prefix+":"+nodeName);											
										}
										else if (prefix.equals(EUROPEANA_PREFIX)) {
											nodeBodyPartialPart = doc.createElementNS(OAIUtils.ESE_SCHEMA_LOCATION, prefix+":"+nodeName);											
										}
									}else{
										nodeBodyPartialPart = doc.createElementNS(OAIResponse.NAMESPACE, nodeName);
									}
									if(nodeBodyPartialPart!=null && nodeBodyPart!=null && text!=null && (listIdentifierNode!=null || !text.trim().isEmpty() && identifier.trim().equals((ese.getOaiIdentifier()+OAIUtils.SPECIAL_KEY+i)))){
										nodeBodyPartialPart.setTextContent(text);
										nodeBodyPart.appendChild(nodeBodyPartialPart);
									}
								}
								break;
							case XMLStreamConstants.CHARACTERS:
								text += r.getText();
								if (nodeName!=null && nodeName.endsWith("identifier") && !nodeHeadPart.hasChildNodes()){
									if(identifier!=null && identifier.trim().equals((ese.getOaiIdentifier()+OAIUtils.SPECIAL_KEY+i))){
										found = true;
									}
									Node identifierNode = doc.createElementNS(OAIResponse.NAMESPACE, "identifier");
									identifierNode.setTextContent(ese.getOaiIdentifier()+OAIUtils.SPECIAL_KEY+i);
									nodeHeadPart.appendChild(identifierNode);
									Node nodeDatestamp = doc.createElementNS(OAIResponse.NAMESPACE, "datestamp");
									Date eseDate = ese.getModificationDate();
									nodeDatestamp.setTextContent(OAIUtils.parseDateToISO8601(eseDate));
									nodeHeadPart.appendChild(nodeDatestamp);
									Node nodeSetSpec = doc.createElementNS(OAIResponse.NAMESPACE, "setSpec");
									nodeSetSpec.setTextContent(ese.getEset());
									nodeHeadPart.appendChild(nodeSetSpec);
								}
						}
					}
					r.close();
				}catch(Exception exception){
					LOG.error("CanNOT 'parse' an Ese, identifier: "+identifier+" -- "+exception.getCause());
				}
			}
			else if(ese.getMetadataFormat().equals(MetadataFormat.getMetadataFormat(metadataPrefix)) && ese.getEseState().getState().equals(EseState.REMOVED)){
				
				if (identifier == null) {
					// ListRecords part
					listIdentifierNode = doc.createElementNS(OAIResponse.NAMESPACE,"ListRecords");
					
					for (int i = 1; i < ese.getNumberOfRecords() + 1; i ++) {
						listIdentifierNode.appendChild(createSingleDeletedRecord (ese.getOaiIdentifier() + OAIUtils.SPECIAL_KEY + i, doc, ese.getEset(), ese.getModificationDate()));
					}
					return listIdentifierNode;
				}
				else {
					// GetRecord part
					Integer recordNumber = Integer.valueOf(identifier.substring(identifier.lastIndexOf(OAIUtils.SPECIAL_KEY) + 1));
					if ( recordNumber <= ese.getNumberOfRecords()) {
						// The record exists
						return createSingleDeletedRecord (identifier, doc, ese.getEset(), ese.getModificationDate());
					}
					else {
						// The record is out of bounds
						return null;
					}
					
				}
			}

		}
		
		return listIdentifierNode;
	}

	/**
	 * This method validates all the possible attributes from an OAI-PMH request
	 * 
	 * Possible attributes:
	 * identifier -> String (no restrictions)
	 * metadataPrefix -> String (no restrictions)
	 * from -> UTCdatetime (ISO8601 standard. Two possible patterns: YYYY-MM-DD OR YYYY-MM-DDThh:mm:ssZ)
	 * until -> UTCdatetime (ISO8601 standard)
	 * set -> Pattern ([A-Za-z0-9\-_\.!~\*'\(\)])+(:[A-Za-z0-9\-_\.!~\*'\(\)]+)*
	 * resumptionToken -> String (no restriction)
	 * 
	 * @param attributes
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	public static boolean validateRequestAttributes(Map<String, String> attributes) {
		boolean result = true;
		String value = null;
		Iterator it = attributes.entrySet().iterator();
		
		while (result && it.hasNext()) {
			Map.Entry entry = (Map.Entry)it.next();

			// identifier, metadataPrefix and resumptionToken attributes won't be validated because they are Strings with no restrictions
			if (entry.getKey().equals(IDENTIFIER_ATTRIBUTE) || entry.getKey().equals(METADATAPREFIX_ATTRIBUTE) || entry.getKey().equals(RESUMPTIONTOKEN_ATTRIBUTE)) {
				LOG.debug("The attribute " + entry.getKey() + " doesn't need to be validated");
			}
			else {
				if (entry.getKey().equals(FROM_ATTRIBUTE)) {
					value = attributes.get(FROM_ATTRIBUTE);
					if (!validateFromUntilAttribute(value)){
						LOG.info("The attribute " + entry.getKey() + " with value " + entry.getValue() + " doesn't have a proper format");
						result = false;
					}
					
				}
				if (entry.getKey().equals(UNTIL_ATTRIBUTE)) {
					value = attributes.get(UNTIL_ATTRIBUTE);
					if (!validateFromUntilAttribute(value)){
						LOG.info("The attribute " + entry.getKey() + " with value " + entry.getValue() + " doesn't have a proper format");
						result = false;
					}
					
				}
				if (entry.getKey().equals(SET_ATTRIBUTE)) {
					value = attributes.get(SET_ATTRIBUTE);
					if (!validateSetAttribute(value)){
						LOG.info("The attribute " + entry.getKey() + " with value " + entry.getValue() + " doesn't have a proper format");
						result = false;
					}
					
				}
			}
		}
		
		return result;
	}

	
	/**
	 * This method validates the value of from or until attributes against two possible patterns: YYYY-MM-DD and YYYY-MM-DDThh:mm:ssZ 
	 * 
	 * @param value
	 * @return boolean
	 */
	private static boolean validateFromUntilAttribute(String value) {

		boolean result = true;
		Pattern pattern = null;
		Matcher matcher = null;
		
		// YYYY-MM-DD pattern
		pattern = Pattern.compile("^([0-9]{4})[- /.](0[1-9]|1[012])[- /.](0[1-9]|[12][0-9]|3[01])$");
	    matcher = pattern.matcher(value);
	    
	    if (!matcher.matches()) {
	    	// Try matching YYYY-MM-DDThh:mm:ssZ pattern
			pattern = Pattern.compile("^([0-9]{4})(-(?:1[0-2]|0?[1-9])(-(?:3[01]|[12][0-9]|0[1-9])?)?)([T]+((?:2[0-3]|[01][0-9])(:(?:[0-5][0-9])(:(?:[0-5][0-9])?)?)?)[Z]$)?");
		    matcher = pattern.matcher(value);
		    result = matcher.matches();
	    }
	    
	    return result;
	}
	
	/**
	 * This method validates the value of set attribute against the setSpec pattern: ([A-Za-z0-9\-_\.!~\*'\(\)])+(:[A-Za-z0-9\-_\.!~\*'\(\)]+)* 
	 * 
	 * @param value
	 * @return boolean
	 */
	private static boolean validateSetAttribute(String value) {

		boolean result = true;
		Pattern pattern = null;
		Matcher matcher = null;
		
		// ([A-Za-z0-9\-_\.!~\*'\(\)])+(:[A-Za-z0-9\-_\.!~\*'\(\)]+)* pattern
		pattern = Pattern.compile("([A-Za-z0-9\\-_\\.!~\\*'\\(\\)])+(:[A-Za-z0-9\\-_\\.!~\\*'\\(\\)]+)*");
	    matcher = pattern.matcher(value);
	    result = matcher.matches();
	    
	    return result;
	}
	
	/**
	 * This method builds a deleted record to be displayed in a ListRecords or GetRecord OAI-PMH request
	 * @param identifier Record's identifier
	 * @param doc
	 * @param set ESE's set
	 * @param eseDate ESE's modification date
	 * @return A deleted record from OAI-PMH repository
	 */
	private static Node createSingleDeletedRecord (String identifier, Document doc, String set, Date eseDate) {
		
		Node recordNode = doc.createElementNS(OAIResponse.NAMESPACE, "record");
		Element nodeHeadPart = doc.createElementNS(OAIResponse.NAMESPACE, "header");;
		nodeHeadPart.setAttribute("status", "deleted");
		Node identifierNode = doc.createElementNS(OAIResponse.NAMESPACE, "identifier");
		identifierNode.setTextContent(identifier);
		nodeHeadPart.appendChild(identifierNode);
		Node nodeDatestamp = doc.createElementNS(OAIResponse.NAMESPACE, "datestamp");
		nodeDatestamp.setTextContent(OAIUtils.parseDateToISO8601(eseDate));
		nodeHeadPart.appendChild(nodeDatestamp);
		Node nodeSetSpec = doc.createElementNS(OAIResponse.NAMESPACE, "setSpec");
		nodeSetSpec.setTextContent(set);
		nodeHeadPart.appendChild(nodeSetSpec);
		recordNode.appendChild(nodeHeadPart);
		
		return recordNode;
	}

}
