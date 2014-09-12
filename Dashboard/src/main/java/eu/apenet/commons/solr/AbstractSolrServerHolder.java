package eu.apenet.commons.solr;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

public abstract class AbstractSolrServerHolder {

	private final static Logger LOGGER = Logger.getLogger(AbstractSolrServerHolder.class);
	protected abstract String getSolrUrl();
	private HttpSolrServer solrServer;
	
	public boolean isAvailable() {
		initSolrServer();
		return solrServer != null;
	}

	public long deleteByQuery(String query) throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.deleteByQuery(query);
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not execute query: " + query, e);
			}
		} else {
			throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
		}
	}

	public long add(Collection<SolrInputDocument> documents) throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.add(documents);
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not add documents", e);
			}
		} else {
			throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
		}
	}

	public long hardCommit() throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				solrServer.commit(true, true, false);
				LOGGER.info("hardcommit: " + (System.currentTimeMillis() - startTime) + "ms");
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not commit", e);
			}
		} else {
			throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
		}
	}
	private HttpSolrServer initSolrServer() {
		try {
			if (solrServer == null) {
				LOGGER.debug("Create new solr client: " + getSolrUrl());
				solrServer = new HttpSolrServer(getSolrUrl());
			}
		} catch (Exception e) {
			LOGGER.error("Solr server " + getSolrUrl() + " is not available", e);
		}
		return solrServer;
	}
	public long rebuildSpellchecker() throws SolrServerException {
		if (isAvailable()) {
			try {
				long startTime = System.currentTimeMillis();
				SolrQuery query = new SolrQuery();
				query.setRows(0);
				query.setQuery("*:*");
				query.setRequestHandler("list");
				query.set("spellcheck", "on");
				query.set("spellcheck.build", "true");
				query.set("spellcheck.count", "0");
				solrServer.query(query);
				LOGGER.info("rebuild spellchecker of " + getSolrUrl() + ": " + (System.currentTimeMillis() - startTime) + "ms");
				return System.currentTimeMillis() - startTime;
			} catch (Exception e) {
				throw new SolrServerException("Could not rebuild spellchecker", e);
			}
		} else {
			throw new SolrServerException("Solr server " + getSolrUrl() + " is not available");
		}
	}

}
