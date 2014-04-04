package eu.apenet.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.archivesportaleurope.commons.config.ApeConfig;
import eu.archivesportaleurope.commons.config.ApePortalAndDashboardConfig;
import eu.archivesportaleurope.commons.config.ApePortalConfig;
import eu.archivesportaleurope.commons.config.DashboardConfig;

/**
 * User: Yoann Moranville
 * Date: Sep 7, 2010
 *
 * @author Yoann Moranville
 */
public class APEnetUtilities {
    
	private static final int MAX_ERROR_LINES = 2;
	private static ApeConfig config;
	// Constants
	private static final Logger LOG = Logger.getLogger(APEnetUtilities.class);
    public static final String FILESEPARATOR = "/";
	private static final String UTF8 = "utf-8";

    public static String encodeString(String stringToEncode){
        try {
            if (stringToEncode == null)
                throw new NullPointerException();
            return URLEncoder.encode(stringToEncode, UTF8);
        } catch (UnsupportedEncodingException e){
            LOG.error("Could not encode string: " + stringToEncode, e);
            return sanitizeHTMLIdAttribute(stringToEncode);
        } catch (NullPointerException e){
            LOG.error("String is null", e);
            return null;
        }
    }

    private static String sanitizeHTMLIdAttribute(String s) {
        StringBuilder sb = new StringBuilder();
        int firstLegal = 0;
        while (firstLegal < s.length() && !isAZ(s.charAt(firstLegal)))
            ++firstLegal;
        for (int i = firstLegal; i < s.length(); ++i){
            final char ch = s.charAt(i);
            if (isOkIdInnerChar(ch))
                sb.append(ch);
        }
        return sb.length() == s.length()? s : sb.toString();
    }

    private static boolean isOkIdInnerChar(char ch) {
        return isAZ(ch) || isNum(ch) || isSpecial(ch);
    }

    private static boolean isSpecial(char ch) {
        switch (ch) {
            case '-':
            case '_':
            case ':':
            case '.':
                return true;
            default:
                return false;
        }
    }

    private static boolean isAZ(char ch) {
        return ('A' <= ch && ch <= 'Z') || ('a' <= ch && ch <= 'z');
    }

    private static boolean isNum(char ch) {
        return '0' <= ch && ch <= '9';
    }


	public static ApeConfig getConfig() {
		return config;
	}
	
	public static DashboardConfig getDashboardConfig(){
		if (config instanceof DashboardConfig){
			return (DashboardConfig) config;
		}
		throw new BadConfigurationException("The configuration object is no DashboardConfig, but " + config.getClass().getName());
	}

	public static ApePortalConfig getApePortalConfig(){
		if (config instanceof ApePortalConfig){
			return (ApePortalConfig) config;
		}
		throw new BadConfigurationException("The configuration object is no ApePortalConfig, but " + config.getClass().getName());
	}

	public static ApePortalAndDashboardConfig getApePortalAndDashboardConfig(){
		if (config instanceof ApePortalAndDashboardConfig){
			return (ApePortalAndDashboardConfig) config;
		}
		throw new BadConfigurationException("The configuration object is no ApePortalAndDashboardConfig, but " + config.getClass().getName());
	}
	public static void setConfig(ApeConfig config) {
		APEnetUtilities.config = config;
	}
    public static String convertToFilename(String name){
   		return name.replaceAll("[^a-zA-Z0-9\\-\\.]", "_");
    }
    public static void logThrowable(Throwable throwable){
    	LOG.error(generateThrowableLog(throwable));
    }
    public static String generateThrowableLog(Throwable throwable){
    	String result = "";
    	result+= throwable.getClass().getName()+ " " +throwable.getMessage()  + "\n";
    	result+= generateThrowableStackTraceLog(throwable.getStackTrace());
    	result+=generateThrowableCauseLog(throwable.getCause(), 0);
    	return result;
    }
    private static String generateThrowableCauseLog(Throwable throwable, int depth){
    	String result = "";
    	if (throwable != null){
    		result+= "Caused by: " +  throwable.getClass().getName()+ " " + throwable.getMessage()  +"\n";
    		result+= generateThrowableStackTraceLog(throwable.getStackTrace());
    		result+=generateThrowableCauseLog(throwable.getCause(), depth++);
    	}
    	return result;
    }
    private static String generateThrowableStackTraceLog(StackTraceElement[] elements){
    	String result = "";
    	for (int i = 0; i < MAX_ERROR_LINES && i < elements.length ;i++){
    		StackTraceElement element = elements[i];
    		result += "\t" + element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")\n" ;
    	}
    	if (elements.length > MAX_ERROR_LINES){
    		result += "\t... " + (elements.length -MAX_ERROR_LINES) + " more\n";
    	}
    	return result;
    }
    public static String removeSpecialUrlCharactersFromUrlPart(String urlPart) {
    	if (urlPart != null){
	        String result = urlPart.replaceAll(":", "_COLON_");
	        //result = result.replaceAll("*", "_ASTERISK_"); 
	        result = result.replaceAll("=", "_COMP_");	
	        result = result.replaceAll("/", "_SLASH_");
	        result = result.replaceAll("\\", "_BSLASH_");
	        //result = result.replaceAll("[", "_LSQBRKT_");
	        //result = result.replaceAll("]", "_RSQBRKT_");
	        //result = result.replaceAll("+", "_PLUS_");
	        result = result.replaceAll("%", "_PERCENT_");
	        result = result.replaceAll("@", "_ATCHAR_");
	        result = result.replaceAll("$", "_DOLLAR_");
	        //result = result.replaceAll("#", "_HASH_");
	        //result = result.replaceAll("^", "_CFLEX_");
	        result = result.replaceAll("&", "_AMP_");
	        result = result.replaceAll("(", "_LRDBRKT_");
	        result = result.replaceAll(")", "_RRDBRKT_");
	        result = result.replaceAll("!", "_EXCLMARK_");
	        result = result.replaceAll("~", "_TILDE_");
	        return result;
    	}else {
    		return null;
    	}
    }

    public static String restoreSpecialUrlCharactersInUrlPart(String urlPart) {
    	if (urlPart != null){
	        String result =  urlPart.replaceAll("_COLON_", ":");
	        //result = result.replaceAll("_ASTERISK_", "*"); 
	        result = result.replaceAll("_COMP_", "=");	        
	        result = result.replaceAll("_SLASH_", "/");
	        result = result.replaceAll("_BSLASH_", "\\");
	        //result = result.replaceAll("_LSQBRKT_", "[");
	        //result = result.replaceAll("_RSQBRKT_", "]");
	        //result = result.replaceAll("_PLUS_", "+");
	        result = result.replaceAll("_PERCENT_", "%");
	        result = result.replaceAll("_ATCHAR_", "@");
	        result = result.replaceAll("_DOLLAR_", "$");
	        //result = result.replaceAll("_HASH_", "#");
	        //result = result.replaceAll("_CFLEX_", "^");
	        result = result.replaceAll("_AMP_", "&");
	        result = result.replaceAll("_LRDBRKT_", "(");
	        result = result.replaceAll("_RRDBRKT_", ")");
	        result = result.replaceAll("_EXCLMARK_", "!");
	        result = result.replaceAll("_TILDE_", "~");
	        return result.trim();
    	}else {
    		return null;
    	}
    }
}
