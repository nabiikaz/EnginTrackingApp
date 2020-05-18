package com.pfe.enginapp.models;

public class auth {

    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String TOKEN = "token";

    private String username,authToken;

    public auth(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
