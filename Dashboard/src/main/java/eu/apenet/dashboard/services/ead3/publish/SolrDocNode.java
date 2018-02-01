/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.services.ead3.publish;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mahbub
 */
public class SolrDocNode {

    private SolrDocNode sibling = null;
    private SolrDocNode child = null;
    //This object is for Solr which can handle primitive data types and collections of primitives
    private Map<String, Object> data = new HashMap<>();
    private Map<String, Object> transientData = new HashMap<>();
//    private List<Map<String, Object>> eacData = new ArrayList<>();

    public SolrDocNode getSibling() {
        return sibling;
    }

    public void setSibling(SolrDocNode sibling) {
        if (this.sibling != null) {
            sibling.setSibling(this.sibling);
        }
        this.sibling = sibling;
    }

    public SolrDocNode getChild() {
        return child;
    }

    public void setChild(SolrDocNode child) {
        if (this.child != null) {
            child.setSibling(this.child);
        }
        this.child = child;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void setDataElement(String key, Object value) {
        this.data.put(key, value);
    }

    public Object getDataElement(String key) {
        return this.data.get(key);
    }

//    public List<Map<String, Object>> getEacData() {
//        return eacData;
//    }
//
//    public void setEacData(List<Map<String, Object>> eacData) {
//        this.eacData = eacData;
//    }
    public Map<String, Object> getTransientData() {
        return transientData;
    }

    public void setTransientData(Map<String, Object> transientData) {
        this.transientData = transientData;
    }

    public void setTransientDataElement(String key, Object value) {
        this.transientData.put(key, value);
    }

    public Object getTransientDataElement(String key) {
        return this.transientData.get(key);
    }
}
