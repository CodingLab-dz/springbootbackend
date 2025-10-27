package com.example.pannel2.entity;


import com.example.pannel2.enums.CodeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class ProductCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

   /* private Double buyingPrice;
    private Double sellingPrice;*/

    @Enumerated(EnumType.STRING)
    private CodeStatus status = CodeStatus.AVAILABLE;

    private LocalDateTime addedAt;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NotNull
    @Column(name = "buyingprice")
    private Double buyingPrice;

    public ProductCode(){}

    public ProductCode(Long id, String code,CodeStatus status, LocalDateTime addedAt, Product product, Double buyingprice) {
        this.id = id;
        this.code = code;
        this.status = status;
        this.addedAt = addedAt;
        this.product = product;
        this.buyingPrice = buyingprice;
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

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Product getProduct() {
        return product;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public void setStatus(CodeStatus status) {
        this.status = status;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setBuyingPrice(Double buyingprice) {
        this.buyingPrice = buyingprice;
    }
}
