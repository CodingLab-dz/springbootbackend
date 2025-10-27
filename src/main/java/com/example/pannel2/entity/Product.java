package com.example.pannel2.entity;

import com.example.pannel2.enums.CodeStatus;
import com.example.pannel2.enums.DemandState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "products")
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy; // Only admins

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductCode> codes;

    @Column(name = "buying_price", nullable = false)
    private Double buyingPrice;
    @Column(name = "selling_price", nullable = false)
    private Double sellingPrice;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public List<ProductCode> getCodes() {
        return codes;
    }

    public Double getBuyingPrice() {
        return buyingPrice;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public void setCodes(List<ProductCode> codes) {
        this.codes = codes;
    }

    public void setBuyingPrice(Double buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }
}
/*
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many products belong to one category
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;

    // Code only for 'CODE' type categories; null otherwise
    @Column(unique = true)
    private String code;

    @Column(nullable = false)
    private BigDecimal buyingPrice;

    @Column(nullable = false)
    private BigDecimal sellingPrice;

    // Only for CODE type products
    @Enumerated(EnumType.STRING)
    private CodeStatus status;

    // Only for DEMAND type products
    @Enumerated(EnumType.STRING)
    private DemandState demandState;


    // Constructors
    public Product() {}

    public Product(Category category, String code, BigDecimal buyingPrice, BigDecimal sellingPrice, CodeStatus status, DemandState demandState) {
        this.category = category;
        this.code = code;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.status = status;
        this.demandState = demandState;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public BigDecimal getBuyingPrice() { return buyingPrice; }
    public void setBuyingPrice(BigDecimal buyingPrice) { this.buyingPrice = buyingPrice; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public CodeStatus getStatus() { return status; }
    public void setStatus(CodeStatus status) { this.status = status; }

    public DemandState getDemandState() { return demandState; }
    public void setDemandState(DemandState demandState) { this.demandState = demandState; }
}*/
