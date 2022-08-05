package com.util.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

/**
 * @author nguyenpk
 * @since 2022-08-05
 */

public class VNoSqlUtils {
    private static final Logger log = Logger.getLogger(VNoSqlUtils.class);
    private static VNoSqlUtils instance;

    public static VNoSqlUtils getInstance() {
        if (instance == null) {
            instance = new VNoSqlUtils();
        }
        return instance;
    }

    private int hset(String dbName, String key, String feild, String value) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("insert to GGNAME:" + key + "--> RECORD:IP=" + feild + ", INFO=" + value);
            System.out.println("insert to GGNAME:" + key + "--> RECORD:IP=" + feild + ", INFO=" + value);
            long num = jedis.hset(key, feild, value).longValue();
            if (num > 0L) {
                log.info("Insert Success");
                System.out.println("Insert Success");
            } else {
                log.info("Update Success");
                System.out.println("Update Success");
            }
            return 1;
        } catch (RuntimeException re) {
            System.out.println("insert failed" + re.getMessage());
            log.error("insert failed", re);
            return 0;
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }

    private int hdel(String dbName, String key, String feild) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("delete from GGNAME:" + key + "--> RECORD:IP=" + feild);
            long num = jedis.hdel(key, new String[]{feild}).longValue();
            if (num > 0L) {
                log.info("Delete record " + feild + " from " + key + " Successfully");
            } else {
                log.info("Record " + feild + "from " + key + " not found");
            }
            return 1;
        } catch (RuntimeException re) {
            log.error("insert failed", re);
            return 0;
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }

    private String hget(String dbName, String key, String feild) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        String result = null;
        try {
            log.info("get from GGNAME:" + key + "--> RECORD:IP=" + feild);
            result = jedis.hget(key, feild);
            if (result != null) {
                log.info("get record " + feild + " from " + key + " --> value:" + result);
            } else {
                log.info("Record " + feild + "from " + key + " not found");
            }
        } catch (RuntimeException re) {
            log.error("insert failed", re);
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }

        return result;
    }

    private void hmset(String dbName, String key, String feild, String value) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("insert to GGNAME:" + key + "--> RECORD:IP=" + feild + ", INFO=" + value);

            log.debug("insert successful");
        } catch (RuntimeException re) {
            log.error("insert failed", re);
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }

    public void sadd(String dbName, String key, String value) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("insert to redis: " + key + "| new value: " + value);
            jedis.sadd(key, new String[]{value});
            log.debug("insert successful");
        } catch (RuntimeException re) {
            log.error("insert failed", re);
            throw re;
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }

    public boolean sismember(String dbName, String key, String member) {
        boolean res = false;
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            res = jedis.sismember(key, member).booleanValue();
            log.debug("check exists successful");
            return res;
        } catch (Exception re) {
            log.error("set exprire failed", re);
        } finally {

            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
        return res;
    }
}
