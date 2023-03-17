package com.harrishjoshi.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponse {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
