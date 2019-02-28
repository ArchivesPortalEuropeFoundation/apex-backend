package eu.apenet.commons.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import eu.apenet.commons.exceptions.BadConfigurationException;
import eu.archivesportaleurope.commons.config.ApeConfig;
import eu.archivesportaleurope.commons.config.ApePortalAndDashboardConfig;
import eu.archivesportaleurope.commons.config.ApePortalConfig;
import eu.archivesportaleurope.commons.config.DashboardConfig;
import eu.archivesportaleurope.util.ApeUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: Yoann Moranville Date: Sep 7, 2010
 *
 * @author Yoann Moranville
 */
public class APEnetUtilities {

    private static ApeConfig config;
    // Constants
    private static final Logger LOG = Logger.getLogger(APEnetUtilities.class);
    public static final String FILESEPARATOR = "/";
    private static final String UTF8 = "utf-8";
    public static final Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", Pattern.CASE_INSENSITIVE);
    private static Map<String, String> iso2ToIso3LanguageCodesMap;
    private static Map<String, Locale> iso3ToIso2LanguageCodesMap;
    private static final List<String> DATE_FORMAT_STRINGS = Arrays.asList("d.M.yyyy", "dd.MM.yyyy", "d/M/yyyy", "dd/MM/yyyy");

    static {
        iso2ToIso3LanguageCodesMap = new TreeMap<String, String>();
        iso3ToIso2LanguageCodesMap = new TreeMap<String, Locale>();
        String[] iso2Languages = Locale.getISOLanguages();
        for (String iso2Language : iso2Languages) {
            Locale locale = new Locale(iso2Language);
            String iso3Code = locale.getISO3Language();
            iso2ToIso3LanguageCodesMap.put(iso2Language, iso3Code);
            iso3ToIso2LanguageCodesMap.put(iso3Code, locale);
        }
        // Add 639-2/B variants below this line
        iso2ToIso3LanguageCodesMap.put("sq", "alb");
        iso2ToIso3LanguageCodesMap.put("hy", "arm");
        iso2ToIso3LanguageCodesMap.put("eu", "baq");
        iso2ToIso3LanguageCodesMap.put("my", "bur");
        iso2ToIso3LanguageCodesMap.put("zh", "chi");
        iso2ToIso3LanguageCodesMap.put("cs", "cze");
        iso2ToIso3LanguageCodesMap.put("nl", "dut");
        iso2ToIso3LanguageCodesMap.put("fr", "fre");
        iso2ToIso3LanguageCodesMap.put("ka", "geo");
        iso2ToIso3LanguageCodesMap.put("de", "ger");
        iso2ToIso3LanguageCodesMap.put("el", "gre");
        iso2ToIso3LanguageCodesMap.put("is", "ice");
        iso2ToIso3LanguageCodesMap.put("mk", "mac");
        iso2ToIso3LanguageCodesMap.put("mi", "mao");
        iso2ToIso3LanguageCodesMap.put("ms", "may");
        iso2ToIso3LanguageCodesMap.put("fa", "per");
        iso2ToIso3LanguageCodesMap.put("ro", "rum");
        iso2ToIso3LanguageCodesMap.put("sk", "slo");
        iso2ToIso3LanguageCodesMap.put("bo", "tib");
        iso2ToIso3LanguageCodesMap.put("cy", "wel");

        // Add 639-2/B variants below this line
        iso3ToIso2LanguageCodesMap.put("alb", new Locale("sq"));
        iso3ToIso2LanguageCodesMap.put("arm", new Locale("hy"));
        iso3ToIso2LanguageCodesMap.put("baq", new Locale("eu"));
        iso3ToIso2LanguageCodesMap.put("bur", new Locale("my"));
        iso3ToIso2LanguageCodesMap.put("chi", new Locale("zh"));
        iso3ToIso2LanguageCodesMap.put("cze", new Locale("cs"));
        iso3ToIso2LanguageCodesMap.put("dut", new Locale("nl"));
        iso3ToIso2LanguageCodesMap.put("fre", new Locale("fr"));
        iso3ToIso2LanguageCodesMap.put("geo", new Locale("ka"));
        iso3ToIso2LanguageCodesMap.put("ger", new Locale("de"));
        iso3ToIso2LanguageCodesMap.put("gre", new Locale("el"));
        iso3ToIso2LanguageCodesMap.put("ice", new Locale("is"));
        iso3ToIso2LanguageCodesMap.put("mac", new Locale("mk"));
        iso3ToIso2LanguageCodesMap.put("mao", new Locale("mi"));
        iso3ToIso2LanguageCodesMap.put("may", new Locale("ms"));
        iso3ToIso2LanguageCodesMap.put("per", new Locale("fa"));
        iso3ToIso2LanguageCodesMap.put("rum", new Locale("ro"));
        iso3ToIso2LanguageCodesMap.put("slo", new Locale("sk"));
        iso3ToIso2LanguageCodesMap.put("tib", new Locale("bo"));
        iso3ToIso2LanguageCodesMap.put("wel", new Locale("cy"));
    }

    public static Map<String, String> getIso2ToIso3LanguageCodesMap() {
        return Collections.unmodifiableMap(iso2ToIso3LanguageCodesMap);
    }

    public static Map<String, Locale> getIso3ToIso2LanguageCodesMap() {
        return Collections.unmodifiableMap(iso3ToIso2LanguageCodesMap);
    }

    public static String encodeString(String stringToEncode) {
        try {
            if (stringToEncode == null) {
                throw new NullPointerException();
            }
            return URLEncoder.encode(stringToEncode, UTF8);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Could not encode string: " + stringToEncode, e);
            return sanitizeHTMLIdAttribute(stringToEncode);
        } catch (NullPointerException e) {
            LOG.error("String is null", e);
            return null;
        }
    }

    private static String sanitizeHTMLIdAttribute(String s) {
        StringBuilder sb = new StringBuilder();
        int firstLegal = 0;
        while (firstLegal < s.length() && !isAZ(s.charAt(firstLegal))) {
            ++firstLegal;
        }
        for (int i = firstLegal; i < s.length(); ++i) {
            final char ch = s.charAt(i);
            if (isOkIdInnerChar(ch)) {
                sb.append(ch);
            }
        }
        return sb.length() == s.length() ? s : sb.toString();
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

    public static DashboardConfig getDashboardConfig() {
        if (config instanceof DashboardConfig) {
            return (DashboardConfig) config;
        }
        throw new BadConfigurationException("The configuration object is no DashboardConfig, but "
                + config.getClass().getName());
    }

    public static ApePortalConfig getApePortalConfig() {
        if (config instanceof ApePortalConfig) {
            return (ApePortalConfig) config;
        }
        throw new BadConfigurationException("The configuration object is no ApePortalConfig, but "
                + config.getClass().getName());
    }

    public static ApePortalAndDashboardConfig getApePortalAndDashboardConfig() {
        if (config instanceof ApePortalAndDashboardConfig) {
            return (ApePortalAndDashboardConfig) config;
        }
        throw new BadConfigurationException("The configuration object is no ApePortalAndDashboardConfig, but "
                + config.getClass().getName());
    }

    public static void setConfig(ApeConfig config) {
        APEnetUtilities.config = config;
    }

    public static String convertToFilename(String name) {
        return name.replaceAll("[^a-zA-Z0-9\\-\\.]", "_");
    }

    public static void logThrowable(Throwable throwable) {
        LOG.error(generateThrowableLog(throwable));
    }

    public static String generateThrowableLog(Throwable throwable) {
        return ApeUtil.generateThrowableLog(throwable);
    }

    public static Date extractDate(String dateString) {
        for (String formatString : APEnetUtilities.DATE_FORMAT_STRINGS) {
            try {
                return new SimpleDateFormat(formatString).parse(dateString);
            }
            catch (ParseException e) {}
        }
        return null;
    }

}
