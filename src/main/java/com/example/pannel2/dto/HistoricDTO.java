package com.example.pannel2.dto;

import com.example.pannel2.entity.User;
import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.enums.DemandState;
import com.example.pannel2.enums.UserRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoricDTO {

    private Long id;
    private LocalDateTime addedAt;


    private String username;
    private  String useremail;
    private UserRole userrole;
    private String productname;
    private BigDecimal buyingprice;
    private BigDecimal price;
    private String code;
    private CategoryType producttype;

    public HistoricDTO(){}

    public HistoricDTO(Long id, LocalDateTime addedAt, String product, String user, String useremail, UserRole userrole,BigDecimal buyingprice, BigDecimal price, String code ) {
        this.id = id;
        this.addedAt = addedAt;
        this.productname = product;
        this.username = user;
        this.buyingprice= buyingprice;
        this.price = price;
        this.code= code;
        this.useremail= useremail;
        this.userrole = userrole;
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

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public UserRole getUserrole() {
        return userrole;
    }

    public void setUserrole(UserRole userrole) {
        this.userrole = userrole;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public BigDecimal getBuyingprice() {
        return buyingprice;
    }

    public void setBuyingprice(BigDecimal buyingprice) {
        this.buyingprice = buyingprice;
    }

    public CategoryType getProducttype() {
        return producttype;
    }

    public void setProducttype(CategoryType producttype) {
        this.producttype = producttype;
    }
}
