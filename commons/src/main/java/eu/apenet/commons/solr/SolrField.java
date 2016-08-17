package eu.apenet.commons.solr;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public enum SolrField {
    TITLE(SolrFields.TITLE), SCOPECONTENT(SolrFields.SCOPECONTENT), ALTERDATE(SolrFields.ALTERDATE), OTHER(SolrFields.OTHER), UNITID(SolrFields.UNITID), OTHERUNITID(
            SolrFields.OTHERUNITID),
    EAC_CPF_NAMES(SolrFields.EAC_CPF_NAMES), EAC_CPF_PLACES(SolrFields.EAC_CPF_PLACES), EAC_CPF_OCCUPATION(SolrFields.EAC_CPF_OCCUPATION),
    EAC_CPF_FUNCTION(SolrFields.EAC_CPF_FUNCTION), EAC_CPF_MANDATE(SolrFields.EAC_CPF_MANDATE), EAC_CPF_ENTITY_ID(SolrFields.EAC_CPF_ENTITY_ID),
    EAG_NAME(SolrFields.EAG_NAME), EAG_OTHER_NAMES(SolrFields.EAG_OTHER_NAMES), EAG_PLACES(SolrFields.EAG_PLACES), EAG_ADDRESS(SolrFields.EAG_ADDRESS);

    private String value;
    private static final Integer NO_SELECTION = 0;
    private static final Integer TITLE_SELECTION = 1;
    private static final Integer SCOPECONTENT_SELECTION = 2;
    private static final Integer UNITID_SELECTION = 3;

    private SolrField(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static SolrField getSolrField(String type) {
        for (SolrField solrField : SolrField.values()) {
            if (solrField.toString().equalsIgnoreCase(type)) {
                return solrField;
            }

        }
        return null;
    }

    public HighlightType getType() {
        if (UNITID.equals(this) || OTHERUNITID.equals(this) || EAC_CPF_ENTITY_ID.equals(this)) {
            return HighlightType.UNITID;
        } else {
            return HighlightType.DEFAULT;
        }
    }

    public static List<SolrField> getDefaults() {
        List<SolrField> defaults = new ArrayList<SolrField>();
        defaults.add(TITLE);
        defaults.add(SCOPECONTENT);
        defaults.add(OTHER);
        defaults.add(ALTERDATE);
        return defaults;
    }

    public static List<SolrField> getSolrFieldsById(Integer id) {
        List<SolrField> defaults = new ArrayList<SolrField>();
        if (id == null || NO_SELECTION.equals(id)) {
//			defaults.add(TITLE);
//			defaults.add(SCOPECONTENT);
//			defaults.add(OTHER);
//			defaults.add(ALTERDATE);
        } else if (TITLE_SELECTION.equals(id)) {
            defaults.add(TITLE);
        } else if (SCOPECONTENT_SELECTION.equals(id)) {
            defaults.add(SCOPECONTENT);
        } else if (UNITID_SELECTION.equals(id)) {
            defaults.add(UNITID);
            defaults.add(OTHERUNITID);
        }
        return defaults;
    }

    public static List<SolrField> getSolrFieldsByIdString(String idString) {
        Integer id = null;
        if (StringUtils.isNotBlank(idString) && StringUtils.isNumeric(idString)) {
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
