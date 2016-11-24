/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import eu.apenet.commons.solr.Ead3SolrServerHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author mahbub
 */
public class SolrPublisher {
    private final Ead3SolrServerHolder ead3SolrCore = Ead3SolrServerHolder.getInstance();
    
    public void publish(Iterator<SolrDocNode> docIterator) throws SolrServerException {
        List<SolrInputDocument> solrDocList = new ArrayList<>();
        while(docIterator.hasNext()) {
            SolrDocNode node = docIterator.next();
            Map<String, Object> nodeMap = node.getData();
            SolrInputDocument sorlDoc = new SolrInputDocument();
            
            for (Entry<String, Object> entry : nodeMap.entrySet()) {
                sorlDoc.addField(entry.getKey(), entry.getValue());
            }
            solrDocList.add(sorlDoc);
        }
        this.ead3SolrCore.add(solrDocList);
    }
    
    public void hardCommit() throws SolrServerException {
        this.ead3SolrCore.hardCommit();
    }
}
