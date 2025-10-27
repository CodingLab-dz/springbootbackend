package com.example.pannel2.controller;

import com.example.pannel2.dto.*;
import com.example.pannel2.entity.*;
import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.enums.CodeStatus;
import com.example.pannel2.enums.UserRole;
import com.example.pannel2.repository.*;
import com.example.pannel2.service.ProductService;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")// Allow Next.js (or any frontend) to make requests
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductCodeRepository codeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;


    //create product with code
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest request) {
        // Check if category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create product
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(category);
        product.setBuyingPrice(request.getBuyingPrice());
        product.setSellingPrice(request.getSellingPrice());
        product = productRepository.save(product);

        // Add codes
        List<ProductCode> codes = new ArrayList<>();
        for (CodeRequest codeReq : request.getCodes()) {
            ProductCode code = new ProductCode();
            code.setCode(codeReq.getCode());
            code.setStatus(codeReq.getStatus() != null ? codeReq.getStatus() : CodeStatus.AVAILABLE);
            code.setAddedAt(LocalDateTime.now());
            code.setProduct(product); // associate code with product
            code.setBuyingPrice(codeReq.getBuyingPrice()); // ✅ camelCase
            codes.add(code);
        }

        codeRepository.saveAll(codes);

        return ResponseEntity.status(HttpStatus.CREATED).body("Product created with codes.");
    }
    @PostMapping("/addproduct")
    public ResponseEntity<?> createproductonly(@RequestBody ProductRequest request){
        // Check if category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // Create product
        Product product = new Product();
        product.setName(request.getName());
        product.setCategory(category);
        product.setBuyingPrice(request.getBuyingPrice());
        product.setSellingPrice(request.getSellingPrice());
        productRepository.save(product);

        return  ResponseEntity.status(HttpStatus.CREATED).body("Product created");
    }

    // Create product (admin only - no auth checks yet)
    /*@PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductRequest productRequest) {
        // Validate category existence
        Category category = categoryRepository.findById(productRequest.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().body("Category not found");
        }

        // Validate code presence for CODE category
        if (category.getType() == CategoryType.CODE) {
            if (productRequest.ge getCode() == null || productRequest.getCode().isEmpty()) {
                return ResponseEntity.badRequest().body("Code is required for CODE type category");
            }
            if (productRepository.existsByCode(productRequest.getCode())) {
                return ResponseEntity.badRequest().body("Code already exists");
            }
        }

        // For DEMAND type, demandState must be present
        if (category.getType() == CategoryType.DEMAND) {
            if (productRequest.getDemandState() == null) {
                return ResponseEntity.badRequest().body("demandState is required for DEMAND type category");
            }
        }

        // Create product entity
        Product product = new Product();
        product.setCategory(category);
        product.setName(productRequest.get); setCode(productRequest.getCode());
        product.setBuyingPrice(productRequest.getBuyingPrice());
        product.setSellingPrice(productRequest.getSellingPrice());

        if (category.getType() == CategoryType.CODE) {
            product.setStatus(productRequest.getStatus() != null ? productRequest.getStatus() : CodeStatus.AVAILABLE);
            product.setDemandState(null);
        } else {
            product.setDemandState(productRequest.getDemandState());
            product.setStatus(null);
        }

        Product saved = productRepository.save(product);
        return ResponseEntity.ok(saved);
    }*/

    // List all products
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    //get products with codes
    @GetMapping("/getallwithcodes")
    public ResponseEntity<?> getAllProductsWithAvailableCodes() {
        List<Product> products = productRepository.findAll();

        List<ProductRequest> responseList = products.stream().map(product -> {
            ProductRequest response = new ProductRequest();
            response.setId(product.getId());
            response.setName(product.getName());
            response.setSellingPrice(product.getSellingPrice());
            response.setBuyingPrice(product.getBuyingPrice());
            response.setCategoryId(product.getCategory().getId());

            // Filter only available codes
            List<CodeRequest> availableCodes = product.getCodes().stream()
                    .filter(Code-> Code.getStatus() == CodeStatus.AVAILABLE) // only codes where available == true
                    .map(code -> {
                        CodeRequest codeRes = new CodeRequest();
                        codeRes.setId(code.getId());
                        codeRes.setCode(code.getCode());
                        codeRes.setStatus(code.getStatus());
                        return codeRes;
                    }).collect(Collectors.toList());

            response.setCodes(availableCodes);
            return response;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/for-user/{userId}/{categoryId}")
    public ResponseEntity<List<ProductWithPriceDTO>> getProductsForUser(@PathVariable Long userId, @PathVariable Long categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ProductWithPriceDTO> products = productService.getProductsForUser(user, categoryId);
        return ResponseEntity.ok(products);
    }
    @GetMapping("/product-only/{userId}")
    public ResponseEntity<List<ProductDTO>> getProductOnly(@PathVariable Long userId){
        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        List<ProductDTO> productDTOS = productService.getProductOnlysForUser(user);
        return ResponseEntity.ok(productDTOS);
    }

    @GetMapping("/myproducts/{userId}")
    public ResponseEntity<List<ProductDTO>> getMyproducts(@PathVariable Long userId){
        User user= userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        List<ProductDTO> productDTOS = productService.getProductcreatedbyuser(user);
        return ResponseEntity.ok(productDTOS);
    }

    @PostMapping("/increaseprice/user/{userId}/product/{productId}/monton/{monton}")
    public String increaseProductPrice(@PathVariable Long userId, @PathVariable Long productId, @PathVariable Double monton){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
        productService.increaseProductPrice(user, product, monton);
        return "Selling price for product " + productId + " increased by " + monton + "$ successfully.";
    }

    @PostMapping("/decreaseprice/user/{userId}/product/{productId}/monton/{monton}")
    public String decreaseProductPrice(@PathVariable Long userId, @PathVariable Long productId, @PathVariable Double monton){
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("User not found"));
        Product product = productRepository.findById(productId).orElseThrow(()->new RuntimeException("Product not found"));
        productService.decreaseProductPrice(user, product, monton);
        return "Selling price for product " + productId + " decreased by " + monton + "$ successfully.";
    }
    /*public List<ProductWithPriceResponse> getProductsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getAssignedPackage() != null) {
            // Get products from package
            ProductPackage pkg = user.getAssignedPackage();
            return pkg.getProductPrices().stream()
                    .map(p -> new ProductWithPriceResponse(
                            p.getProduct().getId(),
                            p.getProduct().getName(),
                            p.getSellingPrice()))
                    .collect(Collectors.toList());

        } else if (user.getParentId() != null) {
            User parent = userRepository.findById(user.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            if (parent.getRole() == UserRole.SUBRESELLER) {
                // Get from ProductPrice set by subreseller
                List<ProductPrice> prices = productPriceRepository.findByUser(parent);
                return prices.stream()
                        .map(p -> new ProductWithPriceResponse(
                                p.getProduct().getId(),
                                p.getProduct().getName(),
                                p.getSellingPrice()))
                        .collect(Collectors.toList());

            } else if (parent.getRole() == UserRole.ADMIN) {
                // Return all products with default admin selling price
                return productRepository.findAll().stream()
                        .map(p -> new ProductWithPriceResponse(
                                p.getId(),
                                p.getName(),
                                p.getSellingPrice()))
                        .collect(Collectors.toList());
            }
        }

        // No prices defined
        return new ArrayList<>();
    }*/


    /*public ResponseEntity<List<ProductWithPriceDTO>> getProductsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(productService.getProductsForUser(userId));
    }*/
}

