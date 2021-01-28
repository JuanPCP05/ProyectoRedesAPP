package com.example.proyectoredesapp.Entidades;

import com.example.proyectoredesapp.Entidades.Mensaje;

public class MensajeRecibir extends Mensaje {

    private Long hora;

    public MensajeRecibir() {
    }

    public MensajeRecibir(Long hora) {
        this.hora = hora;
    }

    public MensajeRecibir(String mensaje, String nombre, String fotoPerfil, String tipo_mensaje, String urlFoto, Long hora) {
        super(mensaje, nombre, fotoPerfil, tipo_mensaje, urlFoto);
        this.hora = hora;
    }

    public Long getHora() {
        return hora;
    }

    public void setHora(Long hora) {
        this.hora = hora;
    }
}
