package com.example.pannel2.entity;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class PackageProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_package_id")
    private ProductPackage productPackage;

    private Double sellingPrice;

    // Getters and setters


    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public ProductPackage getProductPackage() {
        return productPackage;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setProductPackage(ProductPackage productPackage) {
        this.productPackage = productPackage;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}

