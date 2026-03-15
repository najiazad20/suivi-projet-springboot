package com.example.suivi_projet.projet.dto;

import java.util.Date;

public class ProjetResponseDTO {
    private int id;
    private String code;
    private String nom;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private double montant;
    private String organismeNom;
    private String chefProjetNom;

    public ProjetResponseDTO() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
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
    public String getOrganismeNom() { return organismeNom; }
    public void setOrganismeNom(String organismeNom) { this.organismeNom = organismeNom; }
    public String getChefProjetNom() { return chefProjetNom; }
    public void setChefProjetNom(String chefProjetNom) { this.chefProjetNom = chefProjetNom; }
}