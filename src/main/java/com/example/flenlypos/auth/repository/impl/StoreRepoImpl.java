package com.example.flenlypos.auth.repository.impl;

import com.example.flenlypos.auth.model.dto.UserDto;
import com.example.flenlypos.auth.model.table.Store;
import com.example.flenlypos.auth.model.table.User;
import com.example.flenlypos.auth.repository.iface.StoreRepo;
import com.example.flenlypos.auth.repository.iface.StoreRepoJpa;
import com.example.flenlypos.auth.repository.iface.UserRepo;
import com.example.flenlypos.auth.repository.iface.UserRepoJpa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StoreRepoImpl implements StoreRepo {

    @Autowired
    StoreRepoJpa storeRepoJpa;

    @Override
    public int add(String name) {
        Store store = new Store();
        store.setName(name);
        storeRepoJpa.save(store);
        return store.getId();
    }
}
