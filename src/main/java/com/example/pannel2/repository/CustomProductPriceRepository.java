package com.example.pannel2.repository;

import com.example.pannel2.entity.CustomProductPrice;
import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.ProductPackage;
import com.example.pannel2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CustomProductPriceRepository extends JpaRepository<CustomProductPrice, Long> {

    // Get all custom prices by package
    List<CustomProductPrice> findByProductPackage(ProductPackage productPackage);
    List<CustomProductPrice> findByProduct(Product product);

    Optional<CustomProductPrice> findByProductAndProductPackage(Product product, ProductPackage productPackage);
    void deleteByProductPackageAndProduct(ProductPackage productPackage, Product product);
}
