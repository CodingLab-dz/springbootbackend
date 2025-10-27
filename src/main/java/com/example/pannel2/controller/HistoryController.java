package com.example.pannel2.controller;


import com.example.pannel2.dto.HistoricDTO;
import com.example.pannel2.dto.HistoryCodeResponce;
import com.example.pannel2.service.HistoricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private HistoricService historicService;




    @GetMapping("/gethistory/{userId}")
    public ResponseEntity<List<HistoricDTO>> getUserHistoryUser(@PathVariable Long userId) {
        try {
            List<HistoricDTO> history = historicService.getHistoribyuser(userId);
            return ResponseEntity.ok(history);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/gethistorysons/{userId}")
    public ResponseEntity<List<HistoricDTO>> getUserHistoryUsersons(@PathVariable Long userId) {
        try {
            List<HistoricDTO> history = historicService.getHistoricuserandsons(userId);
            return ResponseEntity.ok(history);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/gethistorydirectsons/{userId}")
    public ResponseEntity<List<HistoricDTO>> getUserHistoryUserDurectsons(@PathVariable Long userId) {
        try {
            List<HistoricDTO> history = historicService.getHistoricDirectSons(userId);
            return ResponseEntity.ok(history);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/getall")
    public  ResponseEntity<List<HistoricDTO>> getallhistoric(){
        try{
            List<HistoricDTO> historicDTOS = historicService.getallhistori();
            return ResponseEntity.ok(historicDTOS);
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }
}
