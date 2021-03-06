package eu.apenet.commons.solr;

import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.ai.AlType;
import eu.apenet.commons.ai.TreeType;
import static eu.apenet.commons.solr.AbstractSearcher.OR;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public final class SearchUtil {

    private static final String YYYY = "yyyy";
    private static final String YYYY_MM = "yyyy-MM";
    private static final String YYYY_MM_DD = "yyyy-MM-dd";
    private static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd'T'hh:mm:ss'Z'";
    private static final String FULL_DATE_TIME = "yyyy-MM-dd'T'hh_mm_ss";
    private static final SimpleDateFormat FULL_DATE_FORMAT = new SimpleDateFormat(YYYY_MM_DD);
    private static final SimpleDateFormat FULL_SOLR_FORMAT = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
    private static final SimpleDateFormat FULL_DATE_TIME_FORMAT = new SimpleDateFormat(FULL_DATE_TIME);

    public static void addParameter(Map<String, List<String>> parameters, String name, String toBeAdded) {
        if (StringUtils.isNotBlank(toBeAdded)) {
            if (parameters.containsKey(name)) {
                List<String> list = parameters.get(name);
                list.add(toBeAdded);
            } else {
                List<String> list = new ArrayList<String>();
                list.add(toBeAdded);
                parameters.put(name, list);
            }
        }
    }

    public static void setParameter(Map<String, List<String>> parameters, String name, String toBeAdded) {
        if (StringUtils.isNotBlank(toBeAdded)) {
            List<String> list = new ArrayList<String>();
            list.add(toBeAdded);
            parameters.put(name, list);
        }
    }

    public static void setParameter(Map<String, List<String>> parameters, String name, List<String> toBeAdded) {
        if (toBeAdded != null && toBeAdded.size() > 0) {
            parameters.put(name, toBeAdded);
        }
    }

    public static void addRefinement(SolrQueryParameters solrQueryParameters, String name, String toBeAdded) {
        if (StringUtils.isNotBlank(toBeAdded)) {
            solrQueryParameters.getOrParameters().remove(name);
            List<String> list = new ArrayList<String>();
            list.add(toBeAdded);
            solrQueryParameters.getAndParameters().put(name, list);
        }
    }

    public static void addRefinement(SolrQueryParameters solrQueryParameters, FacetType facet, String toBeAdded) {
        if (StringUtils.isNotBlank(toBeAdded)) {
            solrQueryParameters.getOrParameters().remove(facet.getRefinementFieldWithLabel());
            List<String> list = new ArrayList<String>();
            list.add(toBeAdded);
            solrQueryParameters.getAndParameters().put(facet.getRefinementFieldWithLabel(), list);
        }
    }

    public static void addTextRefinement(SolrQueryParameters solrQueryParameters, FacetType facet, List<String> toBeAdded) {
        if (toBeAdded != null && toBeAdded.size() > 0) {
            List<String> textRefinements = new ArrayList<String>();
            for (String text : toBeAdded) {
                textRefinements.add("\"" + text + "\"");
            }
            solrQueryParameters.getAndParameters().put(facet.getRefinementFieldWithLabel(), textRefinements);
        }
    }

    public static void addRefinement(SolrQueryParameters solrQueryParameters, FacetType facet, List<String> toBeAdded) {
        if (toBeAdded != null && toBeAdded.size() > 0) {
            // solrQueryParameters.getOrParameters().remove(facet.getRefinementField());
            solrQueryParameters.getAndParameters().put(facet.getRefinementFieldWithLabel(), toBeAdded);
        }
    }

    public static boolean isValidDate(String date) {
        return parseDate(date, true) != null;
    }

    public static void setFromDate(Map<String, List<String>> parameters, String fromDate, boolean exact) {
        if (StringUtils.isNotBlank(fromDate)) {
            String date = parseDate(fromDate, true);
            if (exact) {
                setParameter(parameters, Ead3SolrFields.START_DATE, "[" + date + "T00:00:00Z TO *]");
            } else {
                setParameter(parameters, Ead3SolrFields.END_DATE, "[" + date + "T00:00:00Z TO *]");
            }
        }
    }

    public static void setToDate(Map<String, List<String>> parameters, String toDate, boolean exact) {
        if (StringUtils.isNotBlank(toDate)) {
            String date = parseDate(toDate, false);
            if (exact) {
                setParameter(parameters, Ead3SolrFields.END_DATE, "[* TO " + date + "T23:59:59Z]");
            } else {
                setParameter(parameters, Ead3SolrFields.START_DATE, "[* TO " + date + "T23:59:59Z]");
            }
        }
    }

    public static String parseDate(String onedate, boolean isStartDate) {
        String dateString = onedate.replace("/", "-");
        int numberOfMatches = StringUtils.countMatches(dateString, "-");
        boolean isFullDate = numberOfMatches == 2;
        boolean isYearMonthDate = numberOfMatches == 1;
        boolean isOnlyYearDate = numberOfMatches == 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        boolean lenient = false;
        calendar.setLenient(lenient);
        Date date = null;
        SimpleDateFormat fullDateFormat = new SimpleDateFormat(YYYY_MM_DD);
        fullDateFormat.setCalendar(calendar);
        fullDateFormat.setLenient(lenient);

        /*
		 * full date parsing
         */
        if (isFullDate) {
            try {

                date = fullDateFormat.parse(dateString);
                calendar.setTime(date);
            } catch (ParseException e) {

            }
        } /*
		 * year month date parsing
         */ else if (isYearMonthDate) {
            try {
                SimpleDateFormat yearMonthDateFormat = new SimpleDateFormat(YYYY_MM);
                yearMonthDateFormat.setCalendar(calendar);
                yearMonthDateFormat.setLenient(lenient);
                date = yearMonthDateFormat.parse(dateString);
                calendar.setTime(date);
                if (isStartDate) {
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
            } catch (ParseException e1) {

            }
        } else if (isOnlyYearDate) {
            /*
			 * year date parsing
             */
            try {
                SimpleDateFormat yearDateFormat = new SimpleDateFormat(YYYY);
                yearDateFormat.setCalendar(calendar);
                yearDateFormat.setLenient(lenient);
                date = yearDateFormat.parse(dateString);
                calendar.setTime(date);
                if (isStartDate) {
                    calendar.set(Calendar.MONTH, 0);
                    calendar.set(Calendar.DAY_OF_MONTH, 1);
                } else {
                    calendar.set(Calendar.MONTH, 11);
                    calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                }
            } catch (ParseException e2) {
            }
        }
        if (date != null) {
            return fullDateFormat.format(calendar.getTime());
        }
        return null;
    }

    public static String getHighlightedString(Map<String, Map<String, List<String>>> highlightingMap, String id,
            String fieldName, String defaultValue) {
        try {
            Map<String, List<String>> highlightedMap = highlightingMap.get(id);
            if (highlightedMap != null) {
                List<String> values = highlightedMap.get(fieldName);
                if (values != null) {
                    return values.get(0);
                }
            }
            return defaultValue;
        } catch (NullPointerException e) {
            return defaultValue;
        }

    }

    public static List<String> getHighlightedStrings(Map<String, Map<String, List<String>>> highlightingMap, String id,
            String fieldName, List<String> defaultValue) {
        try {
            Map<String, List<String>> highlightedMap = highlightingMap.get(id);
            if (highlightedMap != null) {
                List<String> values = highlightedMap.get(fieldName);
                if (values != null) {
                    return values;
                }
            }
            return defaultValue;
        } catch (NullPointerException e) {
            return defaultValue;
        }

    }

    public static List<String> getHighlightedStrings(Map<String, Map<String, List<String>>> highlightingMap, String id,
            String fieldName) {
        return getHighlightedStrings(highlightingMap, id, fieldName, new ArrayList<String>());

    }

    public static void addSelectedNodesToQuery(List<String> selectedNodes, SolrQueryParameters solrQueryParameters) {
        if (selectedNodes != null) {
            // Adding the ids of the Finding Aid selected for searching
            List<String> faHgIdsSelected = new ArrayList<String>();
            List<String> archivalInstitutionsIdsSelected = new ArrayList<String>();
            List<String> countriesSelected = new ArrayList<String>();
            for (String item : selectedNodes) {
                AlType alType = AlType.getAlType(item);
                Long id = AlType.getId(item);
                if (AlType.COUNTRY.equals(alType)) {
                    countriesSelected.add(id.toString());
                } else if (AlType.ARCHIVAL_INSTITUTION.equals(alType)) {
                    TreeType treeType = AlType.getTreeType(item);
                    if (TreeType.GROUP.equals(treeType)) {
                        int depth = AlType.getDepth(item);
                        String name = SolrFields.AI_DYNAMIC_ID + depth + SolrFields.DYNAMIC_STRING_SUFFIX;
                        SearchUtil.addParameter(solrQueryParameters.getOrParameters(), name, SolrFields.AI_DYNAMIC + id.toString());
                    } else {
                        archivalInstitutionsIdsSelected.add(id.toString());
                    }
                } else if (AlType.FINDING_AID.equals(alType)) {
                    faHgIdsSelected.add(alType.toString() + id);
                } else if (AlType.SOURCE_GUIDE.equals(alType)) {
                    faHgIdsSelected.add(alType.toString() + id);
                } else if (AlType.HOLDINGS_GUIDE.equals(alType)) {
                    faHgIdsSelected.add(alType.toString() + id);
                }
            }

            if (countriesSelected.size() > 0) {
                SearchUtil.setParameter(solrQueryParameters.getOrParameters(), Ead3SolrFields.COUNTRY_ID,
                        countriesSelected);
            }
            if (archivalInstitutionsIdsSelected.size() > 0) {
                SearchUtil.setParameter(solrQueryParameters.getOrParameters(), Ead3SolrFields.AI_ID,
                        archivalInstitutionsIdsSelected);
            }

            SearchUtil.setParameter(solrQueryParameters.getOrParameters(), Ead3SolrFields.ID, faHgIdsSelected);
        }
    }

    public static void addPublishedDates(String publishFromDate, String publishToDate,
            SolrQueryParameters solrQueryParameters) {
        if (StringUtils.isNotBlank(publishFromDate) || StringUtils.isNotBlank(publishToDate)) {
            String solrFromDate = getSolrPublishedDate(publishFromDate);
            String solrToDate = getSolrPublishedDate(publishToDate);
            if (solrFromDate != null || solrToDate != null) {
                if (solrFromDate == null) {
                    solrFromDate = "*";
                } else if (!solrFromDate.endsWith("Z")) {
                    solrFromDate += "T00:00:00Z";
                }
                if (solrToDate == null) {
                    solrToDate = "*";
                } else if (!solrToDate.endsWith("Z")) {
                    solrToDate += "T23:59:59Z";
                }
                String solrQuery = "[" + solrFromDate + " TO " + solrToDate + "]";
                setParameter(solrQueryParameters.getAndParameters(), Ead3SolrFields.TIMESTAMP, solrQuery);
            }
        }
    }

    public static boolean isValidPublishedDate(String dateString) {
        return getSolrPublishedDate(dateString) != null;
    }

    private static String getSolrPublishedDate(String dateString) {
        if (StringUtils.isNotBlank(dateString)) {
            if (dateString.contains("T")) {
                try {
                    Date date = FULL_DATE_TIME_FORMAT.parse(dateString);
                    return FULL_SOLR_FORMAT.format(date);
                } catch (ParseException e) {
                }
            } else {
                try {
                    Date date = FULL_DATE_FORMAT.parse(dateString);
                    return FULL_DATE_FORMAT.format(date);
                } catch (ParseException e) {
                }
            }

        }
        return null;
    }

    public static String getFullDateTimePublishedDate(Date date) {
        return FULL_DATE_TIME_FORMAT.format(date);
    }

    public static String convertToOrQuery(List<String> list) {
        String result = null;
        if (list != null && list.size() > 0) {

            if (list.size() == 1) {
                result = escapeSolrCharactersInRefinements(list.get(0));
            } else {
                result = "(";
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        result += escapeSolrCharactersInRefinements(list.get(i)) + ")";
                    } else {
                        result += escapeSolrCharactersInRefinements(list.get(i)) + OR;
                    }
                }
            }
        }
        return result;
    }

    public static String escapeSolrCharacters(String term) {
        if (StringUtils.isNotBlank(term)) {
            term = term.replaceAll(" - ", " \"-\" ");
            term = term.replaceAll(" \\+ ", " \"+\" ");
        }
        // term = escapeSolrCharactersInRefinements(term);
        return term;
    }

    public static String escapeSolrCharactersInRefinements(String refinement) {
        return escapeSolrCharacters(refinement);
        // if (StringUtils.isNotBlank(refinement)){
        // refinement = refinement.replaceAll(ESCAPING_CHARACTER,
        // ESCAPING_CHARACTER + ESCAPING_CHARACTER);
        // refinement = refinement.replaceAll("-", ESCAPING_CHARACTER + "-");
        // refinement = refinement.replaceAll("\\+", ESCAPING_CHARACTER + "+");
        // refinement = refinement.replaceAll("&&", ESCAPING_CHARACTER + "&&");
        // refinement = refinement.replaceAll("\\|\\|", ESCAPING_CHARACTER +
        // "||");
        // refinement = refinement.replaceAll("!", ESCAPING_CHARACTER + "!");
        // refinement = refinement.replaceAll("\\(", ESCAPING_CHARACTER + "(");
        // refinement = refinement.replaceAll("\\)", ESCAPING_CHARACTER + ")");
        // refinement = refinement.replaceAll("\\{", ESCAPING_CHARACTER + "{");
        // refinement = refinement.replaceAll("\\}", ESCAPING_CHARACTER + "}");
        // refinement = refinement.replaceAll("\\[", ESCAPING_CHARACTER + "[");
        // refinement = refinement.replaceAll("\\]", ESCAPING_CHARACTER + "]");
        // refinement = refinement.replaceAll("\\^", ESCAPING_CHARACTER + "^");
        // refinement = refinement.replaceAll("\"", ESCAPING_CHARACTER + "\"");
        // refinement = refinement.replaceAll("~", ESCAPING_CHARACTER + "~");
        // refinement = refinement.replaceAll("\\*", ESCAPING_CHARACTER + "*");
        // refinement = refinement.replaceAll("\\?", ESCAPING_CHARACTER + "?");
        // refinement = refinement.replaceAll(":", ESCAPING_CHARACTER + ":");
        //
        // }
        // return refinement;
    }
}
