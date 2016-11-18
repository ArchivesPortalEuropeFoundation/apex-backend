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
public class SolrDocTree {
    SolrDocTree sibling = null;
    SolrDocTree child = null;
    Map<String, Object> data = new HashMap<>();

    public SolrDocTree getSibling() {
        return sibling;
    }

    public void setSibling(SolrDocTree sibling) {
        this.sibling = sibling;
    }

    public SolrDocTree getChild() {
        return child;
    }

    public void setChild(SolrDocTree child) {
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
}
