package eu.apenet.oaiserver.util;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;

import eu.apenet.oaiserver.verb.GetRecord;
import eu.apenet.oaiserver.verb.Identify;
import eu.apenet.oaiserver.verb.ListIdentifiers;
import eu.apenet.oaiserver.verb.ListMetadataFormats;
import eu.apenet.oaiserver.verb.ListRecords;
import eu.apenet.oaiserver.verb.ListSets;
import eu.apenet.persistence.dao.ResumptionTokenDAO;
import eu.apenet.persistence.factory.DAOFactory;
import eu.apenet.persistence.vo.MetadataFormat;
import eu.apenet.persistence.vo.ResumptionToken;

public class OAISyntaxChecker {
	
	private static Logger log = Logger.getLogger(OAIResponse.class);
	
	/**
	 * This method returns an InputStream with the response document; checks if there are
	 * something wrong and launch verb logic. It has support for badVerb and badArguments
	 * 
	 * @param params
	 * @param request
	 * @return InputStream
	 * @throws DOMException
	 * @throws ParserConfigurationException
	 * @throws ParseException
	 * @throws Exception
	 */
	public static InputStream check(Map<String,String> params,HttpServletRequest request) throws DOMException, ParserConfigurationException, ParseException, Exception {
		InputStream inputStream = null;
		Document doc = null;
		String verb = params.get("verb");
		if(verb!=null && !verb.isEmpty()){
			if(verb.equals("ListRecords")){
				if(checkListIdentifiersAndListRecords(params) && !checkRepeatedParameters(request)){
					ListRecords listRecords = new ListRecords(params);
					listRecords.setUrl(OAIUtils.getURL(request));
					listRecords.execute();
					inputStream = listRecords.getInputStream();
				}
			}else if(verb.equals("GetRecord")){
				if(checkGetRecordArguments(params) && !checkRepeatedParameters(request)){
					GetRecord getRecords = new GetRecord(params);
					getRecords.setUrl(OAIUtils.getURL(request));
					getRecords.execute();
					inputStream = getRecords.getInputStream();
				}
			}else if(verb.equals("ListSets")){
				if(checkListSetsArguments(params) && !checkRepeatedParameters(request)){
					ListSets listSets = new ListSets(params);
					listSets.setUrl(OAIUtils.getURL(request));
					listSets.execute();
					inputStream = listSets.getInputStream();
				}
			}else if(verb.equals("ListIdentifiers")){
				if(checkListIdentifiersAndListRecords(params) && !checkRepeatedParameters(request)){
					ListIdentifiers listIdentifiers = new ListIdentifiers(params);
					listIdentifiers.setUrl(OAIUtils.getURL(request));
					listIdentifiers.execute();
					inputStream = listIdentifiers.getInputStream();
				}
			}else if(verb.equals("ListMetadataFormats")){
				if(checkListMetadataFormats(params) && !checkRepeatedParameters(request)){
					ListMetadataFormats listMetadataFormats = new ListMetadataFormats(params);
					listMetadataFormats.setUrl(OAIUtils.getURL(request));
					listMetadataFormats.execute();
					inputStream = listMetadataFormats.getInputStream();
				}
			}else if(verb.equals("Identify")){
				if(checkIdentifyArguments(params) && !checkRepeatedParameters(request)){
					Identify identify = new Identify();
					identify.setUrl(OAIUtils.getURL(request));
					identify.execute();
					inputStream = identify.getInputStream();
				}
			}else{
				doc = OAIUtils.getResponseError(OAIResponse.appendRequestNode(verb,OAIUtils.getURL(request),params, false),"badVerb");
				inputStream = OAIUtils.getInputStreamOfDocument(doc);
			}
			if(inputStream == null){
				doc = OAIUtils.getEmptyDocument();
				doc = OAIResponse.appendRequestNode(verb,OAIUtils.getURL(request),params, false);
				inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(doc,"badArgument"));
			}
		}else{
			doc = OAIUtils.getEmptyDocument();
			doc = OAIResponse.appendRequestNode(verb,OAIUtils.getURL(request),params,false);
			inputStream = OAIUtils.getInputStreamOfDocument(OAIUtils.getResponseError(doc,"badVerb"));
		}
		return inputStream;
	}

	private static boolean checkListMetadataFormats(Map<String, String> params) {
		int size = params.size();
		if(size==1){
			return true;
		}else if(size==2 && params.containsKey("identifier")){
			return true;
		}
		return false;
	}

	private static boolean checkListSetsArguments(Map<String, String> params){
		int size = params.size();
		if(size==1){
			return true;
		}else if(size==2 && params.containsKey("resumptionToken")){
			return true;
		}
		return false;
	}

	private static boolean checkListIdentifiersAndListRecords(Map<String, String> params) {
		int size = params.size();
		if(size<=5){
			if(!params.containsKey("resumptionToken") && params.containsKey("metadataPrefix")){
				switch(size){
				case 2:
					return true;
				case 3:
					if(params.containsKey("from")||params.containsKey("until")||params.containsKey("set")){
						return true;
					}
				case 4:
					if((params.containsKey("from") && params.containsKey("until") && !params.containsKey("set")) || 
							(params.containsKey("from") && !params.containsKey("until") && params.containsKey("set")) ||
							(!params.containsKey("from") && params.containsKey("until") && params.containsKey("set"))){
						return true;
					}
				case 5:
					if(params.containsKey("from") && params.containsKey("until") && params.containsKey("set")){
						return true;
					}
				}
			}else if(params.containsKey("resumptionToken") && !params.containsKey("metadataPrefix") && params.size()==2){
				return true;
			}
		}
		return false;
	}

	private static boolean checkIdentifyArguments(Map<String, String> params) {
		if(params.size()==1){
			return true;
		}
		return false;
	}

	private static boolean checkGetRecordArguments(Map<String, String> params){
		int size = params.size();
		if(size==3){
			if(params.containsKey("identifier") && params.containsKey("metadataPrefix")){
				return true;
			}
		}
		return false;
	}

	/**
	 * This method get all parameters from the request and put these in a map.
	 * @param request
	 * @return Map<String,String>
	 */
	public static Map<String,String> fillParams(HttpServletRequest request) {
		Map<String,String> params = new HashMap<String,String>();
		Enumeration<String> names = request.getParameterNames();
		int counter = 0;
		while(names.hasMoreElements()){
			String nextElement = names.nextElement();
			params.put(nextElement,request.getParameter(nextElement).toString());
			counter++;
		}
		return params;
	}
	
	private static boolean checkRepeatedParameters(HttpServletRequest request){
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			if(request.getParameterValues(names.nextElement()).length>1){
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a resumptionToken is valid or not. The resumptionTokens have
	 * expiration dates and a syntax that need to be checked to inform system
	 * that could be a badResumptionToken. Returns the state.
	 * 
	 * @param fromDate
	 * @param untilDate
	 * @param metadataPrefix
	 * @param set
	 * @param limit
	 * @return boolean
	 */
	public static boolean checkValidResumptionToken(Date fromDate,Date untilDate,String metadataPrefix,String set,String limit){
		boolean state = false;
		try{
			OAIUtils.removeOldResumptionTokens();
			ResumptionTokenDAO resumptionTokenDAO = DAOFactory.instance().getResumptionTokenDAO();
			MetadataFormat metadataFormat = DAOFactory.instance().getMetadataFormatDAO().getMetadataFormatByName(metadataPrefix);
			ResumptionToken resumptionToken = resumptionTokenDAO.getResumptionToken(fromDate,untilDate,metadataFormat,set,limit);
			if(resumptionToken!=null && resumptionToken.getExpirationDate().after(new Date())){
				state = true;
			}else{
				state = false;
				if(resumptionToken!=null){
					resumptionTokenDAO.delete(resumptionToken);
				}
			}
		}catch(Exception e){
			log.error("Error trying to validate a resumptionToken:"+fromDate+"/"+untilDate+"/"+set+"/"+metadataPrefix+"/"+limit,e);
		}
		return state;
	}
}
