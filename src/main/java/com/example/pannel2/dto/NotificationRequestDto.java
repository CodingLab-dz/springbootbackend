package com.example.pannel2.dto;

import com.example.pannel2.entity.Notification;

public class NotificationRequestDto {

    private String datedebut;
    private String datefin;
    private String titre;
    private String contenu;
    private String langue;

    public NotificationRequestDto(){}
    public NotificationRequestDto(Notification notification) {
        this.datedebut = notification.getDatedebut();
        this.datefin = notification.getDatefin();
        this.titre = notification.getTitre();
        this.contenu = notification.getContenu();
        this.langue= notification.getLangue();
    }

    public String getLangue() {
        return langue;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }


    public String getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(String datedebut) {
        this.datedebut = datedebut;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
}
