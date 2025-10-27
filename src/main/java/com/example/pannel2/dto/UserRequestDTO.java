package com.example.pannel2.dto;

import com.example.pannel2.enums.UserRole;

public class UserRequestDTO {
    private String email;
    private  String fullname;
    private String password;
    private String phone;
    private Double balance;
    private UserRole role;
    private Long parentId;
    private  String telegramkey;
    private  String fa;

    public UserRequestDTO() {}


    // Getters and Setters


    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getTelegramkey() {
        return telegramkey;
    }

    public void setTelegramkey(String telegramkey) {
        this.telegramkey = telegramkey;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public Double getBalance() {
        return balance;
    }

    public UserRole getRole() {
        return role;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
