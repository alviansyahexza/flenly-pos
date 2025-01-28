package com.example.flenlypos.inventory.controller;

import com.example.flenlypos.ApiResponse;
import com.example.flenlypos.inventory.model.dto.ProductDto;
import com.example.flenlypos.inventory.model.form.ProductFindForm;
import com.example.flenlypos.inventory.model.form.ProductForm;
import com.example.flenlypos.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping
    public ApiResponse<ProductDto> add(@RequestBody ProductForm productForm) {
        ProductDto result = productService.add(productForm);
        return ApiResponse.<ProductDto>builder()
                .data(result)
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<ProductDto> update(@PathVariable int id, @RequestBody ProductForm productForm) {
        ProductDto result = productService.update(id, productForm);
        return ApiResponse.<ProductDto>builder()
                .data(result)
                .build();
    }

    @GetMapping
    public ApiResponse<List<ProductDto>> findAll(@ModelAttribute ProductFindForm form) {
        List<ProductDto> result = productService.findAll(form);
        return ApiResponse.<List<ProductDto>>builder()
                .data(result)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> find(@PathVariable int id) {
        ProductDto result = productService.find(id);
        return ApiResponse.<ProductDto>builder()
                .data(result)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> delete(@PathVariable int id) {
        productService.delete(id);
        return ApiResponse.<Boolean>builder()
                .data(true)
                .build();
    }
}
