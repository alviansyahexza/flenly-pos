package com.example.flenlypos.inventory.model.form;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductFindForm {
    @Min(value = 1) int page;
    @Min(value = 1) int size;
}
