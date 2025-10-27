package com.example.pannel2.dto;


import com.example.pannel2.entity.User;
import com.example.pannel2.enums.UserRole;

public class UserResponseDTO {
    private Long id;
    private String email;
    private  String fullname;
    private String phone;
    private Double balance;
    private String role;
    private Long parentId;
    private  String password;

    private Long assignedPackageId;
    private  String telegramkey;
    private String fa;


    public UserResponseDTO() {}

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.balance = user.getBalance();
        this.role = user.getRole().toString();
        this.parentId = user.getParentId();
        this.password = user.getPassword();
        this.assignedPackageId =
                user.getAssignedPackage() != null ? user.getAssignedPackage().getId() : null;
        this.telegramkey = user.getTelegramkey();
        this.fullname = user.getFullname();
        this.fa= user.getFa();
    }

    // Getters and Setters


    public String getFa() {
        return fa;
    }

    public void setFa(String fa) {
        this.fa = fa;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getTelegramkey() {
        return telegramkey;
    }

    public void setTelegramkey(String telegramkey) {
        this.telegramkey = telegramkey;
    }

    public Long getAssignedPackageId() {
        return assignedPackageId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAssignedPackageId(Long assignedPackageId) {
        this.assignedPackageId = assignedPackageId;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Double getBalance() {
        return balance;
    }

    public String getRole() {
        return role;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
