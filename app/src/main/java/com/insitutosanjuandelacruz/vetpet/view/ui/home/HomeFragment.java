package com.insitutosanjuandelacruz.vetpet.view.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.ktx.Firebase;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentHomeBinding;
import com.insitutosanjuandelacruz.vetpet.view.ui.home.decorator.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MaterialCalendarView calendarView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseFirestore db;
    private List<CalendarDay> reservedDates = new ArrayList<>();
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        calendarView = binding.calendarView;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUser = mAuth.getCurrentUser();


        // Consultar los documentos de la colección "appointment" para el usuario actual
        db.collection("appointment")
                .whereEqualTo("userId", currentUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Extraer la fecha del documento y añadirla a la lista de fechas reservadas
                            Timestamp timestamp = document.getTimestamp("date");
                            if (timestamp != null) {
                                Date date = timestamp.toDate();
                                CalendarDay calendarDay = CalendarDay.from(date);
                                reservedDates.add(calendarDay);
                            }
                        }
                        // Añadir el decorador al calendario con las fechas reservadas
                        calendarView.addDecorator(new EventDecorator(Color.RED, reservedDates));
                    } else {
                        // Manejar el caso de error en la consulta
                    }
                });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}