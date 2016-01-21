/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.request;

import eu.archivesportaleurope.apeapi.utils.CommonUtils;
import javax.validation.constraints.Max;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Mahbub
 */
@XmlRootElement
public class SearchRequest {

    String query;

    @Max(value = 50, message = "Count must not be more than 50")
    int count;
    int start;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = CommonUtils.getGenericQueryString(query);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

}
