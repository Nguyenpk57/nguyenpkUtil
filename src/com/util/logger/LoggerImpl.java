/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.logger;

import com.util.func.Constants;
import com.util.bean.Stack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.extras.DOMConfigurator;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author nguyenpk
 */
public class LoggerImpl implements ILogger {

    private static final Object LOCK = new Object();
    private static LoggerImpl instance;
    private static HashMap<String, LoggerImpl> instances;
    protected String name;
    protected Logger logger;
    protected boolean print = true;

    private LoggerImpl() {
        DOMConfigurator.configure(Constants.Config.DOM_CONFIGURATOR_LOG_PATH);
        name = this.getClass().getName();
        this.logger = Logger.getLogger(name);
    }

    private LoggerImpl(String logger) {
        DOMConfigurator.configure(Constants.Config.DOM_CONFIGURATOR_LOG_PATH);
        name = logger;
        this.logger = Logger.getLogger(name);
    }

    public static LoggerImpl getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new LoggerImpl();
            }
            return instance;
        }
    }

    public static LoggerImpl getInstance(Class clazz) {
        return getInstance(clazz.getName());
    }

    public static LoggerImpl getInstance(String clazzName) {
        synchronized (LOCK) {
            if (instances == null) {
                instances = new HashMap<String, LoggerImpl>();
            }
            if (!instances.containsKey(clazzName)) {
                instances.put(clazzName, new LoggerImpl(clazzName));
            }
            return instances.get(clazzName);
        }
    }

    @Override
    public Object getLogger() {
        return logger;
    }

    @Override
    public String getName() {
        return name;
    }

    //<editor-fold defaultstate="collapsed" desc="log">
    @Override
    public void error(Object obj) {
        this.logger.error(obj);
        print(obj);
    }

    @Override
    public void error(Object obj, Throwable t) {
        this.logger.error(obj, t);
        print(obj, t);
    }

    @Override
    public void info(Object obj) {
        this.logger.info(obj);
        print(obj);
    }

    @Override
    public void info(Object obj, Throwable t) {
        this.logger.info(obj, t);
        print(obj, t);
    }

    @Override
    public void warn(Object obj) {
        this.logger.warn(obj);
        print(obj);
    }

    @Override
    public void warn(Object obj, Throwable t) {
        this.logger.warn(obj, t);
        print(obj, t);
    }

    @Override
    public void debug(Object obj) {
        this.logger.debug(obj);
        print(obj);
    }

    @Override
    public void debug(Object obj, Throwable t) {
        this.logger.debug(obj, t);
        print(obj, t);
    }

    @Override
    public void fatal(Object obj) {
        this.logger.fatal(obj);
        print(obj);
    }

    @Override
    public void fatal(Object obj, Throwable t) {
        this.logger.fatal(obj, t);

        print(obj, t);
    }

    private void print(Object... objs) {
        if (print) {
            if (objs != null) {
                for (Object obj : objs) {
                    if (obj instanceof Throwable) {
                        ((Throwable) obj).printStackTrace();
                        continue;
                    }
                    System.out.println(StringUtils.join(DateFormatUtils.format(new Date(), "yyyy/MM/dd HH:mm:ss.SSS"),
                            StringUtils.join("LoggerImpl.print:", String.valueOf(obj))));
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="logs">
    @Override
    public void errors(Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.error(log);
        print(log);
    }

    @Override
    public void errors(Throwable t, Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.error(log, t);
        print(log, t);
    }

    @Override
    public void infos(Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.info(log);
        print(log);
    }

    @Override
    public void infosTransaction(Object... values) {
        String log = catchLogTransaction(values);
        this.logger.info(log);
        print(log);
    }

    @Override
    public void infos(Throwable t, Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.info(log, t);
        print(log, t);
    }

    @Override
    public void warns(Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.warn(log);
        print(log);
    }

    @Override
    public void warns(Throwable t, Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.warn(log, t);
        print(log, t);
    }

    @Override
    public void debugs(Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.debug(log);
        print(log);
    }

    @Override
    public void debugs(Throwable t, Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.debug(log, t);
        print(log, t);
    }

    @Override
    public void fatals(Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.fatal(log);
        print(log);
    }

    @Override
    public void fatals(Throwable t, Object... values) {
        String log = new Stack().catchInfo() + ":" + catchLog(values);
        this.logger.fatal(log, t);
        print(log, t);
    }

    private String catchLog(Object... values) {
        String result = "null";
        if (values == null) {
            return result;
        }
        int length = values.length;
        if (length == 0) {
            return result;
        }
        List<String> strs = new ArrayList<String>();
        int i = 0;
        while (i < length) {
            Object key = values[i];
            key = key == null ? "null" : String.valueOf(key).trim();
            int j = i + 1;
            if (j < length) {
                Object value = values[j];
                value = value == null ? "null" : String.valueOf(value).trim();
                strs.add(key + "=" + value);
                i = j + 1;
                continue;
            }
            strs.add(key + "=null");
            i = j + 1;
        }
        result = strs + "|";
        return result;
    }

    private String catchLogTransaction(Object... values) {
        StringBuilder result = new StringBuilder();
        if (values == null) {
            return StringUtils.EMPTY;
        }
        int length = values.length;
        if (length == 0) {
            return StringUtils.EMPTY;
        }
        for (int i = 0; i < values.length; i++) {
            result.append(values[i]).append(Constants.SEPARATE_TRANSACTION_LOG);
        }
        return result.toString();
    }
    //</editor-fold>

    @Override
    public String toString() {
        return new StringBuilder()
                .append("name=").append(name).append(",")
                .append("print=").append(print).append(",")
                .append("additivity=").append(this.logger.getAdditivity()).append(",")
                .append("debugEnabled=").append(this.logger.isDebugEnabled()).append(",")
                .append("infoEnabled=").append(this.logger.isInfoEnabled()).append(",")
                .append("traceEnabled=").append(this.logger.isTraceEnabled())
                .toString();
    }
}
