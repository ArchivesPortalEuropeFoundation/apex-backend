package eu.apenet.oaiserver.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.apenet.oaiserver.config.MetadataFormats;
import eu.apenet.oaiserver.config.dao.ResumptionTokensDAOFront;
import eu.apenet.oaiserver.config.vo.ResumptionTokens;
import org.apache.log4j.Logger;


public class OAIUtils {
	//This part has the standard response description and never should be change. 
	private static final String IDENTIFIER_ATTRIBUTE = "identifier";
	private static final String METADATAPREFIX_ATTRIBUTE = "metadataPrefix";
	private static final String FROM_ATTRIBUTE = "from";
	private static final String UNTIL_ATTRIBUTE = "until";
	private static final String SET_ATTRIBUTE = "set";
	public static final String VERB = "verb";
	private static final String RESUMPTIONTOKEN_ATTRIBUTE = "resumptionToken";
	public static final long EXPIRATION_TIME_IN_MILLISECONDS = 1000*60*30; //10 minutes
	public static final String SPECIAL_KEY = "-";
	private static Logger LOG = Logger.getLogger(OAIUtils.class);
	
	public static ResumptionTokens buildResumptionToken(Map<String,String> arguments,int limit){
        ResumptionTokensDAOFront resumptionTokensDAOFront = new ResumptionTokensDAOFront();
		Date expirationDate = new Date();
		expirationDate = new Date(expirationDate.getTime()+OAIUtils.EXPIRATION_TIME_IN_MILLISECONDS);
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
		ResumptionTokens result = null;
		try {
			try{
				ResumptionTokens resToken = new ResumptionTokens();
				resToken.setFromDate(OAIUtils.parseStringToISO8601Date(from));
				resToken.setUntilDate(OAIUtils.parseStringToISO8601Date(until));
				if(set!=null && !set.isEmpty()){
					resToken.setSet(set.trim());
				}
				resToken.setLastRecordHarvested(limit +"");
				resToken.setExpirationDate(expirationDate);
				MetadataFormats metadataFormats = MetadataFormats.getMetadataFormats(metadataPrefix);
				resToken.setMetadataFormats(metadataFormats);
                result = resumptionTokensDAOFront.saveResumptionTokens(resToken);
			}catch(Exception e){
				LOG.error("Error trying to save the resumptionToken value in DDBB:",e);
			}
			resumptionToken = limit +"";
		} catch (Exception e) {
			LOG.error("Error trying to obtain the last identifier to be stored in ddbb (building resumptionToken):"+e.getCause());
		} finally{
			OAIUtils.removeOldResumptionTokens();
		}
		return result;
	}
	public static ResumptionTokens buildResumptionToken(ResumptionTokens oldResumptionTokens, int limit){
        ResumptionTokensDAOFront resumptionTokensDAOFront = new ResumptionTokensDAOFront();
		Date expirationDate = new Date();
		expirationDate = new Date(expirationDate.getTime() + OAIUtils.EXPIRATION_TIME_IN_MILLISECONDS);
		ResumptionTokens result = null;
		try {
			try{
				ResumptionTokens resToken = new ResumptionTokens();
				resToken.setFromDate(oldResumptionTokens.getFromDate());
				resToken.setUntilDate(oldResumptionTokens.getUntilDate());
				resToken.setSet(oldResumptionTokens.getSet());
				resToken.setLastRecordHarvested(limit + "");
				resToken.setExpirationDate(expirationDate); 
				resToken.setMetadataFormats(oldResumptionTokens.getMetadataFormats());
                result = resumptionTokensDAOFront.saveResumptionTokens(resToken);
			}catch(Exception e){
				LOG.error("Error trying to save the resumptionToken value in DDBB:",e);
			}
		} catch (Exception e) {
			LOG.error("Error trying to obtain the last identifier to be stored in ddbb (building resumptionToken):"+e.getCause());
		} finally{
			OAIUtils.removeOldResumptionTokens();
		}
		return result;
	}
	/**
	 * Deletes all values in DDBB that has never been used again.
	 */
	public static void removeOldResumptionTokens() {
        ResumptionTokensDAOFront resumptionTokensDAOFront = new ResumptionTokensDAOFront();
		resumptionTokensDAOFront.removeOldResumptionToken();
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
		TimeZone tz = TimeZone.getTimeZone("UTC");
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
	

}
