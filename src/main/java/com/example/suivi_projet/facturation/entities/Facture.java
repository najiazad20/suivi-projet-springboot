package com.example.suivi_projet.facturation.entities;

import jakarta.persistence.*;
import java.util.Date;
import com.example.suivi_projet.projet.entities.Phase;

@Entity
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    @Temporal(TemporalType.DATE)
    private Date dateFacture;

    @OneToOne
    private Phase phase;

    public Facture() {
    }

    public Facture( String code, Date dateFacture, Phase phase) {
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

    public Date getDateFacture() {
        return dateFacture;
    }

    public void setDateFacture(Date dateFacture) {
        this.dateFacture = dateFacture;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}