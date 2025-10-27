package com.example.pannel2.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "thepackages")
@Entity
public class ProductPackage {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "creator_id")
        @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "packages"})
        private User creator;

        @OneToMany(mappedBy = "productPackage", cascade = CascadeType.ALL, orphanRemoval = true)
        @JsonManagedReference
        private List<CustomProductPrice> customPrices = new ArrayList<>();

        public ProductPackage() {}

        public ProductPackage(String name, User creator) {
            this.name = name;
            this.creator = creator;
        }

        // ✅ Helper method: always keep both sides in sync
        public void addCustomPrice(CustomProductPrice price) {
            if (!this.customPrices.contains(price)) {
                this.customPrices.add(price);
                price.setProductPackage(this);
            }
        }

        // ✅ Helper method: remove custom price safely
        public void removeCustomPrice(CustomProductPrice price) {
            this.customPrices.remove(price);
            price.setProductPackage(null);
        }

        // Getters & setters (important for JSON serialization)
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public User getCreator() { return creator; }
        public void setCreator(User creator) { this.creator = creator; }

        public List<CustomProductPrice> getCustomPrices() { return customPrices; }
        public void setCustomPrices(List<CustomProductPrice> customPrices) { this.customPrices = customPrices; }
    }






/*
@Entity
@Table(name = "packages")
public class ProductPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @OneToMany(mappedBy = "pkg", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomProductPrice> customPrices;

    // Constructors
    public ProductPackage() {}

    public ProductPackage(String name, User creator) {
        this.name = name;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public User getCreator() {
        return creator;
    }

    public List<CustomProductPrice> getCustomPrices() {
        return customPrices;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setCustomPrices(List<CustomProductPrice> customPrices) {
        this.customPrices = customPrices;
    }
}*/
