package com.example.suivi_projet.projet.dto;

import java.util.Date;

public class LigneEmployePhaseCreateDTO {
    private Date dateDebut;
    private Date dateFin;

    public LigneEmployePhaseCreateDTO() {}

    public LigneEmployePhaseCreateDTO(Date dateDebut, Date dateFin) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
}