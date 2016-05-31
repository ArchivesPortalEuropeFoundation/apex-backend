/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.archivesportaleurope.apeapi.request.SearchRequest;
import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import java.text.ParseException;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * Any search service will have to implement this interface.
 * @author Mahbub
 */
public interface SearchService {
    EadResponseSet search(SearchRequest request, String extraSearchParam) throws SolrServerException, ParseException;
    EadResponseSet searchOpenData(SearchRequest request) throws SolrServerException, ParseException;
}
