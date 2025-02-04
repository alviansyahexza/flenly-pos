package com.example.flenlypos.auth.repository.impl;

import com.example.flenlypos.auth.model.table.User;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.repository.iface.UserRepoJpa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public int signUp(String username, String password, String role, int storeId, Optional<Integer> addedById) {
        User user = User.add(username, password, role, storeId, addedById);
        userRepoJpa.save(user);
        return user.getId();
    }
}
