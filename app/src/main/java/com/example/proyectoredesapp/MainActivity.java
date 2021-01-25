package com.example.proyectoredesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int FOTO_ENVIADA = 1;

    private TextView nombre;

    private CircleImageView fotoPerfil;

    private RecyclerView rvMensajes;

    private EditText txtMensajes;

    private Button btnEnviar;
    private ImageButton btnGaleria;

    private AdaptarMensaje adaptador;

    private FirebaseDatabase baseDatos;
    private DatabaseReference referenciaDB;
    private FirebaseStorage almacenamientoDatos;
    private StorageReference referenciaAlmacenamiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (TextView) findViewById(R.id.NombreIdentificador);
        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        rvMensajes = (RecyclerView) findViewById(R.id.EspacioDeMensajes);
        txtMensajes = (EditText) findViewById(R.id.EscribirUnMensaje);
        btnEnviar = (Button) findViewById(R.id.BotonEnviar);
        btnGaleria = (ImageButton) findViewById(R.id.enviarFoto);

        baseDatos = FirebaseDatabase.getInstance("https://appchat-af17d-default-rtdb.firebaseio.com/");
        referenciaDB = baseDatos.getReference("chat");  //SaLa de chat (donde se guardarán todos los chats)
        almacenamientoDatos = FirebaseStorage.getInstance();


        adaptador = new AdaptarMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adaptador);

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                referenciaDB.push().setValue(new Mensaje(txtMensajes.getText().toString(), nombre.getText().toString(),"","1", "00:00"));
                txtMensajes.setText("");
            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), FOTO_ENVIADA);
            }
        });

        adaptador.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setScrollBar();
            }
        });

        referenciaDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Mensaje m = dataSnapshot.getValue(Mensaje.class);
                adaptador.addMensaje(m);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void setScrollBar(){
        rvMensajes.scrollToPosition(adaptador.getItemCount()-1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FOTO_ENVIADA && resultCode == RESULT_OK){
            Uri u = data.getData();
            referenciaAlmacenamiento = almacenamientoDatos.getReference("imagenes_app"); //Imagenes_app
            final StorageReference referenciaFoto = referenciaAlmacenamiento.child(u.getLastPathSegment()); //obtiene una key unica para la referencia de la foto
            referenciaFoto.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    Mensaje m = new Mensaje("Te han enviado una foto", nombre.getText().toString(), u.toString(), "", "2", "00:00");
                    referenciaDB.push().setValue(m);
                }
            });
        }
    }
}