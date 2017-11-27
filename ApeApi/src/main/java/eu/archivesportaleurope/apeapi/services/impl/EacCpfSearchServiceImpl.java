/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services.impl;

import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.EadResponseDictionary;
import eu.archivesportaleurope.apeapi.exceptions.InternalErrorException;
import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.utils.PropertiesUtil;
import eu.archivesportaleurope.apeapi.services.SearchService;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class EacCpfSearchServiceImpl extends SearchService {

    private String solrUrl;
    private final String onlyOpenData = "openData:true";
    private final SolrSearchUtil eacSearchUtil;
    private final PropertiesUtil propertiesUtil;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<String, String> extraParam;

    public EacCpfSearchServiceImpl(String solrUrl, String solrCore, String propFileName) {
        this.extraParam = new HashMap<>();
        this.solrUrl = solrUrl;
        logger.debug("Solr server got created!");
        this.eacSearchUtil = new SolrSearchUtil(solrUrl, solrCore);
        this.propertiesUtil = new PropertiesUtil(propFileName);
    }

    public EacCpfSearchServiceImpl(SolrClient solrServer, String propFileName) {
        this.extraParam = new HashMap<>();
        this.solrUrl = "";
        this.eacSearchUtil = new SolrSearchUtil(solrServer);
        //ToDo: quick fix for solrj5 EmbeddedSolrServer, which is failing to use default core
        if (solrServer instanceof EmbeddedSolrServer) {
            EmbeddedSolrServer embeddedSolrServer = (EmbeddedSolrServer) solrServer;
            Collection<String> coreNames = embeddedSolrServer.getCoreContainer().getAllCoreNames();
            if (!coreNames.isEmpty()) {
                this.eacSearchUtil.setCoreName(coreNames.iterator().next());
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
    public QueryResponse search(SearchRequest searchRequest, Map<String, String>  extraSearchParam, boolean includeFacet) {

        try {
            List<ListFacetSettings> facetSettingsList = null;
            if (includeFacet) {
                facetSettingsList = FacetType.getDefaultEacCPfListFacetSettings();
            }
            return this.search(searchRequest, extraSearchParam, facetSettingsList, propertiesUtil, eacSearchUtil, new EadResponseDictionary());

        } catch (InternalErrorException ex) {
            throw new InternalErrorException("Solarserver Exception", ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public QueryResponse searchOpenData(SearchRequest request) {
        extraParam.clear();
        extraParam.put("q", this.onlyOpenData);
        return this.search(request, extraParam, true);
    }
}
