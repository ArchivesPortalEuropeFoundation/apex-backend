/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.utils;

/**
 *
 * @author kaisar
 */
public class CommonUtils {

    private CommonUtils() {
    }

    public static String splitByColon(String str, int number) {
        String ret = "";
        if (str != null && !str.isEmpty()) {
            String[] strs = str.split(":");
            if (strs.length > number) {
                ret = strs[number];
            }
        }
        return ret;
    }
}
