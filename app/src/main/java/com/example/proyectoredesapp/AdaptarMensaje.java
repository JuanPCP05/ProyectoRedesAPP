package com.example.proyectoredesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdaptarMensaje extends RecyclerView.Adapter<AdaptadorMensajes> {

    private List<Mensaje> listMensaje = new ArrayList<>();
    private Context c;

    public AdaptarMensaje(Context c) {
        this.c = c;
    }

    public void addMensaje(Mensaje m){
        listMensaje.add(m);
        notifyItemInserted(listMensaje.size());
    }

    @NonNull
    @Override
    public AdaptadorMensajes onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.tarjeta_vista_mensajes, parent, false);
        return new AdaptadorMensajes(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorMensajes holder, int position) {
        holder.getNombre().setText(listMensaje.get(position).getNombre());
        holder.getHora().setText(listMensaje.get(position).getHora());
        holder.getMensajeReducido().setText(listMensaje.get(position).getMensaje());
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
