package com.example.pannel2.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.example.pannel2.entity.Product;



public interface ProductRepository extends JpaRepository<Product, Long> {

}