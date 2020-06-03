package com.pfe.enginapp.models;

import com.google.android.gms.maps.model.LatLng;

public class Intervention {
    String _id;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    Adresse adresse;
    String[] description;

    String dateTimeAppel,dateTimeDepart,dateTimeArrive;

    Transfere transfere;

    String statut;
    String numTel;
    String id_unite;
    String cco_agent_secondaire;
    String id_team;

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String[] getDescription() {
        return description;
    }

    public void setDescription(String[] description) {
        this.description = description;
    }

    public String getDateTimeAppel() {
        return dateTimeAppel;
    }

    public void setDateTimeAppel(String dateTimeAppel) {
        this.dateTimeAppel = dateTimeAppel;
    }

    public String getDateTimeDepart() {
        return dateTimeDepart;
    }

    public void setDateTimeDepart(String dateTimeDepart) {
        this.dateTimeDepart = dateTimeDepart;
    }

    public String getDateTimeArrive() {
        return dateTimeArrive;
    }

    public void setDateTimeArrive(String dateTimeArrive) {
        this.dateTimeArrive = dateTimeArrive;
    }

    public Transfere getTransfere() {
        return transfere;
    }

    public void setTransfere(Transfere transfere) {
        this.transfere = transfere;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public String getId_unite() {
        return id_unite;
    }

    public void setId_unite(String id_unite) {
        this.id_unite = id_unite;
    }

    public String getCco_agent_secondaire() {
        return cco_agent_secondaire;
    }

    public void setCco_agent_secondaire(String cco_agent_secondaire) {
        this.cco_agent_secondaire = cco_agent_secondaire;
    }

    public String getId_team() {
        return id_team;
    }

    public void setId_team(String id_team) {
        this.id_team = id_team;
    }

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    String bilan; //cco_agent_principale,id_unite_principale,id_node




    public class Adresse {
        Gps_coordonnee gps_coordonnee;
        String wilaya,daira,adresse_rue;

        public Gps_coordonnee getGps_coordonnee() {
            return gps_coordonnee;
        }

        public void setGps_coordonnee(Gps_coordonnee gps_coordonnee) {
            this.gps_coordonnee = gps_coordonnee;
        }

        public String getWilaya() {
            return wilaya;
        }

        public void setWilaya(String wilaya) {
            this.wilaya = wilaya;
        }

        public String getDaira() {
            return daira;
        }

        public void setDaira(String daira) {
            this.daira = daira;
        }

        public String getAdresse_rue() {
            return adresse_rue;
        }

        public void setAdresse_rue(String adresse_rue) {
            this.adresse_rue = adresse_rue;
        }
    }

    public class Transfere{
        String lieu,dateTimeDepart;
        Gps_coordonnee gps_coordonnee;

        public String getLieu() {
            return lieu;
        }

        public void setLieu(String lieu) {
            this.lieu = lieu;
        }

        public String getDateTimeDepart() {
            return dateTimeDepart;
        }

        public void setDateTimeDepart(String dateTimeDepart) {
            this.dateTimeDepart = dateTimeDepart;
        }

        public Gps_coordonnee getGps_coordonnee() {
            return gps_coordonnee;
        }

        public void setGps_coordonnee(Gps_coordonnee gps_coordonnee) {
            this.gps_coordonnee = gps_coordonnee;
        }
    }



    public class Gps_coordonnee{
        float lat,lng;

        public float getLat() {
            return lat;
        }

        public void setLat(float lat) {
            this.lat = lat;
        }

        public float getLng() {
            return lng;
        }

        public void setLng(float lng) {
            this.lng = lng;
        }

        public LatLng getLatLng(){
            return  new LatLng(lat,lng);
        }
    }




}
