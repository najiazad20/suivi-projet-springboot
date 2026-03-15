package com.example.suivi_projet.projet.dto;

import java.util.Date;

public class PhaseDTO {

    private int id;
    private String code;
    private String libelle;
    private String description;
    private Date dateDebut;
    private Date dateFin;
    private double montant;

    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;

    private int projetId;

    public PhaseDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public boolean isEtatRealisation() {
        return etatRealisation;
    }

    public void setEtatRealisation(boolean etatRealisation) {
        this.etatRealisation = etatRealisation;
    }

    public boolean isEtatFacturation() {
        return etatFacturation;
    }

    public void setEtatFacturation(boolean etatFacturation) {
        this.etatFacturation = etatFacturation;
    }

    public boolean isEtatPaiement() {
        return etatPaiement;
    }

    public void setEtatPaiement(boolean etatPaiement) {
        this.etatPaiement = etatPaiement;
    }

    public int getProjetId() {
        return projetId;
    }

    public void setProjetId(int projetId) {
        this.projetId = projetId;
    }
}