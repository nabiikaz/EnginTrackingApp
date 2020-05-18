package com.pfe.enginapp.models;

public class agent {

    String nom,prenom,date_de_naissance,numTel;

    public agent(String nom, String prenom, String date_de_naissance, String numTel) {
        this.nom = nom;
        this.prenom = prenom;
        this.date_de_naissance = date_de_naissance;
        this.numTel = numTel;
    }

    public agent() {
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDate_de_naissance() {
        return date_de_naissance;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDate_de_naissance(String date_de_naissance) {
        this.date_de_naissance = date_de_naissance;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }
}
