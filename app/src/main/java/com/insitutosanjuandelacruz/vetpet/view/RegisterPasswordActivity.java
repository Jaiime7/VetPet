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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.type.DateTime;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.controller.ValidateEmailPassword;
import com.google.firebase.firestore.FirebaseFirestore;

import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
        int day = extras.getInt("day");
        int month = extras.getInt("month");
        int year = extras.getInt("year");
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
                    Calendar calendar = new GregorianCalendar(year, month, day);
                    Timestamp date = new Timestamp(calendar.getTime());
                    writeToDb(email, password, date);
                }
            }
        });
    }

    public void writeToDb(String email, String password, Timestamp date) {

        // Encrypt method
        UUID uuid= UUID.randomUUID();
        String name = uuid.toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user = mAuth.getCurrentUser();
                try {
                    user.sendEmailVerification();
                    Toast.makeText(RegisterPasswordActivity.this, "A verification email has been sent.", Toast.LENGTH_SHORT).show();
                    String id = mAuth.getCurrentUser().getUid();
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", id);
                    userMap.put("name", "Guest-" + name);
                    userMap.put("email", email);
                    userMap.put("birthdate", date);
                    userMap.put("gender","");
                    userMap.put("userType","Client");
                    userMap.put("address","");
                    userMap.put("profileImage","default_profile_image.jpg");
                    firebaseFirestore.collection("user").add(userMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            finish();
                            startActivity(new Intent(RegisterPasswordActivity.this, LoginActivity.class));
                            Toast.makeText(RegisterPasswordActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPasswordActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Manejo de errores
                Exception exception = task.getException();
                Toast.makeText(RegisterPasswordActivity.this, "Error al crear cuente" + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}