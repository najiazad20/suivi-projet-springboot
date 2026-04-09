package com.example.suivi_projet.facturation.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Date;
import com.example.suivi_projet.projet.entities.Phase;

@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    @Temporal(TemporalType.DATE)
    private LocalDate dateFacture;

    @OneToOne
    private Phase phase;

    public Facture() {
    }

    public Facture( String code, LocalDate dateFacture, Phase phase) {
        this.id = id;
        this.code = code;
        this.dateFacture = dateFacture;
        this.phase = phase;
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

    public LocalDate getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(LocalDate dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}