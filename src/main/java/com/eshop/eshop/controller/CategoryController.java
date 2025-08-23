package com.eshop.eshop.controller;

import com.eshop.eshop.exception.AlreadyExistException;
import com.eshop.eshop.exception.ResourceNotFound;
import com.eshop.eshop.model.Category;
import com.eshop.eshop.response.ApiResponse;
import com.eshop.eshop.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories(){
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity.ok(new ApiResponse("Categories Found", categories));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Categories retrieval failed", INTERNAL_SERVER_ERROR));

        }
    }

        @PostMapping("/add")
        public ResponseEntity<ApiResponse> addCategory(@RequestBody Category categ) {
            try {
                Category category = categoryService.addCategory(categ);
                return ResponseEntity.ok(new ApiResponse("Category added successfully", category));
            } catch (AlreadyExistException e) {
                return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), null));
            }
        }
        @GetMapping("/category/{id}/category")
        public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){
            try {
                Category category = categoryService.getCategoryById(id);
                return ResponseEntity.ok(new ApiResponse("Found", category));
            } catch (ResourceNotFound e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
            }

        }
        @GetMapping("/{name}/category")
        public ResponseEntity<ApiResponse> getCategroyByName(@PathVariable String name){
            try {
                Category category = categoryService.getCategoryByName(name);
                return ResponseEntity.ok(new ApiResponse("Found", category));
            } catch (ResourceNotFound e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
            }

        }

        @DeleteMapping("/category/{id}/delete")
        public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){
            try {
                categoryService.deleteCategoryById(id);
                return ResponseEntity.ok(new ApiResponse("Deleted Successfully",null));
            } catch (ResourceNotFound e) {
                return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
            }
        }

        @PutMapping("/category/{id}/update")
        public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
            Category updatedCategory = categoryService.updateCategory(category, id);
            return ResponseEntity.ok(new ApiResponse("Updated Successfully",updatedCategory));
        } catch (ResourceNotFound e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    }


