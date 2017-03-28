/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.common.datatypes;

/**
 * solr filed names 
 * @author mahbub
 */
public interface SolrApiResponseDictionary {
    String getSolrFieldName(String responseFieldName);
    String getResponseFieldName(String sorlFieldName);
}
