package com.example.flenlypos.auth.repository.iface;

import com.example.flenlypos.auth.model.table.Store;
import com.example.flenlypos.auth.model.table.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepoJpa extends CrudRepository<Store, Integer> {
}