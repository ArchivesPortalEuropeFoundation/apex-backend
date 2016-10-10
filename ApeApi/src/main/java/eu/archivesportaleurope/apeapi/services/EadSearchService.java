/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.archivesportaleurope.apeapi.request.InstituteDocRequest;
import eu.archivesportaleurope.apeapi.request.PageRequest;
import eu.archivesportaleurope.apeapi.request.QueryPageRequest;
import eu.archivesportaleurope.apeapi.request.SearchDocRequest;
import eu.archivesportaleurope.apeapi.response.hierarchy.HierarchyResponseSet;
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

    public abstract QueryResponse getDescendants(String id, QueryPageRequest searchRequest);
    
    public abstract HierarchyResponseSet getAncestors(String id, PageRequest pageRequest);

    public abstract QueryResponse getChildren(String id, QueryPageRequest searchRequest);
    
    public abstract HierarchyResponseSet getChildren(String id, PageRequest searchRequest);
}
