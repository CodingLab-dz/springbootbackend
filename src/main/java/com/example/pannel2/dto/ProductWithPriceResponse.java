package com.example.pannel2.dto;

public class ProductWithPriceResponse {
    private Long productId;
    private String name;
    private Double price;

    public ProductWithPriceResponse(Long productId, String name, Double price) {
        this.productId = productId;
        this.name = name;
        this.price = price;
    }

    // Getters and setters...

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

