/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.archivesportaleurope.apeapi.common.datatypes.EadResponseDictionary;
import eu.archivesportaleurope.apeapi.common.datatypes.SolrApiResponseDictionary;
/**
 *
 * @author mahbub
 */
@XmlRootElement
public class EadFacetFields {
    
    private List<NameCountPair> country;
    private List<NameCountPair> subject;    
    private final List<NameCountPair> repository;    
    private final List<NameCountPair> docType;
    private final List<NameCountPair> levelName;    
    private final List<NameCountPair> hasDigitalObject;
    private final List<NameCountPair> digitalObjectType;
    private final List<NameCountPair> unitDateType;
    
    
    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private final transient SolrApiResponseDictionary dictionary = new EadResponseDictionary();
    
    public EadFacetFields() {
        this.country = new ArrayList<>();
        this.subject = new ArrayList<>();
        this.repository = new ArrayList<>();
        this.docType = new ArrayList<>();
        this.levelName = new ArrayList<>();
        this.hasDigitalObject = new ArrayList<>();
        this.digitalObjectType = new ArrayList<>();
        this.unitDateType = new ArrayList<>();
    }

    public EadFacetFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultEadListFacetSettings = FacetType.getDefaultEadListFacetSettings();
        for (ListFacetSettings facetSettings : defaultEadListFacetSettings) {
            try {
                if (!facetSettings.getFacetType().isDate()) {
                    String name = dictionary.getResponseFieldName(facetSettings.getFacetType().getName());
                    Object field = FieldUtils.readField(this, name, true);
                    //Will refelct one of the above List<NameCountPair> fields, so filed object will hold a list.class type
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

    public List<NameCountPair> getSubject() {
        return subject;
    }

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
            String[] arr = countObj.getName().split(":");
            pair.setName(arr[0]);
            pair.setId(arr[arr.length - 1]);
            pair.setFrequency(countObj.getCount());
            field.add(pair);
        }
    }
}
