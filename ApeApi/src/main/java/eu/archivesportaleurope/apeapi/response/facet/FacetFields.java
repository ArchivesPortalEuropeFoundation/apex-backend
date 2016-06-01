/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mahbub
 */
@XmlRootElement
public class FacetFields {

    private List<NameCountPair> country;
    private List<NameCountPair> topic;
    @JsonProperty("repository")
    private final List<NameCountPair> ai;
    private final List<NameCountPair> type;
    private final List<NameCountPair> level;
    @JsonProperty("hasDigitalObject")
    private final List<NameCountPair> dao;
    @JsonProperty("digitalObjectType")
    private final List<NameCountPair> roledao;
    private final List<NameCountPair> dateType;
    private final List<NameCountPair> startdate;
    private final List<NameCountPair> enddate;
    
    final private transient org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    public FacetFields() {
        this.country = new ArrayList<>();
        this.topic = new ArrayList<>();
        this.ai = new ArrayList<>();
        this.type = new ArrayList<>();
        this.level = new ArrayList<>();
        this.dao = new ArrayList<>();
        this.roledao = new ArrayList<>();
        this.dateType = new ArrayList<>();
        this.startdate = new ArrayList<>();
        this.enddate = new ArrayList<>();
    }
    
    public FacetFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultEadListFacetSettings = FacetType.getDefaultEadListFacetSettings();
        for (ListFacetSettings facetSettings : defaultEadListFacetSettings) {
            try {
                Object field = FieldUtils.readField(this, facetSettings.getFacetType().getName(), true);
                Method setMethod = thisClass.getMethod("setValue", List.class, FacetField.class);
                setMethod.invoke(this, field, queryResponse.getFacetField(facetSettings.getFacetType().getName()));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                logger.debug("Reflecion exception: ",ex);
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

    public List<NameCountPair> getStartdate() {
        return startdate;
    }

    public List<NameCountPair> getEnddate() {
        return enddate;
    }
    
    public void setValue(List<NameCountPair> field, FacetField values) {
        if (values == null) {
            return;
        }
        List<FacetField.Count> counts = values.getValues();
        for (int i = 0; i < counts.size(); i++) {
            NameCountPair tmpCountry = new NameCountPair();
            FacetField.Count countObj = counts.get(i);
            String[] arr = countObj.getName().split(":");
            tmpCountry.setName(arr[0]);
            tmpCountry.setId(arr[arr.length-1]);
            tmpCountry.setFrequency(countObj.getCount());
            field.add(tmpCountry);
        }
    }
}
