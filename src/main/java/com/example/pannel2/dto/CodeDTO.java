package com.example.pannel2.dto;

import com.example.pannel2.entity.Product;
import com.example.pannel2.enums.CodeStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class CodeDTO {

    private Long id;
    private String code;
    //private Double buyingPrice;
    //private Double sellingPrice;// default true
    private LocalDate addingDate;
    @Enumerated(EnumType.STRING)
    private CodeStatus status = CodeStatus.AVAILABLE;

    private Double buyingPrice;
    private Double sellingPrice;
    private String product;

    // Getters and Setters


    public CodeDTO(Long id, String code, LocalDate addingDate, CodeStatus status, Double buyingPrice, Double sellingPrice, String product) {
        this.id = id;
        this.code = code;
        this.addingDate = addingDate;
        this.status = status;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.product = product;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public CodeStatus getStatus() {
        return status;
    }


    public LocalDate getAddingDate() {
        return addingDate;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setCode(String code) {
        this.code = code;
    }




    public void setStatus(CodeStatus status) {
        this.status = status;
    }

    public void setAddingDate(LocalDate addingDate) {
        this.addingDate = addingDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBuyingPrice(Double buyingprice) {
        this.buyingPrice = buyingprice;
    }
}
