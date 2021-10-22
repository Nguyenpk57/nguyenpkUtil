/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class NumberUtil {

    private static final Object LOCK = new Object();
    private static NumberUtil instance;
    private HashMap<String, DecimalFormat> formats;

    private NumberUtil() {
        initialization();
    }

    public static NumberUtil getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new NumberUtil();
            }
            return instance;
        }
    }

    private void initialization() {
        if (formats == null) {
            formats = new HashMap<String, DecimalFormat>();
        }
    }

    private DecimalFormat getFormat(String pattern) {
        if (pattern == null) {
            return null;
        }
        pattern = pattern.trim();
        if (pattern.isEmpty()) {
            return null;
        }
        if (!formats.containsKey(pattern)) {
            DecimalFormat sdf = new DecimalFormat(pattern);
            formats.put(pattern, sdf);
        }
        return formats.get(pattern);
    }

    public <T extends Number> T toNumber(Object value, Class<T> clazz) {
        return (T) toNumber(clazz, value);
    }

    public Number toNumber(Class clazz, Object value) {
        Number num = null;
        if (value == null || !isNumberClass(clazz)) {
            return num;
        }
        if (value instanceof Number && value.getClass().equals(clazz)) {
            num = (Number) value;
            return num;
        }
        String s = String.valueOf(value).trim();
        if (s.isEmpty()) {
            return num;
        }
        try {
            num = new BigDecimal(s);

            if (Byte.class.equals(clazz)) {
                return new Byte(num.byteValue());
            }
            if (Short.class.equals(clazz)) {
                return new Short(num.shortValue());
            }
            if (Integer.class.equals(clazz)) {
                return new Integer(num.intValue());
            }
            if (Long.class.equals(clazz)) {
                return new Long(num.longValue());
            }
            if (Float.class.equals(clazz)) {
                return new Float(num.floatValue());
            }
            if (Double.class.equals(clazz)) {
                return new Double(num.doubleValue());
            }
            if (BigInteger.class.equals(clazz)) {
                return new BigInteger(s);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return num;
    }

    public <T extends Number> List<T> toNumbers(Class<T> clazz, List values) {
        List<T> nums = null;
        if (values == null || values.isEmpty() || !isNumberClass(clazz)) {
            return nums;
        }
        nums = new ArrayList<T>();
        for (Object value : values) {
            T num = toNumber(value, clazz);
            nums.add(num);
        }
        return nums;
    }

    public boolean isNumberClass(Class clazz) {
        Class tmpClass = clazz;
        while (tmpClass != null && !Object.class.equals(tmpClass)) {
            if (Number.class.equals(tmpClass)) {
                return true;
            }
            tmpClass = tmpClass.getSuperclass();
        }
        return false;
    }

    public String format(Number number, String pattern) {
        return getFormat(pattern).format(number);
    }

    public String format(Number number, int scale) {
        return format(number, getPattern(scale));
    }

    private String getPattern(int scale) {
        StringBuilder sb = new StringBuilder().append("#");
        if (scale <= 0) {
            return sb.toString();
        }
        sb.append(".");
        for (int i = 0; i < scale; i++) {
            sb.append("#");
        }
        return sb.toString();
    }

    public Number round(Class clazz, Number number, int scale) {
        if (number == null) {
            return number;
        }
        double factor = Math.pow(10D, scale);
        return toNumber(clazz, Math.round(number.doubleValue() * factor) / factor);
    }

    //<editor-fold defaultstate="collapsed" desc="nvl">
    public <T extends Number> T nvl(T value, Class<T> clazz) {
        return (T) nvl(value);
    }

    public <T extends Number> T nvl(T value, T nvl, Class<T> clazz) {
        return (T) nvl(value, nvl);
    }

    public Number nvl(Number value) {
        return nvl(value, 0);
    }

    public BigInteger nvl(BigInteger value) {
        return (BigInteger) nvl(value, new BigInteger("0"));
    }

    public Double nvl(Double value) {
        return (Double) nvl(value, 0D);
    }

    public Float nvl(Float value) {
        return (Float) nvl(value, 0F);
    }

    public Long nvl(Long value) {
        return (Long) nvl(value, 0L);
    }

    public Integer nvl(Integer value) {
        return (Integer) nvl(value, 0);
    }

    public Short nvl(Short value) {
        Short nvl = 0;
        return (Short) nvl(value, nvl);
    }

    public Byte nvl(Byte value) {
        Byte nvl = 0;
        return (Byte) nvl(value, nvl);
    }

    public Number nvl(Number value, Number nvl) {
        if (value == null) {
            return nvl;
        }
        return value;
    }
    //</editor-fold>

    /**
     * a - b
     *
     * @param <T>
     * @param a
     * @param b
     * @return Danh sach co trong a ma khong co trong b
     */
    public <T extends Number> List<T> minus(List<T> a, List<T> b) {
        List<T> result = null;
        if (a == null || a.isEmpty()) {
            return result;
        }
        if (b == null || b.isEmpty()) {
            result = a;
            return result;
        }
        result = new ArrayList<T>();
        for (T value : a) {
            if (!b.contains(value)) {
                result.add(value);
            }
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="buildValue">
    public Integer buildValue(Integer value) {
        return value == null || value < 0 ? 0 : value;
    }

    public Long buildValue(Long value) {
        return value == null || value < 0L ? 0L : value;
    }

    public Float buildValue(Float value) {
        return value == null || value < 0F ? 0F : value;
    }

    public Double buildValue(Double value) {
        return value == null || value < 0D ? 0D : value;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="min">
    public Long min(Long a, Long b) {
        if (a == null) {
            a = 0L;
        }
        if (b == null) {
            b = 0L;
        }
        if (a < b) {
            return a;
        }
        return b;
    }
    //</editor-fold>

    public Float getPercent(Long a, Long b, int scale) {
        Float result = 0F;
        if (a == null || a <= 0L || b == null || b <= 0L) {
            return result;
        }
        result = (Float) round(Float.class, (b * 100F / a), scale);
        return result;
    }

    public Long random() {
        return new Double(Math.random() * 100).longValue();
    }

    public Long random(Long min, Long max) {
        min = buildValue(min);
        max = buildValue(max);
        if (min.equals(max)) {
            return min;
        }
        if (min > max) {
            return random(max, min);
        }
        return min + new Double(Math.random() * (max - min + 1)).longValue();
    }

    public Integer random(Integer min, Integer max) {
        min = buildValue(min);
        max = buildValue(max);
        if (min.equals(max)) {
            return min;
        }
        if (min > max) {
            return random(max, min);
        }
        return min + new Double(Math.random() * (max - min)).intValue();
    }

    public byte[] removeZero(byte[] values) throws Exception {
        if (values == null) {
            return values;
        }
        int length = values.length;
        int size = length;
        for (int i = length - 1; i >= 0; i--) {
            byte value = values[i];
            if (value == 0) {
                size--;
                continue;
            }
            break;
        }
        if (size == length) {
            return values;
        }
        byte[] result = new byte[size];
        for (int i = 0; i < size; i++) {
            byte value = values[i];
            result[i] = value;
        }
        return result;
    }
}
