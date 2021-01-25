package com.example.proyectoredesapp;

public class Mensaje {

    private String mensaje, nombre, fotoPerfil, tipo_mensaje, hora, urlFoto;

    public Mensaje() {
    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String tipo_mensaje, String hora) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.tipo_mensaje = tipo_mensaje;
        this.hora = hora;
    }

    public Mensaje(String mensaje, String nombre, String fotoPerfil, String tipo_mensaje, String hora, String urlFoto) {
        this.mensaje = mensaje;
        this.nombre = nombre;
        this.fotoPerfil = fotoPerfil;
        this.tipo_mensaje = tipo_mensaje;
        this.hora = hora;
        this.urlFoto = urlFoto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public String getTipo_mensaje() {
        return tipo_mensaje;
    }

    public void setTipo_mensaje(String tipo_mensaje) {
        this.tipo_mensaje = tipo_mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
