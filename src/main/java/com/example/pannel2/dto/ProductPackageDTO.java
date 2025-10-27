package com.example.pannel2.dto;

import com.example.pannel2.entity.ProductPackage;

import java.util.List;

public class ProductPackageDTO {
    private Long id;
    private String name;
    private Long createdById;
    private List<CustomProductPriceDTO> customPrices;
    public ProductPackageDTO(){}

    public ProductPackageDTO(ProductPackage pkg) {
        this.id = pkg.getId();
        this.name = pkg.getName();
        this.createdById = pkg.getCreator() != null ? pkg.getCreator().getId() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public List<CustomProductPriceDTO> getCustomPrices() {
        return customPrices;
    }

    public void setCustomPrices(List<CustomProductPriceDTO> customPrices) {
        this.customPrices = customPrices;
    }
}
