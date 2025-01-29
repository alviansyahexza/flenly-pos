package com.example.flenlypos.customer.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerForm {
    private String name;
    private String email;
    private String phone;

    public boolean isEmptyEmail() {
        return email == null || email.isBlank();
    }

    public boolean isEmptyPhone() {
        return phone == null || phone.isBlank();
    }
}
