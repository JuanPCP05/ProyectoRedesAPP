package com.example.proyectoredesapp.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectoredesapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText txtCorreo, txtContrasena;
    private Button btnRegistrarse, btnIngresar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtContrasena = (EditText) findViewById(R.id.contraseñaIngreso);
        txtCorreo = (EditText) findViewById(R.id.correoIngreso);
        btnIngresar = (Button) findViewById(R.id.btnIngresar);
        btnRegistrarse = (Button) findViewById(R.id.btnRegistrarse);

        mAuth = FirebaseAuth.getInstance();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo, contrasena;
                correo = txtCorreo.getText().toString();
                if(validarCorreo(correo) && validarContrasena()){
                    contrasena = txtContrasena.getText().toString();
                    mAuth.signInWithEmailAndPassword(correo, contrasena)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        siguienteActividad();
                                        Toast.makeText(Login.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Login.this, "Error, datos incorrectos", Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });
                }else{
                    Toast.makeText(Login.this, "Error en el ingreso", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registro.class));
            }
        });
    }

    private boolean validarCorreo(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean validarContrasena(){
        String contrasena;

        contrasena = txtContrasena.getText().toString();

            if (contrasena.length() >=6 && contrasena.length() <=16){
                return true;
            }else{
                return false;
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(Login.this, "Bienvenido de nuevo", Toast.LENGTH_SHORT).show();
            siguienteActividad();
        }
    }

    private void siguienteActividad(){
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }
}
