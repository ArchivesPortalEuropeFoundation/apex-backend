/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.manual.eaccpf.util;

/**
 *
 * @author papp
 */
public class MapEntry implements Comparable<MapEntry>{
    private String key;
    private String value;

    public MapEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(MapEntry other) {
        return this.getValue().compareTo(other.getValue());
    }
    
    @Override
    public String toString(){
        return value;
    }
}
