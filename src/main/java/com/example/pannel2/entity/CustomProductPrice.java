package com.example.pannel2.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "custom_product_prices")
public class CustomProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal customBuyingPrice;
    private BigDecimal customSellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    @JsonBackReference
    private ProductPackage productPackage;

    public CustomProductPrice() {}

    public CustomProductPrice(Product product, BigDecimal buying, BigDecimal selling) {
        this.product = product;
        this.customBuyingPrice = buying;
        this.customSellingPrice = selling;
    }

    // ✅ Getters & Setters (important for Jackson & persistence)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getCustomBuyingPrice() { return customBuyingPrice; }
    public void setCustomBuyingPrice(BigDecimal customBuyingPrice) { this.customBuyingPrice = customBuyingPrice; }

    public BigDecimal getCustomSellingPrice() { return customSellingPrice; }
    public void setCustomSellingPrice(BigDecimal customSellingPrice) { this.customSellingPrice = customSellingPrice; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public ProductPackage getProductPackage() { return productPackage; }
    public void setProductPackage(ProductPackage productPackage) { this.productPackage = productPackage; }
}
