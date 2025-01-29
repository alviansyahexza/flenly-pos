package com.example.flenlypos.auth.repository.iface;

import com.example.flenlypos.auth.model.dto.UserDto;

public interface UserRepo {
    UserDto findByUsername(String username);
}