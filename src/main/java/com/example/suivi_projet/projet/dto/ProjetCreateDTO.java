package com.example.suivi_projet.projet.dto;

import java.util.Date;

public class ProjetCreateDTO {
    private String code;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private double montant;
    private int organismeId;
    private int chefProjetId;

    public ProjetCreateDTO() {}

    // Getters & Setters
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }
    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }
    public int getOrganismeId() { return organismeId; }
    public void setOrganismeId(int organismeId) { this.organismeId = organismeId; }
    public int getChefProjetId() { return chefProjetId; }
    public void setChefProjetId(int chefProjetId) { this.chefProjetId = chefProjetId; }
}