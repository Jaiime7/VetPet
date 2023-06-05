package com.insitutosanjuandelacruz.vetpet.view.ui.pets.editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.model.Pet;
import com.insitutosanjuandelacruz.vetpet.view.LoginActivity;

public class EditPetActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextSpecies;
    private EditText editTextRace;
    private EditText editTextGender;
    private EditText editTextWeight;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        String petId = getIntent().getStringExtra("petId");

        editTextName = findViewById(R.id.editTextName);
        editTextSpecies = findViewById(R.id.editTextSpecies);
        editTextRace = findViewById(R.id.editTextRace);
        editTextGender = findViewById(R.id.editTextGender);
        editTextWeight = findViewById(R.id.editTextWeight);
        buttonSave = findViewById(R.id.buttonSave);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference petsRef = db.collection("pet");

        // Repite el mismo proceso para los demás campos de la mascota
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = petsRef.whereEqualTo("id", petId);

                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                // Se encontró al menos un documento con el campo "Id" igual a petId

                                for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                                    String documentId = documentSnapshot.getId();
                                    Pet pet = documentSnapshot.toObject(Pet.class);
                                    pet.setName(editTextName.getText().toString());
                                    pet.setRace(editTextRace.getText().toString());
                                    pet.setSpecies(editTextSpecies.getText().toString());
                                    pet.setGender(editTextGender.getText().toString());
                                    petsRef.document(documentId).set(pet)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(EditPetActivity.this, "Update ok", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(EditPetActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                // No se encontraron documentos con el campo "Id" igual a petId
                            }
                        } else {
                            // Maneja el error en caso de que la consulta falle
                        }
                    }
                });
            }
        });
    }
}
