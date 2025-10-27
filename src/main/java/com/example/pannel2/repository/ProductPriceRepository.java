package com.example.pannel2.repository;

import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.ProductPrice;
import com.example.pannel2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    // Find all product prices defined by a specific user (subreseller or admin)
    List<ProductPrice> findByUser(User user);

    // Find a specific product price for a given user and product
    //ProductPrice findByUserAndProduct(User user, Product product);

    // Optional: find all prices for a given product
    List<ProductPrice> findByProduct(Product product);
    Optional<ProductPrice> findByUserAndProduct(User user, Product product);
    void deleteByUserAndProduct(User user, Product product);
}

