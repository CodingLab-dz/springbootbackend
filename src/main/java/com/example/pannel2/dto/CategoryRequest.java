package com.example.pannel2.dto;

import com.example.pannel2.entity.Category;
import com.example.pannel2.enums.CategoryType;

public class CategoryRequest {
    private Long id;
    private String name;
    private CategoryType type;
    private String color;

    public Long getId() {
        return id;
    }

    public CategoryRequest(){}

    public CategoryRequest(Category categoy) {
        this.id = categoy.getId();
        this.name = categoy.getName();
        this.type = categoy.getType();
        this.color = categoy.getColor();
    }


    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public CategoryType getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }
}
