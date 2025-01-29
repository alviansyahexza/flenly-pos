package com.example.flenlypos.auth.repository.impl;

import com.example.flenlypos.auth.model.table.User;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.repository.iface.UserRepoJpa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRepoImpl implements UserRepo {

    @Autowired
    UserRepoJpa userRepoJpa;

    @Override
    public UserDto findByUsername(String username) {
        User byUsername = userRepoJpa.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));
        return UserDto.fromUser(byUsername);
    }
}
