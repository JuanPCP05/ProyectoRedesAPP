package com.example.proyectoredesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AdaptarMensaje extends RecyclerView.Adapter<AdaptadorMensajes> {

    private List<MensajeRecibir> listMensaje = new ArrayList<>();
    private Context c;

    public AdaptarMensaje(Context c) {
        this.c = c;
    }

    public void addMensaje(MensajeRecibir m){
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
        holder.getMensajeReducido().setText(listMensaje.get(position).getMensaje());
        if(listMensaje.get(position).getTipo_mensaje().equals("2")){
            holder.getFotoMensaje().setVisibility(View.VISIBLE);
            holder.getMensajeReducido().setVisibility(View.VISIBLE);
            Glide.with(c).load(listMensaje.get(position).getUrlFoto()).into(holder.getFotoMensaje());
        }else if (listMensaje.get(position).getTipo_mensaje().equals("1")){
            holder.getFotoMensaje().setVisibility(View.GONE);
            holder.getMensajeReducido().setVisibility(View.VISIBLE);
        }

        Long codigoHora = listMensaje.get(position).getHora();
        Date d = new Date(codigoHora);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        holder.getHora().setText(sdf.format(d));
    }

    @Override
    public int getItemCount() {
        return listMensaje.size();
    }
}
