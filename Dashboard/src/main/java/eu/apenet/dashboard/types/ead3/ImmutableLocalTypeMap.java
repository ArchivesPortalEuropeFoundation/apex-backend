/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.types.ead3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author kaisar
 */
public class ImmutableLocalTypeMap {

    private static class ImmutableLocalTypeMapHolder {

        private final static ImmutableLocalTypeMap INSTANCE = new ImmutableLocalTypeMap();
    }

    private final Map<String, String> typeMap = new HashMap<>();

    private ImmutableLocalTypeMap() {
        for (ApeType field : ApeType.values()) {
            typeMap.put(field.getValue(), field.getValue());
        }
    }

    public static ImmutableLocalTypeMap getInstance() {
        return ImmutableLocalTypeMapHolder.INSTANCE;
    }

    public Map<String, String> getUnmodifiableMap() {
        return Collections.unmodifiableMap(typeMap);
    }

}
