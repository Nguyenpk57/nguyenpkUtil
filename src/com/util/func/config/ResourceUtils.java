/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.config;

import com.util.func.Constants;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;

/**
 * Get language file
 * @author nguyenpk
 * @since 2021-10-19
 */
public class ResourceUtils {
    
    private static final Object LOCK = new Object();
    private static ResourceUtils instance;
    private HashMap<String, ResourceBundle> bundles;

    private ResourceUtils() {
        initialization();
    }

    public static ResourceUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ResourceUtils();
            }
            return instance;
        }
    }

    private void initialization() {
        if (bundles == null) {
            bundles = new HashMap<String, ResourceBundle>();
        }
    }

    public ResourceBundle getBundle(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        fileName = fileName.trim();
        if (!bundles.containsKey(fileName)) {
            synchronized (LOCK) {
                ResourceBundle resourceBundle = ResourceBundle.getBundle(fileName);
                if (resourceBundle != null) {
                    bundles.put(fileName, resourceBundle);
                }
            }
        }
        return bundles.get(fileName);
    }

    public String getResourceDefault(String key) {
        return getResource(Constants.Config.DEFAULT_BUNDLE, key);
    }

    public String getResource(String fileName, String key) {
        return getBundle(fileName).getString(key);
    }
}
