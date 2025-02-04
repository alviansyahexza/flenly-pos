package com.example.flenlypos.auth.service;

import com.example.flenlypos.FValidator;
import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.form.SignUp;
import com.example.flenlypos.auth.model.form.UserAdd;
import com.example.flenlypos.auth.repository.iface.StoreRepo;
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
public class StoreService {

    @Autowired
    StoreRepo storeRepo;

    public int add() {
        return storeRepo.add("Your Flenly Store");
    }
}