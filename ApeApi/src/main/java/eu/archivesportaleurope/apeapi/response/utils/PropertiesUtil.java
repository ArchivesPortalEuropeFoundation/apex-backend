/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.apeapi.response.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author kaisar
 */
public class PropertiesUtil {

    private Properties properties = new Properties();
    private InputStream inputStream = null;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PropertiesUtil(String fileName) {
        inputStream = PropertiesUtil.class.getResourceAsStream(fileName);
        try {
            properties.load(inputStream);
        } catch (IOException ex) {
            logger.error("Properties file not foud!!!:" + ex.getMessage());
        }

    }

    public String getValueFromKey(String key) {
        return properties.getProperty(key,"10");
    }

}
