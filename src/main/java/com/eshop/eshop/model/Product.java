package com.eshop.eshop.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String name;
    private String brand;
    private String description;
    private int stockCount; // stock quantity
    private BigDecimal price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    public Product(String name, String brand, String description, int stockCount, BigDecimal price, Category category) {
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.stockCount = stockCount;
        this.price = price;
        this.category = category;
    }
}
