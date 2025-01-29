package com.example.flenlypos.auth.tools.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum UserRole {
    ROOT, ADMIN;

    private String value;

    public String val() {
        return "ROLE_" + this.name();
    }
}
