package eu.apenet.commons.solr;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

import eu.apenet.commons.utils.APEnetUtilities;

public class UpdateSolrServerHolder {
	private static final String SOLR_NOT_AVAILABLE = "solr_index_not_available";
	private static final long WAIT_TIME = 30 * 3600 * 1000;
	private static final Logger LOGGER = Logger.getLogger(UpdateSolrServerHolder.class);
	private CommonsHttpSolrServer solrServer;
	private boolean available = false;
	private static UpdateSolrServerHolder instance;
	private String solrIndexUrl;

	private UpdateSolrServerHolder(){
		
	}
	public static UpdateSolrServerHolder getInstance(){
		if (instance == null){
			instance = new UpdateSolrServerHolder();
		}
		return instance;
	}
	public boolean isAvailable() {
		getSolrServer();
		return available;
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
	public void add(Collection<SolrInputDocument> documents) throws SolrServerException{
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
	@Deprecated
	public UpdateResponse rollback() throws SolrServerException {
		if (isAvailable()) {
			try {
				return solrServer.rollback();
			} catch (Exception e) {
				throw new SolrServerException("Could not rollback", e);
			} 
		} else {
			throw new SolrServerException("Solr server " + solrIndexUrl + " is not available");
		}
	}
//	public QueryResponse query(SolrParams query) throws SolrServerException {
//		if (isAvailable()) {
//			try {
//				return solrServer.query(query);
//			} catch (SolrServerException e) {
//				throw new SolrServerException("Could not execute query: " + query.toString(), e);
//			}
//		} else {
//			throw new SolrServerException("Server is not available");
//		}
//	}

	private CommonsHttpSolrServer getSolrServer() {
		//Map<String, Object> session = ActionContext.getContext().getSession();
		//HolderInfo notAvailableObject = (HolderInfo) session.get(SOLR_NOT_AVAILABLE);
//		if (notAvailableObject == null) {
			if (solrServer == null) {
				try {
					LOGGER.debug("Create new solr client");
					solrIndexUrl = APEnetUtilities.getDashboardConfig().getSolrIndexUrl();
					solrServer = new CommonsHttpSolrServer(solrIndexUrl);
					//solrServer.setRequestWriter(new XMLRequestWriter());
					solrServer.setParser(new XMLResponseParser());
					solrServer.ping();
					solrServer.setParser(new BinaryResponseParser());
					solrServer.setRequestWriter(new BinaryRequestWriter());
					available = true;
				} catch (Exception e) {
					//session.put(SOLR_NOT_AVAILABLE, new HolderInfo(System.currentTimeMillis()));
					available = false;
					LOGGER.error("Solr server " + solrIndexUrl + " is not available",e);
				}
			}
//		} else {
//			if (notAvailableObject.time == null || (System.currentTimeMillis() - notAvailableObject.time) > WAIT_TIME) {
//				//session.remove(SOLR_NOT_AVAILABLE);
//				return getSolrServer();
//			}
//		}
		return solrServer;
	}

	private static class HolderInfo implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5543153624623825258L;

		transient Long time;
		public HolderInfo(Long time) {
			this.time = time;
		}
	}
}
