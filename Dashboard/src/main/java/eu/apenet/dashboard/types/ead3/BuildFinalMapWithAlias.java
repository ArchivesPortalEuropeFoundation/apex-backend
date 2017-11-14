/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.types.ead3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kaisar
 */
public class BuildFinalMapWithAlias {

    private Map<String, String> alaisMap;
    private final Map<String, String> immutableMap = ImmutableLocalTypeMap.getInstance().getUnmodifiableMap();

    public BuildFinalMapWithAlias() {

        alaisMap = new HashMap<>();
        alaisMap.putAll(immutableMap);

    }

    public Map<String, String> getAlaisMap() {
        return alaisMap;
    }

    public void setAlaisMap(Map<String, String> alaisMap) {
        this.alaisMap = alaisMap;
    }

    public void addToMap(Map.Entry<String, List<String>> entry) {
        if (null != entry) {
            if (immutableMap.containsKey(entry.getKey())) {
                for (String s : entry.getValue()) {
                    alaisMap.put(s, entry.getKey());
                }
            }
        }
    }

    public void addAllToMap(Map<String, List<String>> partMap) {
        if (null != partMap) {
            for (Map.Entry<String, List<String>> entry : partMap.entrySet()) {
                this.addToMap(entry);
            }
        }
    }
}
