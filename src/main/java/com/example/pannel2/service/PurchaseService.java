package com.example.pannel2.service;


import com.example.pannel2.entity.*;
import com.example.pannel2.enums.CategoryType;
import com.example.pannel2.enums.CodeStatus;
import com.example.pannel2.enums.DemandState;
import com.example.pannel2.enums.UserRole;
import com.example.pannel2.repository.*;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PurchaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private HistoricRepository historicRepository;

    @Autowired
    private ProductCodeRepository codeRepository;
    @Autowired
    private CustomProductPriceRepository packageProductPriceRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private HistorycodeRepository historycodeRepository;
    @Autowired
    private ProductCodeRepository productCodeRepository;

    @Autowired
    private DemandeRepository demandeRepository;
    //private HistoryCodeService historyCodeService;

    //@Autowired
    //private SaleRepository saleRepository;


    public  String purchasecodeadmin(Long userId, Long productId){
        if (userId == null || productId == null) {
            throw new IllegalArgumentException("User ID and Product ID must not be null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Optional<ProductCode> optionalCode = codeRepository.findFirstByProductAndStatusOrderByAddedAtAsc (product, CodeStatus.AVAILABLE);

        if (optionalCode.isEmpty()) {
            throw new RuntimeException("No available code for this product");
        }

        ProductCode code = optionalCode.get();
        if (user.getRole() != UserRole.ADMIN){
            throw  new RuntimeException("only admin can use this function");
        }
        code.setStatus(CodeStatus.SOLD);
        if (code.getId() == null) throw new RuntimeException("CRITICAL: Code ID is null before code save!");
        try {
            codeRepository.save(code);
        } catch (Exception e) {
            System.err.println("Error saving code: " + e.getMessage());
            throw e;
        }
        HistoryCode historyCode = new HistoryCode(code, user, code.getBuyingPrice(), LocalDateTime.now(), product);
        historycodeRepository.save(historyCode);

        return "le code:" + code.getCode() +"du produit: "+ product.getName() + "est affectué au admin:"+ user.getEmail();
    }

    public  String purchasecodeadminbycode(Long userId, Long id){
        if (userId == null || id == null) {
            throw new IllegalArgumentException("User ID and Product ID must not be null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
       ProductCode code = productCodeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

       Product product = code.getProduct();

        if (user.getRole() != UserRole.ADMIN){
            throw  new RuntimeException("only admin can use this function");
        }
        code.setStatus(CodeStatus.SOLD);
        if (code.getId() == null) throw new RuntimeException("CRITICAL: Code ID is null before code save!");
        try {
            codeRepository.save(code);
        } catch (Exception e) {
            System.err.println("Error saving code: " + e.getMessage());
            throw e;
        }
        HistoryCode historyCode = new HistoryCode(code, user, code.getBuyingPrice(), LocalDateTime.now(), product);
        historycodeRepository.save(historyCode);

        return "le code:" + code.getCode() +"du produit: "+ product.getName() + "est affectué au admin:"+ user.getEmail();
    }

    @Transactional
    public String purchaseCodeProduct(Long buyerId, Long productId) {
         if (buyerId == null || productId == null) {
            throw new IllegalArgumentException("User ID and Product ID must not be null");
        }

System.out.println(buyerId);
        System.out.println(productId);
        // 1. Fetch buyer and product
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        //son for admin part
        BigDecimal sonbuyingprice = getPriceForUser(buyer, product);


        // 2. Get first available code
        Optional<ProductCode> optionalCode = codeRepository.findFirstByProductAndStatusOrderByAddedAtAsc (product, CodeStatus.AVAILABLE);

        if (optionalCode.isEmpty()) {
            throw new RuntimeException("No available code for this product");
        }

        ProductCode code = optionalCode.get();

        BigDecimal sellingPrice1 = getPriceForUser(buyer, product);
        if (buyer.getBalance() < (sellingPrice1.doubleValue())){
            throw new RuntimeException("Insufficient balance");
        }
        code.setStatus(CodeStatus.SOLD);
        if (code.getId() == null) throw new RuntimeException("CRITICAL: Code ID is null before code save!");
        try {
            codeRepository.save(code);
        } catch (Exception e) {
            System.err.println("Error saving code: " + e.getMessage());
            throw e;
        }
        HistoryCode historyCode = new HistoryCode(code, buyer, sonbuyingprice.doubleValue(), LocalDateTime.now(), product);
        historycodeRepository.save(historyCode);
        System.out.println("outside loop - buyerId: " + buyer.getId() );

        while (buyer.getParentId() != null){
            System.out.println("Inside loop - buyerId: " + buyer.getId() + ", parentId: " + buyer.getParentId());
            BigDecimal sellingPrice = getPriceForUser(buyer, product);
            BigDecimal price = getPrice(buyer, product);
            //double price = getPriceForUser(buyer, product);
            BigDecimal buyerBalance = BigDecimal.valueOf(buyer.getBalance());
            if (buyerBalance.compareTo(sellingPrice) < 0){
                throw new RuntimeException("Insufficient balance");
            }
            buyer.setBalance((buyerBalance.subtract(sellingPrice)).doubleValue());
            if (buyer.getId() == null) throw new RuntimeException("CRITICAL: Buyer ID is null before user save!");
            try {
                userRepository.save(buyer);
            } catch (Exception e) {
                System.err.println("Error saving buyer: " + e.getMessage());
                throw e;
            }
            //mark code as SOLD

            Historic historic = new Historic(buyer, product, sellingPrice, price, code.getCode(), LocalDate.now().atStartOfDay());
            if (historic.getId() != null) throw new RuntimeException("CRITICAL: Historic ID is NOT null before save (should be new)!");
            try {
                historicRepository.save(historic);
            } catch (Exception e) {
                System.err.println("Error saving historic: " + e.getMessage());
                throw e;
            }
            //get the patent info

            User parentOpt = userRepository.findById(buyer.getParentId())
                    .orElseThrow(() -> new RuntimeException("Buyer not found"));
            if (parentOpt.getParentId() == null){
                if (buyer.getAssignedPackage() == null){
                    sonbuyingprice = BigDecimal.valueOf(product.getSellingPrice());
                }else {
                    List<CustomProductPrice> packagePrices = packageProductPriceRepository.findByProductPackage(buyer.getAssignedPackage());
                    for (CustomProductPrice ppp : packagePrices) {
                        Product product1 = ppp.getProduct();
                        if (product1.getId() == product.getId()){
                            sonbuyingprice = BigDecimal.valueOf(product1.getSellingPrice());
                            break;
                        }
                        // Optional<ProductCode> optionalCode = codeRepository.findFirstByProductAndStatusOrderByAddedAtAsc (product, CodeStatus.AVAILABLE);
                    }
                }
                sonbuyingprice= getPriceForUser(buyer, product);
            }
            buyer = parentOpt;


        }
        //admin part
        /*double balace = buyer.getBalance();
        double addedbalance = (sonbuyingprice).doubleValue() - code.getBuyingPrice();
        BigDecimal codeBuyingprice= BigDecimal.valueOf(code.getBuyingPrice());
        buyer.setBalance(balace + addedbalance);
        userRepository.save(buyer);
        Historic historic = new Historic(buyer, product, sonbuyingprice, codeBuyingprice, code.getCode(), LocalDate.now().atStartOfDay());
        historicRepository.save(historic);*/
        List<User> admins = userRepository.findByRole(UserRole.ADMIN);
        for (User admin: admins){
            double balance = admin.getBalance();
            double addedbalance = (sonbuyingprice).doubleValue() - code.getBuyingPrice();
            BigDecimal codeBuyingprice= BigDecimal.valueOf(code.getBuyingPrice());
            admin.setBalance(balance + addedbalance);
            userRepository.save(admin);
            Historic historic = new Historic(admin, product, sonbuyingprice, codeBuyingprice, code.getCode(), LocalDate.now().atStartOfDay());
            historicRepository.save(historic);
        }



        return "Purchase successful. Code: " + code.getCode();
    }
    public String refundCodeProduct(Long buyerId, Long codeId){
        if (buyerId == null || codeId == null) {
            throw new IllegalArgumentException("Original Buyer ID and Code ID must not be null");
        }
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Original Buyer not found"));
        ProductCode code = codeRepository.findById(codeId)
                .orElseThrow(() -> new RuntimeException("Product code not found"));
        Product product = productRepository.findById(code.getProduct().getId())  // Assuming code has product reference
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the code is eligible for refund (must be SOLD)
        if (code.getStatus() != CodeStatus.SOLD) {
            throw new RuntimeException("Code is not in SOLD status and cannot be refunded");
        }
        BigDecimal sonbuyingprice = getPriceForUser(buyer, product);
        code.setStatus(CodeStatus.AVAILABLE);
        codeRepository.save(code);
        while (buyer.getParentId() != null) {
            System.out.println("Inside loop - buyerId: " + buyer.getId() + ", parentId: " + buyer.getParentId());

            // Calculate the selling price for the current user in the chain (same as purchase)
            BigDecimal sellingPrice = getPriceForUser(buyer, product);

            // Add back the selling price to the current user's balance (reverse of deduction in purchase)
            BigDecimal buyerBalance = BigDecimal.valueOf(buyer.getBalance());
            buyer.setBalance((buyerBalance.add(sellingPrice)).doubleValue());
            userRepository.save(buyer);

            User parentOpt = userRepository.findById(buyer.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));

            // Logic to calculate sonbuyingprice when reaching the admin level (same as purchase)
            // This is used later for the admin profit reversal.
            // Note: This logic appears buggy in original (e.g., package null handling), but mirrored exactly.
            if (parentOpt.getParentId() == null) {
                if (buyer.getAssignedPackage() != null) {
                    sonbuyingprice = BigDecimal.valueOf(product.getSellingPrice());
                } else {
                    // Note: This may throw if assignedPackage is null, mirroring original bug
                    List<CustomProductPrice> packagePrices = packageProductPriceRepository.findByProductPackage(buyer.getAssignedPackage());
                    for (CustomProductPrice ppp : packagePrices) {
                        Product product1 = ppp.getProduct();
                        if (product1.getId() == product.getId()) {
                            sonbuyingprice = BigDecimal.valueOf(product1.getSellingPrice());
                            break;
                        }
                        // Optional<ProductCode> optionalCode = ... (unused/dead code in original, ignored)
                    }
                }
                // Override with price for current buyer (mirroring original)
                sonbuyingprice = getPriceForUser(buyer, product);
            }
            List<Historic> historicList = historicRepository.findAll();
            List<Historic> toDelete = new ArrayList<>();
            for (Historic h : historicList) {
                if (h.getCode().equals(code.getCode())) {  // Use .equals() for objects
                    toDelete.add(h);
                }
            }
            // Delete outside the loop to avoid modification issues
            for (Historic h : toDelete) {
                historicRepository.delete(h);
            }



        // Move to parent for next iteration
        buyer = parentOpt;
     }

    // 3. The admin/top-level part: reverse the profit addition
    // In purchase, profit (sonbuyingprice - code buying price) is added to the top user's balance.
    // Here, we subtract it to reverse (give back the net profit to the system/chain).
    // buyer is now the top-level user (admin) after the loop.
    double balance = buyer.getBalance();
    double subtractedBalance = (sonbuyingprice).doubleValue() - code.getBuyingPrice();
    // Subtract the profit (reverse of addition in purchase)
    // Note: If subtractedBalance is negative (unlikely), this would add, but mirrors logic.
    buyer.setBalance(balance - subtractedBalance);
    userRepository.save(buyer);

    List<HistoryCode> originalBuyerHistory = historycodeRepository.findByUserId(buyerId);
        boolean deletedAny = false;
        for (HistoryCode historyCode : originalBuyerHistory) {
            // Match by code ID (adjust field name if your HistoryCode uses e.g., getCodeValue() for string matching)
            if (historyCode.getCode() != null && historyCode.getCode().getId().equals(code.getId())) {
                historycodeRepository.delete(historyCode);
                deletedAny = true;
                System.out.println("Deleted HistoryCode for original buyer: " + historyCode.getId());
            }
        }
        if (!deletedAny) {
            System.out.println("No matching HistoryCode found for original buyer and code: " + codeId);
            // Optional: throw new RuntimeException("HistoryCode not found for refund");
        }



        return "Refund successful. Code: " + code.getCode();
    }

    public String refundCodeProductadmin(Long buyerId, Long codeId){
        if (buyerId == null || codeId == null) {
            throw new IllegalArgumentException("Original Buyer ID and Code ID must not be null");
        }
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Original Buyer not found"));
        ProductCode code = codeRepository.findById(codeId)
                .orElseThrow(() -> new RuntimeException("Product code not found"));
        Product product = productRepository.findById(code.getProduct().getId())  // Assuming code has product reference
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if the code is eligible for refund (must be SOLD)
        if (code.getStatus() != CodeStatus.SOLD) {
            throw new RuntimeException("Code is not in SOLD status and cannot be refunded");
        }
        code.setStatus(CodeStatus.AVAILABLE);
        codeRepository.save(code);
        List<HistoryCode> originalBuyerHistory = historycodeRepository.findByUserId(buyerId);
        boolean deletedAny = false;
        for (HistoryCode historyCode : originalBuyerHistory) {
            // Match by code ID (adjust field name if your HistoryCode uses e.g., getCodeValue() for string matching)
            if (historyCode.getCode() != null && historyCode.getCode().getId().equals(code.getId())) {
                historycodeRepository.delete(historyCode);
                deletedAny = true;
                System.out.println("Deleted HistoryCode for original buyer: " + historyCode.getId());
            }
        }
        if (!deletedAny) {
            System.out.println("No matching HistoryCode found for original buyer and code: " + codeId);
            // Optional: throw new RuntimeException("HistoryCode not found for refund");
        }

        return "Refund successful. Code: " + code.getCode();
    }



    public String purchaseDemandProduct(Long buyerId, Long productId, String demanderequest) {
        if (buyerId == null || productId == null) {
            throw new IllegalArgumentException("User ID and Product ID must not be null");
        }

        // 1. Fetch buyer and product
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("Buyer not found"));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        BigDecimal sonbuyingprice = getPriceForUser(buyer, product);
        BigDecimal sellingPrice1 = getPriceForUser(buyer, product);
        if (buyer.getBalance()< (sellingPrice1).doubleValue()){
            throw new RuntimeException("Insufficient balance");
        }else {
            Demande demande= new Demande(buyer, product, DemandState.ON_ATTENTE, (sellingPrice1).doubleValue(), LocalDate.now().atStartOfDay(),demanderequest, "" );//(code, buyer, sellingPrice1, LocalDate.now().atStartOfDay(), product);
            demandeRepository.save(demande);
        }

        return "demande aplaied";
    }
    public String AcceptDemand(Long demandeId, String repense){
        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("demande not found"));
        User demander = userRepository.findById(demande.getUser().getId()).orElseThrow(() -> new RuntimeException("Buyer not found"));
        Product product = productRepository.findById(demande.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        BigDecimal sonbuyingprice = getPriceForUser(demander, product);

        BigDecimal sellingPrice1 = getPriceForUser(demander, product);
        if (BigDecimal.valueOf(demander.getBalance()).compareTo(sellingPrice1) < 0){
            throw new RuntimeException("Insufficient balance");
        }
        while (demander.getParentId() != null){

            BigDecimal sellingPrice = getPriceForUser(demander, product);
            BigDecimal price = getPrice(demander, product);
            //double price = getPriceForUser(buyer, product);
            BigDecimal buyerBalance = BigDecimal.valueOf(demander.getBalance());
            if (buyerBalance.compareTo(sellingPrice) < 0){
                throw new RuntimeException("Insufficient balance");
            }
            demander.setBalance(buyerBalance.subtract(sellingPrice).doubleValue());
            userRepository.save(demander);
            demande.setState(DemandState.ACCEPTE);
            demande.setRepense(repense);
            demandeRepository.save(demande);
            Historic historic = new Historic(demander, product, sellingPrice, price, "", LocalDate.now().atStartOfDay());
            historicRepository.save(historic);
            User parentOpt = userRepository.findById(demander.getParentId())
                    .orElseThrow(() -> new RuntimeException("Buyer not found"));
            if (parentOpt.getParentId() == null){
                if (demander.getAssignedPackage() != null){
                    sonbuyingprice = BigDecimal.valueOf(product.getSellingPrice());
                }else {
                    List<CustomProductPrice> productPrices = packageProductPriceRepository.findByProductPackage(demander.getAssignedPackage());
                   // List<PackageProductPrice> packagePrices = packageProductPriceRepository.findByProductPackage(demander.getAssignedPackage());

                    for (CustomProductPrice pp : productPrices){
                        Product product1 = pp.getProduct();
                        if (product1.getId() == product.getId()){
                            sonbuyingprice = BigDecimal.valueOf(product1.getSellingPrice());
                            break;
                        }
                    }
                   /* for (PackageProductPrice ppp : packagePrices) {
                        Product product1 = ppp.getProduct();
                        if (product1.getId() == product.getId()){
                            sonbuyingprice = product1.getSellingPrice();
                            break;
                        }
                        // Optional<ProductCode> optionalCode = codeRepository.findFirstByProductAndStatusOrderByAddedAtAsc (product, CodeStatus.AVAILABLE);
                    }*/
                }
                sonbuyingprice= getPriceForUser(demander, product);
            }
            demander = parentOpt;
        }

        // the admin part
        double balace = demander.getBalance();
        double addedbalance = ((sonbuyingprice).doubleValue()) - product.getBuyingPrice();
        BigDecimal productBuyingprice= BigDecimal.valueOf(product.getBuyingPrice());
        demander.setBalance(balace + addedbalance);
        userRepository.save(demander);
        Historic historic = new Historic(demander, product, sonbuyingprice,productBuyingprice , "", LocalDate.now().atStartOfDay());
        historicRepository.save(historic);




        return "demand accepted";
    }

    public String DenaiDemande(Long demandeId, String repense){

        Demande demande = demandeRepository.findById(demandeId)
                .orElseThrow(() -> new RuntimeException("demande not found"));
        demande.setState(DemandState.REFUSE);
        demande.setRepense(repense);
        demandeRepository.save(demande);

        return "demande denaied";
    }
    private BigDecimal getPriceForUser(User user, Product product) {
        // Initialize price with the product's default selling price
        BigDecimal price = BigDecimal.valueOf(product.getSellingPrice());

        // Check if user has an assigned package
        if (user.getAssignedPackage() != null) {
            List<CustomProductPrice> productPrices = packageProductPriceRepository.findByProductPackage(user.getAssignedPackage());
            //List<PackageProductPrice> packagePrices = packageProductPriceRepository.findByProductPackage(user.getAssignedPackage());
            for (CustomProductPrice ppp : productPrices) {
                // Check if this PackageProductPrice entry is for the current product
                if (ppp.getProduct().getId() == product.getId()) {
                    // If a package-specific price is found, use it and break
                    price = ppp.getCustomSellingPrice(); // Use the price from PackageProductPrice
                    break;
                }
            }
        } else { // User does not have a package, check for parent's custom prices
            Optional<User> parentOpt = userRepository.findById(user.getParentId());
            if (parentOpt.isPresent()) {
                User parent = parentOpt.get();
                List<ProductPrice> parentPrices = productPriceRepository.findByUser(parent);
                for (ProductPrice pp : parentPrices) { // Renamed price1 to pp for clarity
                    // Check if this ProductPrice entry is for the current product
                    if (pp.getProduct().getId() == product.getId()) {
                        // If a parent-specific price is found, use it and break
                        price = pp.getSellingPrice() ; // Use the price from ProductPrice
                        break;
                    }
                }
            }
        }
        return price;
    }


    private BigDecimal getPrice(User user, Product product) {
        // Initialize price with the product's default selling price
        BigDecimal price = BigDecimal.valueOf(product.getBuyingPrice());

        // Check if user has an assigned package
        if (user.getAssignedPackage() != null) {
            List<CustomProductPrice> productPrices = packageProductPriceRepository.findByProductPackage(user.getAssignedPackage());
            for (CustomProductPrice ppp : productPrices) {
                // Check if this PackageProductPrice entry is for the current product
                if (ppp.getProduct() != null && ppp.getProduct().getId() != null && ppp.getProduct().getId().equals(product.getId())) {
                    // If a package-specific price is found, use it and break
                    price = ppp.getCustomBuyingPrice(); // Use the price from PackageProductPrice
                    break;
                }
            }
        } else { // User does not have a package, check for parent's custom prices
            Optional<User> parentOpt = userRepository.findById(user.getParentId());
            if (parentOpt.isPresent()) {
                User parent = parentOpt.get();
                if (parent.getRole() == UserRole.ADMIN){
                    price= BigDecimal.valueOf(product.getBuyingPrice());
                }else{
                    if (parent.getAssignedPackage() != null) {
                        List<CustomProductPrice> parentProductPrices = packageProductPriceRepository.findByProductPackage(parent.getAssignedPackage());
                        for (CustomProductPrice pppp : parentProductPrices) {
                            if (pppp.getProduct() != null && pppp.getProduct().getId() != null && pppp.getProduct().getId().equals(product.getId())) {
                                price = pppp.getCustomSellingPrice();
                                break;
                            }
                        }
                    }
                    Optional<User> grandPOpt = userRepository.findById(parent.getParentId());
                    if (grandPOpt.isPresent()) {
                        User grandP = grandPOpt.get();
                        List<ProductPrice> parentPrices = productPriceRepository.findByUser(grandP);
                        for (ProductPrice pp : parentPrices) {
                            if (pp.getProduct() != null && pp.getProduct().getId() != null && pp.getProduct().getId().equals(product.getId())) {
                                price = pp.getSellingPrice(); // Use the price from ProductPrice
                                break;
                            }
                        }
                    }
                }
            }
        }
        return price;
    }


}
