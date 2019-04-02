/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.common.datatypes;

import com.google.common.collect.ImmutableMap;
import eu.apenet.commons.solr.Ead3SolrFields;
import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;

/**
 *
 * @author mahbub
 */
public final class Ead3ResponseDictionary implements SolrApiResponseDictionary {

    private static final BidiMap EAD3_RESPONSE_DIC = new TreeBidiMap(ImmutableMap.builder()
            .put("repository", Ead3SolrFields.AI)
            .put("hasDigitalObject", Ead3SolrFields.DAO)
            .put("digitalObjectType", Ead3SolrFields.DAO_TYPE)
            .put("unitDateType", Ead3SolrFields.DATE_TYPE)
            .put("fromDate", Ead3SolrFields.START_DATE)
            .put("toDate", Ead3SolrFields.END_DATE)
            .build()
    );

    @Override
    public String getSolrFieldName(String responseFieldName) {
        if (EAD3_RESPONSE_DIC.containsKey(responseFieldName)) {
            return (String) EAD3_RESPONSE_DIC.get(responseFieldName);
        }
        return responseFieldName;
    }

    @Override
    public String getResponseFieldName(String sorlFieldName) {
        if (EAD3_RESPONSE_DIC.containsValue(sorlFieldName)) {
            return (String) EAD3_RESPONSE_DIC.getKey(sorlFieldName);
        }
        return sorlFieldName;
    }
}
