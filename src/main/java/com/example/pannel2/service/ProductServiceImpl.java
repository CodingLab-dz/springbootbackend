package com.example.pannel2.service;

import com.example.pannel2.dto.*;
import com.example.pannel2.entity.*;
import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.enums.DemandState;
import com.example.pannel2.enums.CodeStatus;
import com.example.pannel2.enums.UserRole;
import com.example.pannel2.repository.*;
import com.example.pannel2.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomProductPriceRepository customPriceRepository;

    private ProductWithPriceDTO productWithPriceDTO;

    private CategoryRepository categoryRepository;


    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private PackageService packageService;
    @Autowired
    private UserRepository userRepo;

    //@Override


    /*public List<ProductWithPriceDTO> getProductsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        ProductPackage assignedPackage = user.getAssignedPackage();
        List<Product> allProducts = productRepository.findAll();

        List<ProductWithPriceDTO> result = new ArrayList<>();

        for (Product product : allProducts) {
            ProductWithPriceDTO dto = new ProductWithPriceDTO();

            dto.setProductId(product.getId());  // use product's ID directly
            dto.setName(product.getCode());
            dto.setCategoryName(product.getCategory().getName());
            dto.setCategoryType(product.getCategory().getType().name());

            // CODE type logic
            if (product.getCategory().getType() == CategoryType.CODE) {
                dto.setCode(product.getCode());
                dto.setStatus(product.getStatus().name());
            }

            // DEMAND type logic
            if (product.getCategory().getType() == CategoryType.DEMAND) {
                dto.setDemandState(DemandState.ON_ATTENTE);  // use enum directly
            }

            // Pricing
            if (assignedPackage != null) {
                CustomProductPrice price = customPriceRepository.findByProductAndPkg(product, assignedPackage).orElse(null);
                if (price != null) {
                    dto.setSellingPrice(price.getCustomSellingPrice());
                    dto.setBuyingPrice(price.getCustomBuyingPrice());
                } else {
                    dto.setSellingPrice(product.getSellingPrice());
                    dto.setBuyingPrice(product.getBuyingPrice());
                }
            } else {
                dto.setSellingPrice(product.getSellingPrice());
                dto.setBuyingPrice(product.getBuyingPrice());
            }

            result.add(dto);
        }

        return result;
    }*/

    public Product createProduct(String name, Long categoryId, Long adminId, Double buyingPrice, Double sellingPrice) {
        User admin = userRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
        if (!admin.getRole().equals(UserRole.ADMIN)) {
            throw new RuntimeException("Only admins can create products");
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(name);
        product.setCategory(category);
        product.setCreatedBy(admin);
        product.setBuyingPrice(buyingPrice);
        product.setSellingPrice(sellingPrice);


        return productRepository.save(product);
    }


    public List<ProductDTO> getProductOnlysForUser(User user){
        List<ProductDTO> result = new ArrayList<>();
        if (user.getAssignedPackage() != null){
            List<CustomProductPrice> packagePrices = customPriceRepository.findByProductPackage(user.getAssignedPackage()) ;
            for (CustomProductPrice  ppp : packagePrices){
                Product product = ppp.getProduct();
                result.add(new ProductDTO(
                   product.getId(),
                   product.getName(),
                   product.getCategory().getId(),
                   product.getBuyingPrice(),
                   ppp.getCustomSellingPrice().doubleValue(),
                   product.getCategory().getName(),
                   product.getCategory().getType()
                ));
            }
        } else if (user.getParentId() != null) {
            Optional<User> parentOpt = userRepository.findById(user.getParentId());
            if (parentOpt.isPresent()) {
                User parent = parentOpt.get();
                List<ProductPrice> parentPrices = productPriceRepository.findByUser(parent);
                if (!parentPrices.isEmpty()) {
                    for (ProductPrice price : parentPrices) {
                        Product product = price.getProduct();
                        result.add(new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getCategory().getId(),
                                product.getBuyingPrice(),
                                price.getSellingPrice().doubleValue(),
                                product.getCategory().getName(),
                                product.getCategory().getType()
                        ));
                    }
                } else if (parent.getRole() == UserRole.ADMIN) {
                    List<Product> products = productRepository.findAll();
                    for (Product product : products) {
                        result.add(new ProductDTO(
                                product.getId(),
                                product.getName(),
                                product.getCategory().getId(),
                                product.getBuyingPrice(),
                                product.getSellingPrice().doubleValue(),
                                product.getCategory().getName(),
                                product.getCategory().getType()
                        ));
                    }
                }
            }
        } else if (user.getRole() == UserRole.ADMIN) {
            List<Product> products = productRepository.findAll();

            for (Product product : products){
                BigDecimal sellingPrice = product.getSellingPrice() != null
                        ? BigDecimal.valueOf(product.getSellingPrice())
                        : BigDecimal.ZERO;
                result.add(new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getId(),
                        product.getBuyingPrice(),
                        sellingPrice.doubleValue(),
                        product.getCategory().getName(),
                        product.getCategory().getType()
                ));
            }
        }
        return result;
    }


    public List<ProductDTO> getProductcreatedbyuser(User user) {
        List<ProductDTO> result = new ArrayList<>();

        // 1️⃣ If the user has custom prices (direct ProductPrice entries)
        List<ProductPrice> productPrices = productPriceRepository.findByUser(user);

        if (!productPrices.isEmpty()) {
            for (ProductPrice price : productPrices) {
                Product product = price.getProduct();
                if (product != null) {
                    // 🔑 Determine buying price from parent / package instead of admin
                    double buyingPrice = resolveBuyingPrice(user, product);

                    result.add(new ProductDTO(
                            product.getId(),
                            product.getName(),
                            product.getCategory() != null ? product.getCategory().getId() : null,
                            buyingPrice,
                            price.getSellingPrice() != null ? price.getSellingPrice().doubleValue() : 0.0,
                            product.getCategory() != null ? product.getCategory().getName() : "No Category",
                            product.getCategory().getType()


                    ));
                }
            }
        }

        // 2️⃣ If user is an admin → return default products
        else if (user.getRole() == UserRole.ADMIN) {
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                result.add(new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getCategory() != null ? product.getCategory().getId() : null,
                        product.getBuyingPrice() != null ? product.getBuyingPrice().doubleValue() : 0.0,
                        product.getSellingPrice() != null ? product.getSellingPrice().doubleValue() : 0.0,
                        product.getCategory() != null ? product.getCategory().getName() : "No Category",
                        product.getCategory().getType()

                ));
            }
        }

        return result;
    }

    private double resolveBuyingPrice(User user, Product product) {
        // 1) If user has an assigned package -> try to get package custom price for this product
        if (user.getAssignedPackage() != null) {
            Optional<CustomProductPrice> opt = customPriceRepository
                    .findByProductAndProductPackage(product, user.getAssignedPackage());
            if (opt.isPresent()) {
                BigDecimal customSelling = opt.get().getCustomSellingPrice();
                if (customSelling != null) {
                    return customSelling.doubleValue();
                }
            }
        }

        // 2) If user has a parent -> try parent's product price first, then parent's package
        if (user.getParentId() != null) {
            Optional<User> parentOpt = userRepository.findById(user.getParentId());
            if (parentOpt.isPresent()) {
                User parent = parentOpt.get();

                // 2.a) parent's explicit ProductPrice (if any)
                Optional<ProductPrice> parentPriceOpt = productPriceRepository.findByUserAndProduct(parent, product);
                if (parentPriceOpt.isPresent()) {
                    BigDecimal parentSelling = parentPriceOpt.get().getSellingPrice();
                    if (parentSelling != null) {
                        return parentSelling.doubleValue();
                    }
                }

                // 2.b) parent's assigned package (if any)
                if (parent.getAssignedPackage() != null) {
                    Optional<CustomProductPrice> parentPkgOpt =
                            customPriceRepository.findByProductAndProductPackage(product, parent.getAssignedPackage());
                    if (parentPkgOpt.isPresent()) {
                        BigDecimal parentPkgSelling = parentPkgOpt.get().getCustomSellingPrice();
                        if (parentPkgSelling != null) {
                            return parentPkgSelling.doubleValue();
                        }
                    }
                }
            }
        }

        // 3) Fallback: product's buying price (admin/base)
        return product.getBuyingPrice() != null ? product.getSellingPrice().doubleValue() : 0.0;
    }






    /*public List<ProductDTO> getProductcreatedbyuser(User user) {
        List<ProductDTO> result = new ArrayList<>();
        List<ProductPrice> productPrices = productPriceRepository.findByUser(user);

        if (!productPrices.isEmpty()) {
            for (ProductPrice price : productPrices) {
                Product product = price.getProduct();
                if (product != null) {
                    result.add(new ProductDTO(
                            product.getId(),
                            product.getName(),
                            product.getCategory() != null ? product.getCategory().getId() : null,
                            product.getBuyingPrice() != null ? product.getBuyingPrice().doubleValue() : 0.0,
                            price.getSellingPrice() != null ? price.getSellingPrice().doubleValue() : 0.0,
                            product.getCategory() != null ? product.getCategory().getName() : "No Category"
                    ));
                }
            }
        } else if (user.getRole() == UserRole.ADMIN) {
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                result.add(new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getCategory() != null ? product.getCategory().getId() : null,
                        product.getBuyingPrice() != null ? product.getBuyingPrice().doubleValue() : 0.0,
                        product.getSellingPrice() != null ? product.getSellingPrice().doubleValue() : 0.0,
                        product.getCategory() != null ? product.getCategory().getName() : "No Category"
                ));
            }
        }
        return result;
    }*/

    /*public List<ProductDTO> getProductcreatedbyuser(User user){
        List<ProductDTO> result = new ArrayList<>();
        List<ProductPrice> productPrices = productPriceRepository.findByUser(user);
        if (!productPrices.isEmpty()){
            for (ProductPrice price : productPrices){
                Product product = price.getProduct();
                result.add(new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getId(),
                        product.getBuyingPrice(),
                        price.getSellingPrice().doubleValue(),
                        product.getCategory().getName()
                ));
            }
        } else if (user.getRole() == UserRole.ADMIN) {
            List<Product> products = productRepository.findAll();
            for (Product product : products){
                result.add(new ProductDTO(
                        product.getId(),
                        product.getName(),
                        product.getCategory().getId(),
                        product.getBuyingPrice(),
                        product.getSellingPrice().doubleValue(),
                        product.getCategory().getName()
                ));
            }
        }
        return  result;
    }*/

    public void increaseProductPrice(User user, Product product, Double increment){
        if (user.getRole() == UserRole.ADMIN){
            Optional<Product> p = productRepository.findById(product.getId());
            List<ProductPrice> productPrices = productPriceRepository.findByProduct(product);
            List<CustomProductPrice> customProductPrices = customPriceRepository.findByProduct(product);
            p.stream().filter(pro -> pro.getId().equals(product.getId())).forEach(pro -> {
                pro.setSellingPrice(product.getSellingPrice() + increment);
                productRepository.save(pro);
            });
            productPrices.forEach(pro -> {
                pro.setSellingPrice(BigDecimal.valueOf(product.getSellingPrice()).add(BigDecimal.valueOf(increment)));
                productPriceRepository.save(pro);
            });
            customProductPrices.forEach(cp ->{
                cp.setCustomSellingPrice(cp.getCustomSellingPrice().add(BigDecimal.valueOf(increment)));
                customPriceRepository.save(cp);
            });

        }else {
            List<ProductPrice> productPrices = productPriceRepository.findByUser(user);
            List<ProductPackage> productPackages = packageRepository.findByCreator(user) ;
            productPrices.stream().filter(pro -> pro.getProduct().getId().equals(product.getId())).forEach(pro-> {
                pro.setSellingPrice(BigDecimal.valueOf(product.getSellingPrice()).add(BigDecimal.valueOf(increment)));
                productPriceRepository.save(pro);
            });
            for (ProductPackage pro : productPackages){
                pro.getCustomPrices().stream().filter(cp ->cp.getProduct().getId().equals(product.getId())).forEach(cp->{
                    cp.setCustomSellingPrice(cp.getCustomSellingPrice().add(BigDecimal.valueOf(increment)));
                    customPriceRepository.save(cp);
                });
            }
            propagateToChildren(user.getId(), product.getId(), BigDecimal.valueOf(increment));
        }
    }

    private void propagateToChildren(Long parentId, Long productId, BigDecimal increment){
        List<User> children = userRepo.findByParentId(parentId);
        for (User child : children){
            List<ProductPrice> productPrices = productPriceRepository.findByUser(child);

            List<ProductPackage> productPackages = packageRepository.findByCreator(child) ;
            productPrices.stream().filter(pro -> pro.getProduct().getId().equals(productId)).forEach(pro-> {
                pro.setSellingPrice(pro.getSellingPrice().add(increment));
                productPriceRepository.save(pro);
            });
            for (ProductPackage pro : productPackages){
                pro.getCustomPrices().stream().filter(cp ->cp.getProduct().getId().equals(productId)).forEach(cp->{
                    cp.setCustomSellingPrice(cp.getCustomSellingPrice().add(increment));
                    customPriceRepository.save(cp);
                });
            }
            propagateToChildren(child.getId(), productId, increment);
        }
    }


    public void decreaseProductPrice(User user, Product product, Double decrement){
        if (user.getRole() == UserRole.ADMIN){
            Optional<Product> p = productRepository.findById(product.getId());
            List<ProductPrice> productPrices = productPriceRepository.findByProduct(product);
            List<CustomProductPrice> customProductPrices = customPriceRepository.findByProduct(product);
            p.stream().filter(pro -> pro.getId().equals(product.getId())).forEach(pro -> {
                pro.setSellingPrice(product.getSellingPrice() - decrement);
                productRepository.save(pro);
            });
            productPrices.forEach(pro -> {
                pro.setSellingPrice(BigDecimal.valueOf(product.getSellingPrice()).subtract(BigDecimal.valueOf(decrement)));
                productPriceRepository.save(pro);
            });
            customProductPrices.forEach(cp ->{
                cp.setCustomSellingPrice(cp.getCustomSellingPrice().subtract(BigDecimal.valueOf(decrement)));
                customPriceRepository.save(cp);
            });

        }else {
            List<ProductPrice> productPrices = productPriceRepository.findByUser(user);
            List<ProductPackage> productPackages = packageRepository.findByCreator(user) ;
            productPrices.stream().filter(pro -> pro.getProduct().getId().equals(product.getId())).forEach(pro-> {
                pro.setSellingPrice(BigDecimal.valueOf(product.getSellingPrice()).subtract(BigDecimal.valueOf(decrement)));
                productPriceRepository.save(pro);
            });
            for (ProductPackage pro : productPackages){
                pro.getCustomPrices().stream().filter(cp ->cp.getProduct().getId().equals(product.getId())).forEach(cp->{
                    cp.setCustomSellingPrice(cp.getCustomSellingPrice().subtract(BigDecimal.valueOf(decrement)));
                    customPriceRepository.save(cp);
                });
            }
            propagateToChildren2(user.getId(), product.getId(), BigDecimal.valueOf(decrement));
        }
    }

    private void propagateToChildren2(Long parentId, Long productId, BigDecimal decrement){
        List<User> children = userRepo.findByParentId(parentId);
        for (User child : children){
            List<ProductPrice> productPrices = productPriceRepository.findByUser(child);

            List<ProductPackage> productPackages = packageRepository.findByCreator(child) ;
            productPrices.stream().filter(pro -> pro.getProduct().getId().equals(productId)).forEach(pro-> {
                pro.setSellingPrice(pro.getSellingPrice().subtract(decrement));
                productPriceRepository.save(pro);
            });
            for (ProductPackage pro : productPackages){
                pro.getCustomPrices().stream().filter(cp ->cp.getProduct().getId().equals(productId)).forEach(cp->{
                    cp.setCustomSellingPrice(cp.getCustomSellingPrice().subtract(decrement));
                    customPriceRepository.save(cp);
                });
            }
            propagateToChildren2(child.getId(), productId, decrement);
        }
    }




    public List<ProductWithPriceDTO> getProductsForUser(User user, Long categoryId) {
        List<ProductWithPriceDTO> result = new ArrayList<>();

        // 1. Use package prices if user has assigned package
        if (user.getAssignedPackage() != null) {
            List<CustomProductPrice> packagePrices = customPriceRepository.findByProductPackage(user.getAssignedPackage());
            for (CustomProductPrice ppp : packagePrices) {
                Product product = ppp.getProduct();
                if (product.getCategory().getId() != null && product.getCategory().getId().equals(categoryId)) {
                    BigDecimal buyingPrice = product.getBuyingPrice() != null
                            ? BigDecimal.valueOf(product.getBuyingPrice())
                            : BigDecimal.ZERO;

                    result.add(new ProductWithPriceDTO(
                            product.getId(),
                            product.getName(),
                            buyingPrice,
                            ppp.getCustomSellingPrice(),
                            product.getCategory().getType(),
                            user.getParentId()
                    ));
                }
            }

            // 2. If user has a parent → check parent prices first
        } else if (user.getParentId() != null) {
            Optional<User> parentOpt = userRepository.findById(user.getParentId());

            if (parentOpt.isPresent()) {
                User parent = parentOpt.get();
                List<ProductPrice> parentPrices = productPriceRepository.findByUser(parent);

                if (!parentPrices.isEmpty()) {
                    // ✅ Use parent’s custom prices
                    for (ProductPrice price : parentPrices) {
                        Product product = price.getProduct();
                        if (product.getCategory().getId() != null && product.getCategory().getId().equals(categoryId)) {
                            BigDecimal buyingPrice = product.getBuyingPrice() != null
                                    ? BigDecimal.valueOf(product.getBuyingPrice())
                                    : BigDecimal.ZERO;
                            BigDecimal sellingPrice = price.getSellingPrice() != null
                                    ? price.getSellingPrice()
                                    : BigDecimal.ZERO;

                            result.add(new ProductWithPriceDTO(
                                    product.getId(),
                                    product.getName(),
                                    buyingPrice,
                                    sellingPrice, // ✅ Now using parent's price
                                    product.getCategory().getType(),
                                    user.getParentId()
                            ));
                        }
                    }

                } else if (parent.getRole() == UserRole.ADMIN) {
                    // ✅ Fallback only if parent is actually an admin
                    List<Product> products = productRepository.findAll();
                    for (Product product : products) {
                        if (product.getCategory().getId() != null && product.getCategory().getId().equals(categoryId)) {
                            BigDecimal buyingPrice = product.getBuyingPrice() != null
                                    ? BigDecimal.valueOf(product.getBuyingPrice())
                                    : BigDecimal.ZERO;
                            BigDecimal sellingPrice = product.getSellingPrice() != null
                                    ? BigDecimal.valueOf(product.getSellingPrice())
                                    : BigDecimal.ZERO;

                            result.add(new ProductWithPriceDTO(
                                    product.getId(),
                                    product.getName(),
                                    buyingPrice,
                                    sellingPrice,
                                    product.getCategory().getType(),
                                    user.getParentId()
                            ));
                        }
                    }
                }
            }

            // 3. If user is admin → return default product prices
        } else if (user.getRole() == UserRole.ADMIN) {
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                if (product.getCategory().getId() != null && product.getCategory().getId().equals(categoryId)) {
                    BigDecimal buyingPrice = product.getBuyingPrice() != null
                            ? BigDecimal.valueOf(product.getBuyingPrice())
                            : BigDecimal.ZERO;
                    BigDecimal sellingPrice = product.getSellingPrice() != null
                            ? BigDecimal.valueOf(product.getSellingPrice())
                            : BigDecimal.ZERO;

                    result.add(new ProductWithPriceDTO(
                            product.getId(),
                            product.getName(),
                            buyingPrice,
                            sellingPrice,
                            product.getCategory().getType(),
                            user.getParentId()
                    ));
                }
            }
        }

        return result;
    }



    /*public List<ProductWithPriceDTO> getProductsForUser(User user, Long categoryId) {
        List<ProductWithPriceDTO> result = new ArrayList<>();

        // 1. Use package prices if user has assigned package
        if (user.getAssignedPackage() != null) {
            List<CustomProductPrice> packagePrices = customPriceRepository.findByProductPackage(user.getAssignedPackage()) ;
            for (CustomProductPrice ppp : packagePrices) {
                Product product = ppp.getProduct();
                if (product.getCategory().getId() !=null && product.getCategory().getId().equals(categoryId)){
                    result.add(new ProductWithPriceDTO(

                            product.getId(),
                            product.getName(),
                            BigDecimal.valueOf(product.getBuyingPrice()),
                            ppp.getCustomSellingPrice(),
                            product.getCategory().getType(),
                            user.getParentId()
                    ));
                }
            }

            // 2. Check parent prices
        } else if (user.getParentId() != null) {
            Optional<User> parentOpt = userRepository.findById(user.getParentId());

            if (parentOpt.isPresent()) {
                User parent = parentOpt.get();
                List<ProductPrice> parentPrices = productPriceRepository.findByUser(parent);

                if (!parentPrices.isEmpty()) {
                    for (ProductPrice price : parentPrices) {
                        Product product = price.getProduct();
                        if (product.getCategory().getId() !=null && product.getCategory().getId().equals(categoryId)){
                            result.add(new ProductWithPriceDTO(
                                    product.getId(),
                                    product.getName(),
                                    BigDecimal.valueOf(product.getBuyingPrice()),
                                    price.getSellingPrice(),
                                    product.getCategory().getType(),
                                    user.getParentId()
                            ));
                        }
                    }

                    // ✅ Fallback to default prices if parent is admin with no custom prices
                }else if (parent.getRole() == UserRole.ADMIN) {
                    List<Product> products = productRepository.findAll();
                    for (Product product : products) {
                        if (product.getCategory().getId() !=null && product.getCategory().getId().equals(categoryId)){
                            result.add(new ProductWithPriceDTO(
                                    product.getId(),
                                    product.getName(),
                                    BigDecimal.valueOf(product.getBuyingPrice()),
                                    BigDecimal.valueOf(product.getSellingPrice()),
                                    product.getCategory().getType(),
                                    user.getParentId()
                            ));
                        }
                    }
                }
            }
            // 3. If user is admin, return default prices
        } else if (user.getRole() == UserRole.ADMIN) {
            List<Product> products = productRepository.findAll();
            for (Product product : products) {
                if (product.getCategory().getId() !=null && product.getCategory().getId().equals(categoryId)){
                    result.add(new ProductWithPriceDTO(
                            product.getId(),
                            product.getName(),
                            BigDecimal.valueOf(product.getBuyingPrice()),
                            BigDecimal.valueOf(product.getSellingPrice()),
                            product.getCategory().getType(),
                            user.getParentId()
                    ));
                }
            }
        }

        // 3. If no package and no parent pricing, return empty list
        return result;
    }*/

   /* @Override
    public List<ProductWithPriceDTO> getProductsForUser(User userId) {
        return null;
    }*/






    /*public List<ProductWithPriceDTO> getProductsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        ProductPackage assignedPackage = user.getAssignedPackage();
        List<Product> allProducts = productRepository.findAll();

        List<ProductWithPriceDTO> result = new ArrayList<>();

        for (Product product : allProducts) {
            //Long someValue = null;

            if (productWithPriceDTO != null) {
                Long productId = productWithPriceDTO.getProductId();
                // continue processing

                ProductWithPriceDTO dto = new ProductWithPriceDTO();
                dto.setProductId(productId);
                dto.setName(product.getCode());
                dto.setCategoryName(product.getCategory().getName());
                dto.setCategoryType(product.getCategory().getType().name());
                //dto.setCreatedBy(product.get getCreatedBy().getId());
            } else {
                // handle the null case (e.g., log it or skip)
                System.out.println("productWithPriceDTO is null");
                continue; // or return an error, or skip this record
            }
            ProductWithPriceDTO dto = new ProductWithPriceDTO();
            //dto.setProductId(productId);
            //dto.setName(product.getCode());
            //dto.setCategoryName(product.getCategory().getName());
            //dto.setCategoryType(product.getCategory().getType().name());
            //dto.setCreatedBy(product.get getCreatedBy().getId());

            // CODE type logic
            if (product.getCategory().getType() == CategoryType.CODE) {
                dto.setCode(product.getCode());
                dto.setStatus(product.getStatus().name());
            }

            // DEMAND type logic
            if (product.getCategory().getType()== CategoryType.DEMAND) {
                dto.setDemandState(DemandState.valueOf("ON_ATTENTE"));
            }

            // Pricing
            if (assignedPackage != null) {
                //CustomProductPrice price = customPriceRepository.findByProductAndPkg(product, assignedPackage);
                CustomProductPrice price = customPriceRepository.findByProductAndPkg(product, assignedPackage).orElse(null);
                if (price != null) {
                    dto.setSellingPrice(price.getCustomSellingPrice());
                    dto.setBuyingPrice(price.getCustomBuyingPrice());
                } else {
                    dto.setSellingPrice(product.getSellingPrice());
                    dto.setBuyingPrice(product.getBuyingPrice());
                }
            } else {
                dto.setSellingPrice(product.getSellingPrice());
                dto.setBuyingPrice(product.getBuyingPrice());
            }

            result.add(dto);
        }

        return result;
    }*/
}
