package com.util.func.structure.service.impl;

import com.util.func.GsonUtils;
import com.util.func.cache.session.ISession;
import com.util.func.response.Result;
import com.util.func.structure.DTO.Customer;
import com.util.func.structure.repository.impl.CustomerRepositoryImpl;
import com.util.func.structure.service.ICustomerService;
import com.util.logger.LoggerImpl;

import java.util.List;

public class CustomerServiceImpl extends BaseService implements ICustomerService {

    public CustomerServiceImpl() {
        this.logger = LoggerImpl.getInstance(this.getClass());
        this.repository = new CustomerRepositoryImpl(this, this.logger, this.language);
    }

    public CustomerServiceImpl(String language) {
        this.logger = LoggerImpl.getInstance(this.getClass());
        this.language = language;
        this.repository = new CustomerRepositoryImpl(this, this.logger, this.language);
    }

    @Override
    public Result get(ISession session) throws Exception {
        Result result = new Result(true, "R0000", "R0000");
        try {
            List<Customer> lstCustomers = ((CustomerRepositoryImpl) repository).get(session);
            result.put("lstCustomers", lstCustomers);
        } catch (Exception ex) {
            result = new Result(false, "E0000", "E0000", ex, ex.getMessage());
        } finally {
            logger.info("getVasOrderPromos:result=" + GsonUtils.getInstance(logger).to(result));
        }
        return result;
    }
}
