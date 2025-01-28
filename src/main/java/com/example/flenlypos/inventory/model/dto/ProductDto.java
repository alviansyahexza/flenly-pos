package com.example.flenlypos.inventory.model.dto;

import com.example.flenlypos.inventory.model.form.ProductForm;
import com.example.flenlypos.inventory.model.table.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class ProductDto {
    private Integer id;
    private String name;
    private Long price;
    private Integer stock;
    private String qrCode;
    private Instant createdOn;
    private Instant lastUpdatedOn;
    private Instant deletedOn;

    public static ProductDto fromProduct(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .qrCode(product.getQrCode())
                .price(product.getPrice())
                .createdOn(product.getCreatedOn())
                .lastUpdatedOn(product.getLastUpdatedOn())
                .deletedOn(product.getDeletedOn())
                .build();
    }

    public static ProductDto fromProductForm(ProductForm form) {
        return ProductDto.builder()
                .name(form.getName())
                .stock(form.getStock())
                .qrCode(form.getQrCode())
                .price(form.getPrice())
                .build();
    }
}