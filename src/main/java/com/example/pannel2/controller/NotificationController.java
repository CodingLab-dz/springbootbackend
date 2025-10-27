package com.example.pannel2.controller;


import com.example.pannel2.dto.NotificationDTO;
import com.example.pannel2.dto.NotificationRequestDto;
import com.example.pannel2.entity.Notification;
import com.example.pannel2.repository.NotificationRepository;
import com.example.pannel2.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepo;
    @Autowired
    private NotificationService notificationService;

    @PostMapping("/createnotification")
    public NotificationRequestDto createnotifiacation(@RequestBody NotificationRequestDto notificationDTO){
        Notification save= notificationService.createNotification(notificationDTO.getDatedebut(), notificationDTO.getDatefin(), notificationDTO.getTitre(), notificationDTO.getContenu(), notificationDTO.getLangue());
        return new NotificationRequestDto( save);
    }

    @GetMapping("/getallnotification")
    public List<NotificationDTO> getallnotification(){
        return notificationService.getnotifications();
    }

    @DeleteMapping("/deletenotification/{id}")
    public String deletenotification(@PathVariable Long id){
        return notificationService.deletenotification(id);


    }
}
