package com.example.pannel2.service;

import com.example.pannel2.dto.NotificationDTO;
import com.example.pannel2.entity.Notification;
import com.example.pannel2.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;




    public Notification createNotification(String datedebut, String datefin, String title, String content, String langue){

        Notification not = new Notification(datedebut, datefin, title, content, langue);
        return  notificationRepo.save(not);
    }

    public List<NotificationDTO> getnotifications(){
        List<Notification> listnot = notificationRepo.findAll();
        List<NotificationDTO> notDto = new ArrayList<>();

        for (Notification not : listnot){
            notDto.add(new NotificationDTO(
                    not.getId(),
                    not.getDatedebut(),
                    not.getDatefin(),
                    not.getTitre(),
                    not.getContenu(),
                    not.getLangue()
            ));
        }
        return  notDto;
    }
    public String deletenotification(Long id){
        Notification not = notificationRepo.findById(id).orElseThrow(() -> new RuntimeException("notification not found"));
        notificationRepo.delete(not);
        return "notification supprime";
    }
}
