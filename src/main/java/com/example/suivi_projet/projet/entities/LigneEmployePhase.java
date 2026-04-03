package com.example.suivi_projet.projet.entities;

import com.example.suivi_projet.organisation.entities.Employe;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class LigneEmployePhase {

    @EmbeddedId
    private LigneEmployePhaseId id;

    @Temporal(TemporalType.DATE)
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    private Date dateFin;

    @ManyToOne
    @MapsId("employeId")
    private Employe employe;

    @ManyToOne
    @MapsId("phaseId")
    private Phase phase;

    public LigneEmployePhase() {}

    public LigneEmployePhase(Date dateDebut, Date dateFin, Employe employe, Phase phase) {
        this.id = new LigneEmployePhaseId(employe.getId(), phase.getId());
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employe = employe;
        this.phase = phase;
    }

    public LigneEmployePhaseId getId() { return id; }
    public void setId(LigneEmployePhaseId id) { this.id = id; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public Employe getEmploye() { return employe; }
    public void setEmploye(Employe employe) { this.employe = employe; }

    public Phase getPhase() { return phase; }
    public void setPhase(Phase phase) { this.phase = phase; }
}