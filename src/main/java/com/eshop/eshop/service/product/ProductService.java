package com.eshop.eshop.service.product;

import com.eshop.eshop.dto.ProductDto;
import com.eshop.eshop.model.Product;
import request.AddProductRequest;
import request.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequest product);
    Product getProductById(Long id);
    void deleteProductById(Long id);
    Product updateProduct(UpdateProductRequest product, Long productId);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    List<Product> getProductByBrand(String brand);
    List<Product> getProductByBrandAndCategory(String brand, String category);
    List<Product> getProductByName(String name);
    List<Product> getProductByNameAndBrand(String name, String brand);
    Long countProductsByBrandAndName(String brand, String name);
    ProductDto convertToDto(Product product);
    List<ProductDto> getConvertedProducts(List<Product> products);
}
