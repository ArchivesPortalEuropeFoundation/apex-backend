/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.apenet.dashboard.types.ead3;

import eu.apenet.dashboard.exception.ItemNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.archivesportaleurope.apetypes.LocalTypes;
import org.apache.log4j.Logger;

/**
 *
 * @author kaisar
 */
public class LocalTypeMap {

    private final Map<String, String> alaisMap;
    private final Map<String, String> immutableMap = DefaultLocalTypeMap.getInstance().getUnmodifiableMap();
    protected static final Logger LOGGER = Logger.getLogger(LocalTypeMap.class);
    
    public LocalTypeMap() {
        alaisMap = new HashMap<>(immutableMap);
    }

    public String getApeType(String localType) throws ItemNotFoundException {
        if (alaisMap.containsKey(localType)) {
            return alaisMap.get(localType);
        }
        throw new ItemNotFoundException("Unknown localtype: " + localType);
    }

    public void add(String apeType, String localType) {
        if (null == apeType || apeType.isEmpty() || null == localType || localType.isEmpty()) {
            return;
        }
        if (immutableMap.containsKey(apeType)) {
            alaisMap.put(localType, apeType);
        } else {
            LOGGER.debug("Unknown apeType got ignored: "+apeType);
        }
    }

    public void add(String apeType, String... localTypes) {
        for (String s : localTypes) {
            LocalTypeMap.this.add(s, apeType);
        }
    }

    public void add(Map.Entry<String, Set<String>> entry) {
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
                this.add(entry);
            });
        }
    }
    
    public void add(LocalTypes localTypes) {
        localTypes.getLocalType().forEach((localType) -> {
            String localTypeId = localType.getId();
            localType.getAliases().getAlias().forEach((alais) -> {
                this.add(localTypeId, alais.getValue());
            });
        });
    }
}
