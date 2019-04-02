/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import eu.apenet.commons.solr.Ead3SolrFields;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kaisar
 */
public class SortFields {

    private enum SortFieldsEnum {
        date(Ead3SolrFields.START_DATE),
        title(Ead3SolrFields.SORTABLE_UNIT_TITLE),
        referenceCode(Ead3SolrFields.SORTABLE_UNIT_ID),
        findingAidNo(Ead3SolrFields.SORTABLE_RECORD_ID),
        orderId(Ead3SolrFields.ORDER_ID),
        id(Ead3SolrFields.ID);
        private final String solrFieldValue;

        private SortFieldsEnum(String solrFieldValue) {
            this.solrFieldValue = solrFieldValue;
        }

        public String getSolrFieldValue() {
            return this.solrFieldValue;
        }
    }

    private final Map<String, String> solrSortFieldMap = new HashMap<>();
    private Map<String, String> solrEACSortFieldMap = new HashMap<>();

    public SortFields() {
        this.setSolrSortFieldMap();
        this.setSolrEACSortFieldMap();
    }

    public Map<String, String> getSolrSortFieldMap() {
        return solrSortFieldMap;
    }

    public Set<String> getRepresentationFieldName() {
        return this.solrSortFieldMap.keySet();
    }

    public final void setSolrSortFieldMap() {
        for (SortFieldsEnum sortField : SortFieldsEnum.values()) {
            this.solrSortFieldMap.put(sortField.name(), sortField.getSolrFieldValue());
        }
    }

    public Map<String, String> getSolrEACSortFieldMap() {
        return solrEACSortFieldMap;
    }

    public Set<String> getRepresentationEacFieldName() {
        return this.solrEACSortFieldMap.keySet();
    }

    public final void setSolrEACSortFieldMap() {
        this.solrEACSortFieldMap.put(SortFieldsEnum.date.name(), SortFieldsEnum.date.getSolrFieldValue());
    }

}
