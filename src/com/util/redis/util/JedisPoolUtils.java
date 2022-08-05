package com.util.redis.util;

import com.util.func.config.ResourceUtils;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author nguyenpk
 * @since 2022-08-05
 */

public class JedisPoolUtils {
    private static final Logger logger = Logger.getLogger(JedisPoolUtils.class);
    private static JedisSentinelPool pool = null;

    private static void createJedisPool() {
        try {
            JedisPoolConfig config = new JedisPoolConfig();

            String maxWaitMillis = ResourceUtils.getInstance().getResourceDefault("REDIS_MAX_WAIT_MILLIS");
            String maxTotal = ResourceUtils.getInstance().getResourceDefault("REDIS_MAX_TOTAL");
            String maxIdle = ResourceUtils.getInstance().getResourceDefault("REDIS_MAX_IDLE");
            String minIdle = ResourceUtils.getInstance().getResourceDefault("REDIS_MIN_IDLE");
            String redisSentinel = ResourceUtils.getInstance().getResourceDefault("REDIS_SENTINEL_IP_PORT");
            String masterName = ResourceUtils.getInstance().getResourceDefault("REDIS_DB_NAME");
//            String password = ResourceUtils.getInstance().getResourceDefault("REDIS_PASSWORD");

            config.setMaxTotal(Integer.parseInt(maxTotal));
            config.setMaxWaitMillis(Long.parseLong(maxWaitMillis));
            config.setMaxIdle(Integer.parseInt(maxIdle));
            config.setMinIdle(Integer.parseInt(minIdle));

            Set<String> sentinels = new HashSet<>(Arrays.asList(redisSentinel.split(",")));
            pool = new JedisSentinelPool(masterName, sentinels);

        } catch (Exception ex) {
            logger.error("ERROR INIT DATABASE REDIS ", ex);
        }
    }

    private static synchronized void poolInit() {
        if (pool == null)
            createJedisPool();
    }

    protected static Jedis getJedis() {
        if (pool == null)
            poolInit();
        return pool.getResource();
    }

    public static void release(Jedis jedis) {
        if (null != jedis) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        Jedis jedis = getJedis();
    }
}
