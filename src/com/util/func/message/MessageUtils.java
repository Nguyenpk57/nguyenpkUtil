/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.message;

import com.util.bean.Constants;
import com.util.func.StringUtils;
import com.util.func.config.PropertiesUtils;
import com.util.func.config.ResourceUtils;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import java.util.HashMap;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class MessageUtils {

    private static final Object LOCK = new Object();
    private static MessageUtils instance;
    private static HashMap<String, MessageUtils> instances;
    private String localeSeparator;
    protected ILogger logger;

    private MessageUtils() {
        initialization();
    }

    private MessageUtils(ILogger logger) {
        this.logger = logger;
        initialization();
    }

    public static MessageUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new MessageUtils();
            }
            return instance;
        }
    }

    public static MessageUtils getInstance(ILogger logger) {
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
                instances = new HashMap<String, MessageUtils>();
            }
            if (!instances.containsKey(name)) {
                instances.put(name, new MessageUtils(logger));
            }
            return instances.get(name);
        }
    }

    private void initialization() {
        localeSeparator = "_";
        if (logger == null) {
            logger = LoggerImpl.getInstance(this.getClass());
        }
    }

    private String getLanguageFile(String language) {
        return new StringBuilder().append(PropertiesUtils.getInstance().getProperty(Constants.LANGUAGE_FILE_PATH))
                .append(localeSeparator)
                .append(org.apache.commons.lang3.StringUtils.trim(language)).toString();
    }

    //<editor-fold defaultstate="collapsed" desc="getMessage">
    public String getMessage(String language, String key) {
        try {
            String languageFilePath = getLanguageFile(language);
            return ResourceUtils.getInstance().getResource(languageFilePath, key);
        } catch (Throwable ex) {
            logger.error(ex.getMessage());
            return key;
        }
    }

    public String getMessage(String language, String key, Object... args) {
        try {
            return StringUtils.getInstance().getFormat(getMessage(language, key)).format(buildArgs(language, args));
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return key;
        }
    }

    private Object[] buildArgs(String language, Object... args) {
        Object[] result = null;
        if (args == null) {
            return result;
        }
        int length = args.length;
        if (length == 0) {
            return result;
        }
        result = new Object[length];
        int i = 0;
        for (Object arg : args) {
            if (arg == null) {
                result[i++] = null;
                continue;
            }
            if (arg instanceof MessageArgument) {
                result[i++] = ((MessageArgument) arg).getMessage(logger, language);
                continue;
            }
            result[i++] = arg;
        }
        return result;
    }
    //</editor-fold>
}
