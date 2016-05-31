/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mahbub
 */
@XmlRootElement
public class FacetFields {

    private final List<Country> country;
    final private transient org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());
    public FacetFields() {
        this.country = new ArrayList<>();
    }
    
    public FacetFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = FacetFields.class;
        List<ListFacetSettings> defaultEadListFacetSettings = FacetType.getDefaultEadListFacetSettings();
        for (ListFacetSettings facetSettings : defaultEadListFacetSettings) {
            try {
                Method setMethod = thisClass.getMethod("set"+StringUtils.capitalize(facetSettings.getFacetType().getName()), String.class);
                setMethod.invoke(this, queryResponse.getFacetField(facetSettings.getFacetType().getName()));
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                logger.debug("Reflecion exception: ",ex);
            }
        }
    }

    public List<Country> getCountry() {
        return country;
    }
    
    public void setCountry(FacetField countryValues) {
        if (countryValues == null) {
            return;
        }
        List<FacetField.Count> counts = countryValues.getValues();
        for (int i = 0; i < counts.size(); i++) {
            Country tmpCountry = new Country();
            FacetField.Count countObj = counts.get(i);
            String[] arr = countObj.getName().split(":");
            tmpCountry.setName(arr[0]);
            tmpCountry.setId(arr[arr.length-1]);
            tmpCountry.setFrequency(countObj.getCount());
            country.add(tmpCountry);
        }
    }
}
