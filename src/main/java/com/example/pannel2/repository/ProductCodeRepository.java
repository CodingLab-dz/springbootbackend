package com.example.pannel2.repository;

import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.ProductCode;
import com.example.pannel2.enums.CodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductCodeRepository extends JpaRepository<ProductCode, Long> {
    boolean existsByCode(String code);
    List<ProductCode> findByProductAndStatus(Product product, CodeStatus status);
    //Optional<ProductCode> findFirstByProductAndStatusOrderByAddedAtAsc(Product product, CodeStatus status);



    Optional<ProductCode> findFirstByProductAndStatusOrderByAddedAtAsc(Product product, CodeStatus status);


}
