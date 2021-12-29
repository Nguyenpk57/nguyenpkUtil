/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.config;

import com.util.bean.Constants;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 * Get config file
 * @author nguyenpk
 * @since 2021-10-19
 */
public class PropertiesUtils {

    private static final Object LOCK = new Object();
    private static PropertiesUtils instance;
    private HashMap<String, Properties> properties;

    private PropertiesUtils() {
        initialization();
    }

    public static PropertiesUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new PropertiesUtils();
            }
            return instance;
        }
    }

    private void initialization() {
        if (properties == null) {
            properties = new HashMap<String, Properties>();
        }
    }

    public Properties getProperties(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        fileName = fileName.trim();
        if (!properties.containsKey(fileName)) {
            synchronized (LOCK) {
                Properties pro;
                try {
                    pro = new Properties();
                    pro.load(new FileInputStream(fileName));
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    pro = null;
                }
                if (pro != null) {
                    properties.put(fileName, pro);
                }
            }
        }
        return properties.get(fileName);
    }

    public String getProperty(String key) {
        return getProperty(Constants.Config.DEFAULT_PROPERTIES, key);
    }

    public String getProperty(String fileName, String key) {
        return getProperties(fileName).getProperty(key);
    }
}
