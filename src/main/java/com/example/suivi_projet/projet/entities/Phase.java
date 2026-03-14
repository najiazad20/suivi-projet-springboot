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

    private double pourcentage;

    private boolean terminee;
    private boolean facturee;
    private boolean payee;

    @ManyToOne
    private Projet projet;

    public Phase() {
    }

    public Phase(int id, String code, String libelle, String description,
                 Date dateDebut, Date dateFin, double pourcentage,
                 boolean terminee, boolean facturee, boolean payee, Projet projet) {
        this.id = id;
        this.code = code;
        this.libelle = libelle;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.pourcentage = pourcentage;
        this.terminee = terminee;
        this.facturee = facturee;
        this.payee = payee;
        this.projet = projet;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public String getLibelle() { return libelle; }

    public void setLibelle(String libelle) { this.libelle = libelle; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Date getDateDebut() { return dateDebut; }

    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }

    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public double getPourcentage() { return pourcentage; }

    public void setPourcentage(double pourcentage) { this.pourcentage = pourcentage; }

    public boolean isTerminee() { return terminee; }

    public void setTerminee(boolean terminee) { this.terminee = terminee; }

    public boolean isFacturee() { return facturee; }

    public void setFacturee(boolean facturee) { this.facturee = facturee; }

    public boolean isPayee() { return payee; }

    public void setPayee(boolean payee) { this.payee = payee; }

    public Projet getProjet() { return projet; }

    public void setProjet(Projet projet) { this.projet = projet; }
}