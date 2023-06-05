package com.insitutosanjuandelacruz.vetpet.view.ui.pets.adder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.view.LoginActivity;
import com.insitutosanjuandelacruz.vetpet.view.MainActivity;
import com.insitutosanjuandelacruz.vetpet.view.RegisterPasswordActivity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddPetActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText editTextName;
    EditText editTextSpecie;
    EditText editTextDate;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore firestore;
    int year, month, day;

    Button btnAddPet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pet);
        editTextName = findViewById(R.id.editNamePet);
        editTextSpecie = findViewById(R.id.editSpeciePet);
        editTextDate = findViewById(R.id.editDatePet);
        btnAddPet = findViewById(R.id.btnAddPet);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        btnAddPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String species = editTextSpecie.getText().toString().trim();
                String date = editTextDate.getText().toString().trim();
                Calendar calendar = new GregorianCalendar(year, month, day);
                Timestamp timestampDate = new Timestamp(calendar.getTime());
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(species) && !TextUtils.isEmpty(date)) {
                    // All fields have data, proceed with Firestore upload
                    uploadPetData(name, species, timestampDate);
                } else {
                    Toast.makeText(AddPetActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog y mostrarlo
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this, year, month, day);
        datePickerDialog.show();
    }

    private void uploadPetData(String name, String species, Timestamp date) {
        String id = mAuth.getCurrentUser().getUid();
        Map<String, Object> petMap = new HashMap<>();
        petMap.put("id", UUID.randomUUID().toString());
        petMap.put("name", name);
        petMap.put("race", "");
        petMap.put("birthdate", date);
        petMap.put("specie", species);
        petMap.put("gender","");
        petMap.put("weight",0);
        petMap.put("userId",currentUser.getUid());
        petMap.put("petImage","default_pet_image.jpg");
        firestore.collection("pet").add(petMap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                finish();
                startActivity(new Intent(AddPetActivity.this, MainActivity.class));
                Toast.makeText(AddPetActivity.this, "Pet register success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddPetActivity.this, "Error uploading pet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        editTextDate.setText(selectedDate);
    }

}