/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.archivesportaleurope.apeapi.response.common.AutocompletionResponseSet;
import eu.archivesportaleurope.apeapi.response.common.SuggestionResponseSet;
import eu.archivesportaleurope.apeapi.services.SuggestionService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.TermsResponse;

/**
 *
 * @author kaisar
 */
public class SuggestionServiceImpl implements SuggestionService {

    private final String solrUrl;
    private final String solrCore;
    private final SolrSearchUtil suggestionSearchUtil;

    private SuggestionServiceImpl(String solrCore) throws NamingException {
        this.solrUrl = InitialContext.doLookup("java:comp/env/solrHost");
        this.solrCore = solrCore;
        this.suggestionSearchUtil = new SolrSearchUtil(this.solrUrl, this.solrCore);
    }

    private SuggestionServiceImpl(String solrUrl, String solrCore) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        this.suggestionSearchUtil = new SolrSearchUtil(this.solrUrl, this.solrCore);
    }

    @Override
    public AutocompletionResponseSet autoComplete(String term) throws SolrServerException {
        TermsResponse termResponse = suggestionSearchUtil.getTermsResponse(term);
        AutocompletionResponseSet responseSet = new AutocompletionResponseSet();
        responseSet.add(termResponse, "archives");
        return responseSet;
    }

    @Override
    public SuggestionResponseSet suggest(String term) throws SolrServerException {
        SolrQuery query = new SolrQuery(term);
        this.suggestionSearchUtil.setQuery(query);
        return new SuggestionResponseSet(this.suggestionSearchUtil.getSuggestion());
    }

}
