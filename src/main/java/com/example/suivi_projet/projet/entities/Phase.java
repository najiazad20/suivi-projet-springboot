package com.example.suivi_projet.projet.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Phase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private String libelle;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private double montant;

    private boolean etatRealisation;
    private boolean etatFacturation;
    private boolean etatPaiement;

    @ManyToOne
    private Projet projet;

    public Phase() {
    }

    public Phase(String code, String libelle, String description, Date dateDebut, Date dateFin, double montant, boolean etatRealisation, boolean etatFacturation, boolean etatPaiement, Projet projet) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
        this.etatRealisation = etatRealisation;
        this.etatFacturation = etatFacturation;
        this.etatPaiement = etatPaiement;
        this.projet = projet;
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

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
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

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }
}