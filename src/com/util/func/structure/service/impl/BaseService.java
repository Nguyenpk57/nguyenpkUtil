package com.util.func.structure.service.impl;

import com.util.func.structure.repository.IRepository;
import com.util.func.structure.service.IService;
import com.util.logger.ILogger;

import java.util.List;

public abstract class BaseService implements IService {

    protected ILogger logger;
    protected String language;
    protected IRepository repository;


    @Override
    public Object uniqueObject(List objs) {
        Object result = null;
        if (objs == null) {
            return result;
        }
        for (Object obj : objs) {
            if (obj != null) {
                return obj;
            }
        }
        return result;
    }
}
