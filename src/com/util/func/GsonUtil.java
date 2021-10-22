/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class GsonUtil {

    private static final Object LOCK = new Object();
    private static GsonUtil instance;
    private static HashMap<String, GsonUtil> instances;
    private static HashMap<String, Gson> gsons;
    private Gson gson;
    private ILogger logger;

    private GsonUtil() {
        initialization();
    }

    private GsonUtil(ILogger logger) {
        this.logger = logger;
        initialization();
    }

    public static GsonUtil getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new GsonUtil();
            }
            return instance;
        }
    }

    public static GsonUtil getInstance(ILogger logger) {
        if (logger == null) {
            return getInstance();
        }
        String name = logger.getName();
        if (name == null) {
            return getInstance();
        }
        name = name.trim();
        if (name.isEmpty()) {
            return getInstance();
        }
        synchronized (LOCK) {
            if (instances == null) {
                instances = new HashMap<String, GsonUtil>();
            }
            if (!instances.containsKey(name)) {
                instances.put(name, new GsonUtil(logger));
            }
            return instances.get(name);
        }
    }

    private void initialization() {
        if (gson == null) {
            gson = new Gson();
        }
        if (gsons == null) {
            gsons = new HashMap<String, Gson>();
        }
        if (logger == null) {
            logger = LoggerImpl.getInstance(this.getClass());
        }
    }

    public Gson buildGson(String pattern) {
        if (StringUtils.isEmpty(pattern)) {
            return null;
        }
        pattern = pattern.trim();
        if (!gsons.containsKey(pattern)) {
            gsons.put(pattern, new GsonBuilder().setDateFormat(pattern).create());
        }
        return gsons.get(pattern);
    }

    public String to(Object obj) {
        return to(gson, obj);
    }

    public String toExcludedAnnotation(Object obj) {
        return toExcludedAnnotation(gson, obj);
    }

    public String to(String dateFormat, Object obj) {
        return to(buildGson(dateFormat), obj);
    }

    private String to(Gson gson, Object obj) {
        String result = null;
        try {
            if (obj == null) {
                return result;
            }
            if (obj instanceof ResultSet) {
                List ja = new ArrayList();
                try {
                    ResultSet rs = (ResultSet) obj;
                    while (rs.next()) {
                        ResultSetMetaData data = rs.getMetaData();
                        int cols = data.getColumnCount();
                        HashMap jo = new HashMap();
                        for (int i = 1; i <= cols; i++) {
                            jo.put(data.getColumnLabel(i), rs.getObject(i));
                        }
                        ja.add(jo);
                    }
                } catch (Throwable ex) {
                    logger.error(ex.getMessage(), ex);
                }
                result = gson.toJson(ja);
                return result;
            }
            result = gson.toJson(obj);
            return result;
        } catch (Throwable ex) {
            logger.error("GsonUtil.to:Exception=" + ex.getMessage());
            return result;
        }
    }

    private String toExcludedAnnotation(Gson gson, Object obj) {
        String result = null;
        try {
            if (obj == null) {
                return result;
            }
            if (obj instanceof ResultSet) {
                List ja = new ArrayList();
                try {
                    ResultSet rs = (ResultSet) obj;
                    while (rs.next()) {
                        ResultSetMetaData data = rs.getMetaData();
                        int cols = data.getColumnCount();
                        HashMap jo = new HashMap();
                        for (int i = 1; i <= cols; i++) {
                            jo.put(data.getColumnLabel(i), rs.getObject(i));
                        }
                        ja.add(jo);
                    }
                } catch (Throwable ex) {
                    logger.error(ex.getMessage(), ex);
                }
                result = gson.toJson(ja);
                return result;
            }
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            result = gson.toJson(obj);
            return result;
        } catch (Throwable ex) {
            logger.error("GsonUtil.to:Exception=" + ex.getMessage());
            return result;
        }
    }

    public Object from(String json, Type type) {
        return from(gson, json, type);
    }

    public Object from(String dateFormat, String json, Type type) {
        return from(buildGson(dateFormat), json, type);
    }

    private Object from(Gson gson, String json, Type type) {
        return json == null ? null : gson.fromJson(json, type);
    }
}
