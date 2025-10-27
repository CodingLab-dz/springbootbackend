package com.example.pannel2.controller;
import com.example.pannel2.dto.ProductPackageDTO;
import com.example.pannel2.entity.ProductPackage;
import com.example.pannel2.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")// Allow Next.js (or any frontend) to make requests
@RequestMapping("/api/packages")
public class PackageController {

    @Autowired
    private PackageService packageService;


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("timestamp", LocalDateTime.now());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
    /**
     * Create a new package with custom prices.
     */
    @PostMapping
    public ResponseEntity<Map<String, String>> createPackage(@RequestParam Long creatorId,
                                                             @RequestParam String name,
                                                             @RequestBody List<PackageService.CustomPriceInput> customPrices) {
        //return packageService.createPackage(creatorId, name, customPrices);
        packageService.createPackage(creatorId, name, customPrices);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Package added successfully");

        return ResponseEntity.ok(response);
    }

    /**
     * Assign a package to a direct child user.
     */
    @PostMapping("/assign/{packageId}")
    public String assignPackage(@RequestParam Long parentId,
                                @RequestParam Long childUserId,
                                @PathVariable Long packageId) {
        packageService.assignPackageToUser(parentId, packageId, childUserId);
        return "Package assigned successfully to user " + childUserId;
    }

    @GetMapping("/creator/{creatorId}")
    public List<ProductPackageDTO> getPackagesByCreator(@PathVariable Long creatorId) {
        return packageService.getPackagesByCreator(creatorId)
                .stream()
                .map(ProductPackageDTO::new)
                .toList();
    }

    @GetMapping("/{packageId}/products")
    public ProductPackageDTO getPackageProducts(@PathVariable Long packageId) {
        return packageService.getPackageWithProducts(packageId);
    }

    @PostMapping("/{packageId}/products/{productId}/increase-price")
    public String increaseProductPrice(@PathVariable Long packageId,
                                       @PathVariable Long productId,
                                       @RequestParam Long parentId,
                                       @RequestParam BigDecimal increment) {
        packageService.increaseProductPriceForPackageAndChildren(parentId, packageId, productId, increment);
        return "Selling price for product " + productId + " increased by " + increment + "$ successfully.";
    }

    @PostMapping("/{packageId}/products/{productId}/decrease-price")
    public String decreaseProductPrice(@PathVariable Long packageId,
                                       @PathVariable Long productId,
                                       @RequestParam Long parentId,
                                       @RequestParam BigDecimal increment) {
        packageService.dicreaseProductPriceForPackageAndChildren(parentId, packageId, productId, increment);
        return "Selling price for product " + productId + " decreased by " + increment + "$ successfully.";
    }

    @PostMapping("/{packageId}/addProduct/{productId}")
    public ResponseEntity<String> addProductToPackage(
            @PathVariable Long packageId,
            @PathVariable Long productId,
            @RequestParam BigDecimal buyingPrice,
            @RequestParam BigDecimal sellingPrice) {

        packageService.addProductToPackage(packageId, productId, buyingPrice, sellingPrice);
        return ResponseEntity.ok("Product added to package successfully!");
    }

    @DeleteMapping("/remove/{packageId}/product/{productId}/user/{userId}")
    public ResponseEntity<String> removeProductFromPackage(
            @PathVariable Long packageId,
            @PathVariable Long productId,
            @PathVariable Long userId) {

        packageService.removeProductCascade(userId, packageId, productId);
        return ResponseEntity.ok("Product removed from package and all descendant packages!");
    }


}
