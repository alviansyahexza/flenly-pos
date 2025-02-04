package com.example.flenlypos.auth.service;

import com.example.flenlypos.FValidator;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.form.SignUp;
import com.example.flenlypos.auth.model.form.UserAdd;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.tools.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    FValidator validator;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StoreService storeService;

    @Override
    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public int signUp(SignUp signUpForm) {
        validator.validateParam(signUpForm);
        int storeId = storeService.add();
        int id = userRepo.signUp(
                signUpForm.getUsername(),
                passwordEncoder.encode(signUpForm.getPassword()),
                UserRole.ROOT.val(),
                storeId,
                Optional.empty()
        );
        return id;
    }

    public int add(UserAdd userAddForm, int storeId, int userId) {
        validator.validateParam(userAddForm);
        int id = userRepo.signUp(
                userAddForm.getUsername(),
                passwordEncoder.encode(userAddForm.getPassword()),
                UserRole.ROOT.val(),
                storeId,
                Optional.of(userId)
        );
        return id;
    }
}