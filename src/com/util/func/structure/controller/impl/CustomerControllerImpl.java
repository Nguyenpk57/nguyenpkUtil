package com.util.func.structure.controller.impl;

import com.util.func.cache.session.ISession;
import com.util.func.response.Result;
import com.util.func.structure.controller.ICustomerController;
import com.util.func.structure.service.ICustomerService;
import com.util.func.structure.service.impl.CustomerServiceImpl;
import com.util.logger.LoggerImpl;

import java.util.Calendar;

public class CustomerControllerImpl extends BaseController implements ICustomerController {
    public CustomerControllerImpl() {
        this.logger = LoggerImpl.getInstance(this.getClass());
        service = new CustomerServiceImpl();
    }

    public CustomerControllerImpl(String language) {
        this.logger = LoggerImpl.getInstance(this.getClass());
        this.language = language;
        service = new CustomerServiceImpl(this.language);
    }

    @Override
    public Result get(ISession session) {
        long start = Calendar.getInstance().getTimeInMillis();
        Result result = new Result(true, "R0000", "R0000");
        try {
            result = ((ICustomerService) service).get(session);
        } catch (Throwable ex) {
            result = new Result(false, "E0000", "E0000", ex, ex.getMessage());
            logger.error(ex.getMessage(), ex);
        } finally {
            long end = Calendar.getInstance().getTimeInMillis();
            logger.infos("duration", (end - start) + "(ms)");
        }
        return result;
    }
}
