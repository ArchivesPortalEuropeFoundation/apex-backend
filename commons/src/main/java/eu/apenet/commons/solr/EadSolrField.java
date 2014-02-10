package eu.apenet.commons.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public enum EadSolrField {
	TITLE("title"), SCOPECONTENT("scopecontent"), ALTERDATE("alterdate"), OTHER("other"), UNITID("unitid"), OTHERUNITID(
			"otherunitid");
	private String value;
	private static final Integer NO_SELECTION = 0;
	private static final Integer TITLE_SELECTION = 1;
	private static final Integer SCOPECONTENT_SELECTION = 2;
	private static final Integer UNITID_SELECTION = 3;

	private EadSolrField(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public static EadSolrField getSolrField(String type) {
		if (TITLE.toString().equalsIgnoreCase(type)) {
			return TITLE;
		} else if (SCOPECONTENT.toString().equalsIgnoreCase(type)) {
			return SCOPECONTENT;
		} else if (OTHER.toString().equalsIgnoreCase(type)) {
			return OTHER;
		} else if (UNITID.toString().equalsIgnoreCase(type)) {
			return UNITID;
		} else if (OTHERUNITID.toString().equalsIgnoreCase(type)) {
			return OTHERUNITID;
		} else if (ALTERDATE.toString().equalsIgnoreCase(type)) {
			return ALTERDATE;
		}
		return null;
	}

	public HighlightType getType() {
		if (TITLE.equals(this) || SCOPECONTENT.equals(this) || OTHER.equals(this) || ALTERDATE.equals(this)) {
			return HighlightType.DEFAULT;
		} else {
			return HighlightType.UNITID;
		}
	}

	public static List<EadSolrField> getDefaults() {
		List<EadSolrField> defaults = new ArrayList<EadSolrField>();
		defaults.add(TITLE);
		defaults.add(SCOPECONTENT);
		defaults.add(OTHER);
		defaults.add(ALTERDATE);
		return defaults;
	}

	public static List<EadSolrField> getSolrFieldsById(Integer id) {
		List<EadSolrField> defaults = new ArrayList<EadSolrField>();
		if (id == null || NO_SELECTION.equals(id)) {
//			defaults.add(TITLE);
//			defaults.add(SCOPECONTENT);
//			defaults.add(OTHER);
//			defaults.add(ALTERDATE);
		}else if (TITLE_SELECTION.equals(id)){
			defaults.add(TITLE);
		}else if (SCOPECONTENT_SELECTION.equals(id)){
			defaults.add(SCOPECONTENT);
		}else if (UNITID_SELECTION.equals(id)){
			defaults.add(UNITID);
			defaults.add(OTHERUNITID);
		}
		return defaults;
	}

	public static List<EadSolrField> getSolrFieldsByIdString(String idString){
		Integer id = null;
		if (StringUtils.isNotBlank(idString) && StringUtils.isNumeric(idString)){
			id = Integer.parseInt(idString);
		}
		return getSolrFieldsById(id);
	}
//	
//	public static String getSolrFieldsStringByIdString(String idString){
//		String result = null;
//		List<SolrField> fields = getSolrFieldsByIdString(idString);
//		for (SolrField field: fields){
//			if (result == null){
//				result = field.name();
//			}else {
//				result += WHITESPACE + field.name();
//			}
//		}
//		return result;
//	}


}
