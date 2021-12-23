package com.util.func.cache;

import com.util.func.FileConfigUtils;
import com.util.func.GsonUtil;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;
import org.apache.commons.lang3.BooleanUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public class CacheBusiness {

    private static final Object LOCK = new Object();
    private static CacheBusiness instance;
    private static HashMap<String, CacheBusiness> instances;
    protected ILogger logger;
    protected static HashMap map = new HashMap();
    protected boolean useCache;

    //<editor-fold defaultstate="collapsed" desc="keys">
    private final String CONFIG_CODE = "CONFIG_CODE";
    private final String CONFIG_GROUP = "CONFIG_GROUP";
    private final String PARAM_CODE_VALUES = "PARAM_CODE_VALUES";
    private final String PARAM_GROUP_VALUES = "PARAM_GROUP_VALUES";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="instance">
    private CacheBusiness() {
        logger = LoggerImpl.getInstance(this.getClass());
        initialization();
    }

    private CacheBusiness(ILogger logger) {
        this.logger = logger;
        initialization();
    }

    public static CacheBusiness getInstance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new CacheBusiness();
            }
            return instance;
        }
    }

    public static CacheBusiness getInstance(ILogger logger) {
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
                instances = new HashMap<String, CacheBusiness>();
            }
            if (!instances.containsKey(name)) {
                instances.put(name, new CacheBusiness(logger));
            }
            return instances.get(name);
        }
    }

    private void initialization() {
        try {
            useCache = BooleanUtils.toBoolean((FileConfigUtils.getInstance().getValue("use_cache_data")));
        } catch (Exception ex) {
            useCache = false;
            logger.error(ex.getMessage(), ex);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="clearCache">
    public Object setCache(String key, Object value) {
        Object result = null;
        Object resultRemoveCache = null;
        try {
            if (!useCache) {
                return result;
            }
            synchronized (LOCK) {
                if (map.containsKey(key)) {
                    resultRemoveCache = clearCache(key);
                }
                map.put(key, value);
            }
            result = map.get(key);
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.infos("setCache ----", "key", key, "resultRemoveCache", GsonUtil.getInstance().to(resultRemoveCache), "result", GsonUtil.getInstance().to(result));
        }
        return result;
    }

    public void clear() {
        synchronized (LOCK) {
            logger.info("Start CacheBusiness.clear");
            if (useCache) {
                map.clear();
            }
            logger.info("End CacheBusiness.clear");
        }
    }

    public Object clearCache(String key) {
        Object result = null;
        try {
            if (!useCache) {
                return result;
            }
            synchronized (LOCK) {
                if (map.containsKey(key)) {
                    result = map.remove(key);
                }
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.infos("clearCache ----", "key", key, "result", GsonUtil.getInstance().to(result));
        }
        return result;
    }

    public Object getCache(String key) {
        Object result = null;
        try {
            if (!useCache) {
                return result;
            }
            synchronized (LOCK) {
                if (!map.containsKey(key)) {
                    return result;
                }
                result = map.get(key);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        } finally {
            logger.infos("getCache ----", "key", key, "result", GsonUtil.getInstance().to(result));
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="load config">
    /*  TABLE: CONFIG
        PARAM_ID
        PARAM_GROUP
        PARAM_CODE  --UNIQUE
        PARAM_NAME
        PARAM_VALUE
        STATUS      -- 1
        CREATE_USER
        CREATE_TIME
        UPDATE_USER
        UPDATE_TIME
    */
    public Config getConfig(ISession session, String paramCode) {
        Config result = null;
        if (paramCode == null) {
            return result;
        }
        paramCode = paramCode.trim();
        if (paramCode.isEmpty()) {
            return result;
        }
        try {
            if (!useCache) {
                IParamBusiness business = new ParamBusinessImpl(logger);
                result = business.getConfig(session, paramCode);
                return result;
            }
            synchronized (LOCK) {
                HashMap<String, Config> m = (HashMap<String, Config>) map.get(CONFIG_CODE);
                if (m == null) {
                    m = new HashMap<String, Config>();
                }
                if (!m.containsKey(paramCode)) {
                    IParamBusiness business = new ParamBusinessImpl(logger);
                    result = business.getConfig(session, paramCode);
                    m.put(paramCode, result);
                    map.put(CONFIG_CODE, m);
                    return result;
                }
                result = m.get(paramCode);
                map.put(CONFIG_CODE, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public List<Config> getConfigs(ISession session, String paramGroup) {
        List<Config> result = null;
        if (paramGroup == null) {
            return result;
        }
        paramGroup = paramGroup.trim();
        if (paramGroup.isEmpty()) {
            return result;
        }
        try {
            if (!useCache) {
                IParamBusiness business = new ParamBusinessImpl(logger);
                result = business.getConfigsByGroup(session, paramGroup);
                return result;
            }
            synchronized (LOCK) {
                HashMap<String, List<Config>> m = (HashMap<String, List<Config>>) map.get(CONFIG_GROUP);
                if (m == null) {
                    m = new HashMap<String, List<Config>>();
                }
                if (!m.containsKey(paramGroup)) {
                    IParamBusiness business = new ParamBusinessImpl(logger);
                    result = business.getConfigsByGroup(session, paramGroup);
                    m.put(paramGroup, result);
                    map.put(CONFIG_GROUP, m);
                    return result;
                }
                result = m.get(paramGroup);
                map.put(CONFIG_GROUP, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="load value">
    public String getValueByCode(ISession session, String paramCode) {
        String result = null;
        if (paramCode == null) {
            return result;
        }
        paramCode = paramCode.trim();
        if (paramCode.isEmpty()) {
            return result;
        }
        try {
            if (!useCache) {
                IParamBusiness business = new ParamBusinessImpl(logger);
                result = business.getValueByCode(session, paramCode);
                return result;
            }
            synchronized (LOCK) {
                HashMap<String, String> m = (HashMap<String, String>) map.get(PARAM_CODE_VALUES);
                if (m == null) {
                    m = new HashMap<String, String>();
                }
                if (!m.containsKey(paramCode)) {
                    IParamBusiness business = new ParamBusinessImpl(logger);
                    result = business.getValueByCode(session, paramCode);
                    m.put(paramCode, result);
                    map.put(PARAM_CODE_VALUES, m);
                    return result;
                }
                result = m.get(paramCode);
                map.put(PARAM_CODE_VALUES, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    public List<String> getValuesByGroup(ISession session, String paramGroup) {
        List<String> result = null;
        if (paramGroup == null) {
            return result;
        }
        paramGroup = paramGroup.trim();
        if (paramGroup.isEmpty()) {
            return result;
        }
        try {
            if (!useCache) {
                IParamBusiness business = new ParamBusinessImpl(logger);
                result = business.getValuesByGroup(session, paramGroup);
                return result;
            }
            synchronized (LOCK) {
                HashMap<String, List<String>> m = (HashMap<String, List<String>>) map.get(PARAM_GROUP_VALUES);
                if (m == null) {
                    m = new HashMap<String, List<String>>();
                }
                if (!m.containsKey(paramGroup)) {
                    IParamBusiness business = new ParamBusinessImpl(logger);
                    result = business.getValuesByGroup(session, paramGroup);
                    m.put(paramGroup, result);
                    map.put(PARAM_GROUP_VALUES, m);
                    return result;
                }
                result = m.get(paramGroup);
                map.put(PARAM_GROUP_VALUES, m);
            }
        } catch (Throwable ex) {
            logger.error(ex.getMessage(), ex);
        }
        return result;
    }

    //</editor-fold>


}
