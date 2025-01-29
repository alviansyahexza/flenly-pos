package com.example.flenlypos.customer.repository.iface;

import com.example.flenlypos.customer.model.table.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepoJpa extends JpaRepository<Customer, Integer> {
}
