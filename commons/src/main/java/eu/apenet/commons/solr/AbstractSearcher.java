package eu.apenet.commons.solr;

import eu.apenet.commons.solr.facet.FacetType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrRequest.METHOD;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.TermsResponse;

import eu.apenet.commons.solr.SolrField;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.apenet.commons.utils.APEnetUtilities;

public abstract class AbstractSearcher {

    public static final String OR = " OR ";
    protected static final String WHITESPACE = " ";
    // private static final String ESCAPING_CHARACTER= "\\\\";
    private static final Pattern NORMAL_TERM_PATTERN = Pattern.compile("[\\p{L}\\p{Digit}\\s]+");
    private static final String QUERY_TYPE_LIST = "list";
    protected static final String COLON = ":";
    protected final static SimpleDateFormat SOLR_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private final static Logger LOGGER = Logger.getLogger(AbstractSearcher.class);
    protected static Integer TIME_ALLOWED;
    protected static Integer TIME_ALLOWED_TREE;
    protected static Integer HTTP_TIMEOUT;
    private SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
    private HttpSolrServer solrServer;

    protected abstract String getCore();

    protected final HttpSolrServer getSolrServer() {
        if (solrServer == null) {
            try {
                solrServer = new HttpSolrServer(getSolrSearchUrl(), null);
                solrServer.setConnectionTimeout(HTTP_TIMEOUT);
                solrServer.setSoTimeout(HTTP_TIMEOUT);
                LOGGER.info("Successfully instantiate the solr client: " + getSolrSearchUrl());
            } catch (Exception e) {
                LOGGER.error("Unable to instantiate the solr client: " + e.getMessage());
            }
        }
        return solrServer;
    }

    protected final String getSolrSearchUrl() {
        return APEnetUtilities.getApePortalConfig().getBaseSolrSearchUrl() + "/" + getCore();
    }

    public TermsResponse getTerms(String term) throws SolrServerException {
        SolrQuery query = queryBuilder.getTermQuery(term);
        
        String resultLog = "Query;terms;" + getCore() + ";'" + term + "';";
        long startTime = System.currentTimeMillis();
        try {
            QueryResponse queryResponse = query(query);
            TermsResponse response = queryResponse.getTermsResponse();
            if (LOGGER.isDebugEnabled()) {
                resultLog += (System.currentTimeMillis() - startTime) + "ms;" + queryResponse.getQTime() + "ms;;;;;;;;";
                LOGGER.debug(resultLog);
            }
            return response;
        } catch (SolrServerException e) {
            resultLog += (System.currentTimeMillis() - startTime) + "ms;;;;;;;;;" + e.getMessage();
            LOGGER.error(resultLog);
            throw e;
        }

    }

    public long getNumberOfResults(SolrQueryParameters solrQueryParameters) throws SolrServerException, ParseException {
        QueryResponse queryResponse = getListViewResults(solrQueryParameters, 0, 0, null, null, null, null, false,
                false, false);
        return queryResponse.getResults().getNumFound();
    }

    public QueryResponse performNewSearchForListView(SolrQueryParameters solrQueryParameters, int rows,
            List<ListFacetSettings> facetSettings) throws SolrServerException, ParseException {
        return getListViewResults(solrQueryParameters, 0, rows, facetSettings, null, null, null, true, true, false);
    }

    public QueryResponse updateListView(SolrQueryParameters solrQueryParameters, int start, int rows,
            List<ListFacetSettings> facetSettings, String orderByField, String startDate, String endDate)
            throws SolrServerException, ParseException {
        return getListViewResults(solrQueryParameters, start, rows, facetSettings, orderByField, startDate, endDate,
                false, true, true);
    }

    

    private QueryResponse getListViewResults(SolrQueryParameters solrQueryParameters, int start, int rows,
            List<ListFacetSettings> facetSettingsList, String orderByField, String startDate, String endDate,
            boolean needSuggestions, boolean highlight, boolean update) throws SolrServerException, ParseException {
        
        SolrQuery query = queryBuilder.getListViewQuery(start, facetSettingsList, orderByField, startDate, endDate, highlight);
        if (rows > 50) {
            rows = 10;
        }
        query.setRows(rows);
        return executeQuery(query, solrQueryParameters, QUERY_TYPE_LIST, needSuggestions, update);
    }

    protected QueryResponse executeQuery(SolrQuery query, SolrQueryParameters solrQueryParameters, String queryType,
            boolean needSuggestions, boolean update) throws SolrServerException {
        query.setQuery(SearchUtil.escapeSolrCharacters(solrQueryParameters.getTerm()));

        if (solrQueryParameters.getAndParameters() != null) {
            for (Map.Entry<String, List<String>> criteria : solrQueryParameters.getAndParameters().entrySet()) {
                if (criteria.getValue() != null) {
                    query.addFilterQuery(criteria.getKey() + COLON + SearchUtil.convertToOrQuery(criteria.getValue()));

                }
            }
        }
        if (solrQueryParameters.getOrParameters() != null) {
            String orQuery = null;
            for (Map.Entry<String, List<String>> criteria : solrQueryParameters.getOrParameters().entrySet()) {
                if (criteria.getValue() != null) {
                    if (orQuery == null) {
                        orQuery = criteria.getKey() + COLON + SearchUtil.convertToOrQuery(criteria.getValue());
                    } else {
                        orQuery += OR + criteria.getKey() + COLON + SearchUtil.convertToOrQuery(criteria.getValue());
                    }

                }
            }
            if (orQuery != null) {
                query.addFilterQuery(orQuery);
            }
        }
        String searchableField = null;
        for (SolrField field : solrQueryParameters.getSolrFields()) {
            if (searchableField == null) {
                searchableField = field.toString();
            } else {
                searchableField += WHITESPACE + field.toString();
            }
        }

        if (searchableField != null) {
            query.set("qf", searchableField);
            query.set("hl.fl", searchableField);
        }
        if (!solrQueryParameters.isMatchAllWords()) {
            query.set("mm", "0%");
        }
        if (queryType != null) {
            query.setRequestHandler(queryType);
        }
        if (needSuggestions) {
            needSuggestions = isSimpleSearchTerms(solrQueryParameters.getTerm());
        }
        if (needSuggestions
                && !(solrQueryParameters.getSolrFields().contains(SolrField.UNITID)
                || solrQueryParameters.getSolrFields().contains(SolrField.OTHERUNITID) || solrQueryParameters
                .getSolrFields().contains(SolrField.EAC_CPF_ENTITY_ID))
                && StringUtils.isNotBlank(solrQueryParameters.getTerm())) {
            query.set("spellcheck", "on");
        }
        Integer timeAllowed = -1;
        if (solrQueryParameters.isTimeAllowed()) {
            timeAllowed = TIME_ALLOWED_TREE;
            if (QUERY_TYPE_LIST.equals(queryType)) {
                timeAllowed = TIME_ALLOWED;
            }
            query.setTimeAllowed(timeAllowed);
        }
        String resultLog = "Query;";
        if (query.getRows() == 0) {
            if (QUERY_TYPE_LIST.equals(queryType)) {
                resultLog += "count;";
            } else {
                resultLog += queryType + "-count;";
            }

        } else if (QUERY_TYPE_LIST.equals(queryType)) {
            if (update) {
                resultLog += queryType + "-update;";
            } else {
                resultLog += queryType + "-new;";
            }
        } else {
            resultLog += queryType + ";";
        }
        resultLog += getCore() + ";'" + solrQueryParameters.getTerm() + "';";
        String resultLogSuffix = "";
        if (solrQueryParameters.isMatchAllWords()) {
            resultLogSuffix += "AND;";
        } else {
            resultLogSuffix += "OR;";
        }
        if (query.getRows() == 0) {
            resultLogSuffix += ";";
        } else {
            resultLogSuffix += query.getRows() + "rows;";
        }
        if (solrQueryParameters.getAndParameters() != null) {
            resultLogSuffix += solrQueryParameters.getAndParameters().size() + ";";
        } else {
            resultLogSuffix += ";";
        }
        if (solrQueryParameters.getOrParameters() != null) {
            resultLogSuffix += solrQueryParameters.getOrParameters().size() + ";";
        } else {
            resultLogSuffix += ";";
        }
        if (searchableField == null) {
            resultLogSuffix += ";";
        } else {
            resultLogSuffix += searchableField + ";";
        }
        if (query.getSorts().size() > 0) {
            resultLogSuffix += query.getSorts().get(0).getItem() + ";";
        } else {
            resultLogSuffix += ";";
        }

        long startTime = System.currentTimeMillis();
        try {

            QueryResponse result = query(query);
            resultLog += (System.currentTimeMillis() - startTime) + "ms;" + result.getQTime() + "ms;" + result.getResults().getNumFound()
                    + " hits;" + resultLogSuffix;
            if (result.getHeader().get("partialResults") != null) {
                resultLog += "exceed query time(" + timeAllowed + "ms)";
            }
            LOGGER.debug(resultLog);
            return result;
        } catch (SolrServerException e) {
            resultLog += (System.currentTimeMillis() - startTime) + "ms;;;" + resultLogSuffix + e.getMessage();
            LOGGER.error(resultLog);
            throw e;
        }
    }

    private static boolean isSimpleSearchTerms(String term) {
        Matcher matcher = NORMAL_TERM_PATTERN.matcher(term);
        return matcher.matches();
    }

    private QueryResponse query(SolrQuery query) throws SolrServerException {
        return getSolrServer().query(query, METHOD.POST);
    }
}
