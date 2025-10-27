package com.example.pannel2.service;


import com.example.pannel2.dto.CodeRequest;
import com.example.pannel2.dto.HistoryCodeDTO;
import com.example.pannel2.entity.ProductCode;
import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.User;
import com.example.pannel2.enums.CodeStatus;
import com.example.pannel2.enums.UserRole;
import com.example.pannel2.repository.ProductCodeRepository;
import com.example.pannel2.repository.ProductRepository;
import com.example.pannel2.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CodeService {
    private final ProductRepository productRepository;
    private final ProductCodeRepository productCodeRepository;
    private final UserRepository userRepository;

    public CodeService(ProductRepository productRepository, ProductCodeRepository productCodeRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.productCodeRepository = productCodeRepository;
        this.userRepository = userRepository;
    }

    public void addCodeToProduct(Long productId, ProductCode productCode) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        productCode.setProduct(product);
        productCode.setAddedAt(LocalDateTime.now());
        productCodeRepository.save(productCode);
    }



   /* public List<ProductCode> getusercodes(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        switch (user.getRole()){
            case
        }

    }*/

}
