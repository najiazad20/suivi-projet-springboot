package com.example.suivi_projet.projet.entities;

import jakarta.persistence.*;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nom;
    private String cheminFichier;

    @ManyToOne
    private Projet projet;

    public Document() {
    }

    public Document(int id, String nom, String cheminFichier, Projet projet) {
        this.id = id;
        this.nom = nom;
        this.cheminFichier = cheminFichier;
        this.projet = projet;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }

    public void setNom(String nom) { this.nom = nom; }

    public String getCheminFichier() { return cheminFichier; }

    public void setCheminFichier(String cheminFichier) { this.cheminFichier = cheminFichier; }

    public Projet getProjet() { return projet; }

    public void setProjet(Projet projet) { this.projet = projet; }
}