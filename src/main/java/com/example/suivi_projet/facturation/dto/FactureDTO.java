package com.example.suivi_projet.facturation.dto;

import java.util.Date;

public class FactureDTO {

    private int id;
    private String code;
    private Date dateFacture;
    private int phaseId;
    private double montantPhase; // Montant de la phase pour la consultation/comptabilité

    public FactureDTO() {}

    public FactureDTO(int id, String code, Date dateFacture, int phaseId, double montantPhase) {
        this.id = id;
        this.code = code;
        this.dateFacture = dateFacture;
        this.phaseId = phaseId;
        this.montantPhase = montantPhase;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Date getDateFacture() { return dateFacture; }
    public void setDateFacture(Date dateFacture) { this.dateFacture = dateFacture; }
    public int getPhaseId() { return phaseId; }
    public void setPhaseId(int phaseId) { this.phaseId = phaseId; }
    public double getMontantPhase() { return montantPhase; }
    public void setMontantPhase(double montantPhase) { this.montantPhase = montantPhase; }
}