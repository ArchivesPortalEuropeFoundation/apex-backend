package eu.apenet.commons.solr;

import java.io.Serializable;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BinaryRequestWriter;
import org.apache.solr.client.solrj.impl.BinaryResponseParser;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.SolrParams;

import com.opensymphony.xwork2.ActionContext;

import eu.apenet.commons.utils.APEnetUtilities;

public class SearchSolrServerHolder {
	private static final String SOLR_NOT_AVAILABLE = "solr_search_not_available";
	private static final long WAIT_TIME = 30 * 3600 * 1000;
	private static final Logger LOGGER = Logger.getLogger(SearchSolrServerHolder.class);
	private static CommonsHttpSolrServer solrServer;
	private static boolean available = false;
	private static SearchSolrServerHolder instance;

	private SearchSolrServerHolder(){
		
	}
	public static SearchSolrServerHolder getInstance(){
		if (instance == null){
			instance = new SearchSolrServerHolder();
		}
		return instance;
	}
	
	public boolean isAvailable() {
		getSolrServer();
		return available;
	}

	public QueryResponse query(SolrParams query) throws SolrServerException {
		if (isAvailable()) {
			try {
				return solrServer.query(query);
			} catch (SolrServerException e) {
				throw new SolrServerException("Could not execute query: " + query.toString(), e);
			}
		} else {
			throw new SolrServerException("Server is not available");
		}
	}

	private CommonsHttpSolrServer getSolrServer() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		HolderInfo notAvailableObject = (HolderInfo) session.get(SOLR_NOT_AVAILABLE);
		if (notAvailableObject == null) {
			if (solrServer == null) {
				try {
					LOGGER.debug("Create new solr client");
					solrServer = new CommonsHttpSolrServer(APEnetUtilities.getDashboardConfig().getSolrSearchUrl());
					//solrServer.setRequestWriter(new XMLRequestWriter());
					solrServer.setParser(new XMLResponseParser());
					solrServer.ping();
					solrServer.setParser(new BinaryResponseParser());
					solrServer.setRequestWriter(new BinaryRequestWriter());
					available = true;
				} catch (Exception e) {
					session.put(SOLR_NOT_AVAILABLE, new HolderInfo(System.currentTimeMillis()));
					available = false;
					LOGGER.error(e.getMessage());
				}
			}
		} else {
			if (notAvailableObject.time == null || (System.currentTimeMillis() - notAvailableObject.time) > WAIT_TIME) {
				session.remove(SOLR_NOT_AVAILABLE);
				return getSolrServer();
			}
		}
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
