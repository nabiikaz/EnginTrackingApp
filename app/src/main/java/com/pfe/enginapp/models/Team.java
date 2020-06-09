package com.pfe.enginapp.models;

import java.util.ArrayList;

public class Team {



    String _id,date ;
    Boolean disponibilite;
    Engin engin;
    ArrayList<Agent> agents;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(Boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public Engin getEngin() {
        return engin;
    }

    public void setEngin(Engin engin) {
        this.engin = engin;
    }

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public void setAgents(ArrayList<Agent> agents) {
        this.agents = agents;
    }

    public class Agent{

        public static final String SECOURS_TYPE = "secours";
        public static final String CHEF_TYPE = "chef";
        public static final String CHAUFFEUR_TYPE = "chauffeur";
        String id_agent,nom,prenom,username,type;

        public String getId_agent() {
            return id_agent;
        }

        public void setId_agent(String id_agent) {
            this.id_agent = id_agent;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getPrenom() {
            return prenom;
        }

        public void setPrenom(String prenom) {
            this.prenom = prenom;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public class Engin {
        String code_name,matricule;

        public String getCode_name() {
            return code_name;
        }

        public void setCode_name(String code_name) {
            this.code_name = code_name;
        }

        public String getMatricule() {
            return matricule;
        }

        public void setMatricule(String matricule) {
            this.matricule = matricule;
        }
    }


    public Agent getAgent(String id){
        for (Agent agent : agents) {
            if(agent.getId_agent() == id){
                return agent;
            }
        }

        return null;

    }

    public String getChefId(){
        for (Agent agent : agents) {
            if(agent.getType() == Agent.CHEF_TYPE ){
                return agent.getId_agent();
            }
        }

        return  "";

    }
}
