package com.util.func.cache.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {

    protected HashMap map = new HashMap();

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

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
