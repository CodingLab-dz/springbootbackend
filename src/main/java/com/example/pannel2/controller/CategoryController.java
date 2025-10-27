package com.example.pannel2.controller;


import com.example.pannel2.dto.CategoryRequest;
import com.example.pannel2.entity.Category;
import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.repository.CategoryRepository;
import com.example.pannel2.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@CrossOrigin(origins = "http://localhost:3000")// Allow Next.js (or any frontend) to make requests
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;
   // private CategoryRequest categoryRequest;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/addcategory")
    public ResponseEntity<?> addCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = new Category();
        String name = categoryRequest.getName(); // make sure this line works
        CategoryType type = categoryRequest.getType();
        String color = categoryRequest.getColor();
        category.setName(name);
        category.setType(type);
        category.setColor(color);
        Category saved = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getColor()));
    }
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setType(categoryRequest.getType());
        category.setColor(categoryRequest.getColor());

        Category saved = categoryRepository.save(category);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CategoryResponse(saved.getId(), saved.getName(), saved.getType(), saved.getColor()));
    }
/*
    @Autowired
    private CategoryRepository categoryRepository;

    // Create category (admin only - for now no auth checks)
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            return ResponseEntity.badRequest().build();
        }
        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(saved);
    }
*/
    // List all categories

    @GetMapping("/getcategories")
    public List<CategoryRequest> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryRequest::new)
                .toList();
    }
}

