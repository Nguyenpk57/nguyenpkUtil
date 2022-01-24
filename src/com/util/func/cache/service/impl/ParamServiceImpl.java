package com.util.func.cache.service.impl;

import com.util.func.cache.service.IParamService;
import com.util.func.cache.entity.Param;
import com.util.func.cache.session.ISession;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;

import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public class ParamServiceImpl implements IParamService {

    private ILogger logger;

    public ParamServiceImpl() {
        this.logger = LoggerImpl.getInstance(this.getClass());
    }

    public ParamServiceImpl(ILogger logger) {
        this.logger = logger;
    }

    @Override
    public String getValueByCode(ISession session, String paramCode) throws Exception {
        return null;
    }

    @Override
    public List<String> getValuesByGroup(ISession session, String paramGroup) throws Exception {
        return null;
    }

    @Override
    public Param getConfig(ISession session, String paramCode) throws Exception {
        return null;
    }

    @Override
    public List<Param> getConfigsByGroup(ISession session, String paramGroup) throws Exception {
        return null;
    }

}
