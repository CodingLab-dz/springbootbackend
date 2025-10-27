package com.example.pannel2.dto;

import java.math.BigDecimal;

public class CustomProductPriceDTO {

    private Long id;
    private Long productId;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;
    private String productName;

    public CustomProductPriceDTO(Long id, Long productId, BigDecimal buyingPrice, BigDecimal sellingPrice, String productName) {
        this.id = id;
        this.productId = productId;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = sellingPrice;
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(BigDecimal buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
