/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import eu.apenet.commons.solr.DateGap;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.FacetValue;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
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
 * @author mahbub
 */
@XmlRootElement
public class EadFacetFields {

    private List<NameCountPair> country;
    @JsonProperty("subject")
    private List<NameCountPair> topic;
    @JsonProperty("repository")
    private final List<NameCountPair> ai;
    @JsonProperty("docType")
    private final List<NameCountPair> type;
    private final List<NameCountPair> level;
    @JsonProperty("hasDigitalObject")
    private final List<NameCountPair> dao;
    @JsonProperty("digitalObjectType")
    private final List<NameCountPair> roledao;
    @JsonProperty("unitDateType")
    private final List<NameCountPair> dateType;
    
    private static final transient Map<String, String> FIELDNAMES = new HashMap<>();
    
    static {
        Field[] fields = EadFacetFields.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonProperty.class)) {
                String annotationValue = field.getAnnotation(JsonProperty.class).value();
                FIELDNAMES.put(annotationValue, field.getName());
            } else {
                FIELDNAMES.put(field.getName(), field.getName());
            }
        }
    }

    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public EadFacetFields() {
        this.country = new ArrayList<>();
        this.topic = new ArrayList<>();
        this.ai = new ArrayList<>();
        this.type = new ArrayList<>();
        this.level = new ArrayList<>();
        this.dao = new ArrayList<>();
        this.roledao = new ArrayList<>();
        this.dateType = new ArrayList<>();
    }

    public EadFacetFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultEadListFacetSettings = FacetType.getDefaultEadListFacetSettings();
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

    public List<NameCountPair> getTopic() {
        return topic;
    }

    public List<NameCountPair> getAi() {
        return ai;
    }

    public List<NameCountPair> getType() {
        return type;
    }

    public List<NameCountPair> getLevel() {
        return level;
    }

    public List<NameCountPair> getDao() {
        return dao;
    }

    public List<NameCountPair> getRoledao() {
        return roledao;
    }

    public List<NameCountPair> getDateType() {
        return dateType;
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
