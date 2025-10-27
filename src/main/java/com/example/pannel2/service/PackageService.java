package com.example.pannel2.service;


import com.example.pannel2.dto.CustomProductPriceDTO;
import com.example.pannel2.dto.ProductPackageDTO;
import com.example.pannel2.dto.ProductWithPriceDTO;
import com.example.pannel2.entity.*;
import com.example.pannel2.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.pannel2.enums.UserRole;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private CustomProductPriceRepository customPriceRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private ProductPriceRepository productpriceRepo;

    /**
     * Create a new package with custom prices.
     */


    @Transactional
    public ProductPackage createPackage(Long creatorId, String packageName,
                                        List<CustomPriceInput> customPrices) {

        User creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator user not found."));

        ProductPackage productPackage = new ProductPackage(packageName, creator);

        // Save parent FIRST (make sure it has an id in DB)
        productPackage = packageRepository.saveAndFlush(productPackage);

        for (CustomPriceInput input : customPrices) {
            Product product = productRepo.findById(input.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + input.getProductId()));

            CustomProductPrice price = new CustomProductPrice(product, input.getBuyingPrice(), input.getSellingPrice());

            // link both sides correctly
            productPackage.addCustomPrice(price);
        }

        // Now save again to persist children
        return packageRepository.save(productPackage);
    }





   /* @Transactional
    public ProductPackage createPackage(Long creatorId, String packageName,
                                        List<CustomPriceInput> customPrices) {

        User creator = userRepo.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("Creator user not found."));

        if (!(creator.getRole() == UserRole.ADMIN || creator.getRole() == UserRole.SUBRESELLER)) {
            throw new RuntimeException("Only Admins and Subresellers can create packages.");
        }

        ProductPackage productPackage = new ProductPackage(packageName, creator);

        for (CustomPriceInput input : customPrices) {
            Product product = productRepo.findById(input.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + input.getProductId()));

            CustomProductPrice price = new CustomProductPrice(product, input.getBuyingPrice(), input.getSellingPrice());

            // 🔑 Attach both sides before save
            productPackage.addCustomPrice(price);
        }

        // 🔑 Save only the parent, children cascade automatically
        return packageRepository.save(productPackage);
    }*/


    @Transactional
    public ProductPackageDTO getPackageWithProducts(Long packageId) {
        ProductPackage pkg = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        ProductPackageDTO dto = new ProductPackageDTO();
        dto.setId(pkg.getId());
        dto.setName(pkg.getName());

        List<CustomProductPriceDTO> prices = pkg.getCustomPrices()
                .stream()
                .map(price -> new CustomProductPriceDTO(
                        price.getId(),
                        price.getProduct().getId(),
                        price.getCustomBuyingPrice(),
                        price.getCustomSellingPrice(),
                        price.getProduct().getName()
                ))
                .toList();

        dto.setCustomPrices(prices);
        return dto;
    }

    @Transactional
    public void increaseProductPriceForPackageAndChildren(Long parentId, Long packageId,
                                                          Long productId, BigDecimal increment) {
        // 1. Get the package
        ProductPackage productPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        // 2. Update product price inside parent package
        productPackage.getCustomPrices().stream()
                .filter(price -> price.getProduct().getId().equals(productId))
                .forEach(price -> price.setCustomSellingPrice(price.getCustomSellingPrice().add(increment)));

        packageRepository.save(productPackage);

        // 3. Propagate to children
        propagateToChildren(parentId, productId, increment);
    }

    private void propagateToChildren(Long parentId, Long productId, BigDecimal increment) {
        List<User> children = userRepo.findByParentId(parentId);

        for (User child : children) {
            ProductPackage assigned = child.getAssignedPackage();

            if (assigned != null) {
                assigned.getCustomPrices().stream()
                        .filter(price -> price.getProduct().getId().equals(productId))
                        .forEach(price -> price.setCustomSellingPrice(price.getCustomSellingPrice().add(increment)));

                packageRepository.save(assigned);
            }

            // Recursive call for grandchildren
            propagateToChildren(child.getId(), productId, increment);
        }
    }


    @Transactional
    public void dicreaseProductPriceForPackageAndChildren(Long parentId, Long packageId,
                                                          Long productId, BigDecimal diccrement) {

        // 1. Get the package
        ProductPackage productPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        // 2. Update product price inside parent package
        productPackage.getCustomPrices().stream()
                .filter(price -> price.getProduct().getId().equals(productId))
                .forEach(price -> price.setCustomSellingPrice(price.getCustomSellingPrice().subtract(diccrement)));

        packageRepository.save(productPackage);
        // 3. Propagate to children
        propagateDicToChildren(parentId, productId, diccrement);
    }

    private void propagateDicToChildren(Long parentId, Long productId, BigDecimal dicrement) {
        List<User> children = userRepo.findByParentId(parentId);

        for (User child : children) {
            ProductPackage assigned = child.getAssignedPackage();

            if (assigned != null) {
                assigned.getCustomPrices().stream()
                        .filter(price -> price.getProduct().getId().equals(productId))
                        .forEach(price -> price.setCustomSellingPrice(price.getCustomSellingPrice().subtract(dicrement)));

                packageRepository.save(assigned);
            }

            // Recursive call for grandchildren
            propagateDicToChildren(child.getId(), productId, dicrement);
        }
    }


    private void propagateProductPriceUpdateToChildren(Long parentId, Long packageId, Long productId, BigDecimal increment) {
        // Get direct children of parent
        List<User> children = userRepo.findByParentId(parentId);

        for (User child : children) {
            ProductPackage childPkg = child.getAssignedPackage();

            if (childPkg != null && childPkg.getId().equals(packageId)) {
                // Find the product price in child’s package
                childPkg.getCustomPrices().stream()
                        .filter(p -> p.getProduct().getId().equals(productId))
                        .findFirst()
                        .ifPresent(price -> price.setCustomSellingPrice(price.getCustomSellingPrice().add(increment) ));

                // Recursive call for grandchildren
                propagateProductPriceUpdateToChildren(child.getId(), packageId, productId, increment);
            }
        }
    }


    @Transactional
    public void addProductToPackage(Long packageId, Long productId, BigDecimal buyingPrice, BigDecimal sellingPrice) {
        ProductPackage productPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Prevent duplicates
        boolean exists = productPackage.getCustomPrices().stream()
                .anyMatch(price -> price.getProduct().getId().equals(productId));

        if (exists) {
            throw new RuntimeException("Product already exists in package");
        }

        CustomProductPrice newPrice = new CustomProductPrice();
        newPrice.setProduct(product);
        newPrice.setCustomBuyingPrice(buyingPrice);
        newPrice.setCustomSellingPrice(sellingPrice);
        newPrice.setProductPackage(productPackage);

        productPackage.getCustomPrices().add(newPrice);

        packageRepository.save(productPackage);
    }


    @Transactional
    public void removeProductCascade(Long userId, Long packageId, Long productId) {
        // 1. Get user
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Get product
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 3. Remove product from given package
        ProductPackage targetPackage = packageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        customPriceRepo.deleteByProductPackageAndProduct(targetPackage, product);

        // 4. Recursively handle descendants
        removeFromDescendants(user, product);
    }


    private void removeFromDescendants(User parent, Product product) {
        List<User> children = userRepo.findByParentId(parent.getId());

        for (User child : children) {
            // --- Assigned Package ---
            if (child.getAssignedPackage() != null) {
                customPriceRepo.deleteByProductPackageAndProduct(child.getAssignedPackage(), product);
            }

            // --- Created Packages by this child ---
            List<ProductPackage> childPackages = packageRepository.findByCreator(child);
            for (ProductPackage pkg : childPackages) {
                customPriceRepo.deleteByProductPackageAndProduct(pkg, product);
            }

            // --- ProductPrice entries ---
            productpriceRepo.deleteByUserAndProduct(child, product);

            // --- Recurse deeper (grandchildren, etc.) ---
            removeFromDescendants(child, product);
        }
    }








    /*public ProductPackage createPackage(Long creatorId, String packageName,
                                 List<CustomPriceInput> customPrices) {

        Optional<User> creatorOpt = userRepo.findById(creatorId);
        if (creatorOpt.isEmpty()) {
            throw new RuntimeException("Creator user not found.");
        }

        User creator = creatorOpt.get();
        if (!(creator.getRole() == UserRole.ADMIN || creator.getRole() == UserRole.SUBRESELLER)) {
            throw new RuntimeException("Only Admins and Subresellers can create packages.");
        }




        ProductPackage pkg = new ProductPackage(packageName, creator);

        for (CustomPriceInput input : customPrices) {
            Product product = productRepo.findById(input.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + input.getProductId()));

            CustomProductPrice price = new CustomProductPrice(product, input.getBuyingPrice(), input.getSellingPrice());
            pkg.addCustomPrice(price);  // 🔑 parent manages children
        }

        return packageRepository.save(pkg); // cascade saves prices too

        /*ProductPackage pkg = new ProductPackage(packageName, creator);
       // pkg = packageRepository.save(pkg); // Save first to generate ID

        // loop over products and add prices
        for (CustomPriceInput input : customPrices) {
            productRepo.findById(input.getProductId()).ifPresent(product -> {
                CustomProductPrice price = new CustomProductPrice(
                        pkg, product, input.getBuyingPrice(), input.getSellingPrice()
                );

                // save price
                customPriceRepo.save(price);

                // link it back to the package
                pkg.getCustomPrices().add(price);
            });
        }

        // save package again with linked prices
        return packageRepository.save(pkg);
    }*/

    public List<ProductPackage> getPackagesByCreator(Long creatorId) {
        return packageRepository.findByCreatorId(creatorId);
    }

    /**
     * Assign package to a child user (must be a direct child).
     */
    public void assignPackageToUser(Long parentId, Long packageId, Long childUserId) {
        Optional<ProductPackage> pkgOpt = packageRepository.findById(packageId);
        Optional<User> childOpt = userRepo.findById(childUserId);

        if (pkgOpt.isEmpty() || childOpt.isEmpty()) {
            throw new RuntimeException("Package or user not found.");
        }

        ProductPackage pkg = pkgOpt.get();
        User child = childOpt.get();

        if (!Objects.equals(child.getParentId(), parentId)) {
            throw new RuntimeException("You can only assign packages to your direct children.");
        }

        child.setAssignedPackage(pkg);
        userRepo.save(child);
    }

    //get product for user
    /*public List<ProductWithPriceDTO> getProductsForUser(Long userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Product> allProducts = productRepo.findAll();
        ProductPackage pkg = user.getAssignedPackage();
        List<CustomProductPrice> customPrices = (pkg != null)
                ? customPriceRepo.findByPkg(pkg)
                : List.of();

        Map<Long, CustomProductPrice> priceMap = customPrices.stream()
                .collect(Collectors.toMap(cpp -> cpp.getProduct().getId(), cpp -> cpp));

        List<ProductWithPriceDTO> result = new ArrayList<>();

        for (Product product : allProducts) {
            CustomProductPrice customPrice = priceMap.get(product.getId());

            BigDecimal buying = (customPrice != null) ? customPrice.getCustomBuyingPrice() : product.getBuyingPrice();
            BigDecimal selling = (customPrice != null) ? customPrice.getCustomSellingPrice() : product.getSellingPrice();

            ProductWithPriceDTO dto = new ProductWithPriceDTO();
            dto.setProductId(product.getId());
            dto.setCode(product.getCode());
            dto.setCategoryName(product.getCategory().getName());
            dto.setType(product.getCategory().getType().name());
            dto.setBuyingPrice(buying);
            dto.setSellingPrice(selling);
            dto.setStatus(product.getStatus() != null ? product.getStatus().name() : null);
            dto.setDemandState(product.getDemandState());

            result.add(dto);
        }

        return result;
    }*/


    // DTO for input
    public static class CustomPriceInput {
        private Long productId;
        private BigDecimal buyingPrice;
        private BigDecimal sellingPrice;

        // Getters and setters
        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public BigDecimal getBuyingPrice() {
            return buyingPrice;
        }

        public void setBuyingPrice(BigDecimal buyingPrice) {
            this.buyingPrice = buyingPrice;
        }

        public BigDecimal getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(BigDecimal sellingPrice) {
            this.sellingPrice = sellingPrice;
        }
    }
}
