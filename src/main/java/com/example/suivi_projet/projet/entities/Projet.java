package com.example.suivi_projet.projet.entities;

import jakarta.persistence.*;
import java.util.Date;
import com.example.suivi_projet.organisation.entities.*;

@Entity
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private String nom;
    private String description;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    private double montant;

    @ManyToOne
    private Organisme organisme;

    @ManyToOne
    private Employe chefProjet;

    public Projet() {
    }

    public Projet(String code, String nom, String description,
                  Date dateDebut, Date dateFin, double montant,
                  Organisme organisme, Employe chefProjet) {
        this.id = id;
        this.code = code;
        this.nom = nom;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
        this.organisme = organisme;
        this.chefProjet = chefProjet;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public Organisme getOrganisme() {
        return organisme;
    }

    public void setOrganisme(Organisme organisme) {
        this.organisme = organisme;
    }

    public Employe getChefProjet() {
        return chefProjet;
    }

    public void setChefProjet(Employe chefProjet) {
        this.chefProjet = chefProjet;
    }
}


