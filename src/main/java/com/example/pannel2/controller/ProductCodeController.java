package com.example.pannel2.controller;


import com.example.pannel2.dto.CodeDTO;
import com.example.pannel2.dto.CodeRequest;
import com.example.pannel2.entity.Product;
import com.example.pannel2.entity.ProductCode;
import com.example.pannel2.enums.CodeStatus;
import com.example.pannel2.repository.ProductCodeRepository;
import com.example.pannel2.repository.ProductRepository;
import com.example.pannel2.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-codes")
public class ProductCodeController {
    private final CodeService codeService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductCodeRepository productCodeRepository;
    public ProductCodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<?> addCodesToProduct(
            @PathVariable Long productId,
            @RequestBody List<CodeRequest> codes
    ) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        Product product = optionalProduct.get();

        for (CodeRequest codeRequest : codes) {
            ProductCode code = new ProductCode();
            code.setCode(codeRequest.getCode());
            code.setAddedAt(LocalDateTime.now());
            code.setStatus(CodeStatus.AVAILABLE);
            code.setProduct(product); // important to link it
            code.setBuyingPrice(codeRequest.getBuyingPrice());

            productCodeRepository.save(code);
        }
        return ResponseEntity.ok("Codes added");
    }


    @GetMapping("/allcodes")
    public ResponseEntity<?> getallcodes(){
        List<ProductCode> codes = productCodeRepository.findAll();
        if (codes.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no codes found");
        }
        List<CodeDTO> dto = new ArrayList<>();
        for (ProductCode code : codes){
            dto.add(new CodeDTO(
                    code.getId(),
                    code.getCode(),
                    code.getAddedAt().toLocalDate(),
                    code.getStatus(),
                    code.getBuyingPrice(),
                    code.getProduct().getSellingPrice(),
                    code.getProduct().getName()
            ));
        }
        return ResponseEntity.ok(dto);
    }
}
