/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import eu.archivesportaleurope.apeapi.response.ead.EadResponseSet;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import org.apache.commons.io.FileUtils;
import org.apache.solr.client.solrj.SolrServer;
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

    public static void setupData(String dataFile, String coreName) throws IOException, SolrServerException, InterruptedException {
        CoreContainer coreContainer;
        String resource = "src/test/resources";
        String solrHome = resource + "/solr";
        
        File index = new File (solrHome + "/" + coreName + "/data/index/"); 
        if (index.exists()) { 
            FileUtils.cleanDirectory(index); 
        } 
        
        coreContainer = new CoreContainer(solrHome);
        coreContainer.load();
        SolrServer solr = new EmbeddedSolrServer(coreContainer, coreName);

        JsonToObject jsonToObject = new JsonToObject();
        Collection<SolrInputDocument> docs = jsonToObject.getEadSolrDocs(jsonToObject.getObject(resource+dataFile, EadResponseSet.class));
        LOGGER.info(":::::::::: docs number " + docs.size());
        solr.add(docs);
        solr.commit(true, true, false);
        solr.shutdown();
    }

}
