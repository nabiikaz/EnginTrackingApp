package com.pfe.enginapp.models;

public class Auth {

    public final static String USERNAME = "username";
    public final static String PASSWORD = "password";
    public final static String TOKEN = "token";



    private String username;
    private String password;
    private String authToken;

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Auth(String username, String password, String authToken) {
        this.username = username;
        this.password = password;
        this.authToken = authToken;
    }

    public Auth(String authToken) {
        this.authToken = authToken;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
