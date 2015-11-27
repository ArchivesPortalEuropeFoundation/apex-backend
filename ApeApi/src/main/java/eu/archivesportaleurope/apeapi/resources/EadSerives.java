/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

import com.google.gson.Gson;
import eu.archivesportaleurope.apeapi.utils.SolrSearchUtil;
import eu.archivesportaleurope.backend.apeapi.common.fieldDef.QueryParamNames;
import eu.archivesportaleurope.backend.apeapi.entity.ead.AutocompletionResults;
import eu.archivesportaleurope.backend.apeapi.entity.ead.EadSearchResults;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
@Path("/")
public class EadSerives {

    private final SolrSearchUtil eadUtil = new SolrSearchUtil("http://localhost:8080/solr", "eads");
    @Context
    UriInfo uriInfo;

    @GET
    @Path("/autocomplete")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getIt(@QueryParam(QueryParamNames.Q) @DefaultValue("") String q) {
        try {
            AutocompletionResults results = new AutocompletionResults();
            if (q.length() > 2) {
                results.add(eadUtil.getTermsResponse(q), "archievs");
            }
            return Response.ok(new Gson().toJson(results)).build();
        } catch (SolrServerException ex) {
            Logger.getLogger(EadSerives.class.getName()).log(Level.SEVERE, null, ex);
            return Response.noContent().build();
        }
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam(QueryParamNames.Q) @DefaultValue("") String q,
            @QueryParam(QueryParamNames.COUNT) @DefaultValue("10") Integer count,
            @QueryParam(QueryParamNames.START) @DefaultValue("0") Integer start) {
        String uri = uriInfo.getRequestUri().getRawPath();
        try {
            SolrQuery query = new SolrQuery(q);
            query.setStart(start);
            query.setRows(count);

            eadUtil.setQuery(query);
            QueryResponse response = eadUtil.getSearchResponse();
            return Response.ok(new Gson().toJson(new EadSearchResults(response, uri))).build();
        } catch (SolrServerException ex) {
            Logger.getLogger(EadSerives.class.getName()).log(Level.SEVERE, null, ex);
            return Response.noContent().build();
        }
    }
}
