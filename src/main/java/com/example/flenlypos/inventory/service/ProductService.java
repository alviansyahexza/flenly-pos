package com.example.flenlypos.inventory.service;

import com.example.flenlypos.FValidator;
import com.example.flenlypos.inventory.model.dto.ProductDto;
import com.example.flenlypos.inventory.model.form.ProductFindForm;
import com.example.flenlypos.inventory.model.form.ProductForm;
import com.example.flenlypos.inventory.repository.iface.ProductRepo;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;
    @Autowired
    FValidator validator;

    public ProductDto add(ProductForm params) {
        validator.validateParam(params);
        ProductDto productDto = ProductDto.fromProductForm(params);
        int id = productRepo.add(productDto);
        return productRepo.findById(id);
    }

    @Transactional
    public ProductDto update(int id, ProductForm params) {
        validator.validateParam(params);
        ProductDto byId = productRepo.findById(id);
        byId.setName(params.getName());
        byId.setQrCode(params.getQrCode());
        byId.setStock(params.getStock());
        byId.setPrice(params.getPrice());
        productRepo.update(byId);
        return productRepo.findById(id);
    }

    public void delete(int id) {
        productRepo.delete(id);
    }

    public ProductDto find(int id) {
        return productRepo.findById(id);
    }

    public List<ProductDto> findAll(ProductFindForm params) {
        validator.validateParam(params);
        return (List<ProductDto>) productRepo.findAll(params.getPage(), params.getSize());
    }
}
