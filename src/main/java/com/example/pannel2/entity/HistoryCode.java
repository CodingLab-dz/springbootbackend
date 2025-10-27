package com.example.pannel2.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "historycode")
public class HistoryCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_code_id")
    private ProductCode code;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double buyingprice;

    private LocalDateTime addedAt;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public  HistoryCode(){}
    public HistoryCode(ProductCode code, User user, Double buyingprice, LocalDateTime addedAt, Product product) {
        this.code = code;
        this.user = user;
        this.buyingprice = buyingprice;
        this.addedAt = addedAt;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public ProductCode getCode() {
        return code;
    }

    public User getUser() {
        return user;
    }

    public Double getBuyingprice() {
        return buyingprice;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public Product getProduct() {
        return product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(ProductCode code) {
        this.code = code;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setBuyingprice(Double buyingprice) {
        this.buyingprice = buyingprice;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
