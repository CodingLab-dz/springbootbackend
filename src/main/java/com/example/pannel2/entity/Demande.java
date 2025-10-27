package com.example.pannel2.entity;


import com.example.pannel2.enums.DemandState;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "demandes")
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private DemandState state = DemandState.ON_ATTENTE;


    private Double buyingprice;


    private LocalDateTime addedAt;
    @Column(name="demande")
    private String demande;
    @Column(name="repense")
    private  String repense;

    public Demande (){}

    public Demande(User user, Product product, DemandState state, Double buyingprice, LocalDateTime addedAt, String dmande, String repense) {
        this.user = user;
        this.product = product;
        this.state = state;
        this.buyingprice = buyingprice;
        this.addedAt = addedAt;
        this.demande= dmande;
        this.repense= repense;
    }

    public String getDemande() {
        return demande;
    }

    public void setDemande(String demande) {
        this.demande = demande;
    }

    public String getRepense() {
        return repense;
    }

    public void setRepense(String repense) {
        this.repense = repense;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getAddedAt() {
        return addedAt;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public DemandState getState() {
        return state;
    }

    public void setState(DemandState state) {
        this.state = state;
    }

    public Double getBuyingprice() {
        return buyingprice;
    }

    public void setBuyingprice(Double buyingprice) {
        this.buyingprice = buyingprice;
    }

    public void setAddedAt(LocalDateTime addedAt) {
        this.addedAt = addedAt;
    }
}
