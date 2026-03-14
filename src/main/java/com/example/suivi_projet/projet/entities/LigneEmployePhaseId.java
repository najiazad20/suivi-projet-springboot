package com.example.suivi_projet.projet.entities;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class LigneEmployePhaseId implements Serializable {

    private int employeId;
    private int phaseId;

    public LigneEmployePhaseId() {
    }

    public LigneEmployePhaseId(int employeId, int phaseId) {
        this.employeId = employeId;
        this.phaseId = phaseId;
    }

    public int getEmployeId() {
        return employeId;
    }

    public void setEmployeId(int employeId) {
        this.employeId = employeId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }
}
