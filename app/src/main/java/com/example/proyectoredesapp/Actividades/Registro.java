package com.example.proyectoredesapp.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoredesapp.Entidades.Usuario;
import com.example.proyectoredesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Registro extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtContrasena, txtRepetirContrasena;
    private Button btnRegistrar;

    private FirebaseAuth mAuth;
    private FirebaseDatabase baseDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtNombre = (EditText) findViewById(R.id.nombre);
        txtCorreo = (EditText) findViewById(R.id.correo);
        txtContrasena = (EditText) findViewById(R.id.contraseña);
        txtRepetirContrasena = (EditText) findViewById(R.id.rContraseña);

        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        mAuth = FirebaseAuth.getInstance();
        baseDatos = FirebaseDatabase.getInstance();


        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo, contrasena, nombre;
                correo = txtCorreo.getText().toString();
                nombre = txtNombre.getText().toString();
                if(validarContraseña() && validarCorreo(correo) && validarNombre(nombre)) {
                    contrasena = txtContrasena.getText().toString();
                    mAuth.createUserWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener(Registro.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(Registro.this, "Se registro correctamente", Toast.LENGTH_SHORT).show();
                                        Usuario usuario = new Usuario();
                                        usuario.setCorreo(correo);
                                        usuario.setNombre(nombre);
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        DatabaseReference referenciaUsuarioBD = baseDatos.getReference("Usuarios/"+currentUser.getUid());
                                        referenciaUsuarioBD.setValue(usuario);
                                        startActivity(new Intent(Registro.this, MainActivity.class));
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Registro.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(Registro.this, "Error al registrar", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validarCorreo(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validarContraseña(){
        String contrasena, rContrasena;

        contrasena = txtContrasena.getText().toString();
        rContrasena = txtRepetirContrasena.getText().toString();

        if(contrasena.equals(rContrasena)){
            if (contrasena.length() >=6 && contrasena.length() <=16){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    private boolean validarNombre(String nombre){
        return !nombre.isEmpty();
    }

}
