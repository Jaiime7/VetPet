package com.insitutosanjuandelacruz.vetpet.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.controller.ValidateEmailPassword;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterPasswordActivity extends AppCompatActivity {

    public EditText editTextPassword1;
    public EditText editTextPassword2;
    public ImageButton imageButtonNext;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        Bundle extras = getIntent().getExtras();
        String email = extras.getString("email");
        editTextPassword1 = findViewById(R.id.editTextTextPassword1);
        editTextPassword2 = findViewById(R.id.editTextTextPassword2);
        imageButtonNext = findViewById(R.id.imageButtonNext2);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateEmailPassword val = new ValidateEmailPassword();
                if (editTextPassword1.getText().length() == 0 || editTextPassword2.getText().length() == 0) {
                    Toast.makeText(RegisterPasswordActivity.this, "You must type the password twice", Toast.LENGTH_SHORT).show();
                } else if (!editTextPassword1.getText().toString().equals(editTextPassword2.getText().toString())) {
                    Toast.makeText(RegisterPasswordActivity.this, "Passwords must match", Toast.LENGTH_SHORT).show();
                } else if (!val.validatePassword(editTextPassword1.getText().toString())) {
                    Toast.makeText(RegisterPasswordActivity.this, "The password must meet the following requirements", Toast.LENGTH_SHORT).show();
                } else {
                    String password = editTextPassword1.getText().toString();
                    writeToDb(email, password);
                }
            }
        });
    }

    public void writeToDb(String email, String password) {
        // Encrypt method
        UUID uuid= UUID.randomUUID();
        String name = uuid.toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> user = new HashMap<>();
                user.put("id", id);
                user.put("name", "Guest-" + name);
                user.put("email", email);
                user.put("password", password);
                user.put("birthdate", new Date());
                user.put("gender","");
                user.put("userType","Client");
                user.put("address","");
                user.put("profileImage","");
                firebaseFirestore.collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        finish();
                        startActivity(new Intent(RegisterPasswordActivity.this, LoginActivity.class));
                        Toast.makeText(RegisterPasswordActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterPasswordActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterPasswordActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}