/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.common.datatypes;

import com.google.common.collect.ImmutableMap;
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
    private static final BidiMap RESPONSE_DIC = new TreeBidiMap(ImmutableMap.builder()
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

    public static String getSolrFieldName(String responseFieldName) {
        if (RESPONSE_DIC.containsKey(responseFieldName)) {
            return (String) RESPONSE_DIC.get(responseFieldName);
        }
        return responseFieldName;
    }

    public static String getResponseFiledName(String sorlFieldName) {
        if (RESPONSE_DIC.containsValue(sorlFieldName)) {
            return (String) RESPONSE_DIC.getKey(sorlFieldName);
        }
        return sorlFieldName;
    }
}
