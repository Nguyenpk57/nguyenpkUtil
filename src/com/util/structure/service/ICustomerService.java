package com.util.structure.service;

import com.util.func.cache.session.ISession;
import com.util.func.response.Result;

public interface ICustomerService {
    Result get(ISession session) throws Exception;
}
