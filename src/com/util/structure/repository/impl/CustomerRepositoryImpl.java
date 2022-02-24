package com.util.structure.repository.impl;

import com.util.func.cache.session.ISession;
import com.util.structure.DTO.Customer;
import com.util.structure.repository.ICustomerRepository;
import com.util.structure.service.IService;
import com.util.logger.ILogger;
import com.util.logger.LoggerImpl;

import java.util.List;

public class CustomerRepositoryImpl extends BaseRepository implements ICustomerRepository {

    public CustomerRepositoryImpl(IService service) {
        this.logger = LoggerImpl.getInstance(this.getClass());
        if (service == null) {
            throw new UnsupportedOperationException();
        }
        this.service = service;
    }

    public CustomerRepositoryImpl(IService service, ILogger logger, String language) {
        this.logger = logger;
        if (service == null) {
            throw new UnsupportedOperationException();
        }
        this.service = service;
        this.language = language;
    }

    @Override
    public List<Customer> get(ISession session) {
        return null;
    }
}
