/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

import eu.apenet.commons.solr.DateGap;
import eu.apenet.commons.solr.facet.FacetType;
import eu.apenet.commons.solr.facet.FacetValue;
import eu.apenet.commons.solr.facet.ListFacetSettings;
import eu.archivesportaleurope.apeapi.common.datatypes.EadResponseDictionary;
import eu.archivesportaleurope.apeapi.common.datatypes.SolrApiResponseDictionary;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
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
public class FacetDateFields {
    private final List<NameCountPair> fromDate;
    private final List<NameCountPair> toDate;

    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());
    private final transient SolrApiResponseDictionary dictionary = new EadResponseDictionary();
    
    public FacetDateFields() {
        this.fromDate = new ArrayList<>();
        this.toDate = new ArrayList<>();
    }

    public FacetDateFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultDateListFacetSettings = FacetType.getDefaultDateListFacetSettings();
        for (ListFacetSettings facetSettings : defaultDateListFacetSettings) {
            try {
                Object field = FieldUtils.readField(this, 
                        dictionary.getResponseFieldName(facetSettings.getFacetType().getName()), true);
                Method setMethod = thisClass.getMethod("setDate", List.class, FacetField.class);
                setMethod.invoke(this, field, queryResponse.getFacetDate(facetSettings.getFacetType().getName()));
            } catch (Exception ex) {
                logger.debug("Reflecion exception: ", ex);
            }
        }
    }

    public List<NameCountPair> getFromDate() {
        return fromDate;
    }

    public List<NameCountPair> getToDate() {
        return toDate;
    }

    public void setDate(List<NameCountPair> field, FacetField values) throws ParseException {
        if (values == null) {
            return;
        }
        DateGap dateGap = DateGap.getGapByName(values.getGap().substring(1));

        List<FacetField.Count> counts = values.getValues();
        for (int i = 0; i < counts.size(); i++) {
            NameCountPair pair = new NameCountPair();
            FacetField.Count countObj = counts.get(i);
            String[] arr = countObj.getName().split("T");
            pair.setName(FacetValue.getDateSpan(dateGap, arr[0]));
            pair.setId(arr[0] + "_"+dateGap.next().getId());
            pair.setFrequency(countObj.getCount());
            field.add(pair);
        }
    }
}
