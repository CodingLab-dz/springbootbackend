package com.example.pannel2.dto;

import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.enums.DemandState;

import java.math.BigDecimal;

public class ProductWithPriceDTO {


    //private Long id;
    private String name;
   // private String status;
    //private DemandState demandState;
   // private String categoryName;
    private CategoryType categoryType;
    private BigDecimal sellingPrice;
    private BigDecimal buyingPrice;
    private Long createdBy;


    private Long productId;
  //  private String code; // null for demand-type
   // private CategoryType type; // CODE or DEMAND

    public ProductWithPriceDTO() {
    }

    public ProductWithPriceDTO(Long productId, String name, BigDecimal  buyingPrice, BigDecimal  sellingPrice, CategoryType cattype, Long createdBy) {
        this.productId = productId;
        this.name = name;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.categoryType = cattype;
        this.createdBy = createdBy;
    }

    public Long getProductId() {
        return productId;
    }

    /*public String getCode() {
        return code;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getType() {
        return type;
    }*/

    public BigDecimal  getBuyingPrice() {
        return buyingPrice;
    }

    public BigDecimal  getSellingPrice() {
        return sellingPrice;
    }

    /*public String getStatus() {
        return status;
    }

    public DemandState getDemandState() {
        return demandState;
    }

    public Long getId() {
        return id;
    }*/

    public String getName() {
        return name;
    }

    public CategoryType getCategoryType() {
        return categoryType;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    /*public void setCode(String code) {
        this.code = code;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setType(String type) {
        this.type = type;
    }*/

    public void setBuyingPrice(BigDecimal  buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public void setSellingPrice(BigDecimal  sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    /*public void setStatus(String status) {
        this.status = status;
    }

    public void setDemandState(DemandState demandState) {
        this.demandState = demandState;
    }

    public void setId(Long id) {
        this.id = id;
    }*/

    public void setName(String name) {
        this.name = name;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
