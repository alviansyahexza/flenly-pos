package com.example.flenlypos.auth.model.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAdd {
    @NotBlank
    @Size(min = 5, max = 100)
    private String password;
    @NotBlank @Size(min = 3, max = 100)
    private String username;
    private String role;
}