package com.eshop.eshop.repository;

import com.eshop.eshop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByBrandAndCategoryName(String brand, String category);

    List<Product> findByName(String name);

    List<Product> findByNameAndBrand(String name, String brand);

    Long countByBrandAndName(String brand, String name);

    boolean existsByNameAndBrand(String name, String brand);
}
