package com.util.func.cache.entity;

import java.util.HashMap;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public class BaseEntity {

    protected HashMap map = new HashMap();

    public HashMap getMap() {
        return map;
    }

    public void setMap(HashMap map) {
        this.map = map;
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public void put(Object key, Object value) {
        map.put(key, value);
    }
}
