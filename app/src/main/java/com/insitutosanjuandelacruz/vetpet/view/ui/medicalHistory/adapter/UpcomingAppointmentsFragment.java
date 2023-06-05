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

import java.util.ArrayList;
import java.util.List;

public class UpcomingAppointmentsFragment extends Fragment {

    private TextView upcomingAppointmentsTextView;
    private FirebaseFirestore db;
    private CollectionReference appointmentRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);
        upcomingAppointmentsTextView = view.findViewById(R.id.upcoming_appointments_text_view);

        db = FirebaseFirestore.getInstance();
        appointmentRef = db.collection("appointment");

        loadUpcomingAppointments();

        return view;
    }

    private void loadUpcomingAppointments() {
        // Obtener la fecha actual
        long currentTimeMillis = System.currentTimeMillis();

        // Consultar las citas próximas (con fecha posterior a la actual)
        Query query = appointmentRef.whereGreaterThan("date", currentTimeMillis);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> upcomingAppointments = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Obtener los datos relevantes de la cita
                    String appointmentType = document.getString("type");
                    String appointmentDate = document.getString("date");
                    // Agregar los datos a la lista de citas próximas
                    upcomingAppointments.add(appointmentType + " - " + appointmentDate);
                }
                // Mostrar las citas próximas en el TextView
                upcomingAppointmentsTextView.setText(upcomingAppointments.toString());
            } else {
                // Manejar el caso de error en la consulta
            }
        });
    }
}
