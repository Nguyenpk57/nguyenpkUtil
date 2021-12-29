/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.func;

import com.util.annotation.Column;
import com.util.annotation.Entity;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public class ReflectUtils {

    private static final Object LOCK = new Object();
    private static ReflectUtils instance;
    private static HashMap<String, ReflectUtils> instances;
    private static final HashMap map = new HashMap();
    protected ILogger logger;
    //<editor-fold defaultstate="collapsed" desc="key">
    private final String GETTER_PREFIX = "get";
    private final String GETTER_BOOLEAN_PREFIX = "is";
    private final String SETTER_PREFIX = "set";
    private final String ALL_FIELD = "ALL_FIELD";
    private final String COLUMN_FIELD = "COLUMN_FIELD";
    private final String GETTER = "GETTER";
    private final String GETTERS = "GETTERS";
    private final String SETTER = "SETTER";
    private final String SETTERS = "SETTERS";
    //</editor-fold>

    private ReflectUtils() {
        logger = LoggerImpl.getInstance(this.getClass());
    }

    private ReflectUtils(ILogger logger) {
        this.logger = logger;
    }

    public static ReflectUtils getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new ReflectUtils();
            }
            return instance;
        }
    }

    public static ReflectUtils getInstance(ILogger logger) {
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
                instances = new HashMap<String, ReflectUtils>();
            }
            if (!instances.containsKey(name)) {
                instances.put(name, new ReflectUtils(logger));
            }
            return instances.get(name);
        }
    }

    /**
     *
     * @param class1
     * @param class2
     * @return Class super nearest - ==> Cast
     */
    public Class getCastClass(Class class1, Class class2) {
        Class result = null;
        if (class1 == null || class2 == null) {
            return result;
        }
        if (class1.equals(class2)) {
            result = class1;
            return result;
        }
        List<Class> class1s = getSuperclass(class1);
        List<Class> class2s = getSuperclass(class2);
        if (class1s == null || class2s == null || class1s.isEmpty() || class2s.isEmpty()) {
            return result;
        }
        for (Class clazz : class1s) {
            if (Object.class.equals(clazz)) {
                continue;
            }
            if (class2.equals(clazz) || class2s.contains(clazz)) {
                result = clazz;
                return result;
            }
        }
        for (Class clazz : class2s) {
            if (Object.class.equals(clazz)) {
                continue;
            }
            if (class1.equals(clazz) || class1s.contains(clazz)) {
                result = clazz;
                return result;
            }
        }
        return result;
    }

    /**
     * List class super
     *
     * @param clazz
     * @return List class super of class clazz
     */
    public List<Class> getSuperclass(Class clazz) {
        List<Class> result = null;
        if (clazz == null) {
            return result;
        }
        result = new ArrayList<Class>();
        Class superclass = clazz.getSuperclass();
        while (superclass != null && !Object.class.equals(superclass)) {
            result.add(superclass);
            superclass = superclass.getSuperclass();
        }
        if (Object.class.equals(superclass)) {
            result.add(superclass);
        }
        return result;
    }

    /**
     * Lay ten phuong thuc truoc do
     *
     * @return Ten phuong thuc
     */
    public String getCaller() {
        StackTraceElement[] es = new Throwable().getStackTrace();
        String result = null;
        if (es == null) {
            return result;
        }
        int n = es.length;
        if (n <= 2) {
            return result;
        }
        StackTraceElement e = es[2];
        return e.getClassName() + "." + e.getMethodName();
    }

    public List<Field> getProperties(Class clazz) {
        if (isEntity(clazz)) {
            return getColumFields(clazz);
        }
        return getAllField(clazz);
    }

    private boolean isEntity(Class clazz) {
        Object anno = clazz == null ? null : clazz.getAnnotation(Entity.class);
        return anno != null && anno instanceof Entity;
    }

    public List<Field> getAllField(Class clazz) {
        List<Field> result = null;
        if (clazz == null) {
            return result;
        }
        String className = clazz.getName();
        if (className == null || className.isEmpty()) {
            return result;
        }
        try {
            synchronized (LOCK) {
                HashMap<String, List<Field>> m = (HashMap<String, List<Field>>) map.get(ALL_FIELD);
                if (m == null) {
                    m = new HashMap<String, List<Field>>();
                }
                if (!m.containsKey(className)) {
                    Field[] fields = clazz.getDeclaredFields();
                    if (fields != null) {
                        result = new ArrayList<Field>();
                        List<Field> catchList = Arrays.asList(fields);
                        if (!catchList.isEmpty()) {
                            result.addAll(catchList);
                        }
                    }
                    m.put(className, result);
                    map.put(ALL_FIELD, m);
                    return result;
                }
                result = m.get(className);
                map.put(ALL_FIELD, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    /**
     * Get all fields have annotation Colum
     *
     * @param clazz
     * @return List fields colum
     */
    public List<Field> getColumFields(Class clazz) {
        List<Field> result = null;
        if (clazz == null) {
            return result;
        }
        String className = clazz.getName();
        if (className == null || className.isEmpty()) {
            return result;
        }
        try {
            synchronized (LOCK) {
                HashMap<String, List<Field>> m = (HashMap<String, List<Field>>) map.get(COLUMN_FIELD);
                if (m == null) {
                    m = new HashMap<String, List<Field>>();
                }
                if (!m.containsKey(className)) {
                    Field[] fields = clazz.getDeclaredFields();
                    if (fields != null) {
                        result = new ArrayList<Field>();
                        for (Field field : fields) {
                            Column column = field.getAnnotation(Column.class);
                            if (column != null) {
                                result.add(field);
                            }
                        }
                    }
                    m.put(className, result);
                    map.put(COLUMN_FIELD, m);
                    return result;
                }
                result = m.get(className);
                map.put(COLUMN_FIELD, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    //<editor-fold defaultstate="collapsed" desc="getSetter">
    /**
     * Get getter corresponding field
     *
     * @param clazz
     * @param field
     * @return
     */
    public Method getGetter(Class clazz, Field field) {
        Method result = null;
        if (clazz == null || field == null) {
            return result;
        }
        String className = clazz.getName();
        if (className == null || className.isEmpty()) {
            return result;
        }
        String fieldName = field.getName();
        if (fieldName == null || fieldName.isEmpty()) {
            return result;
        }
        try {
            synchronized (LOCK) {
                HashMap<String, HashMap<String, Method>> m = (HashMap<String, HashMap<String, Method>>) map.get(GETTER);
                if (m == null) {
                    m = new HashMap<String, HashMap<String, Method>>();
                }
                HashMap<String, Method> mm = m.get(className);
                if (mm == null) {
                    mm = new HashMap<String, Method>();
                }
                if (!(m.containsKey(className) && mm.containsKey(fieldName))) {
                    List<Method> getters = getGetters(clazz);
                    if (getters != null) {
                        for (Method getter : getters) {
                            if (isFieldGetter(getter.getName(), fieldName) && field.getType().equals(getter.getReturnType())) {
                                result = getter;
                                break;
                            }
                        }
                    }
                    mm.put(fieldName, result);
                    m.put(className, mm);
                    map.put(GETTER, m);
                    return result;
                }
                result = mm.get(fieldName);
                m.put(className, mm);
                map.put(GETTER, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    private boolean isFieldGetter(String getterName, String fieldName) {
        return getterName.equals(GETTER_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)) || getterName.equals(GETTER_BOOLEAN_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getGetters">
    private List<Method> getGetters(Class clazz) {
        List<Method> result = null;
        if (clazz == null) {
            return result;
        }
        String className = clazz.getName();
        if (className == null || className.isEmpty()) {
            return result;
        }
        try {
            synchronized (LOCK) {
                HashMap<String, List<Method>> m = (HashMap<String, List<Method>>) map.get(GETTERS);
                if (m == null) {
                    m = new HashMap<String, List<Method>>();
                }
                if (!m.containsKey(className)) {
                    Method[] methods = clazz.getDeclaredMethods();
                    if (methods != null) {
                        result = new ArrayList<Method>();
                        for (Method method : methods) {
                            if (isGetter(method)) {
                                result.add(method);
                            }
                        }
                    }
                    m.put(className, result);
                    map.put(GETTERS, m);
                    return result;
                }
                result = m.get(className);
                map.put(GETTERS, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    private boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0) {
            if (method.getName().startsWith(GETTER_PREFIX) && !method.getReturnType().equals(Void.TYPE)) {
                return true;
            }
            if (method.getName().startsWith(GETTER_BOOLEAN_PREFIX) && method.getReturnType().equals(Boolean.TYPE)) {
                return true;
            }
        }
        return false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getSetter">
    /**
     * Get setter corresponding with field
     *
     * @param clazz
     * @param field
     * @return
     */
    public Method getSetter(Class clazz, Field field) {
        Method result = null;
        if (clazz == null || field == null) {
            return result;
        }
        String className = clazz.getName();
        if (className == null || className.isEmpty()) {
            return result;
        }
        String fieldName = field.getName();
        if (fieldName == null || fieldName.isEmpty()) {
            return result;
        }
        try {
            synchronized (LOCK) {
                HashMap<String, HashMap<String, Method>> m = (HashMap<String, HashMap<String, Method>>) map.get(SETTER);
                if (m == null) {
                    m = new HashMap<String, HashMap<String, Method>>();
                }
                HashMap<String, Method> mm = m.get(className);
                if (mm == null) {
                    mm = new HashMap<String, Method>();
                }
                if (!(m.containsKey(className) && mm.containsKey(fieldName))) {
                    List<Method> setters = getSetters(clazz);
                    if (setters != null) {
                        for (Method setter : setters) {
                            if (isFieldSetter(setter.getName(), fieldName) && field.getType().equals(setter.getParameterTypes()[0])) {
                                result = setter;
                                break;
                            }
                        }
                    }
                    mm.put(fieldName, result);
                    m.put(className, mm);
                    map.put(SETTER, m);
                    return result;
                }
                result = mm.get(fieldName);
                m.put(className, mm);
                map.put(SETTER, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    private boolean isFieldSetter(String setterName, String fieldName) {
        return setterName.equals(SETTER_PREFIX + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getSetters">
    public List<Method> getSetters(Class clazz) {
        List<Method> result = null;
        if (clazz == null) {
            return result;
        }
        String className = clazz.getName();
        if (className == null || className.isEmpty()) {
            return result;
        }
        try {
            synchronized (LOCK) {
                HashMap<String, List<Method>> m = (HashMap<String, List<Method>>) map.get(SETTERS);
                if (m == null) {
                    m = new HashMap<String, List<Method>>();
                }
                if (!m.containsKey(className)) {
                    Method[] methods = clazz.getDeclaredMethods();
                    if (methods != null) {
                        result = new ArrayList<Method>();
                        for (Method method : methods) {
                            if (isSetter(method)) {
                                result.add(method);
                            }
                        }
                    }
                    m.put(className, result);
                    map.put(SETTERS, m);
                    return result;
                }
                result = m.get(className);
                map.put(SETTERS, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    private boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) && method.getReturnType().equals(Void.TYPE) && method.getParameterTypes().length == 1 && method.getName().startsWith(SETTER_PREFIX);
    }
    //</editor-fold>

    /**
     * Create new object of class clazz
     *
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Object createInstance(Class clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        if (clazz == null) {
            return null;
        }
        Constructor constructor = clazz.getConstructor();
        return constructor.newInstance();
    }

    /**
     * Call getter lay ra value field cua object obj
     *
     * @param obj
     * @param getter
     * @return
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Object getValue(Object obj, Method getter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object result = null;
        if (obj == null || getter == null) {
            return result;
        }
        result = getter.invoke(obj);
        return result;
    }

    /**
     * Call setter set value cho object obj
     *
     * @param obj
     * @param value
     * @param setter
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public void setValue(Object obj, Object value, Method setter) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (obj == null || setter == null) {
            return;
        }
        setter.invoke(obj, value);
    }

    //<editor-fold defaultstate="collapsed" desc="clone">
    /**
     * Clone Object obj
     *
     * @param obj
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public Object clone(Object obj) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Object result = null;
        if (obj == null) {
            return result;
        }
        Class clazz = obj.getClass();
        result = createInstance(clazz);
        if (result == null) {
            return result;
        }
        List<Field> fields = getAllField(clazz);
        if (fields == null || fields.isEmpty()) {
            return result;
        }
        for (Field field : fields) {
            setValue(result, getValue(obj, getGetter(clazz, field)), getSetter(clazz, field));
        }
        return result;
    }

    public Object hyperClone(Object obj) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        Object result = null;
        if (obj == null) {
            return result;
        }
        Class clazz = obj.getClass();
        Class superClazz = clazz.getSuperclass();
        result = createInstance(clazz);
        if (result == null) {
            return result;
        }
        List<Field> fields = getAllField(clazz);
        List<Field> inheritedFields = getAllField(superClazz);
        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                setValue(result, getValue(obj, getGetter(clazz, field)), getSetter(clazz, field));
            }
        }
        if (inheritedFields != null && !inheritedFields.isEmpty()) {
            for (Field field : inheritedFields) {
                setValue(result, getValue(obj, getGetter(superClazz, field)), getSetter(superClazz, field));
            }
        }
        return result;
    }

    public List clones(List objs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        List result = null;
        if (objs == null || objs.isEmpty()) {
            return result;
        }
        result = new ArrayList();
        for (Object obj : objs) {
            result.add(clone(obj));
        }
        return result;
    }

    /**
     * Trim tat ca field kieu String
     *
     * @param obj
     * @return
     */
    public Object trim(Object obj) {
        if (obj == null) {
            return obj;
        }
        Class clazz = obj.getClass();
        List<Field> fields = getAllField(clazz);
        if (fields == null) {
            return obj;
        }
        for (Field field : fields) {
            if (field == null) {
                continue;
            }
            if (String.class.equals(field.getType())) {
                try {
                    String value = (String) getValue(obj, getGetter(clazz, field));
                    setValue(obj, org.apache.commons.lang3.StringUtils.trim(value), getSetter(clazz, field));
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        }
        return obj;
    }

    public String getStackTrace(Throwable exeption) {
        if (exeption == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        exeption.printStackTrace(new PrintWriter(sw));
        return sw.toString();

    }

    public Object escapeHtml(Object obj) throws Exception {
        if (obj != null) {
            Class clazz = obj.getClass();
            List<Field> fields = getAllField(clazz);
            if (fields != null && !fields.isEmpty()) {
                for (Field field : fields) {
                    Object objectValue = getValue(obj, getGetter(clazz, field));
                    if (objectValue instanceof String) {
                        setValue(obj, StringUtils.getInstance().trimXmlTag(org.apache.commons.lang3.StringUtils.stripAccents((String) objectValue)), getSetter(clazz, field));
                    }
                }
            }
        }
        return obj;
    }

}
