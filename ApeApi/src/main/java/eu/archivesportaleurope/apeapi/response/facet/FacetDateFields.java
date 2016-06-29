/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.facet;

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
public class FacetDateFields {

    @JsonProperty("fromDate")
    private final List<NameCountPair> startdate;
    @JsonProperty("toDate")
    private final List<NameCountPair> enddate;

    private static final transient Map<String, String> DATE_FIELDNAMES = new HashMap<>();

    static {
        Field[] fields = FacetDateFields.class.getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonProperty.class)) {
                String annotationValue = field.getAnnotation(JsonProperty.class).value();
                DATE_FIELDNAMES.put(annotationValue, field.getName());
            } else {
                DATE_FIELDNAMES.put(field.getName(), field.getName());
            }
        }
    }

    private final transient Logger logger = LoggerFactory.getLogger(this.getClass());

    public FacetDateFields() {
        this.startdate = new ArrayList<>();
        this.enddate = new ArrayList<>();
    }

    public FacetDateFields(QueryResponse queryResponse) {
        this();
        Class<?> thisClass = this.getClass();
        List<ListFacetSettings> defaultDateListFacetSettings = FacetType.getDefaultDateListFacetSettings();
        for (ListFacetSettings facetSettings : defaultDateListFacetSettings) {
            try {
                Object field = FieldUtils.readField(this, facetSettings.getFacetType().getName(), true);
                Method setMethod = thisClass.getMethod("setDate", List.class, FacetField.class);
                setMethod.invoke(this, field, queryResponse.getFacetDate(facetSettings.getFacetType().getName()));
            } catch (Exception ex) {
                logger.debug("Reflecion exception: ", ex);
            }
        }
    }

    public List<NameCountPair> getStartdate() {
        return startdate;
    }

    public List<NameCountPair> getEnddate() {
        return enddate;
    }

    public static String getOriginalFieldName(String annotName) {
        return DATE_FIELDNAMES.get(annotName);
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
