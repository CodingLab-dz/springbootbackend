package com.example.pannel2.entity;


import com.example.pannel2.enums.DemandState;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "historic")
public class Historic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private BigDecimal buyingprice;
    @Column(name = "price")
    private BigDecimal price;


    private LocalDateTime addedAt;
    @Column(name= "code")
    private String code;

    public Historic(){}

    public Historic(User user, Product product, BigDecimal buyingprice, BigDecimal price, String code, LocalDateTime addedAt) {
        this.user = user;
        this.product = product;
        this.buyingprice = buyingprice;
        this.price = price;
        this.code= code;
        this.addedAt = addedAt;

    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getBuyingprice() {
        return buyingprice;
    }

    public void setBuyingprice(BigDecimal buyingprice) {
        this.buyingprice = buyingprice;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
