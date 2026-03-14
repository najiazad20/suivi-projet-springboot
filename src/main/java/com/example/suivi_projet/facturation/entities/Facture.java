package com.example.suivi_projet.facturation.entities;

import jakarta.persistence.*;
import java.util.Date;
import com.example.suivi_projet.projet.entities.Phase;

@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String numero;

    @Temporal(TemporalType.DATE)
    private Date dateFacture;

    private double montant;

    @OneToOne
    private Phase phase;

    public Facture() {
    }

    public Facture(int id, String numero, Date dateFacture, double montant, Phase phase) {
        this.id = id;
        this.numero = numero;
        this.dateFacture = dateFacture;
        this.montant = montant;
        this.phase = phase;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNumero() { return numero; }

    public void setNumero(String numero) { this.numero = numero; }

    public Date getDateFacture() { return dateFacture; }

    public void setDateFacture(Date dateFacture) { this.dateFacture = dateFacture; }

    public double getMontant() { return montant; }

    public void setMontant(double montant) { this.montant = montant; }

    public Phase getPhase() { return phase; }

    public void setPhase(Phase phase) { this.phase = phase; }
}