package com.example.flenlypos.auth.repository.iface;

import com.example.flenlypos.auth.model.table.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepoJpa extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}