package com.example.pannel2.dto;

import com.example.pannel2.enums.CategoryType;

public class ProductDTO {
    private Long id;
    private String name;
    private Long categoryId;
    private Double buyingPrice;
    private Double sellingPrice;
    private String catname;
    private CategoryType type;


    public ProductDTO(){}
    public ProductDTO(Long id, String name, Long categoryId, Double buyingPrice, Double sellingPrice, String catname, CategoryType cat) {
        this.id = id;
        this.name = name;
        this.categoryId = categoryId;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.catname = catname;
        this.type = cat;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }
}
