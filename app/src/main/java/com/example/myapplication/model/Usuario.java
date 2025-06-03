package com.example.myapplication.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Usuario {

    private String fullname;
    private String username; // Unico
    private String JWToken;
    private String email;
    private String profilePicture;
    private String password;

    public Usuario(String fullname, String username, String JWToken, String email, String profilePicture, String password) {
        this.fullname = fullname;
        this.username = username;
        this.JWToken = JWToken;
        this.email = email;
        this.profilePicture = profilePicture;
        this.password = password;
    }

    public Usuario(String fullname, String username, String password, String email) {
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = this.password;
    }

    // Metodos para manejar la sesion del usuario
    private static final String PREF_NAME = "user_session";
    private static final String USER_KEY = "currentUser";

    public static void guardarSesion(Context context, Usuario usuario) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = new Gson().toJson(usuario);
        editor.putString(USER_KEY, json);
        editor.apply();
    }

    public static Usuario obtenerSesion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(USER_KEY, null);
        return json != null ? new Gson().fromJson(json, Usuario.class) : null;
    }

    public static void cerrarSesion(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(USER_KEY).apply();
    }

    public static boolean sesionActiva(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(USER_KEY);
    }
}
