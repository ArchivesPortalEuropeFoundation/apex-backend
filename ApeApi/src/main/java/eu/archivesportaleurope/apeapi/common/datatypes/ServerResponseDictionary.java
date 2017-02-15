/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.common.datatypes;

import com.google.common.collect.ImmutableMap;
import eu.apenet.commons.solr.Ead3SolrFields;
import eu.apenet.commons.solr.SolrFields;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;

/**
 *
 * @author mahbub
 */
public final class ServerResponseDictionary {

    /**
     *
     */
    private static final BidiMap EAD_RESPONSE_DIC = new TreeBidiMap(ImmutableMap.builder()
            .put("subject", SolrFields.TOPIC_FACET)
            .put("repository", SolrFields.AI)
            .put("docType", SolrFields.TYPE)
            .put("hasDigitalObject", SolrFields.DAO)
            .put("digitalObjectType", SolrFields.ROLEDAO)
            .put("unitDateType", SolrFields.DATE_TYPE)
            .put("entityType", SolrFields.EAC_CPF_FACET_ENTITY_TYPE)
            .put("place", SolrFields.EAC_CPF_FACET_PLACES)
            .put("fromDate", SolrFields.START_DATE)
            .put("toDate", SolrFields.END_DATE)
            .build()
    );
    
    private static final BidiMap EAD3_RESPONSE_DIC = new TreeBidiMap(ImmutableMap.builder()
            .put("repository", Ead3SolrFields.AI)
            .put("hasDigitalObject", Ead3SolrFields.DAO)
            .put("digitalObjectType", Ead3SolrFields.DAO_TYPE)
            .put("unitDateType", Ead3SolrFields.DATE_TYPE)
            .put("fromDate", Ead3SolrFields.START_DATE)
            .put("toDate", Ead3SolrFields.END_DATE)
            .build()
    );

    public static String getEadSolrFieldName(String responseFieldName) {
        if (EAD_RESPONSE_DIC.containsKey(responseFieldName)) {
            return (String) EAD_RESPONSE_DIC.get(responseFieldName);
        }
        return responseFieldName;
    }

    public static String getEadResponseFieldName(String sorlFieldName) {
        if (EAD_RESPONSE_DIC.containsValue(sorlFieldName)) {
            return (String) EAD_RESPONSE_DIC.getKey(sorlFieldName);
        }
        return sorlFieldName;
    }
    
    public static String getEad3SolrFieldName(String responseFieldName) {
        if (EAD3_RESPONSE_DIC.containsKey(responseFieldName)) {
            return (String) EAD3_RESPONSE_DIC.get(responseFieldName);
        }
        return responseFieldName;
    }

    public static String getEad3ResponseFieldName(String sorlFieldName) {
        if (EAD3_RESPONSE_DIC.containsValue(sorlFieldName)) {
            return (String) EAD3_RESPONSE_DIC.getKey(sorlFieldName);
        }
        return sorlFieldName;
    }
}
