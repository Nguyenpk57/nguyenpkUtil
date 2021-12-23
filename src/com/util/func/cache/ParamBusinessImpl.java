package com.util.func.cache;

import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;

import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public class ParamBusinessImpl implements IParamBusiness {

    private ILogger logger;

    public ParamBusinessImpl() {
        this.logger = LoggerImpl.getInstance(this.getClass());
    }

    public ParamBusinessImpl(ILogger logger) {
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
    public Config getConfig(ISession session, String paramCode) throws Exception {
        return null;
    }

    @Override
    public List<Config> getConfigsByGroup(ISession session, String paramGroup) throws Exception {
        return null;
    }

}
