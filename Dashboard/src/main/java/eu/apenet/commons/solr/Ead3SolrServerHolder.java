/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.commons.solr;

import eu.apenet.commons.utils.APEnetUtilities;

/**
 *
 * @author kaisar
 */
public class Ead3SolrServerHolder extends AbstractSolrServerHolder {

    private static Ead3SolrServerHolder instance;

    private Ead3SolrServerHolder() {
    }

    public static Ead3SolrServerHolder getInstance() {
        if (instance == null) {
            instance = new Ead3SolrServerHolder();
        }
        return instance;
    }

    @Override
    public String getSolrUrl() {
        return APEnetUtilities.getDashboardConfig().getBaseSolrIndexUrl() + "/ead3s";
    }

}
