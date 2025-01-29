package com.example.flenlypos.inventory.repository.impl;

import com.example.flenlypos.inventory.model.dto.ProductDto;
import com.example.flenlypos.inventory.model.table.Product;
import com.example.flenlypos.inventory.repository.iface.ProductRepo;
import com.example.flenlypos.inventory.repository.iface.ProductRepoJpa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductRepoImpl implements ProductRepo {

    @Autowired
    ProductRepoJpa productRepoJpaIface;

    @Override
    public Integer add(ProductDto productDto) {
        Product product = new Product();
        Product.fromDto(productDto, product);
        productRepoJpaIface.save(product);
        return product.getId();
    }

    @Override
    public void update(ProductDto productDto) {
        Product product = productRepoJpaIface.findById(productDto.getId()
        ).orElseThrow(() -> new EntityNotFoundException(String.valueOf(productDto.getId())));
        Product.fromDto(productDto, product);
        productRepoJpaIface.save(product);
    }

    @Override
    public void delete(int id) {
        productRepoJpaIface.deleteById(id);
    }

    @Override
    public ProductDto findById(int id) {
        Product product = productRepoJpaIface.findById(id
        ).orElseThrow(() -> new EntityNotFoundException(String.valueOf(id)));
        return ProductDto.fromProduct(product);
    }

    @Override
    public Iterable<ProductDto> findAll() {
        return findAll(1, 10);
    }

    @Override
    public Iterable<ProductDto> findAll(int page, int size) {
        return findAll(page, size, false);
    }

    @Override
    public Iterable<ProductDto> findAll(int page, int size, boolean includeDeleted) {
        if (includeDeleted) return productRepoJpaIface
                .findAll(Pageable.ofSize(size).withPage(page-1))
                .map(ProductDto::fromProduct);
        return productRepoJpaIface
                .findByDeletedOnNull(PageRequest.of(page-1, size))
                .map(ProductDto::fromProduct);
    }
}
