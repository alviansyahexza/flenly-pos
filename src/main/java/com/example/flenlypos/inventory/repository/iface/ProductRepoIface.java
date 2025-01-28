package com.example.flenlypos.inventory.repository.iface;

import com.example.flenlypos.inventory.model.dto.ProductDto;
import com.example.flenlypos.inventory.model.table.Product;

public interface ProductRepoIface {
    Integer add(ProductDto productDto);
    void update(ProductDto productDto);
    void delete(int id);
    ProductDto findById(int id);
    Iterable<ProductDto> findAll();
    Iterable<ProductDto> findAll(int page, int size);
    Iterable<ProductDto> findAll(int page, int size, boolean includeDeleted);
}


