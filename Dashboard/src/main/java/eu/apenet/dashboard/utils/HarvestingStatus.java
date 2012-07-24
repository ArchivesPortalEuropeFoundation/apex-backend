package eu.apenet.dashboard.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Yoann Moranville
 * Date: 08/03/2012
 *
 * @author Yoann Moranville
 */
public class HarvestingStatus {
    private static List<Integer> harvestingInstitutions;

    public static boolean isHarvesting(Integer aiId) {
        return harvestingInstitutions != null && harvestingInstitutions.contains(aiId);
    }

    public static void addHarvestingInstitution(Integer aiId) {
        if(harvestingInstitutions == null)
            harvestingInstitutions = new ArrayList<Integer>();
        harvestingInstitutions.add(aiId);
    }

    public static void removeHarvestingInstitution(Integer aiId) {
        if(harvestingInstitutions != null && harvestingInstitutions.contains(aiId))
            harvestingInstitutions.remove(aiId);
    }
}
