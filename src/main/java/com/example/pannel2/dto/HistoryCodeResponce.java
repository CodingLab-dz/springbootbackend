package com.example.pannel2.dto;

import java.time.LocalDateTime;
public class HistoryCodeResponce {
    private Long id;
    private CodeRequest code;
    private Double buyingprice;

    private LocalDateTime addedAt;

    private ProductRequest product;

    public HistoryCodeResponce(){}

    public HistoryCodeResponce(Long id, CodeRequest code, Double buyingprice, LocalDateTime addedAt, ProductRequest product) {
        this.id = id;
        this.code = code;
        this.buyingprice = buyingprice;
        this.addedAt = addedAt;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public CodeRequest getCode() {
        return code;
    }

    public Double getBuyingprice() {
        return buyingprice;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public ProductRequest getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(CodeRequest code) {
        this.code = code;
    }

    public void setBuyingprice(Double buyingprice) {
        this.buyingprice = buyingprice;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public void setProduct(ProductRequest product) {
        this.product = product;
    }
}
