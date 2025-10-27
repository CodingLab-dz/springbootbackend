package com.example.pannel2.dto;

import com.example.pannel2.enums.*;

import java.math.BigDecimal;
import java.util.List;

public class ProductRequest {

    private Long id;
    private String name;
    private Long categoryId;
    private Double buyingPrice;
    private Double sellingPrice;

    // A list of codes to add when creating the product
    private List<CodeRequest> codes;

    public String getName() {
        return name;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<CodeRequest> getCodes() {
        return codes;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCodes(List<CodeRequest> codes) {
        this.codes = codes;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }
    // Getters and Setters

   /* private Long categoryId;
    private String name;

    // Getters and setters
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getBuyingPrice() { return buyingPrice; }
    public void setBuyingPrice(BigDecimal buyingPrice) { this.buyingPrice = buyingPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public CodeStatus getStatus() { return status; }
    public void setStatus(CodeStatus status) { this.status = status; }

    public DemandState getDemandState() { return demandState; }
    public void setDemandState(DemandState demandState) { this.demandState = demandState; }*/
}
