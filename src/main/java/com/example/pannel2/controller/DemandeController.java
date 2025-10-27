package com.example.pannel2.controller;

import com.example.pannel2.dto.DemandDTO;
import com.example.pannel2.dto.DemandeUserDTO;
import com.example.pannel2.service.DemandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")// Allow Next.js (or any frontend) to make requests
@RequestMapping("/api/demande")
public class DemandeController {
    private final DemandeService demandeService;

    public DemandeController(DemandeService demandeService) {
        this.demandeService = demandeService;
    }

    @GetMapping("/getuserdemandes/{userId}")
    public ResponseEntity<List<DemandeUserDTO>> getUserdemande(@PathVariable Long userId) {
        try {
            List<DemandeUserDTO> demande = demandeService.getDemandebyuser(userId);
            return ResponseEntity.ok(demande);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getusersonsdemande/{userId}")
    public ResponseEntity<List<DemandDTO>> getUsersonsdemand(@PathVariable Long userId){
        try {
            List<DemandDTO> demande = demandeService.getDemandeUsersons(userId);
            return ResponseEntity.ok(demande);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();        }
    }

    @GetMapping("getalldemandes")
    public ResponseEntity<List<DemandDTO>> getalldemandes(){
        try {
            List<DemandDTO> demand = demandeService.getalldamndes();
            return ResponseEntity.ok(demand);

        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}