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
}
