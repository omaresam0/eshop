package com.eshop.eshop.service.product;

import com.eshop.eshop.dto.ImageDto;
import com.eshop.eshop.dto.ProductDto;
import com.eshop.eshop.exception.AlreadyExistException;
import com.eshop.eshop.exception.ProductNotFoundException;
import com.eshop.eshop.model.*;
import com.eshop.eshop.repository.CategoryRepository;
import com.eshop.eshop.repository.ImageRepository;
import com.eshop.eshop.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import request.AddProductRequest;
import request.UpdateProductRequest;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;

    private final ModelMapper modelMapper;
//    public ProductServiceImpl(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }


    @Override
    public Product addProduct(AddProductRequest request) {
        if(productExists(request.getName(), request.getBrand())){
            throw new AlreadyExistException(request.getName()+" - "+request.getBrand()+" already exists, you can update it");
        }
        // if category exist, save it
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(()-> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    private boolean productExists(String name, String brand){
        return productRepository.existsByNameAndBrand(name,brand);
    }

    private Product createProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getDescription(),
                request.getStockCount(),
                request.getPrice(),
                category

        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {throw new ProductNotFoundException("Product not found!");});
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long id) {
        return productRepository.findById(id)
                .map(product -> updatingProduct(product,request))
                .map(productRepository :: save)
                .orElseThrow(() -> new ProductNotFoundException("Product Not found"));
    }

    public Product updatingProduct(Product product, UpdateProductRequest request){
        product.setName(request.getName());
        product.setBrand(request.getBrand());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setStockCount(request.getStockCount());
        Category category = categoryRepository.findByName(request.getCategory().getName());
        product.setCategory(category);
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByBrandAndCategory(String brand, String category) {
        return productRepository.findByBrandAndCategoryName(brand, category);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByNameAndBrand(String name, String brand) {
        return productRepository.findByNameAndBrand(name, brand);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream().map(this::convertToDto).toList();
    }


    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }


}
