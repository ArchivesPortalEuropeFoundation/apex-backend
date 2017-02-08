/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.ServerResponseDictionary;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class Ead3FacetFields {

    private List<NameCountPair> country;
//    private List<NameCountPair> subject;
    private final List<NameCountPair> repository;
    private final List<NameCountPair> docType;
    private final List<NameCountPair> levelName;
    private final List<NameCountPair> hasDigitalObject;
    private final List<NameCountPair> digitalObjectType;
    private final List<NameCountPair> unitDateType;
//aiName
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public Ead3FacetFields() {
        this.country = new ArrayList<>();
//        this.subject = new ArrayList<>();
        this.repository = new ArrayList<>();
        this.docType = new ArrayList<>();
        this.levelName = new ArrayList<>();
        this.hasDigitalObject = new ArrayList<>();
        this.digitalObjectType = new ArrayList<>();
        this.unitDateType = new ArrayList<>();
    }

    public Ead3FacetFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultEad3ListFacetSettings = FacetType.getDefaultEad3ListFacetSettings();
        for (ListFacetSettings facetSettings : defaultEad3ListFacetSettings) {
            try {
                if (!facetSettings.getFacetType().isDate()) {
                    String name = ServerResponseDictionary.getEadResponseFieldName(facetSettings.getFacetType().getName());
                    Object field = FieldUtils.readField(this, name, true);
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

//    public List<NameCountPair> getSubject() {
//        return subject;
//    }

    public List<NameCountPair> getRepository() {
        return repository;
    }

    public List<NameCountPair> getDocType() {
        return docType;
    }

    public List<NameCountPair> getLevelName() {
        return levelName;
    }

    public List<NameCountPair> getHasDigitalObject() {
        return hasDigitalObject;
    }

    public List<NameCountPair> getDigitalObjectType() {
        return digitalObjectType;
    }

    public List<NameCountPair> getUnitDateType() {
        return unitDateType;
    }

    public void setField(List<NameCountPair> field, FacetField values) {
        if (values == null) {
            return;
        }
        List<FacetField.Count> counts = values.getValues();
        for (int i = 0; i < counts.size(); i++) {
            NameCountPair pair = new NameCountPair();
            FacetField.Count countObj = counts.get(i);
            String[] arr = countObj.getName().split("\\|");
            pair.setName(arr[0]);
            pair.setId(arr[arr.length - 1]);
            pair.setFrequency(countObj.getCount());
            field.add(pair);
        }
    }
}
