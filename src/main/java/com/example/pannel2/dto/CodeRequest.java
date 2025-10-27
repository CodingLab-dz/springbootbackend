package com.example.pannel2.dto;

import com.example.pannel2.enums.CodeStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public class CodeRequest {
    private Long id;
    private String code;
    //private Double buyingPrice;
    //private Double sellingPrice;
    private Boolean available = true; // default true
    private LocalDate addingDate;
    @Enumerated(EnumType.STRING)
    private CodeStatus status = CodeStatus.AVAILABLE;

    private Double buyingPrice;

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public CodeStatus getStatus() {
        return status;
    }

    public Boolean getAvailable() {
        return available;
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



    public void setAvailable(Boolean available) {
        this.available = available;
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
