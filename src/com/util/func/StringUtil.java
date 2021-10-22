/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func;

import java.text.MessageFormat;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class StringUtil {

    private static final Object LOCK = new Object();
    private static StringUtil instance;
    private HashMap<String, MessageFormat> formats;

    public static final String MAC_DEFAULT = "00-00-00-00-00-00";

    private StringUtil() {
        initialization();
    }

    public static StringUtil getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new StringUtil();
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
        pattern = StringUtils.trim(pattern);
        if (StringUtils.isEmpty(pattern)) {
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
        if (StringUtils.isEmpty(mac.trim())) {
            mac = MAC_DEFAULT;
        }
        return mac;
    }

    public String escapeHtml(String s) {
        if (StringUtils.isEmpty(s)) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;");
    }

    public String trimXmlTag(String s) {
        if (StringUtils.isEmpty(s)) {
            return "";
        }
        return s.replace("&", "").replace("<", "").replace(">", "");
    }

}
