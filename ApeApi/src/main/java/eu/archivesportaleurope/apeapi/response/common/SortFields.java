/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kaisar
 */
public class SortFields {

    private enum SortFieldsEnum {
        date("startdate"),
        title("title"),
        referenceCode("unitid"),
        findingAidNo("unitidfond");
        private final String solrFieldValue;

        private SortFieldsEnum(String solrFieldValue) {
            this.solrFieldValue = solrFieldValue;
        }

        public String getSolrFieldValue() {
            return this.solrFieldValue;
        }
    }

    private final Map<String, String> solrSortFieldMap = new HashMap<>();

    public SortFields() {
        this.setSolrSortFieldMap();
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

}
