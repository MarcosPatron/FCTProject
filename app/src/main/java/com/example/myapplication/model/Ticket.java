package com.example.myapplication.model;

import com.example.myapplication.utils.Categoria;
import com.example.myapplication.utils.Prioridad;

public class Ticket {

    private Usuario usuario;
    private Categoria categoria;
    private Prioridad prioridad;
    private String descripcion;

    public Ticket(Usuario usuario, Categoria categoria, Prioridad prioridad, String descripcion) {
        this.usuario = usuario;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.descripcion = descripcion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
