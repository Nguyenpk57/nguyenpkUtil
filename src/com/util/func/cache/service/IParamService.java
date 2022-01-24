package com.util.func.cache.service;

import com.util.func.cache.entity.Param;
import com.util.func.cache.session.ISession;

import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public interface IParamService {

    String getValueByCode(ISession session, String paramCode) throws Exception;

    List<String> getValuesByGroup(ISession session, String paramGroup) throws Exception;

    Param getConfig(ISession session, String paramCode) throws Exception;

    List<Param> getConfigsByGroup(ISession session, String paramGroup) throws Exception;

}
