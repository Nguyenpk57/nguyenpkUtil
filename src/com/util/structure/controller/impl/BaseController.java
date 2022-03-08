package com.util.structure.controller.impl;

import com.util.structure.controller.IController;
import com.util.structure.service.IService;
import com.util.logger.ILogger;

public abstract class BaseController implements IController {
    protected ILogger logger;
    protected String language;
    protected IService service;

}
