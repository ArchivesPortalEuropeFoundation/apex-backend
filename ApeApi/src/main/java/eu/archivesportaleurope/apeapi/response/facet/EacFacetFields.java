/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerResponseDictionary;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
@XmlRootElement
public class EacFacetFields {

    private List<NameCountPair> country;
    private List<NameCountPair> repository;
    private List<NameCountPair> entityType;
    private List<NameCountPair> place;
    private List<NameCountPair> language;
    private List<NameCountPair> dateType;
    
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public EacFacetFields() {
        this.country = new ArrayList<>();
        this.repository = new ArrayList<>();
        this.entityType = new ArrayList<>();
        this.place = new ArrayList<>();
        this.language = new ArrayList<>();
        this.dateType = new ArrayList<>();
    }

    public EacFacetFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultEadListFacetSettings = FacetType.getDefaultEacCPfListFacetSettings();
        for (ListFacetSettings facetSettings : defaultEadListFacetSettings) {
            try {
                if (!facetSettings.getFacetType().isDate()) {
                    Object field = FieldUtils.readField(this, 
                            ServerResponseDictionary.getEadResponseFieldName(facetSettings.getFacetType().getName()), true);
                    Method setMethod = thisClass.getMethod("setField", List.class, FacetField.class);
                    setMethod.invoke(this, field, queryResponse.getFacetField(facetSettings.getFacetType().getName()));
                }
            } catch (Exception ex) {
                logger.debug("Reflecion exception: ", ex);
            }
        }
    }

    public List<NameCountPair> getCountry() {
        return country;
    }

    public void setCountry(List<NameCountPair> country) {
        this.country = country;
    }

    public List<NameCountPair> getRepository() {
        return repository;
    }

    public void setRepository(List<NameCountPair> repository) {
        this.repository = repository;
    }

    public List<NameCountPair> getEntityType() {
        return entityType;
    }

    public void setEntityType(List<NameCountPair> entityType) {
        this.entityType = entityType;
    }

    public List<NameCountPair> getPlace() {
        return place;
    }

    public void setPlace(List<NameCountPair> place) {
        this.place = place;
    }

    public List<NameCountPair> getLanguage() {
        return language;
    }

    public void setLanguage(List<NameCountPair> language) {
        this.language = language;
    }

    public List<NameCountPair> getDateType() {
        return dateType;
    }

    public void setDateType(List<NameCountPair> dateType) {
        this.dateType = dateType;
    }

    public void setField(List<NameCountPair> field, FacetField values) {
        if (values == null) {
            return;
        }
        List<FacetField.Count> counts = values.getValues();
        for (int i = 0; i < counts.size(); i++) {
            NameCountPair pair = new NameCountPair();
            FacetField.Count countObj = counts.get(i);
            String[] arr = countObj.getName().split(":");
            pair.setName(arr[0]);
            pair.setId(arr[arr.length - 1]);
            pair.setFrequency(countObj.getCount());
            field.add(pair);
        }
    }
}
