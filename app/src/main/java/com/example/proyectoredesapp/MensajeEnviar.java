package com.example.proyectoredesapp;

import java.util.Map;

public class MensajeEnviar extends Mensaje{

    private Map hora;

    public MensajeEnviar() {
    }

    public MensajeEnviar(Map hora) {
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String tipo_mensaje, String urlFoto, Map hora) {
        super(mensaje, nombre, fotoPerfil, tipo_mensaje, urlFoto);
        this.hora = hora;
    }

    public MensajeEnviar(String mensaje, String nombre, String fotoPerfil, String tipo_mensaje, Map hora) {
        super(mensaje, nombre, fotoPerfil, tipo_mensaje);
        this.hora = hora;
    }

    public Map getHora() {
        return hora;
    }

    public void setHora(Map hora) {
        this.hora = hora;
    }
}
