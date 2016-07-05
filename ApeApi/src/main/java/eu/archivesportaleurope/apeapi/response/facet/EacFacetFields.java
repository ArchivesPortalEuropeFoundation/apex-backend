/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
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
    @JsonProperty("repository")
    private List<NameCountPair> ai;
    @JsonProperty("entityType")
    private List<NameCountPair> entityTypeFacet;
    @JsonProperty("place")
    private List<NameCountPair> placesFacet;
    private List<NameCountPair> language;
    private List<NameCountPair> dateType;

    private static final transient Map<String, String> FIELDNAMES = new HashMap<>();
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    static {
        Field[] fields = EacFacetFields.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonProperty.class)) {
                String annotationValue = field.getAnnotation(JsonProperty.class).value();
                FIELDNAMES.put(annotationValue, field.getName());
            } else {
                FIELDNAMES.put(field.getName(), field.getName());
            }
        }
    }

    public EacFacetFields() {
        this.country = new ArrayList<>();
        this.ai = new ArrayList<>();
        this.entityTypeFacet = new ArrayList<>();
        this.placesFacet = new ArrayList<>();
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
                    Object field = FieldUtils.readField(this, facetSettings.getFacetType().getName(), true);
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

    public List<NameCountPair> getAi() {
        return ai;
    }

    public void setAi(List<NameCountPair> ai) {
        this.ai = ai;
    }

    public List<NameCountPair> getEntityTypeFacet() {
        return entityTypeFacet;
    }

    public void setEntityTypeFacet(List<NameCountPair> entityTypeFacet) {
        this.entityTypeFacet = entityTypeFacet;
    }

    public List<NameCountPair> getPlacesFacet() {
        return placesFacet;
    }

    public void setPlacesFacet(List<NameCountPair> placesFacet) {
        this.placesFacet = placesFacet;
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

    public static String getOriginalFieldName(String annotName) {
        return FIELDNAMES.get(annotName);
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
