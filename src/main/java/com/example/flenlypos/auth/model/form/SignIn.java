package com.example.flenlypos.auth.model.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SignIn {
    private String password;
    private String username;
}