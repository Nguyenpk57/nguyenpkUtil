package com.util.structure.controller;

import com.util.func.cache.session.ISession;
import com.util.func.response.Result;

public interface ICustomerController {
    Result get(ISession session);
}
