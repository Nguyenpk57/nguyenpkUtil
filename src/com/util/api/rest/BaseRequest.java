/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.util.api.rest;

import com.util.func.GsonUtils;
import com.util.logger.ILogger;
import java.util.HashMap;

/**
 * @author nguyenpk
 * @since 2021-10-19
 */
public abstract class BaseRequest implements IRequest {

    protected ILogger logger;
    protected String language;
    protected HashMap properties;

    @Override
    public String getRequest() {
        return GsonUtils.getInstance(logger).to(properties);
    }
}