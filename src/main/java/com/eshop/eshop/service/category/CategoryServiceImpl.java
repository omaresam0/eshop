package com.eshop.eshop.service.category;

import com.eshop.eshop.exception.AlreadyExistException;
import com.eshop.eshop.exception.ResourceNotFound;
import com.eshop.eshop.model.Category;
import com.eshop.eshop.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ResourceNotFound("Category Not Found"));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }


    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c-> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(()-> new AlreadyExistException(category.getName() + " already exists"));
    }



    @Override
    public Category updateCategory(Category category, Long id) {
        return categoryRepository.findById(id)
                .map(existingCategory -> updateCategoryFields(existingCategory, category))
                .map(categoryRepository::save)
                .orElseThrow(() -> new ResourceNotFound("Category Not Found"));
    }


    private Category updateCategoryFields(Category existingCategory, Category category) {
        existingCategory.setName(category.getName());
        return existingCategory;
    }


    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,
                        ()-> {throw new ResourceNotFound("Category Not Found");
                });
    }
}
