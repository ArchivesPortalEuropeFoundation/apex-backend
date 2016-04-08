package eu.archivesportaleurope.apeapi.services.impl;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mahbub
 */
public class EadSearchSearvice implements SearchService {

    private String solrUrl;
    private final String solrCore;
    private final SolrSearchUtil eadSearchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public EadSearchSearvice(String solrUrl, String solrCore, String propFileName) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        this.eadSearchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public EadSearchSearvice(SolrServer solrServer, String propFileName) {
        this.solrUrl = this.solrCore = "";
        this.eadSearchUtil = new SolrSearchUtil(solrServer);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    @Override
    public EadResponseSet search(SearchRequest searchRequest, String extraSearchParam) throws SolrServerException {
        String extraParam = "";
        if (extraSearchParam != null) {
            extraParam = extraSearchParam;
        }
        SolrQuery query = new SolrQuery(searchRequest.getQuery() + extraParam);
        query.setStart(searchRequest.getStart());
        if (searchRequest.getCount() <= 0) {
            logger.info(":::Default Count vale from prop is : "+propertiesUtil.getValueFromKey("search.request.default.count"));
            query.setRows(Integer.parseInt(propertiesUtil.getValueFromKey("search.request.default.count")));
        } else {
            query.setRows(searchRequest.getCount());
        }

        this.eadSearchUtil.setQuery(query);

        QueryResponse response = this.eadSearchUtil.getSearchResponse();
        return new EadResponseSet(response);
    }

    @Override
    public EadResponseSet searchOpenData(SearchRequest request) throws SolrServerException {
        return this.search(request, " AND openData:true");
    }
}
