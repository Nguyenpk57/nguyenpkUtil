package com.util.structure.repository.impl;

import com.util.func.cache.session.ISession;
import com.util.structure.repository.IRepository;
import com.util.structure.service.IService;
import com.util.logger.ILogger;

import java.util.Date;

/**
 * @author nguyenpk
 * @since 2022-01-24
 */
public abstract class BaseRepository implements IRepository {
    protected ILogger logger;
    protected String language;
    protected IService service;

    @Override
    public Object insert(ISession session, Object newObj) throws Exception {
        session.save(newObj);
        session.flush();
        return newObj;
    }

    @Override
    public Object update(ISession session, Object oldObj, Object newObj) throws Exception {
        session.merge(newObj);
        session.flush();
        return newObj;
    }

    @Override
    public Object delete(ISession session, Object obj) throws Exception {
        session.delete(obj);
        session.flush();
        return obj;
    }

    @Override
    public Date getSysDate(ISession session) throws Exception {
        StringBuilder builder = new StringBuilder()
                .append(" select trunc(sysdate) sys_time from dual ");
        return new Date();
    }

    @Override
    public Date getSysDateTime(ISession session) throws Exception {
        StringBuilder builder = new StringBuilder()
                .append(" select sysdate sys_time from dual ");
        return new Date();
    }

    @Override
    public Date addMonths(ISession session, Date date, Float month) throws Exception {
        StringBuilder builder = new StringBuilder()
                .append(" select (add_months(?,?) + ?) sys_time from dual ");
        return new Date();
    }

    @Override
    public Date addDays(ISession session, Date date, Float month) throws Exception {
        StringBuilder builder = new StringBuilder()
                .append(" select (? + ?) sys_time from dual ");
        return new Date();
    }

    @Override
    public Integer getDays(ISession session, Date from, Date to) throws Exception {
        StringBuilder builder = new StringBuilder()
                .append(" select trunc(?) - trunc(?) days from dual ");
        return 0;
    }

    @Override
    public Long getSequence(ISession session, String sequenceName) throws Exception {
        StringBuilder builder = new StringBuilder()
                .append(" select ").append(sequenceName).append(".nextval value from dual ");
        return 0L;
    }

}
