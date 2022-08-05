package com.util.redis;

import com.util.func.config.ResourceUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author nguyenpk
 * @since 2022-08-05
 */

public class RedisConnectionManager {

    private static final Logger logger = Logger.getLogger(RedisConnectionManager.class);

    private Map<String, JedisPool> connectionMgr;
    private static RedisConnectionManager instance;

    public RedisConnectionManager() {
        init();
    }

    public static RedisConnectionManager getInstance() {
        if (instance == null) {
            instance = new RedisConnectionManager();
        }
        return instance;
    }

    public void init() {
        if (this.connectionMgr == null) {
            this.connectionMgr = new HashMap();
            try {
                String dbName = ResourceUtils.getInstance().getResourceDefault("REDIS_DB_NAME");
                String ip = ResourceUtils.getInstance().getResourceDefault("REDIS_IP");
                String port = ResourceUtils.getInstance().getResourceDefault("REDIS_PORT");
                String timeout = ResourceUtils.getInstance().getResourceDefault("REDIS_TIMEOUT");
                String maxWaitMillis = ResourceUtils.getInstance().getResourceDefault("REDIS_MAX_WAIT_MILLIS");
                String maxTotal = ResourceUtils.getInstance().getResourceDefault("REDIS_MAX_TOTAL");
                String maxIdle = ResourceUtils.getInstance().getResourceDefault("REDIS_MAX_IDLE");
                String minIdle = ResourceUtils.getInstance().getResourceDefault("REDIS_MIN_IDLE");

                Properties pro = new Properties();
                pro.setProperty("ip", ip);
                pro.setProperty("port", port);
                pro.setProperty("timeout", timeout);
                pro.setProperty("maxWaitMillis", maxWaitMillis);
                pro.setProperty("maxTotal", maxTotal);
                pro.setProperty("maxIdle", maxIdle);
                pro.setProperty("minIdle", minIdle);
                JedisPool jedisPool = createJedisPool(pro);

                this.connectionMgr.put(dbName, jedisPool);

            } catch (Exception ex) {
                logger.error("==> ERROR INIT DATABASE REDIS " + ex.getMessage(), ex);
            }
        }
    }

    public JedisPool createJedisPool(Properties pro) throws Exception {
        JedisPool jedisPool;
        int minIdle, maxIdle, maxTotal, maxWaitMillis, timeout, port;
        String ip, password = null;

        if (pro.getProperty("ip") != null && !"".equals(pro.getProperty("ip"))) {
            ip = pro.getProperty("ip");
        } else {
            throw new Exception("ERROR in property: ip");
        }

        if (pro.getProperty("port") != null && !"".equals(pro.getProperty("port"))) {
            try {
                port = Integer.parseInt(pro.getProperty("port"));
            } catch (NumberFormatException ex) {
                logger.error(ex.getMessage(), ex);
                throw new Exception("ERROR in property: port");
            }
        } else {
            throw new Exception("ERROR in property: port");
        }

        if (pro.getProperty("password") != null && !"".equals(pro.getProperty("password"))) {
            try {
                password = pro.getProperty("password");
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                throw new Exception("ERROR in property: password");
            }
        }

        try {
            maxTotal = Integer.parseInt(pro.getProperty("maxTotal"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("ERROR in property: maxTotal");
        }

        try {
            maxIdle = Integer.parseInt(pro.getProperty("maxIdle"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("ERROR in property: maxIdle");
        }

        try {
            minIdle = Integer.parseInt(pro.getProperty("minIdle"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("ERROR in property: minIdle");
        }

        try {
            maxWaitMillis = Integer.parseInt(pro.getProperty("maxWaitMillis"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("ERROR in property: maxWaitMillis");
        }

        try {
            timeout = Integer.parseInt(pro.getProperty("timeout"));
        } catch (NumberFormatException ex) {
            logger.error(ex.getMessage(), ex);
            throw new Exception("ERROR in property: timeout");
        }

        JedisPoolConfig jpc = new JedisPoolConfig();
        jpc.setMaxWaitMillis(maxWaitMillis);
        jpc.setMaxTotal(maxTotal);
        jpc.setMaxIdle(maxIdle);
        jpc.setMinIdle(minIdle);
        jpc.setTestOnBorrow(true);
        jpc.setTestOnReturn(true);
        jpc.setTestWhileIdle(true);

        if (password == null) {
            jedisPool = new JedisPool(jpc, ip, port, timeout);
        } else {
            jedisPool = new JedisPool(jpc, ip, port, timeout, password);
        }
        return jedisPool;
    }


    public Jedis getConnection(String conName) {
        if (this.connectionMgr == null) {
            init();
        }
        if (this.connectionMgr.containsKey(conName)) {
            return ((JedisPool) this.connectionMgr.get(conName)).getResource();
        }
        logger.warn("Connection Manager don't contains key:" + conName);
        return null;
    }


    public void closeConnection(Jedis jedis) {
        if (jedis != null)
            jedis.close();
    }
}
