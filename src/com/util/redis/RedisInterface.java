package com.util.redis;

import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author nguyenpk
 * @since 2022-08-05
 */

public class RedisInterface {
    private SimpleDateFormat sdf;
    private int thread;
    private static final Logger log = Logger.getLogger(RedisInterface.class);

    public RedisInterface(int thread) {
        this.sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        this.thread = thread;
    }

    public String getFromCache(String DbName, String GGNAME, String feild) {
        return hget(DbName, GGNAME, feild);
    }

    private String format(String msisdn, String time) {
        if (msisdn == null || "".equalsIgnoreCase(msisdn)) {
            msisdn = "NULL";
        }

        while (time.contains(" ")) {
            time = time.replace(" ", "");
        }

        if (" ".equalsIgnoreCase(time)) {
            time = "NULL";
        }

        return msisdn + "_" + time;
    }

    private String getTime() {
        return this.sdf.format(new Date(System.currentTimeMillis()));
    }

    private int hset(String dbName, String key, String feild, String value) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("[" + this.thread + "]insert to GGNAME:" + key + "--> RECORD:IP=" + feild + ", INFO=" + value);

            long num = jedis.hset(key, feild, value).longValue();
            if (num > 0L) {
                log.info("[" + this.thread + "]Insert Success");
            } else {

                log.info("[" + this.thread + "]Update Success");
            }
            return 1;
        } catch (RuntimeException re) {
            log.error("[" + this.thread + "]insert failed", re);
            return 0;
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }


    private int hdel(String dbName, String key, String feild) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("[" + this.thread + "]delete from GGNAME:" + key + "--> RECORD:IP=" + feild);
            long num = jedis.hdel(key, new String[]{feild}).longValue();
            if (num > 0L) {
                log.info("[" + this.thread + "]Delete record " + feild + " from " + key + " Successfully");
            } else {
                log.info("[" + this.thread + "]Record " + feild + "from " + key + " not found");
            }
            return 1;
        } catch (RuntimeException re) {
            log.error("[" + this.thread + "]insert failed", re);
            return 0;
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }

    private String hget(String dbName, String key, String feild) {
        log.info("DB NAME = " + dbName);
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        String result = null;
        try {
            log.info("[" + this.thread + "]get from GGNAME:" + key + "--> RECORD:IP=" + feild);
            result = jedis.hget(key, feild);
            if (result != null) {
                log.info("[" + this.thread + "]get record " + feild + " from " + key + " --> value:" + result);
            } else {
                log.info("[" + this.thread + "]Record " + feild + "from " + key + " not found");
            }
        } catch (RuntimeException re) {
            log.error("[" + this.thread + "]Can't get from Redis" + re.getMessage(), re);
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }

        return result;
    }

    private void hmset(String dbName, String key, String feild, String value) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("[" + this.thread + "]insert to GGNAME:" + key + "--> RECORD:IP=" + feild + ", INFO=" + value);

            log.debug("[" + this.thread + "]insert successful");
        } catch (RuntimeException re) {
            log.error("[" + this.thread + "]insert failed", re);
        } finally {
            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
    }

    public void sadd(String dbName, String key, String value) {
        Jedis jedis = RedisConnectionManager.getInstance().getConnection(dbName);
        try {
            log.info("[" + this.thread + "]insert to redis: " + key + "| new value: " + value);
            jedis.sadd(key, new String[]{value});
            log.debug("[" + this.thread + "]insert successful");
        } catch (RuntimeException re) {
            log.error("[" + this.thread + "]insert failed", re);
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
            log.debug("[" + this.thread + "]check exists successful");
            return res;
        } catch (Exception re) {
            log.error("[" + this.thread + "]set exprire failed", re);
        } finally {

            RedisConnectionManager.getInstance().closeConnection(jedis);
        }
        return res;
    }
}
