package com.example.pannel2.entity;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.example.pannel2.enums.UserRole;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;


    private String fullname;

    private String phone;

    private Double balance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    private String telegramkey;
    private String fa;

    private Long parentId;
    @ManyToOne
    @JoinColumn(name = "assigned_package_id")
    @JsonIgnore
    private ProductPackage assignedPackage;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "id", insertable = false, updatable = false)
    private List<User> children;

    // Constructors
    public User() {}

    public User(String email, String fullname, String password, String phone, Double balance, UserRole role, Long parentId, String telegramkey , String fa) {
        this.email = email;
        this.fullname = fullname;
        this.password = password;
        this.phone = phone;
        this.balance = balance;
        this.role = role;
        this.parentId = parentId;
        this.telegramkey = telegramkey;
        this.fa = fa;
    }

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

    public Long getId() {
        return id;
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

    public ProductPackage getAssignedPackage() {
        return assignedPackage;
    }

    public void setId(Long id) {
        this.id = id;
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

    @JsonIgnore
    public List<User> getChildren() {
        return children;
    }

    public void setChildren(List<User> children) {
        this.children = children;
    }

    public void setAssignedPackage(ProductPackage assignedPackage) {
        this.assignedPackage = assignedPackage;
    }
    // (Omitted here for brevity, but should be included for all fields)

    // Enum for Roles

}