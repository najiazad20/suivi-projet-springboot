package com.example.suivi_projet.projet.dto;

import java.util.Date;

public class LigneEmployePhaseResponseDTO {
    private int employeId;
    private int phaseId;
    private Date dateDebut;
    private Date dateFin;

    public LigneEmployePhaseResponseDTO() {}

    public LigneEmployePhaseResponseDTO(int employeId, int phaseId, Date dateDebut, Date dateFin) {
        this.employeId = employeId;
        this.phaseId = phaseId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
    }

    public int getEmployeId() { return employeId; }
    public void setEmployeId(int employeId) { this.employeId = employeId; }

    public int getPhaseId() { return phaseId; }
    public void setPhaseId(int phaseId) { this.phaseId = phaseId; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
}