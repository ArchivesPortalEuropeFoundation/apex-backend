package eu.apenet.oaiserver.util;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import eu.apenet.oaiserver.util.OAIUtils;
import eu.apenet.oaiserver.verb.GetRecord;
import eu.apenet.oaiserver.verb.Identify;
import eu.apenet.oaiserver.verb.ListIdentifiers;
import eu.apenet.oaiserver.verb.ListMetadataFormats;
import eu.apenet.oaiserver.verb.ListRecords;
import eu.apenet.oaiserver.verb.ListSets;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.Ese;
import eu.apenet.persistence.vo.FindingAid;
import eu.apenet.persistence.vo.MetadataFormat;

public class OAIResponse {
	public final static String NAMESPACE = "http://www.openarchives.org/OAI/2.0/";
	public static final Integer LIMIT_PER_RESPONSE = 100; //100 nodes
	public static final String CREATOR = "Archives Portal Europe";
	public final static String[] VALID_VERBS = {"GetRecord","Identify","ListIdentifiers","ListMetadataFormats","ListRecords","ListSets"};
	private static Logger LOG = Logger.getLogger(OAIResponse.class);
	
	/**
	 * This method calls to specific logic method of each verb. It has tasks to do, first step 
	 * is append the request node checking if there are an error in the arguments and if the
	 * verb called is right or not. It builds the badVerb error node and appends it to the 
	 * document too. 
	 * 
	 * @param verb
	 * @param map
	 * @param requestURL
	 * @return Document
	 * @throws Exception
	 */
	public static Document getVerbResponse(String verb,Map<String,String> map,String requestURL) throws Exception{
		boolean found = false;
		for(int i=0;!found && i<OAIResponse.VALID_VERBS.length;i++){
			if(verb.equals(OAIResponse.VALID_VERBS[i])){
				found = true;
			}
		}
		Document doc = null;
		if(found){
			try{
				if(verb.equals("GetRecord")){
					doc = OAIResponse.appendRequestNode(verb,requestURL,map, true);
				}else if(verb.equals("Identify")){
					doc = OAIResponse.appendRequestNode(verb,requestURL,null, true);
				}else if(verb.equals("ListIdentifiers")){
					doc = OAIResponse.appendRequestNode(verb,requestURL,map, true);
				}else if(verb.equals("ListRecords")){
					doc = OAIResponse.appendRequestNode(verb,requestURL,map, true);
				}else if(verb.equals("ListSets")){ 
					doc = OAIResponse.appendRequestNode(verb,requestURL,map, true);  
				}else if(verb.equals("ListMetadataFormats")){
					doc = OAIResponse.appendRequestNode(verb,requestURL,map, true);
				}
			}catch(Exception e){
				throw e;
			}
		}else{
			doc = OAIUtils.getResponseError(doc,"badVerb");
		}
		return doc;
	}
	
	/**
	 * Builds response document, checks if the argument has a supported verb 
	 * and appends it. If it's needed shows the cannotDisseminateFormat error.
	 * 
	 * @param verb
	 * @param requestURL
	 * @param attributes
	 * @param checkParamSyntaxis
	 * @return Document
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 */
	public static Document appendRequestNode(String verb,String requestURL,Map<String,String> attributes, boolean checkParamSyntaxis) throws DOMException, ParserConfigurationException, ParseException {
		Document doc = OAIUtils.getEmptyDocument();
		NodeList mainNode = doc.getElementsByTagName("OAI-PMH");
		Node mainNodeElement = mainNode.item(0);
		Element request = doc.createElementNS(OAIResponse.NAMESPACE,"request");
		boolean metadataPrefixAvailable = false;
		boolean attributesSuccesfullyValidated = false;
		if(attributes==null && !verb.isEmpty() && verb.equals("Identify")){
			request.setAttribute("verb", verb);
			attributesSuccesfullyValidated = true;
		}
		else {
			if(attributes!=null && verb!=null && !verb.isEmpty()){
				if(Arrays.asList(OAIResponse.VALID_VERBS).contains(verb)){
					request.setAttribute("verb", verb);
					
					// Validating the attributes from the request
					if (checkParamSyntaxis && OAIUtils.validateRequestAttributes(attributes)){
						attributesSuccesfullyValidated = true;
						Iterator<String> keys = attributes.keySet().iterator();
						while(!metadataPrefixAvailable && keys.hasNext()){
							String key = keys.next();
							if(verb.equals("GetRecord") || verb.equals("ListIdentifiers") || verb.equals("ListRecords") || verb.equals("ListMetadataFormats")){
								if(key.equals("metadataPrefix") && attributes.get("metadataPrefix")!=null && !verb.equals("ListMetadataFormats")){
									String metadataPrefix = attributes.get(key);
									if(MetadataFormat.getMetadataFormat(metadataPrefix)==null){
										metadataPrefixAvailable = true;
									}
								}
								if(!metadataPrefixAvailable && attributes.get(key)!=null && !attributes.get(key).isEmpty()){
									request.setAttribute(key,attributes.get(key));
								}
							}
						}						
					}
				}
			}

		}
		request.setTextContent(requestURL);
		mainNodeElement.appendChild(request);
		if (checkParamSyntaxis && !attributesSuccesfullyValidated) {
			doc = OAIUtils.getResponseError(doc,"badArgument"); 
		}
		else {
			if (metadataPrefixAvailable) {
				doc = OAIUtils.getResponseError(doc,"cannotDisseminateFormat");
			}
		}
		return doc;
	}

	/**
	 * This method calls to determinate logic method for each verb, fill a document
	 * and transform it in a InputStream to be returned.
	 * 
	 * @param verb
	 * @param arguments
	 * @param requestURL
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream getVerbInfo(String verb,Map<String,String> arguments,String requestURL) throws Exception{
		Document doc = null;
		FindingAid findingAid = null;
		if(verb.equals("GetRecord")){
			String identifier = arguments.get("identifier");
			String metadataPrefix = arguments.get("metadataPrefix");
			doc = GetRecord.getRecordLogic(identifier,metadataPrefix,findingAid,requestURL);
		}else if(verb.equals("Identify")){
			doc = Identify.getIdentifyLogic(requestURL);
		}else if(verb.equals("ListIdentifiers")){
			doc = ListIdentifiers.getListIdentifiersLogic(arguments,requestURL);
		}else if(verb.equals("ListMetadataFormats")){
			doc = ListMetadataFormats.getListMetadataFormatsLogic(arguments,requestURL);
		}else if(verb.equals("ListRecords")){
			doc = ListRecords.getListRecordsLogic(arguments,requestURL);
		}else if(verb.equals("ListSets")){
			doc = ListSets.getListSetsLogic(arguments,requestURL);
		}
		try { //get XML results
			return OAIUtils.getInputStreamOfDocument(doc);
		} catch (Exception e) {
			LOG.error("Error trying to get InputStream: ",e.getCause());
		}
		return null;
	}
	
	/**
	 * It builds, stores and checks a resumption token used in OAI instructions.
	 * 
	 * @param verb
	 * @param resumptionToken
	 * @param arguments
	 * @param doc
	 * @return Document
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 */
	public static Document resumptionTokenLogic(String verb,String resumptionToken,Map<String,String> arguments,Document doc) throws DOMException, ParserConfigurationException, ParseException {
		String[] params = resumptionToken.split("/");
		if(params.length==5){
			try {
				try{
					Date startDate = OAIUtils.parseStringToISO8601Date(params[0]);
					Date untilDate = OAIUtils.parseStringToISO8601Date(params[1]);
					Integer startNumber = null;
					if(params[4]!=null && !params[4].contains(OAIUtils.SPECIAL_KEY)){
						startNumber = new Integer(params[4]);
					}else if(!verb.equals("ListRecords") && !verb.equals("ListIdentifiers") && !verb.equals("ListSets")){
						return OAIUtils.getResponseError(doc,"badResumptionToken");
					}
					String set = params[2];
					if(set!=null && set.isEmpty()){
						set=null;
					}
					String metadataPrefix = params[3];
					if(metadataPrefix!=null && metadataPrefix.isEmpty()){
						metadataPrefix = null;
					}
					if(OAISyntaxChecker.checkValidResumptionToken(startDate,untilDate,metadataPrefix,set,params[4])){
						if(verb.equals("ListSets")){
							List<String> eSets = DAOFactory.instance().getEseDAO().getSetsOfEses(startDate,untilDate,set,startNumber,OAIResponse.LIMIT_PER_RESPONSE+1);
							doc = ListSets.buildListSetResponse(eSets,doc,arguments);
						}else if(verb.equals("ListRecords")){
							Integer firstResult = null;
							String[] limits = null;
							if(params[4].contains(OAIUtils.SPECIAL_KEY)){
								limits = params[4].split(OAIUtils.SPECIAL_KEY);
							}
							try{
								firstResult = new Integer(limits[0]);
							}catch(Exception e){
								LOG.error("Parse error, trying to obtain the start arguments from resumptionToken in ListRecords: "+e.getCause());
								return OAIUtils.getResponseError(doc,"badResumptionToken");
							}
							MetadataFormat metadataFormat = MetadataFormat.getMetadataFormat(metadataPrefix);
							List<Ese> sets = null;
							if(metadataFormat!=null){
								try{
									sets = DAOFactory.instance().getEseDAO().getEsesByArguments(startDate, untilDate,metadataFormat,set,firstResult,OAIResponse.LIMIT_PER_RESPONSE);
									if(sets!=null && sets.isEmpty()){
										sets = DAOFactory.instance().getEseDAO().getEsesByArguments(startDate, untilDate,metadataFormat,set+":",firstResult,OAIResponse.LIMIT_PER_RESPONSE);
									}
								}catch(Exception e){
									LOG.error("Error trying to obtain eses by argument:"+startDate+"-"+untilDate+"-"+metadataFormat+"-"+set+"-"+firstResult+"-"+OAIResponse.LIMIT_PER_RESPONSE,e);
								}
							}
							doc = ListRecords.buildListRecordsResponse(sets,doc,arguments);
						}
					}else{
						doc = OAIUtils.getResponseError(doc,"badResumptionToken");
					}
				}catch(Exception e){
					LOG.error("INVALID date or start- from:"+params[0]+"||until:"+params[1]+"||start:"+params[2]);
					doc = OAIUtils.getResponseError(doc,"badResumptionToken");
				}
			}catch(Exception e){
				LOG.error("INVALID limit in resumptionToken: "+params[3]+": "+e.getCause());
				doc = OAIUtils.getResponseError(doc,"badResumptionToken");
			}
		}else{
			doc = OAIUtils.getResponseError(doc,"badResumptionToken");
		}
		return doc;
	}
}