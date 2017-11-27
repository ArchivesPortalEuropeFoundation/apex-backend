/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.SolrQueryBuilder;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.Ead3ResponseDictionary;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.Ead3SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class Ead3SearchServiceImpl extends Ead3SearchService {

    private String solrUrl;
    private final String solrCore;
    private final String onlyOpenData = "openData:true";
    private final String solrAND = " AND ";
    private final SolrSearchUtil searchUtil;
    private final PropertiesUtil propertiesUtil;
    private final SolrQueryBuilder queryBuilder = new SolrQueryBuilder();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, String> extraParam = new HashMap<>();

    public Ead3SearchServiceImpl(String solrUrl, String solrCore, String propFileName) {
        this.solrUrl = solrUrl;
        this.solrCore = solrCore;
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public Ead3SearchServiceImpl(SolrClient solrServer, String propFileName) {
        this.solrUrl = this.solrCore = "";
        logger.debug("Solr server got created!");
        this.searchUtil = new SolrSearchUtil(solrServer);
        //ToDo: quick fix for solrj5 EmbeddedSolrServer, which is failing to use default core
        if (solrServer instanceof EmbeddedSolrServer) {
            EmbeddedSolrServer embeddedSolrServer = (EmbeddedSolrServer) solrServer;
            Collection<String> coreNames = embeddedSolrServer.getCoreContainer().getAllCoreNames();
            if (!coreNames.isEmpty()) {
                this.searchUtil.setCoreName(coreNames.iterator().next());
            }
        }
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public String getSolrUrl() {
        return solrUrl;
    }

    public void setSolrUrl(String solrUrl) {
        this.solrUrl = solrUrl;
    }

    @Override
    public QueryResponse search(SearchRequest request, Map<String, String> extraSearchParam, boolean includeFacet) {
        List<ListFacetSettings> facetSettingsList = null;
        if (includeFacet) {
            facetSettingsList = FacetType.getDefaultEad3ListFacetSettings();
        }
        return this.search(request, extraSearchParam, facetSettingsList, this.propertiesUtil, this.searchUtil, new Ead3ResponseDictionary());
    }

    @Override
    public QueryResponse searchOpenData(SearchRequest request) {
        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);
        return this.search(request, extraParam, true);
    }

}
