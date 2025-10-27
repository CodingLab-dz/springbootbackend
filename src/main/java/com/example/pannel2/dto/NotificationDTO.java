package com.example.pannel2.dto;

public class NotificationDTO {

    private Long id;
    private String datedebut;
    private String datefin;
    private String titre;
    private String contenu;
    private  String langue;

    public NotificationDTO(){}
    public NotificationDTO(Long id, String datedebut, String datefin,String titre , String contenu, String langue) {
        this.id = id;
        this.datedebut = datedebut;
        this.datefin = datefin;
        this.titre = titre;
        this.contenu = contenu;
        this.langue = langue;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
