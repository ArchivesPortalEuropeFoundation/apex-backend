package eu.apenet.commons.solr;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.dashboard.queue.ManageQueueAction;

public final class SolrUtil {

    private static final Logger LOGGER = Logger.getLogger(ManageQueueAction.class);

    public static void forceSolrCommit() throws SolrServerException {
        LOGGER.info("=== Start hard commit solr cores ===");
        LOGGER.info("EAG hard commit started...");
        EagSolrServerHolder.getInstance().hardCommit();
        LOGGER.info("EAC-CPF hard commit started...");
        EacCpfSolrServerHolder.getInstance().hardCommit();
        LOGGER.info("EAD hard commit started...");
        EadSolrServerHolder.getInstance().hardCommit();
        LOGGER.info("EAD3 hard commit started...");
        Ead3SolrServerHolder.getInstance().hardCommit();
        LOGGER.info("=== Hard commit solr cores finished ===");
    }

    public static void solrOptimize() throws SolrServerException {
        LOGGER.info("=== Start optimize solr cores ===");
        LOGGER.info("EAG optimize started...");
        EagSolrServerHolder.getInstance().optimize();
        LOGGER.info("EAC-CPF optimize started...");
        EacCpfSolrServerHolder.getInstance().optimize();
        LOGGER.info("EAD optimize started...");
        EadSolrServerHolder.getInstance().optimize();
        LOGGER.info("EAD3 optimize started...");
        Ead3SolrServerHolder.getInstance().optimize();
        LOGGER.info("=== Optimize solr cores finished ===");
    }

    public static void rebuildAutosuggestion() throws SolrServerException {
        LOGGER.info("=== Rebuild spellchecker dictionaries ===");
        LOGGER.info("EAG rebuild spellchecker dictionary started...");
        EagSolrServerHolder.getInstance().rebuildSpellchecker();
        LOGGER.info("EAC-CPF rebuild spellchecker dictionary started...");
        EacCpfSolrServerHolder.getInstance().rebuildSpellchecker();
        LOGGER.info("EAD rebuild spellchecker dictionary started...");
        EadSolrServerHolder.getInstance().rebuildSpellchecker();
        LOGGER.info("EAD3 rebuild spellchecker dictionary started...");
        Ead3SolrServerHolder.getInstance().rebuildSpellchecker();
        LOGGER.info("=== Rebuild spellchecker dictionaries finished ===");
    }
}
