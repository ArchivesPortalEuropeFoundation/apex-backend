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
public final class EadResponseDictionary implements SolrApiResponseDictionary {

    /**
     *
     */
    private static final BidiMap EAD_RESPONSE_DIC = new TreeBidiMap(ImmutableMap.builder()
            .put("subject", Ead3SolrFields.TOPIC)
            .put("repository", Ead3SolrFields.AI)
            .put("docType", Ead3SolrFields.RECORD_TYPE)
            .put("hasDigitalObject", Ead3SolrFields.DAO)
            .put("digitalObjectType", Ead3SolrFields.DAO_TYPE)
            .put("unitDateType", Ead3SolrFields.DATE_TYPE)
            .put("entityType", SolrFields.EAC_CPF_FACET_ENTITY_TYPE)
            .put("place", SolrFields.EAC_CPF_FACET_PLACES)
            .put("fromDate", Ead3SolrFields.START_DATE)
            .put("toDate", Ead3SolrFields.END_DATE)
            .build()
    );

    @Override
    public String getSolrFieldName(String responseFieldName) {
        if (EAD_RESPONSE_DIC.containsKey(responseFieldName)) {
            return (String) EAD_RESPONSE_DIC.get(responseFieldName);
        }
        return responseFieldName;
    }

    @Override
    public String getResponseFieldName(String sorlFieldName) {
        if (EAD_RESPONSE_DIC.containsValue(sorlFieldName)) {
            return (String) EAD_RESPONSE_DIC.getKey(sorlFieldName);
        }
        return sorlFieldName;
    }
}
