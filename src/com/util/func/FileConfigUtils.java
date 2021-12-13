package com.util.func;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.util.bean.Constants;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


public class FileConfigUtils {

    private static final Logger logger = Logger.getLogger(FileConfigUtils.class);
    private static final Object LOCK = new Object();
    private static FileConfigUtils instance;
    private HashMap<String, HashMap> configs;

    public static String configProperties;

    private FileConfigUtils() {
        initialization();
    }

    public static FileConfigUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new FileConfigUtils();
            }
            return instance;
        }
    }

    private void initialization() {
        if (configs == null) {
            configs = new HashMap<String, HashMap>();
        }
    }

    private HashMap getConfigs(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        fileName = fileName.trim();
        if (!configs.containsKey(fileName)) {
            synchronized (LOCK) {
                HashMap config = load(fileName);
                if (config != null) {
                    configs.put(fileName, config);
                }
            }
        }
        return configs.get(fileName);
    }

    private HashMap load(String fileName) {
        List<String> lines = read(fileName);
        if (lines == null) {
            return null;
        }
        HashMap result = new HashMap();
        String joiner = "=";
        String comment = "#";
        List<String> keys = new ArrayList<String>();
        for (String line : lines) {
            line = StringUtils.trim(line);
            if (line == null || line.startsWith(comment)) {
                continue;
            }
            String[] arr = line.split(joiner, 2);
            if (arr == null || arr.length != 2) {
                continue;
            }
            String key = StringUtils.trim(arr[0]);
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            String value = StringUtils.trim(arr[1]);
            if (StringUtils.isEmpty(value)) {
                continue;
            }
            if (keys.contains(key)) {
                Object v = result.get(key);
                if (v == null) {
                    result.put(key, value);
                    continue;
                }
                if (v instanceof String) {
                    String vStr = StringUtils.trim((String) v);
                    if (value.equals(vStr)) {
                        continue;
                    }
                    List<String> values = new ArrayList<String>();
                    values.add(vStr);
                    values.add(value);
                    result.put(key, values);
                    continue;
                }
                if (!(v instanceof List)) {
                    continue;
                }
                List<String> values = (List<String>) v;
                if (!values.contains(value)) {
                    values.add(value);
                }
                result.put(key, values);
                continue;
            }
            result.put(key, value);
            keys.add(key);
        }
        return result;
    }

    public String getValue(String key) {
        //String path = configProperties;
        String path = Constants.Config.DEFAULT_PROPERTIES;
        logger.info("FileConfigUtils getValue path: " + path);
        return getValue(getConfigs(path), key);
    }

    private String getValue(HashMap values, String key) {
        if (values == null) {
            return null;
        }
        Object value = values.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            return StringUtils.trim((String) value);
        }
        if (value instanceof List) {
            List<String> lst = (List<String>) value;
            for (String v : lst) {
                v = StringUtils.trim((String) v);
                if (!StringUtils.isEmpty(v)) {
                    return v;
                }
            }
            return null;
        }
        return null;
    }

    public String getValue(String fileName, String key) {
        return getValue(getConfigs(fileName), key);
    }

    public List<String> getValues(String key) {
        //String path = configProperties;
        String path = Constants.Config.DEFAULT_PROPERTIES;
        logger.info("FileConfigUtils getValues path: " + path);
        return getValues(getConfigs(path), key);
    }

    private List<String> getValues(HashMap values, String key) {
        if (values == null) {
            return null;
        }
        Object value = values.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof String) {
            String strV = StringUtils.trim((String) value);
            if (StringUtils.isEmpty(strV)) {
                return null;
            }
            List<String> result = new ArrayList<String>();
            result.add(strV);
            return result;
        }
        if (value instanceof List) {
            return (List<String>) value;
        }
        return null;
    }

    public List<String> getValues(String fileName, String key) {
        return getValues(getConfigs(fileName), key);
    }

    public List<String> read(String filePath) {
        List<String> result = new ArrayList();
        BufferedReader br = null;
        try {
            try {
                br = new BufferedReader(new FileReader(filePath));
            } catch (Exception ex) {
                logger.error("read: " + "filePath: " + filePath + " BufferedReader Exception: " + ex.getMessage());
            }
            if (br == null) {
                br = new BufferedReader(new FileReader(new File(filePath).getCanonicalPath()));
            }
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    result.add(line);
                }
            }
        } catch (Throwable ex) {
            logger.error("read: " + "filePath: " + filePath + " Exception: " + ex.getMessage());
        } finally {
            closes(br);
        }
        return result;
    }

    public void closes(Object... objs) {
        if (objs != null) {
            for (Object obj : objs) {
                close(obj);
            }
        }
    }

    private Object close(Object obj) {
        try {
            if (obj == null) {
                return obj;
            }
            if (obj instanceof FileInputStream) {
                ((FileInputStream) obj).close();
            }
            if (obj instanceof InputStream) {
                ((InputStream) obj).close();
            }
            if (obj instanceof OutputStream) {
                ((OutputStream) obj).close();
            }
            if (obj instanceof ByteArrayOutputStream) {
                ((ByteArrayOutputStream) obj).close();
            }
            if (obj instanceof BufferedReader) {
                ((BufferedReader) obj).close();
            }
            if (obj instanceof FileWriter) {
                ((FileWriter) obj).close();
            }
        } catch (Throwable ex) {
            logger.error("close: " + ex.getMessage());
        } finally {
            obj = null;
        }
        return obj;
    }
}