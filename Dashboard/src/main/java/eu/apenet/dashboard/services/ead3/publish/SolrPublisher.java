/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import com.google.gson.Gson;
import eu.apenet.commons.solr.EacCpfSolrServerHolder;
import eu.apenet.commons.solr.Ead3SolrServerHolder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

/**
 *
 * @author mahbub
 */
public class SolrPublisher {
    
    private final Ead3SolrServerHolder ead3SolrCore = Ead3SolrServerHolder.getInstance();
//    private final EacCpfSolrServerHolder cpfSolrServerHolder = EacCpfSolrServerHolder.getInstance();
    
    protected static final Logger LOGGER = Logger.getLogger(SolrPublisher.class);
    
    public long publish(Iterator<SolrDocNode> docIterator) throws SolrServerException {
        List<SolrInputDocument> solrDocList = new ArrayList<>();
        while (docIterator.hasNext()) {
            SolrDocNode node = docIterator.next();
            Map<String, Object> nodeMap = node.getData();
            SolrInputDocument solrDoc = new SolrInputDocument();
            
            for (Entry<String, Object> entry : nodeMap.entrySet()) {
                solrDoc.addField(entry.getKey(), entry.getValue());
            }
            solrDocList.add(solrDoc);
//            this.publishEacCpf(node);
        }
        this.ead3SolrCore.add(solrDocList);
        return solrDocList.size();
    }
    
//    private long publishEacCpf(SolrDocNode node) throws SolrServerException {
//        List<SolrInputDocument> eacInputDocList = new ArrayList<>();
//        if (null != node.getEacData() && !node.getEacData().isEmpty()) {
//            LOGGER.info("Object EAC size " + node.getEacData().size());
//            for (Map<String, Object> eacNode : node.getEacData()) {
//                SolrInputDocument eacSolrDoc = new SolrInputDocument();
//                for (Entry<String, Object> entry : eacNode.entrySet()) {
//                    eacSolrDoc.addField(entry.getKey(), entry.getValue());
//                }
//                eacInputDocList.add(eacSolrDoc);
//            }
//            this.cpfSolrServerHolder.add(eacInputDocList);
//        }
//        return eacInputDocList.size();
//    }
    
    public void printTree(Iterator<SolrDocNode> docIterator) throws SolrServerException {
        while (docIterator.hasNext()) {
            SolrDocNode node = docIterator.next();
            System.out.println(new Gson().toJson(node));
        }
    }
    
    public void hardCommit() throws SolrServerException {
        this.ead3SolrCore.hardCommit();
    }
}
