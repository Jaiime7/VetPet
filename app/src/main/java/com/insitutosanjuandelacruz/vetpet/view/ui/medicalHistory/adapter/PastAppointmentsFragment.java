package com.insitutosanjuandelacruz.vetpet.view.ui.medicalHistory.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentAppointmentsBinding;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentHistoryMedicalBinding;

import java.util.ArrayList;
import java.util.List;

public class PastAppointmentsFragment extends Fragment {

    private TextView pastAppointmentsTextView;
    private FirebaseFirestore db;
    private CollectionReference appointmentRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_past_appointments, container, false);
        pastAppointmentsTextView = view.findViewById(R.id.past_appointments_text_view);

        db = FirebaseFirestore.getInstance();
        appointmentRef = db.collection("appointment");

        loadPastAppointments();

        return view;
    }

    private void loadPastAppointments() {
        // Obtener la fecha actual
        long currentTimeMillis = System.currentTimeMillis();

        // Consultar las citas pasadas (con fecha anterior a la actual)
        Query query = appointmentRef.whereLessThan("date", currentTimeMillis);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> pastAppointments = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String appointmentType = document.getString("type");
                    String appointmentDate = document.getString("date");
                    // Agregar los datos a la lista de citas pasadas
                    pastAppointments.add(appointmentType + " - " + appointmentDate);
                }
                // Mostrar las citas pasadas en el TextView
                pastAppointmentsTextView.setText(pastAppointments.toString());
            } else {
                // Manejar el caso de error en la consulta
            }
        });
    }
}
