package eu.apenet.commons.solr;

import org.apache.commons.lang.StringUtils;

public final class SolrValues {

    public static final String ESCAPING_CHARACTER = "\\\\";
    public final static String EAD3_PREFIX = "E";
    public final static String HG_PREFIX = "H";
    public final static String FA_PREFIX = "F";
    public final static String SG_PREFIX = "S";
    public final static String AI_PREFIX = "A";
    public final static String C_LEVEL_PREFIX = "C";
    public final static String HG_TYPE = "hg";
    public final static String FA_TYPE = "fa";
    public final static String SG_TYPE = "sg";
    @Deprecated
    public final static String GROUP_SUFFIX = "G";
    public final static String TYPE_LEAF = "L";
    public final static String TYPE_GROUP = "G";
    public final static String DATE_TYPE_NO_DATE_SPECIFIED = "nodate";
    public final static String DATE_TYPE_OTHER_DATE = "otherdate";
    public final static String DATE_TYPE_NORMALIZED = "normal";
    public final static String DATE_TYPE_NORMALIZED_UNKNOWN_STARTDATE = "unknownstartdate";
    public final static String DATE_TYPE_NORMALIZED_UNKNOWN_ENDDATE = "unknownenddate";
    public final static String DATE_TYPE_UNKNOWN_DATE = "unknowndate";
    public static final String LEVEL_CLEVEL = "clevel";
    public static final String LEVEL_ARCHDESC = "archdesc";
    public static final String ROLE_DAO_UNSPECIFIED = "UNSPECIFIED";
    public static final String[] ROLE_DAOS_ALL = new String[]{"IMAGE", "TEXT", "SOUND", "VIDEO", "3D", ROLE_DAO_UNSPECIFIED};

    public final static String EAC_CPF_FACET_ENTITY_TYPE_FAMILY = "family";
    public final static String EAC_CPF_FACET_ENTITY_TYPE_PERSON = "person";
    public final static String EAC_CPF_FACET_ENTITY_TYPE_CORPORATE_BODY = "corporatebody";
    public final static String EAG_REPOSITORY_TYPE_BUSINESS_ARCHIVES = "businessArchives";
    public final static String EAG_REPOSITORY_TYPE_CHURCH_ARCHIVES = "churchArchives";
    public final static String EAG_REPOSITORY_TYPE_COUNTY_ARCHIVES = "countyArchives";
    public final static String EAG_REPOSITORY_TYPE_CULTURAL_ARCHIVES = "culturalArchives";
    public final static String EAG_REPOSITORY_TYPE_MEDIA_ARCHIVES = "mediaArchives";
    public final static String EAG_REPOSITORY_TYPE_MUNICIPAL_ARCHIVES = "municipalArchives";
    public final static String EAG_REPOSITORY_TYPE_NATIONAL_ARCHIVES = "nationalArchives";
    public final static String EAG_REPOSITORY_TYPE_POLITICAL_ARCHIVES = "politicalArchives";
    public final static String EAG_REPOSITORY_TYPE_PRIVATE_ARCHIVES = "privateArchives";
    public final static String EAG_REPOSITORY_TYPE_REGIONAL_ARCHIVES = "regionalArchives";
    public final static String EAG_REPOSITORY_TYPE_SPECIALISED_ARCHIVES = "specialisedArchives";
    public final static String EAG_REPOSITORY_TYPE_UNIVERSITY_ARCHIVES = "universityArchives";
    public final static String[] EAG_REPOSITORY_TYPES = new String[]{EAG_REPOSITORY_TYPE_BUSINESS_ARCHIVES, EAG_REPOSITORY_TYPE_CHURCH_ARCHIVES,
        EAG_REPOSITORY_TYPE_COUNTY_ARCHIVES, EAG_REPOSITORY_TYPE_CULTURAL_ARCHIVES, EAG_REPOSITORY_TYPE_CULTURAL_ARCHIVES, EAG_REPOSITORY_TYPE_MEDIA_ARCHIVES,
        EAG_REPOSITORY_TYPE_MUNICIPAL_ARCHIVES, EAG_REPOSITORY_TYPE_NATIONAL_ARCHIVES, EAG_REPOSITORY_TYPE_POLITICAL_ARCHIVES, EAG_REPOSITORY_TYPE_PRIVATE_ARCHIVES, EAG_REPOSITORY_TYPE_REGIONAL_ARCHIVES,
        EAG_REPOSITORY_TYPE_SPECIALISED_ARCHIVES, EAG_REPOSITORY_TYPE_UNIVERSITY_ARCHIVES};

    public static String escapeSolrCharacters(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replaceAll(ESCAPING_CHARACTER, ESCAPING_CHARACTER + ESCAPING_CHARACTER);
            value = value.replaceAll("-", ESCAPING_CHARACTER + "-");
            value = value.replaceAll("\\+", ESCAPING_CHARACTER + "+");
            value = value.replaceAll("&&", ESCAPING_CHARACTER + "&&");
            value = value.replaceAll("\\|\\|", ESCAPING_CHARACTER + "||");
            value = value.replaceAll("!", ESCAPING_CHARACTER + "!");
            value = value.replaceAll("\\(", ESCAPING_CHARACTER + "(");
            value = value.replaceAll("\\)", ESCAPING_CHARACTER + ")");
            value = value.replaceAll("\\{", ESCAPING_CHARACTER + "{");
            value = value.replaceAll("\\}", ESCAPING_CHARACTER + "}");
            value = value.replaceAll("\\[", ESCAPING_CHARACTER + "[");
            value = value.replaceAll("\\]", ESCAPING_CHARACTER + "]");
            value = value.replaceAll("\\^", ESCAPING_CHARACTER + "^");
            value = value.replaceAll("\"", ESCAPING_CHARACTER + "\"");
            value = value.replaceAll("~", ESCAPING_CHARACTER + "~");
            value = value.replaceAll("\\*", ESCAPING_CHARACTER + "*");
            value = value.replaceAll("\\?", ESCAPING_CHARACTER + "?");
            value = value.replaceAll(":", ESCAPING_CHARACTER + ":");

        }
        return value;
    }

}
