package eu.apenet.commons.solr;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import eu.apenet.commons.utils.APEnetUtilities;

public class UpdateSolrServerHolder {
	private static final int QUEUE_SIZE = 200;
	private static final int MAX_THREADS = 2;
	private static final Logger LOGGER = Logger.getLogger(UpdateSolrServerHolder.class);
	private ConcurrentUpdateSolrServer solrServer;
	private static UpdateSolrServerHolder instance;
	private String solrIndexUrl;

	private UpdateSolrServerHolder() {

	}

	public static UpdateSolrServerHolder getInstance() {
		if (instance == null) {
			instance = new UpdateSolrServerHolder();
		}
		return instance;
	}

	public boolean isAvailable() {
		initSolrServer();
		return solrServer != null;
	}

	public UpdateResponse deleteByQuery(String query) throws SolrServerException {
		if (isAvailable()) {
			try {
				return solrServer.deleteByQuery(query);
			} catch (Exception e) {
				throw new SolrServerException("Could not execute query: " + query, e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}

	public void add(Collection<SolrInputDocument> documents) throws SolrServerException {
		if (isAvailable()) {
			try {
				solrServer.add(documents);
			} catch (Exception e) {
				throw new SolrServerException("Could not add documents", e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}
	public void add(SolrInputDocument document) throws SolrServerException {
		if (isAvailable()) {
			try {
				solrServer.add(document);
			} catch (Exception e) {
				throw new SolrServerException("Could not add documents", e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}
	public UpdateResponse commit() throws SolrServerException {
		if (isAvailable()) {
			try {
				return solrServer.commit();
			} catch (Exception e) {
				throw new SolrServerException("Could not commit", e);
			}
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}

	private ConcurrentUpdateSolrServer initSolrServer() {
		try {
			LOGGER.debug("Create new solr client");
			solrIndexUrl = APEnetUtilities.getDashboardConfig().getSolrIndexUrl();
			solrServer = new ConcurrentUpdateSolrServer(solrIndexUrl, QUEUE_SIZE, MAX_THREADS);
		} catch (Exception e) {
			LOGGER.error("Solr server " + solrIndexUrl + " is not available", e);
		}
		return solrServer;
	}

}
