package com.example.pannel2.repository;

import com.example.pannel2.entity.PackageProductPrice;
import com.example.pannel2.entity.ProductPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageProductPriceRepository extends JpaRepository<PackageProductPrice, Long> {

    List<PackageProductPrice> findByProductPackage(ProductPackage productPackage);

    List<PackageProductPrice> findByProductPackageId(Long packageId);

    PackageProductPrice findByProductPackageIdAndProductId(Long packageId, Long productId);
}

