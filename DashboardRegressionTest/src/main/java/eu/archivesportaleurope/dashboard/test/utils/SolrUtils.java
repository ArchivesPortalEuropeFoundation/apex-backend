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
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

/**
 *
 * @author kaisar
 */
public class SolrUtils {

    public static enum Cores {

        EAG("eag"), EAD("ead"), EAC_CPF("eac-cpfs");
        private final String coreName;

        private Cores(final String coreName) {
            this.coreName = coreName;
        }

        @Override
        public String toString() {
            return coreName;
        }
    }

    private String baseSolrUrl;

    private static final Properties PROPERTIES = new Properties();
    private static SolrUtils solrUtils;

    private SolrUtils() {
        try {
            InputStream is = ClassLoader.getSystemResourceAsStream("config.properties");
            PROPERTIES.load(is);
            this.baseSolrUrl = PROPERTIES.getProperty("baseSolrUrl", "http://localhost:8080");
        } catch (IOException ex) {
            Logger.getLogger(SolrUtils.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            solr.deleteByQuery("*:*");
        } catch (SolrServerException | IOException ex) {
            Logger.getLogger(SolrUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return getSolrUtil();
    }

}
