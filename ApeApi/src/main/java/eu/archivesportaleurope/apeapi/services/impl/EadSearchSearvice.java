package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrQueryBuilder;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.text.ParseException;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    private SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
    private final String solrCore;
    private final SolrSearchUtil eadSearchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public EadSearchSearvice(String solrUrl, String solrCore, String propFileName) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        logger.debug("Solr server got created!");
        this.eadSearchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public EadSearchSearvice(SolrServer solrServer, String propFileName) {
        this.solrUrl = this.solrCore = "";
        logger.debug("Solr server got created!");
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
    public EadResponseSet search(SearchRequest searchRequest, String extraSearchParam) {
        try {
            String extraParam = "";
            if (extraSearchParam != null) {
                extraParam = extraSearchParam;
            }
            List<ListFacetSettings> facetSettingsList = FacetType.getDefaultEadListFacetSettings();
            SolrQuery query = queryBuilder.getListViewQuery(searchRequest.getStartIndex(), facetSettingsList, null, null, null, true);
            query.setQuery(searchRequest.getQuery() + extraParam);
            
            if (searchRequest.getCount() <= 0) {
                logger.info(":::Default Count vale from prop is : " + propertiesUtil.getValueFromKey("search.request.default.count"));
                query.setRows(Integer.parseInt(propertiesUtil.getValueFromKey("search.request.default.count")));
            } else {
                query.setRows(searchRequest.getCount());
            }
            logger.debug("Final search query: "+query.getFields());
            this.eadSearchUtil.setQuery(query);
            
            QueryResponse response = this.eadSearchUtil.getSearchResponse();
            return new EadResponseSet(response);
        } catch (SolrServerException | ParseException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public EadResponseSet searchOpenData(SearchRequest request) {
        return this.search(request, " AND openData:true");
    }
}
