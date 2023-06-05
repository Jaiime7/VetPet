package com.insitutosanjuandelacruz.vetpet.view.ui.appointments;

import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentAppointmentsBinding;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentMeBinding;
import com.insitutosanjuandelacruz.vetpet.view.ui.me.MeViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AppointmentsFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private FragmentAppointmentsBinding binding;
    FirebaseFirestore db;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;
    CollectionReference usersRef;
    CollectionReference petRef;
    Spinner spinnerVet;
    Spinner spinnerPet;

    EditText editTextDate;
    EditText editTextRemarks;
    EditText editTextType;
    Button buttonClear;
    Button buttonSend;
    Calendar calendar;
    int year, month, day, hour, min;

    public static AppointmentsFragment newInstance() {
        return new AppointmentsFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppointmentsViewModel appointmentsViewModel = new ViewModelProvider(this).get(AppointmentsViewModel.class);
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        mAuth= FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();
        spinnerVet = binding.spinnerVetAppo;
        spinnerPet = binding.spinnerPetAppo;
        editTextDate = binding.editTextDateAppo;
        editTextRemarks = binding.editTextRemarks;
        editTextType = binding.editTextTypeAppo;
        buttonClear = binding.buttonClear;
        buttonSend = binding.buttonSendAppo;
        buttonSend.setOnClickListener(new View.OnClickListener() {
            private Calendar calendar = this.calendar;

            @Override
            public void onClick(View view) {
                String vet = spinnerVet.getSelectedItem().toString();
                String pet = spinnerPet.getSelectedItem().toString();
                String type = editTextType.getText().toString();
                String remarks = editTextRemarks.getText().toString();
                String date = editTextDate.getText().toString();
                Calendar calendar = new GregorianCalendar(year, month, day, hour, min);
                Timestamp timestampDate = new Timestamp(calendar.getTime());

                // Verificar que se hayan completado todos los campos
                if (vet.equals("Select a vet...") || pet.equals("Select a pet...") || type.isEmpty() || remarks.isEmpty() || date.isEmpty()) {
                    // Mostrar un mensaje de error indicando que se deben completar todos los campos
                    Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.collection("user").whereEqualTo("email", vet)
                        .whereEqualTo("userType", "Vet")
                        .limit(1)
                        .get()
                        .addOnCompleteListener(task -> {
                            String vetId = task.getResult().getDocuments().get(0).getId();
                            Map<String, Object> appointmentData = new HashMap<>();
                            appointmentData.put("userId", currentUser.getUid());
                            appointmentData.put("vet", vetId);
                            appointmentData.put("pet", pet);
                            appointmentData.put("type", type);
                            appointmentData.put("remarks", remarks);
                            appointmentData.put("date", timestampDate);

                            // Obtener una referencia a la colección "appointment"
                            CollectionReference appointmentRef = db.collection("appointment");

                            // Subir la información a Firestore
                            appointmentRef.add(appointmentData)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Toast.makeText(getContext(), "Appointment created successfully", Toast.LENGTH_SHORT).show();
                                            clearFields();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Mostrar un mensaje de error indicando que ha ocurrido un problema al crear la cita
                                            Toast.makeText(getContext(), "Failed to create appointment", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });

            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearFields();
            }
        });
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("user");
        petRef = db.collection("pet");
        usersRef.whereEqualTo("userType", "Vet")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> vetEmailList = new ArrayList<>();
                        vetEmailList.add("Select a vet...");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String email = document.getString("email");
                            vetEmailList.add(email);
                        }

                        // Aquí debes utilizar el ArrayList vetEmailList para llenar el Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, vetEmailList);
                        spinnerVet.setAdapter(adapter);
                    } else {
                        // Manejar el caso de error en la consulta
                    }
                });
        petRef.whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        List<String> petNameList = new ArrayList<>();
                        petNameList.add("Select a pet...");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                                petNameList.add(name);

                        }

                        // Aquí debes utilizar el ArrayList vetEmailList para llenar el Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, petNameList);
                        spinnerPet.setAdapter(adapter);
                    }
                });
        editTextDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) showDatePickerDialog();
            }
        });
        return root;
    }

    private void clearFields() {
        editTextRemarks.setText("");
        editTextType.setText("");
        editTextDate.setText("");
        spinnerVet.setSelection(0);
        spinnerPet.setSelection(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        this.hour = calendar.get(Calendar.HOUR_OF_DAY);
        this.min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), this, hour, min, DateFormat.is24HourFormat(requireContext()));
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String dateTime = dateFormat.format(calendar.getTime());

        editTextDate.setText(dateTime);
    }
}