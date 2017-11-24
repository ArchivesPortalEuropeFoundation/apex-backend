/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import java.io.IOException;
import java.util.Collection;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.embedded.EmbeddedSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.core.CoreContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class EmbeddedSolrManager {

    final private static transient Logger LOGGER = LoggerFactory.getLogger(EmbeddedSolrManager.class);

    public static <T> void setupData(String dataFile, String coreName, Class<T> type) throws IOException, SolrServerException, InterruptedException {
        CoreContainer coreContainer;
        String resource = "src/test/resources";
        String solrHome = resource + "/solr";

        coreContainer = new CoreContainer(solrHome);
        coreContainer.load();
        SolrClient solr = new EmbeddedSolrServer(coreContainer, coreName);

        solr.deleteByQuery("*:*");
        solr.commit();

        JsonToObject jsonToObject = new JsonToObject();
        Collection<SolrInputDocument> docs;
        docs = jsonToObject.getSolrDocs(jsonToObject.getObject(resource + dataFile, type));
        LOGGER.info("core: "+coreName+" :::::::::: docs number " + docs.size());
        solr.add(docs);
        solr.commit(true, true, false);
        solr.close();
    }

    public static void setupData(String dataFile, String coreName) throws SolrServerException, IOException {
        CoreContainer coreContainer;
        String resource = "src/test/resources";
        String solrHome = resource + "/solr";

        coreContainer = new CoreContainer(solrHome);
        coreContainer.load();
        SolrClient solr = new EmbeddedSolrServer(coreContainer, coreName);

        solr.deleteByQuery("*:*");
        solr.commit();

        JsonToObject jsonToObject = new JsonToObject();
        Collection<SolrInputDocument> docs;
        docs = jsonToObject.getSolrDocsFromGenericJson(resource + dataFile);
        LOGGER.info("core: "+coreName+" :::::::::: docs number " + docs.size());
        solr.add(docs);
        solr.commit(true, true, false);
        solr.close();
    }

}
