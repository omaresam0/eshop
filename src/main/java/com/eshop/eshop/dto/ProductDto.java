package com.eshop.eshop.dto;

import java.math.BigDecimal;
import java.util.List;
import com.eshop.eshop.model.Category;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private int stockCount; // stock quantity
    private BigDecimal price;
    private Category category;
    private List<ImageDto> images;
}
