package com.example.flenlypos.customer.model.form;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerUpdate {
    @NotBlank
    private String name;
    @Min(value = 1)
    private Long price;
    @Min(value = 1)
    private Integer stock;
    private String qrCode;
}
