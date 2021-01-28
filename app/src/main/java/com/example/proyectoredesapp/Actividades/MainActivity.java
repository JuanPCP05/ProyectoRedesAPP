package com.example.proyectoredesapp.Actividades;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.proyectoredesapp.AdaptarMensaje;
import com.example.proyectoredesapp.Entidades.MensajeEnviar;
import com.example.proyectoredesapp.Entidades.MensajeRecibir;
import com.example.proyectoredesapp.Entidades.Usuario;
import com.example.proyectoredesapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private static final int FOTO_ENVIADA = 1;
    private static final int FOTO_PERFIL = 2;

    private String fotoPerfilCadena;
    private String nombreUsuario;

    private TextView nombre;

    private CircleImageView fotoPerfil, salir;

    private RecyclerView rvMensajes;

    private EditText txtMensajes;

    private Button btnEnviar;
    private ImageButton btnGaleria;

    private AdaptarMensaje adaptador;

    private FirebaseDatabase baseDatos;
    private DatabaseReference referenciaDB;
    private FirebaseStorage almacenamientoDatos;
    private StorageReference referenciaAlmacenamiento;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre = (TextView) findViewById(R.id.NombreIdentificador);
        fotoPerfil = (CircleImageView) findViewById(R.id.fotoPerfil);
        salir = (CircleImageView) findViewById(R.id.salir);
        rvMensajes = (RecyclerView) findViewById(R.id.EspacioDeMensajes);
        txtMensajes = (EditText) findViewById(R.id.EscribirUnMensaje);
        btnEnviar = (Button) findViewById(R.id.BotonEnviar);
        btnGaleria = (ImageButton) findViewById(R.id.enviarFoto);

        fotoPerfilCadena = "";

        baseDatos = FirebaseDatabase.getInstance();
        referenciaDB = baseDatos.getReference("chat");  //SaLa de chat (donde se guardarán todos los chats)
        almacenamientoDatos = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();


        adaptador = new AdaptarMensaje(this);
        LinearLayoutManager l = new LinearLayoutManager(this);
        rvMensajes.setLayoutManager(l);
        rvMensajes.setAdapter(adaptador);

        btnEnviar.setOnClickListener(new View.OnClickListener() {  //Permite el envio y descarga de la imagen desde storage firebase
            @Override
            public void onClick(View v) {
                referenciaDB.push().setValue(new MensajeEnviar(txtMensajes.getText().toString(), nombreUsuario,fotoPerfilCadena,"1", ServerValue.TIMESTAMP));
                txtMensajes.setText("");
            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), FOTO_ENVIADA);
            }
        });

        fotoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/jpeg");
                i.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(i, "Selecciona una foto"), FOTO_PERFIL);
            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                siguienteActividad();
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
                MensajeRecibir m = dataSnapshot.getValue(MensajeRecibir.class);
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

        verifyStoragePermissions(this);
    }

    public void setScrollBar(){
        rvMensajes.scrollToPosition(adaptador.getItemCount()-1);
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        int REQUEST_EXTERNAL_STORAGE = 1;
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        }else{
            return true;
        }
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
                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri u) {
                            MensajeEnviar m = new MensajeEnviar("Te han enviado una foto", nombreUsuario,fotoPerfilCadena , "2", u.toString(),ServerValue.TIMESTAMP );
                            referenciaDB.push().setValue(m);
                        }
                    });
                }
            });
        }else if (requestCode == FOTO_PERFIL && resultCode == RESULT_OK){
            Uri u = data.getData();
            referenciaAlmacenamiento = almacenamientoDatos.getReference("foto_perfil"); //Almacena las fotos de perfil del usuario
            final StorageReference referenciaFoto = referenciaAlmacenamiento.child(u.getLastPathSegment()); //obtiene una key unica para la referencia de la foto
            referenciaFoto.putFile(u).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri u) {
                            fotoPerfilCadena = u.toString();
                            MensajeEnviar m = new MensajeEnviar("Se ha actualizado la foto de perfil", nombreUsuario,fotoPerfilCadena , "2", u.toString(),ServerValue.TIMESTAMP );
                            referenciaDB.push().setValue(m);
                            Glide.with(MainActivity.this).load(u.toString()).into(fotoPerfil);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            DatabaseReference referenciaUsuarioBD = baseDatos.getReference("Usuarios/"+currentUser.getUid());
            btnEnviar.setEnabled(false);
            referenciaUsuarioBD.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Usuario usuario = snapshot.getValue(Usuario.class);
                    nombreUsuario = usuario.getNombre();
                    nombre.setText(nombreUsuario);
                    btnEnviar.setEnabled(true);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else{
            siguienteActividad();
        }
    }

    private void siguienteActividad(){
        startActivity(new Intent(MainActivity.this, Login.class));
        finish();
    }
}