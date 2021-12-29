/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Random;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class StringUtils {

    private static final Object LOCK = new Object();
    private static StringUtils instance;
    private HashMap<String, MessageFormat> formats;

    public static final String MAC_DEFAULT = "00-00-00-00-00-00";

    private StringUtils() {
        initialization();
    }

    public static StringUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new StringUtils();
            }
            return instance;
        }
    }

    private void initialization() {
        if (formats == null) {
            formats = new HashMap<String, MessageFormat>();
        }
    }

    public MessageFormat getFormat(String pattern) {
        pattern = org.apache.commons.lang3.StringUtils.trim(pattern);
        if (org.apache.commons.lang3.StringUtils.isEmpty(pattern)) {
            return null;
        }
        if (!formats.containsKey(pattern)) {
            MessageFormat sdf = new MessageFormat(pattern);
            formats.put(pattern, sdf);
        }
        return formats.get(pattern);
    }

    public String format(String pattern, Object... values) {
        MessageFormat mf = getFormat(pattern);
        if (mf == null) {
            return null;
        }
        return mf.format(values);
    }

    public String randomDigits(Integer length) {
        String result = "";
        if (length <= 0) {
            return result;
        }
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            result += r.nextInt(9);
        }
        return result;
    }

    public String trimMAC(String mac) {
        if (mac == null) {
            return null;
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(mac.trim())) {
            mac = MAC_DEFAULT;
        }
        return mac;
    }

    public String escapeHtml(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;");
    }

    public String trimXmlTag(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return "";
        }
        return s.replace("&", "").replace("<", "").replace(">", "");
    }

}
