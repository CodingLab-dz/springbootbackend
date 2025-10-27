package com.example.pannel2.controller;

import com.example.pannel2.dto.ProductWithPriceDTO;
import com.example.pannel2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pannel2.entity.*;

import java.math.BigDecimal;
import java.util.Optional;


@RestController
@RequestMapping("/api/productprice")
public class ProductPriceController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    /*@PostMapping("/add")
    public ResponseEntity<?> addProductPrice(@RequestBody ProductWithPriceDTO dto) {

        User user = userRepository.findById(dto.getCreatedBy())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductPrice price = new ProductPrice();
        price.setUser(user);
        price.setProduct(product);
        price.setSellingPrice(dto.getSellingPrice());

        productPriceRepository.save(price);
        return ResponseEntity.ok("Product price added successfully");
    }*/

    @PostMapping("/add/{userId}/{productId}/{sellingprice}")
    public ResponseEntity<String> addProductPrice(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @PathVariable Double sellingprice) {

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalUser.isEmpty() || optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Product not found");
        }

        User user = optionalUser.get();
        Product product = optionalProduct.get();

        ProductPrice productPrice = new ProductPrice();
        productPrice.setUser(user);
        productPrice.setProduct(product);
        productPrice.setSellingPrice(BigDecimal.valueOf(sellingprice));

        productPriceRepository.save(productPrice);

        return ResponseEntity.ok("Product price added successfully");
    }


}
