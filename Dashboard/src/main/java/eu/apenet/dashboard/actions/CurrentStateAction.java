package eu.apenet.dashboard.actions;

import java.sql.Connection;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import com.opensymphony.xwork2.ActionSupport;
import eu.apenet.commons.utils.APEnetUtilities;
import eu.apenet.commons.utils.NFSChecker;

/**
 * 
 * @author eloy, jara, paul, patricia
 *
 **/

// This class is in charge of checking the current state for
// all the servers and services needed to assure a proper
// behavior for Archives Portal Europe system (only Portal)
public class CurrentStateAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Logger log = Logger.getLogger(getClass());
	
	private String currentStateDDBB;
	private String currentStateNFS;
	private String currentStateTOMCAT;
	private String currentStateSOLR;
	
	
	public String getCurrentStateDDBB() {
		return currentStateDDBB;
	}



	public void setCurrentStateDDBB(String currentStateDDBB) {
		this.currentStateDDBB = currentStateDDBB;
	}



	public String getCurrentStateNFS() {
		return currentStateNFS;
	}



	public void setCurrentStateNFS(String currentStateNFS) {
		this.currentStateNFS = currentStateNFS;
	}



	public String getCurrentStateTOMCAT() {
		return currentStateTOMCAT;
	}



	public void setCurrentStateTOMCAT(String currentStateTOMCAT) {
		this.currentStateTOMCAT = currentStateTOMCAT;
	}



	public String getCurrentStateSOLR() {
		return currentStateSOLR;
	}



	public void setCurrentStateSOLR(String currentStateSOLR) {
		this.currentStateSOLR = currentStateSOLR;
	}



	public String execute() throws Exception{
		
		// Checking DDBB state
		try {
			
			String driver = "org.postgresql.Driver";
			String connectString = "jdbc:postgresql://11.22.33.53:5432/apenet";
			String user = "apenet_dashboard";
			String password = "AP3n3tSQLD4sh";
			
	        Connection databaseConnection = null;
	        Class.forName(driver).newInstance();
			databaseConnection = java.sql.DriverManager.getConnection(connectString, user, password);
			databaseConnection.close();
			databaseConnection = null;
			this.currentStateDDBB = "SUCCESS";
						
		}catch(Exception e){
			log.error("Database error: " + e);
			this.currentStateDDBB = "ERROR";
		}
		
		// Checking Solr index state
		try{
			
			CommonsHttpSolrServer solrServer;
			//First test: The Solr URL is checked
			solrServer = new CommonsHttpSolrServer(APEnetUtilities.getApePortalAndDashboardConfig().getSolrSearchUrl());			
			solrServer.ping();
			solrServer = null;
			//Second test: The Solr URL using a query is checked
			//solrServer = new CommonsHttpSolrServer("http://11.22.33.53:8080/solr/Dashboard0/select/?hl=true&facet.field=country&facet.field=ai&facet.field=type&facet.field=language&facet.field=dao&facet=true&f.startdate.facet.date.start=0200-01-01T00%3A00%3A00Z&f.startdate.facet.date.end=NOW&f.startdate.facet.date.gap=%2B200YEARS&f.enddate.facet.date.start=0200-01-01T00%3A00%3A00Z&f.enddate.facet.date.end=NOW&f.enddate.facet.date.gap=%2B200YEARS&facet.date=startdate&facet.date=enddate&facet.date.include=lower&start=0&rows=20&facet.limit=10&facet.mincount=1&sort=startdate+asc%2Cenddate+asc&q=pares&qt=context");			
			//solrServer.ping();
			//solrServer = null;

			this.currentStateSOLR = "SUCCESS";
		
		}catch(Exception e){
			log.error("SolR error: " + e);
			this.currentStateSOLR = "ERROR";
		}
		
		// If this page is displayed, then Tomcat server is working properly
		this.currentStateTOMCAT = "SUCCESS";
		
		// Checking NFS repository state	
		// assuming no return value required
		try {
			FutureTask<?> checkNFSstateTask = null;
		    // creating new task
			NFSChecker nfsChecker = new NFSChecker();
		    checkNFSstateTask = new FutureTask<Object>(nfsChecker, null);

		    // starting task in a new thread
		    new Thread(checkNFSstateTask).start();

		    // waiting for the execution to finish, timeout after 10 secs 
		    checkNFSstateTask.get(10L, TimeUnit.SECONDS);
		    
		    this.currentStateNFS = nfsChecker.getCurrentStateNFS();
		    nfsChecker = null;
		    checkNFSstateTask = null;
		}
		catch (TimeoutException e) {
		    // handle timeout
			this.currentStateNFS = "ERROR";
		}
		
		return SUCCESS;
	}

	
}