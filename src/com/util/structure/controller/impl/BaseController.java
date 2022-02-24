package com.util.structure.controller.impl;

import com.util.structure.service.IService;
import com.util.logger.ILogger;

public abstract class BaseController {
    protected ILogger logger;
    protected String language;
    protected IService service;

}
