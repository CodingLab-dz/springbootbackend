package com.example.pannel2.dto;

import com.example.pannel2.entity.Product;
import com.example.pannel2.enums.DemandState;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DemandeUserDTO {
    private Long id;
    private LocalDateTime addedAt;

    private ProductRequest product;
    private BigDecimal buyingprice;

    private DemandState state;
    private  String demande;
    private String repense;

    public DemandeUserDTO(){}

    public DemandeUserDTO(Long id, LocalDateTime addedAt, ProductRequest product, DemandState state, BigDecimal buyingprice, String demande, String repense) {
        this.id = id;
        this.addedAt = addedAt;
        this.product = product;
        this.state = state;
        this.buyingprice= buyingprice;
        this.demande= demande;
        this.repense= repense;
    }

    public String getDemande() {
        return demande;
    }

    public void setDemande(String demande) {
        this.demande = demande;
    }

    public String getRepense() {
        return repense;
    }

    public void setRepense(String repense) {
        this.repense = repense;
    }

    public BigDecimal getBuyingprice() {
        return buyingprice;
    }

    public void setBuyingprice(BigDecimal buyingprice) {
        this.buyingprice = buyingprice;
    }

    public DemandState getState() {
        return state;
    }

    public void setState(DemandState state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public ProductRequest getProduct() {
        return product;
    }

    public void setProduct(ProductRequest product) {
        this.product = product;
    }
}
