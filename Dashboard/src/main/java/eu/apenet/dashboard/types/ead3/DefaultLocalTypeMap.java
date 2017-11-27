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
public class DefaultLocalTypeMap {

    private static class DefaultLocalTypeMapHolder {

        private final static DefaultLocalTypeMap INSTANCE = new DefaultLocalTypeMap();
    }

    private final Map<String, String> typeMap = new HashMap<>();

    private DefaultLocalTypeMap() {
        for (ApeType field : ApeType.values()) {
            typeMap.put(field.getValue(), field.getValue());
        }
    }

    public static DefaultLocalTypeMap getInstance() {
        return DefaultLocalTypeMapHolder.INSTANCE;
    }

    public Map<String, String> getUnmodifiableMap() {
        return Collections.unmodifiableMap(typeMap);
    }

}
