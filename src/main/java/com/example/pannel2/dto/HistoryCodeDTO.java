package com.example.pannel2.dto;

import com.example.pannel2.entity.*;

import java.time.LocalDateTime;

public class HistoryCodeDTO {

    private Long id;
    private CodeRequest code;
    private UserResponseDTO user;
    private Double buyingprice;

    private LocalDateTime addedAt;

    private ProductRequest product;

    //constructor
    public HistoryCodeDTO(){}

    public HistoryCodeDTO(Long id, CodeRequest code, UserResponseDTO user, Double buyingprice, LocalDateTime addedAt, ProductRequest product) {
        this.id = id;
        this.code = code;
        this.user = user;
        this.buyingprice = buyingprice;
        this.addedAt = addedAt;
        this.product = product;
    }


    //Getters & Setters


    public Long getId() {
        return id;
    }

    public CodeRequest getCode() {
        return code;
    }

    public UserResponseDTO getUser() {
        return user;
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

    public void setUser(UserResponseDTO user) {
        this.user = user;
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
