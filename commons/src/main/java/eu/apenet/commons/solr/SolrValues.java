package eu.apenet.commons.solr;

import org.apache.commons.lang.StringUtils;

public final class SolrValues {
	public static final String ESCAPING_CHARACTER= "\\\\";
	public final static String HG_PREFIX = "H";
	public final static String FA_PREFIX = "F";
	public final static String SG_PREFIX = "S";
	public final static String AI_PREFIX = "A";
	public final static String C_LEVEL_PREFIX = "C";
	public final static String HG_TYPE = "hg";
	public final static String FA_TYPE = "fa";
	public final static String SG_TYPE = "sg";
	@Deprecated
	public final static String GROUP_SUFFIX= "G";
	public final static String TYPE_LEAF = "L";
	public final static String TYPE_GROUP = "G";
	public final static String DATE_TYPE_NO_DATE_SPECIFIED = "nodate";
	public final static String DATE_TYPE_OTHER_DATE = "otherdate";
	public final static String DATE_TYPE_NORMALIZED = "normal";
	public static final String LEVEL_CLEVEL = "clevel";
	public static final String LEVEL_ARCHDESC = "archdesc";
	
	public static String escapeSolrCharacters(String value){
		if (StringUtils.isNotBlank(value)){
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
