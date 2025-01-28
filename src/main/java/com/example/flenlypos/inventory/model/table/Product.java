package com.example.flenlypos.inventory.model.table;

import com.example.flenlypos.inventory.model.dto.ProductDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE product SET deleted_on = CURRENT_TIMESTAMP WHERE id=?")
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Long price;
    private Integer stock;
    private String qrCode;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant createdOn;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant lastUpdatedOn;

    @Column(insertable = false, updatable = false)
    private Instant deletedOn;

    public static void fromDto(ProductDto productDto, Product product) {
        product.id = productDto.getId();
        product.name = productDto.getName();
        product.price = productDto.getPrice();
        product.stock = productDto.getStock();
        product.qrCode = productDto.getQrCode();
    }
}
