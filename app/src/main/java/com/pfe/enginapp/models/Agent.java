package com.pfe.enginapp.models;

public class Agent {

    public final static String AGENT_ID = "agent_id";
    public final static String AGENT_ROLE = "agent_role";
    public final static String AGENT_USERNAME = "agent_username";
    public final static String AGENT_NOM = "agent_nom";



    String agent_nom;
    String agent_role;
    String agent_username;
    String agent_id;
    String id_unite;

    public String getId_unite() {
        return id_unite;
    }

    public  void setId_unite(String id_unite) {
        this.id_unite = id_unite;
    }



    public String getAgent_id() {
        return agent_id;
    }

    public void setAgent_id(String agent_id) {
        this.agent_id = agent_id;
    }

    public Agent(String agent_id, String agent_nom, String agent_role, String agent_username,String id_unite) {
        this.agent_id = agent_id;
        this.agent_nom = agent_nom;
        this.agent_role = agent_role;
        this.agent_username = agent_username;
        this.id_unite = id_unite;
    }

    public Agent() {
    }

    public String getAgent_nom() {
        return agent_nom;
    }

    public String getAgent_role() {
        return agent_role;
    }

    public String getAgent_username() {
        return agent_username;
    }

    public void setAgent_nom(String agent_nom) {
        this.agent_nom = agent_nom;
    }

    public void setAgent_role(String agent_role) {
        this.agent_role = agent_role;
    }

    public void setAgent_username(String agent_username) {
        this.agent_username = agent_username;
    }

}
