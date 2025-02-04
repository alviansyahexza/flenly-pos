package com.example.flenlypos.auth.repository.iface;

import com.example.flenlypos.auth.model.dto.UserDto;

import java.util.Optional;

public interface UserRepo {
    UserDto findByUsername(String username);
    int signUp(String username, String password, String role, int storeId, Optional<Integer> addedById);
}