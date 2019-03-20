/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class CommonUtils {
    private static final Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    
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
    
    public static String objectToString(Object o) {
        if (o != null) {
            return o.toString();
        } else {
            return "";
        }
    }

    public static int objectToInt(Object o) {
        if (o != null) {
            try {
                return Integer.parseInt(o.toString());
            } catch (NullPointerException | NumberFormatException ex) {
                logger.debug("", ex);
            }
            return 0;
        } else {
            return 0;
        }
    }
}
