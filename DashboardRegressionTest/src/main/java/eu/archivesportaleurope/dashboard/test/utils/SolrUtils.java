/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.test.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;

/**
 *
 * @author kaisar
 */
public class SolrUtils {

    public static enum Cores {

        EAG("eags"), EAD("eads"), EAC_CPF("eac-cpfs");
        private final String coreName;

        private Cores(final String coreName) {
            this.coreName = coreName;
        }

        @Override
        public String toString() {
            return this.coreName;
        }
    }

    private String baseSolrUrl;
    private Logger logger = Logger.getLogger(SolrUtils.class.getName());

    private static final Properties PROPERTIES = new Properties();
    private static SolrUtils solrUtils;

    private SolrUtils() {
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
            PROPERTIES.load(is);
            this.baseSolrUrl = PROPERTIES.getProperty("baseSolrUrl", "http://localhost:8080/solr");
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public static SolrUtils getSolrUtil() {
        if (solrUtils == null) {
            solrUtils = new SolrUtils();
        }
        return solrUtils;
    }

    public String getBaseSolrUrl() {
        return baseSolrUrl;
    }

    public void setBaseSolrUrl(String baseSolrUrl) {
        this.baseSolrUrl = baseSolrUrl;
    }

    public void clearAllCore() {

        for (Cores c : Cores.values()) {
            clearCore(c);
        }
    }

    public void clearCore(Cores coreName) {
        HttpSolrServer solr = new HttpSolrServer(this.getBaseSolrUrl() + "/" + coreName.toString());
        logger.log(Level.INFO, "{0}/{1} is cleaning now!!!", new Object[]{this.getBaseSolrUrl(), coreName.toString()});
        try {
            solr.deleteByQuery("*:*");
        } catch (SolrServerException | IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    public QueryResponse getAllData(Cores coreName, String query) {
        HttpSolrServer solr = new HttpSolrServer(this.getBaseSolrUrl() + "/" + coreName.toString());
        QueryResponse response = null;
        try {
            if ("".equals(query)) {
                response = solr.query(new SolrQuery("*:*"));
            } else {
                response = solr.query(new SolrQuery(query));
            }
        } catch (SolrServerException ex) {
            Logger.getLogger(SolrUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return getSolrUtil();
    }

//    public static void main(String args[]) {
//        SolrUtils.getSolrUtil().clearAllCore();
//    }
}
