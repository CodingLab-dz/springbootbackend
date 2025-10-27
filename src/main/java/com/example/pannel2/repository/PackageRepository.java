package com.example.pannel2.repository;

import com.example.pannel2.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<ProductPackage, Long> {

    // Get all packages created by a specific user
    List<ProductPackage> findByCreator(User creator);
    List<ProductPackage> findByCreatorId(Long creatorId);

    //Optional<ProductPackage> findBuId(Long id);

    @Query("SELECT p FROM ProductPackage p LEFT JOIN FETCH p.customPrices WHERE p.id = :packageId")
    Optional<ProductPackage> findByIdWithCustomPrices(@Param("packageId") Long packageId);
}