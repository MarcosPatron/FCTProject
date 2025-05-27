package com.example.myapplication.API;

public class Usuario {

    private String fullname;
    private String username;
    private String JWToken;
    private String email;
    private String profilePicture;
    private String tipoUsuario;

    public Usuario(String fullname, String username, String JWToken, String email, String profilePicture, String tipoUsuario) {
        this.fullname = fullname;
        this.username = username;
        this.JWToken = JWToken;
        this.email = email;
        this.profilePicture = profilePicture;
        this.tipoUsuario = tipoUsuario;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJWToken() {
        return JWToken;
    }

    public void setJWToken(String JWToken) {
        this.JWToken = JWToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}
