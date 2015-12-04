package eu.archivesportaleurope.apeapi.services.impl;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author Mahbub
 */
public class EadSearchSearvice implements SearchService {

    private final String solrUrl;
//    private final String solrCore;
    private final SolrSearchUtil eadSearchUtil;
    //@Context ServletContext context;
    private EadSearchSearvice(String solrCore) throws NamingException {
        this.solrUrl = InitialContext.doLookup("java:comp/env/solrHost");
        this.eadSearchUtil = new SolrSearchUtil(this.solrUrl, solrCore);
    }

    private EadSearchSearvice(String solrUrl, String solrCore) {
        this.solrUrl = solrUrl;
//        this.solrCore = solrCore;
        this.eadSearchUtil = new SolrSearchUtil(solrUrl, solrCore);
    }

    private EadSearchSearvice(SolrServer solrServer) {
        this.solrUrl = "";
        this.eadSearchUtil = new SolrSearchUtil(solrServer);
    }

    @Override
    public EadResponseSet search(SearchRequest searchRequest) throws SolrServerException {
        SolrQuery query = new SolrQuery(searchRequest.getQuery());
        query.setStart(searchRequest.getStart());
        if (searchRequest.getCount() <= 0) {
            query.setRows(10);
        } else {
            query.setRows(searchRequest.getCount());
        }

        this.eadSearchUtil.setQuery(query);

        QueryResponse response = this.eadSearchUtil.getSearchResponse();
        return new EadResponseSet(response);
    }
}
