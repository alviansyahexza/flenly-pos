package com.example.flenlypos.customer.repository.impl;

import com.example.flenlypos.customer.model.dto.CustomerDto;
import com.example.flenlypos.customer.model.table.Customer;
import com.example.flenlypos.customer.repository.iface.CustomerRepo;
import com.example.flenlypos.customer.repository.iface.CustomerRepoJpa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerRepoImpl implements CustomerRepo {

    @Autowired
    CustomerRepoJpa customerRepoJpa;

    @Override
    public Integer add(CustomerDto customerDto) {
        Customer customer = new Customer();
        Customer.fromDto(customerDto, customer);
        customerRepoJpa.save(customer);
        return customer.getId();
    }

    @Override
    public void delete(int id) {
        customerRepoJpa.deleteById(id);
    }

    @Override
    public CustomerDto findById(int id) {
        Customer customer = customerRepoJpa.findById(id
        ).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return CustomerDto.fromCustomer(customer);
    }
}
