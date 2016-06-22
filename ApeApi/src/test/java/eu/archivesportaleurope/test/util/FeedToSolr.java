/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import java.io.IOException;
import java.util.Collection;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class FeedToSolr {

    final private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SolrServer server;

    public FeedToSolr(SolrServer server) {
        this.server = server;
    }

    public void feed() throws IOException, SolrServerException, InterruptedException {
        JsonToObject jsonToObject = new JsonToObject();
//        this.server.deleteByQuery("*:*");
        Collection<SolrInputDocument> docs = jsonToObject.getEadSolrDocs(jsonToObject.getObject("EadMockData.json", EadResponseSet.class));
        logger.info(":::::::::: docs number " + docs.size());
        logger.debug("Solr server got created! " + server.hashCode());
        this.server.add(docs);
        this.server.commit(false, false, false);
    }

}
