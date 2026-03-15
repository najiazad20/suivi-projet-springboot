package com.example.suivi_projet.organisation.dto;

public class EmployeResponseDTO {

    private int id;
    private String matricule;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String login;
    private String password;
    private String profilNom;


    public EmployeResponseDTO() {}


    public EmployeResponseDTO(int id, String matricule, String nom, String prenom,
                              String telephone, String email, String login, String password, String profilNom) {
        this.id = id;
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.login = login;
        this.password = password;
        this.profilNom = profilNom;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getProfilNom() { return profilNom; }
    public void setProfilNom(String profilNom) { this.profilNom = profilNom; }
}