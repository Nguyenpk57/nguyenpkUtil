package com.util.structure.repository;

import com.util.func.cache.session.ISession;

import java.util.Date;

/**
 * @author nguyenpk
 * @since 2022-01-24
 */
public interface IRepository {
    Object insert(ISession session, Object newObj) throws Exception;

    Object update(ISession session, Object oldObj, Object newObj) throws Exception;

    Object delete(ISession session, Object obj) throws Exception;

    Date getSysDate(ISession session) throws Exception;

    Date getSysDateTime(ISession session) throws Exception;

    Date addMonths(ISession session, Date date, Float month) throws Exception;

    Date addDays(ISession session, Date date, Float month) throws Exception;

    Integer getDays(ISession session, Date from, Date to) throws Exception;

    Long getSequence(ISession session, String sequenceName) throws Exception;

}
