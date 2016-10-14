/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.test.util;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author kaisar
 */
public class SolrJsonObject {

    private String totalResults;
    private int startIndex;
    private int totalPages;
    private List<HashMap<String, String>> eadSearchResults;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<HashMap<String, String>> getEadSearchResults() {
        return eadSearchResults;
    }

    public void setEadSearchResults(List<HashMap<String, String>> eadSearchResults) {
        this.eadSearchResults = eadSearchResults;
    }

    @Override
    public String toString() {
        return "SolrJsonObject{" + "totalResults=" + totalResults + ", startIndex=" + startIndex + ", totalPages=" + totalPages + ", eadSearchResults=" + eadSearchResults + '}';
    }

}
