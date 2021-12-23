package com.util.func.cache;

import java.util.List;

/**
 * @author nguyenpk
 * @since 2021-12-23
 */
public interface IParamBusiness {

    String getValueByCode(ISession session, String paramCode) throws Exception;

    List<String> getValuesByGroup(ISession session, String paramGroup) throws Exception;

    Config getConfig(ISession session, String paramCode) throws Exception;

    List<Config> getConfigsByGroup(ISession session, String paramGroup) throws Exception;

}
