/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 * Any search service will have to implement this interface.
 *
 * @author Mahbub
 */
public abstract class EadSearchService extends SearchService {

    public abstract QueryResponse searchDocPerInstitute(InstituteDocRequest request);

    public abstract QueryResponse searchInstituteInGroup(int startIndex, int count);

    public abstract QueryResponse getEadList(SearchDocRequest searchRequest);
}
