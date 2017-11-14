/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.types.ead3;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author kaisar
 */
public class BuildFinalMapWithAlias {

    private final Map<String, String> alaisMap;
    private final Map<String, String> immutableMap = ImmutableLocalTypeMap.getInstance().getUnmodifiableMap();

    public BuildFinalMapWithAlias() {

        alaisMap = new HashMap<>();
        alaisMap.putAll(immutableMap);

    }

    public Map<String, String> getAlaisMap() {
        return alaisMap;
    }

    public void addToMap(Map.Entry<String, Set<String>> entry) {
        if (null != entry) {
            if (immutableMap.containsKey(entry.getKey())) {
                entry.getValue().forEach((s) -> {
                    alaisMap.put(s, entry.getKey());
                });
            }
        }
    }

    public void addAllToMap(Map<String, Set<String>> partMap) {
        if (null != partMap) {
            partMap.entrySet().forEach((entry) -> {
                this.addToMap(entry);
            });
        }
    }
}
