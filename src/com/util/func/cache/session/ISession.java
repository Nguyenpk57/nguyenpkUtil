package com.util.func.cache.session;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public interface ISession {
    void save(Object newObj);

    void flush();

    void merge(Object newObj);

    void delete(Object obj);
}
