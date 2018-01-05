/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.services;

import eu.archivesportaleurope.apeapi.response.common.AutocompletionResponseSet;
import eu.archivesportaleurope.apeapi.response.common.SuggestionResponseSet;
import java.io.IOException;
import org.apache.solr.client.solrj.SolrServerException;

/**
 *
 * @author kaisar
 */
public interface SuggestionService {

    AutocompletionResponseSet autoComplete(String term) throws SolrServerException, IOException;

    SuggestionResponseSet suggest(String term) throws SolrServerException, IOException;
}
