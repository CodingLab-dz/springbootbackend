package com.example.pannel2.controller;


import com.example.pannel2.dto.HistoryCodeDTO;
import com.example.pannel2.dto.HistoryCodeResponce;
import com.example.pannel2.entity.HistoryCode;
import com.example.pannel2.repository.HistorycodeRepository;
import com.example.pannel2.service.HistoryCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/historycode")
public class HistoryCodeController {

    private HistorycodeRepository historycodeRepository;

    private final HistoryCodeService historyCodeService;

    @Autowired
    public HistoryCodeController(HistoryCodeService historyCodeService) {
        this.historyCodeService = historyCodeService;
    }
    @GetMapping("/gethistory/{userId}")
    public ResponseEntity<List<HistoryCodeResponce>> getUserHistoryCodes(@PathVariable Long userId) {
        try {
            List<HistoryCodeResponce> history = historyCodeService.getHistorycodebyuserid(userId);
            return ResponseEntity.ok(history);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/gethistorysons/{userId}")
    public ResponseEntity<List<HistoryCodeDTO>> getSonsHistoryCodes(@PathVariable Long userId){
        try {
            List<HistoryCodeDTO> history = historyCodeService.getsonshitrory(userId);
            return ResponseEntity.ok(history);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<HistoryCodeDTO>> getAllHistoryCodes(){
        try{
            List<HistoryCodeDTO> historyCodeDTOS = historyCodeService.getallHistorycode();
            return ResponseEntity.ok(historyCodeDTOS);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
