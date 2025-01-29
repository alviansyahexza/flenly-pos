package com.example.flenlypos.customer.repository.iface;

import com.example.flenlypos.customer.model.dto.CustomerDto;

public interface CustomerRepo {
    Integer add(CustomerDto productDto);
    void delete(int id);
    CustomerDto findById(int id);
}


