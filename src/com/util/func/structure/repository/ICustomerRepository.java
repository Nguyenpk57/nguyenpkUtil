package com.util.func.structure.repository;

import com.util.func.cache.session.ISession;
import com.util.func.structure.DTO.Customer;

import java.util.List;

public interface ICustomerRepository {
    List<Customer> get(ISession session);
}
