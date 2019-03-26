/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.resources;

//import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author sowro
 */
public interface ApiService {
    Response serve() throws SolrServerException;
}
