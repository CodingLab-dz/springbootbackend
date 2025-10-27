package com.example.pannel2.service;


import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.repository.CategoryRepository;
import org.antlr.v4.runtime.misc.LogManager;
import org.springframework.stereotype.Service;
import com.example.pannel2.entity.Category;

@Service
public class CategoryService {

    CategoryRepository categoryRepository;
    public Category createCategory(String name, CategoryType type, String color) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setColor(color);
        return categoryRepository.save(category);
    }
}
