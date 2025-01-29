package com.example.flenlypos.inventory.repository.iface;

import com.example.flenlypos.inventory.model.table.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepoJpa extends JpaRepository<Product, Integer> {
    Page<Product> findByDeletedOnNull(Pageable pageable);
}
