/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func.response;

import com.util.func.Constants;
import com.util.bean.Stack;
import com.util.func.message.MessageUtils;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * output standard for Business
 *
 * @author nguyenpk
 * @since 2021-10-19
 */
public class Result {

    private boolean success;
    private String key;
    private String message;
    private Object[] arguments;
    private String exception;
    private HashMap map = new HashMap();
    private List<String> stacks = new ArrayList<String>();

    //<editor-fold defaultstate="collapsed" desc="Constructor">
    public Result() {
        addStack(new Stack());
    }

    public Result(boolean success, String key, String message) {
        addStack(new Stack());
        this.success = success;
        this.key = key;
        this.message = message;
    }

    public Result(boolean success, String key, String message, Throwable exception) {
        addStack(new Stack());
        this.success = success;
        this.key = key;
        this.message = message;
        this.exception = getStackTrace(exception);
    }

    public Result(boolean success, String key, String message, Object... arguments) {
        addStack(new Stack());
        this.success = success;
        this.key = key;
        this.message = message;
        this.arguments = arguments;
    }

    public Result(boolean success, String key, String message, Throwable exception, Object... arguments) {
        addStack(new Stack());
        this.success = success;
        this.key = key;
        this.message = message;
        this.exception = getStackTrace(exception);
        this.arguments = arguments;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getMessage">
    public Result(Result obj) {
        addStack(obj);
        addStack(new Stack());
        if (obj == null) {
            return;
        }
        this.success = obj.isSuccess();
        this.key = obj.getKey();
        this.arguments = obj.getArguments();
        this.message = obj.getMessage(Constants.DEFAULT_LANGUAGE);
        this.exception = obj.getException();
    }

    public Result(String language, Result obj) {
        addStack(obj);
        addStack(new Stack());
        if (obj == null) {
            return;
        }
        this.success = obj.isSuccess();
        this.key = obj.getKey();
        this.arguments = obj.getArguments();
        this.message = obj.getMessage(language);
        this.exception = obj.getException();
    }

    public Result(String language, Result obj, String... keys) {
        addStack(obj);
        addStack(new Stack());
        if (obj == null) {
            return;
        }
        this.success = obj.isSuccess();
        this.key = obj.getKey();
        this.arguments = obj.getArguments();
        this.message = obj.getMessage(language);
        puts(obj, keys);
        this.exception = obj.getException();
    }
    //</editor-fold>

    public boolean isSuccess() {
        return success;
    }

    public final void puts(Result obj, String... keys) {
        if (obj == null) {
            return;
        }
        if (keys != null) {
            for (String k : keys) {
                map.put(k, obj.get(k));
            }
        }
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public void put(Object key, Object value) {
        map.put(key, value);
    }

    public String getMessage(String language) {
        return MessageUtils.getInstance().getMessage(language, message, arguments);
    }

    //<editor-fold defaultstate="collapsed" desc="stacks">
    private void addStack(Result obj) {
        if (obj == null) {
            return;
        }
        addStack(obj.getStacks());
    }

    private void addStack(List<String> stacks) {
        if (stacks == null) {
            return;
        }
        this.stacks.addAll(stacks);
    }

    private void addStack(Stack stack) {
        if (stack == null) {
            return;
        }
        this.stacks.add(stack.catchInfo());
    }

    private String getStackTrace(Throwable exception) {
        if (exception == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="getter, setter">
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }

    public List<String> getStacks() {
        return stacks;
    }

    public void setStacks(List<String> stacks) {
        this.stacks = stacks;
    }
//</editor-fold>

}
