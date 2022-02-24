package com.util.structure.repository;

import com.util.func.cache.session.ISession;
import com.util.structure.DTO.Customer;

import java.util.List;

public interface ICustomerRepository {
    List<Customer> get(ISession session);
}
