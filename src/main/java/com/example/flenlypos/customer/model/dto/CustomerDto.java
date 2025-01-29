package com.example.flenlypos.customer.model.dto;

import com.example.flenlypos.customer.model.form.CustomerForm;
import com.example.flenlypos.customer.model.table.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CustomerDto {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private Instant createdOn;
    private Instant lastUpdatedOn;
    private Instant deletedOn;

    public static CustomerDto fromCustomer(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .createdOn(customer.getCreatedOn())
                .lastUpdatedOn(customer.getLastUpdatedOn())
                .deletedOn(customer.getDeletedOn())
                .build();
    }

    public static CustomerDto fromCustomerForm(CustomerForm form) {
        return CustomerDto.builder()
                .name(form.getName())
                .email(form.getEmail())
                .phone(form.getPhone())
                .build();
    }
}