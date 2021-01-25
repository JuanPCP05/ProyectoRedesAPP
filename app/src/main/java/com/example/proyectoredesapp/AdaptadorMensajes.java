package com.example.proyectoredesapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdaptadorMensajes extends RecyclerView.ViewHolder{

    private CircleImageView fotoPerfil;
    private TextView hora, mensajeReducido, nombre;
    private ImageView fotoMensaje;

    public AdaptadorMensajes(@NonNull View itemView) {
        super(itemView);
        fotoPerfil = (CircleImageView) itemView.findViewById(R.id.imagenPerfil);
        hora = (TextView) itemView.findViewById(R.id.hora);
        nombre = (TextView) itemView.findViewById(R.id.nombreReducido);
        mensajeReducido = (TextView) itemView.findViewById(R.id.pequeñoMensaje);
        fotoMensaje = (ImageView) itemView.findViewById(R.id.imagenMensaje);

    }


    public CircleImageView getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(CircleImageView fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public TextView getHora() {
        return hora;
    }

    public void setHora(TextView hora) {
        this.hora = hora;
    }

    public TextView getMensajeReducido() {
        return mensajeReducido;
    }

    public void setMensajeReducido(TextView mensajeReducido) {
        this.mensajeReducido = mensajeReducido;
    }

    public TextView getNombre() {
        return nombre;
    }

    public void setNombre(TextView nombre) {
        this.nombre = nombre;
    }

    public ImageView getFotoMensaje() {
        return fotoMensaje;
    }

    public void setFotoMensaje(ImageView fotoMensaje) {
        this.fotoMensaje = fotoMensaje;
    }
}
