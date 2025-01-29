package com.example.flenlypos.customer.controller;

import com.example.flenlypos.ApiResponse;
import com.example.flenlypos.customer.model.dto.CustomerDto;
import com.example.flenlypos.customer.model.form.CustomerForm;
import com.example.flenlypos.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @PostMapping
    public ApiResponse<CustomerDto> add(@RequestBody CustomerForm customerForm) {
        CustomerDto result = customerService.add(customerForm);
        return ApiResponse.<CustomerDto>builder()
                .data(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CustomerDto> find(@PathVariable int id) {
        CustomerDto result = customerService.find(id);
        return ApiResponse.<CustomerDto>builder()
                .data(result)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable int id) {
        customerService.delete(id);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }
}
