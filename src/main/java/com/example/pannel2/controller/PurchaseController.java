package com.example.pannel2.controller;


import com.example.pannel2.repository.ProductRepository;
import com.example.pannel2.repository.UserRepository;
import com.example.pannel2.service.PurchaseService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.pannel2.entity.*;

import java.util.Optional;

@RestController

@CrossOrigin(origins = "http://localhost:3000")// Allow Next.js (or any frontend) to make requests
@RequestMapping("/api/purchase")
public class PurchaseController {


    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserRepository userRepository;

    /*@Autowired
    private ProductRepository productRepository;*/


   /* @PostMapping("/buy-code/user/{buyerId}/product/{productId}")
    public ResponseEntity<?> buyCode(@PathVariable Long buyerId, @PathVariable Long productId) {
        try {
            String result = purchaseService.purchaseCodeProduct(buyerId, productId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }*/

    @PostMapping("/buy-code/user/{buyerId}/product/{productId}")
    public ResponseEntity<?> buyCode(@PathVariable Long buyerId, @PathVariable Long productId) {
        try {
            String result = purchaseService.purchaseCodeProduct(buyerId, productId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // Or 400
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @PostMapping("/refund-code/user/{buyerId}/code/{codeId}")
    public ResponseEntity<?> refundcode (@PathVariable Long buyerId, @PathVariable Long codeId){
        try {
            String result = purchaseService.refundCodeProduct(buyerId, codeId);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/refund-code-admin/user/{buyerId}/code/{codeId}")
    public ResponseEntity<?> refundcodeadmin (@PathVariable Long buyerId, @PathVariable Long codeId){
        try {
            String result = purchaseService.refundCodeProductadmin(buyerId, codeId);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/demande")
    public ResponseEntity<?> demandeproduct(@RequestParam Long buyerId, @RequestParam Long productId, @RequestBody String demande){
        try {
            String result = purchaseService.purchaseDemandProduct(buyerId, productId, demande);
            return ResponseEntity.ok(result);
        }catch (Exception err){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err.getMessage());
        }
    }
    @PostMapping("/demande/accepte/{demandeId}")
    public ResponseEntity<?> acceptedemande (@PathVariable Long demandeId, @RequestBody String repense){
        try{
            String result = purchaseService.AcceptDemand(demandeId, repense);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/demande/denaied/{demandeId}")
    public ResponseEntity<?> denaidemande (@PathVariable Long demandeId, @RequestBody String repense){
        try{
            String result = purchaseService.DenaiDemande(demandeId, repense);
            return ResponseEntity.ok(result);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/buy-code/admin/{userId}/product/{productId}")
    public ResponseEntity<?> admingetcode(@PathVariable Long userId, @PathVariable Long productId){
        try {
            String result = purchaseService.purchasecodeadmin(userId, productId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // Or 400
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    @PostMapping("/get-code/admin/{userId}/code/{codeId}")
    public ResponseEntity<?> admingetcodebycode(@PathVariable Long userId, @PathVariable Long codeId){
        try {
            String result = purchaseService.purchasecodeadminbycode(userId, codeId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());  // Or 400
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }


}
