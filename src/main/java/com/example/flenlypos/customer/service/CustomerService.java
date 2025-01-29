package com.example.flenlypos.customer.service;

import com.example.flenlypos.customer.model.dto.CustomerDto;
import com.example.flenlypos.customer.model.form.CustomerForm;
import com.example.flenlypos.customer.repository.iface.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepo customerRepo;

    public CustomerDto add(CustomerForm params)   {
        if (params.isEmptyEmail() || params.isEmptyPhone()) {
            throw new IllegalArgumentException("either email or phone should be filled");
        }

        CustomerDto customerDto = CustomerDto.fromCustomerForm(params);
        int id = customerRepo.add(customerDto);
        return customerRepo.findById(id);
    }

    public void delete(int id) {
        customerRepo.delete(id);
    }

    public CustomerDto find(int id) {
        return customerRepo.findById(id);
    }
}
