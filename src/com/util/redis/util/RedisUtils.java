package com.util.redis.util;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * @author nguyenpk
 * @since 2022-08-05
 */
public class RedisUtils {
    private static final Logger logger = Logger.getLogger(RedisUtils.class);
    private static RedisUtils instance;

    public static RedisUtils getInstance() {
        if (instance == null) {
            instance = new RedisUtils();
        }
        return instance;
    }

    public void set(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            jedis.set(key, value);
        } catch (RuntimeException re) {
            logger.error("RedisUtil hset: ", re);
        } finally {
            JedisPoolUtils.release(jedis);
        }
    }

    public void hset(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            jedis.hset(key, field, value);
        } catch (RuntimeException re) {
            logger.error("RedisUtil hset: ", re);
        } finally {
            JedisPoolUtils.release(jedis);
        }
    }

    public String get(String key) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            return jedis.get(key);
        } catch (RuntimeException re) {
            logger.error("RedisUtil get: ", re);
            return null;
        } finally {
            JedisPoolUtils.release(jedis);
        }
    }

    public String hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = JedisPoolUtils.getJedis();
            return jedis.hget(key, field);
        } catch (RuntimeException re) {
            logger.error("RedisUtil hget: ", re);
            return null;
        } finally {
            JedisPoolUtils.release(jedis);
        }
    }

    public void main(String args[]) {
        RedisUtils.getInstance().hset("a", "1234", "1253");
        String value = RedisUtils.getInstance().hget("a", "1234");
    }
}
