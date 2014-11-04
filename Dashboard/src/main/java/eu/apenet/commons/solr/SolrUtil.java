package eu.apenet.commons.solr;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import eu.apenet.dashboard.queue.ManageQueueAction;

public final class SolrUtil {
	private static final Logger LOGGER = Logger.getLogger(ManageQueueAction.class);

	public static void forceSolrCommit() throws SolrServerException {
		LOGGER.info("=== Start hard commit solr cores ===");
		EagSolrServerHolder.getInstance().hardCommit();
		LOGGER.info("EAG hard commit finished");		
		EacCpfSolrServerHolder.getInstance().hardCommit();
		LOGGER.info("EAC-CPF hard commit finished");
		EadSolrServerHolder.getInstance().hardCommit();
		LOGGER.info("EAD hard commit finished");
		LOGGER.info("=== Hard commit solr cores finished ===");
	}

	public static void solrOptimize() throws SolrServerException {
		LOGGER.info("=== Start optimize solr cores ===");
		EagSolrServerHolder.getInstance().optimize();
		LOGGER.info("EAG optimize finished");		
		EacCpfSolrServerHolder.getInstance().optimize();
		LOGGER.info("EAC-CPF optimize finished");
		EadSolrServerHolder.getInstance().optimize();
		LOGGER.info("EAD optimize finished");
		LOGGER.info("=== Optimize solr cores finished ===");
	}
	
	public static void rebuildAutosuggestion() throws SolrServerException{
		LOGGER.info("=== Rebuild spellchecker dictionaries ===");
		EagSolrServerHolder.getInstance().rebuildSpellchecker();
		LOGGER.info("EAG rebuild spellchecker dictionary finished");		
		EacCpfSolrServerHolder.getInstance().rebuildSpellchecker();
		LOGGER.info("EAC-CPF rebuild spellchecker dictionary finished");
		EadSolrServerHolder.getInstance().rebuildSpellchecker();
		LOGGER.info("EAD rebuild spellchecker dictionary finished");		
		LOGGER.info("=== Rebuild spellchecker dictionaries finished ===");
	}
}
