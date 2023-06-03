package com.insitutosanjuandelacruz.vetpet.view.ui.appointments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentAppointmentsBinding;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentMeBinding;
import com.insitutosanjuandelacruz.vetpet.view.ui.me.MeViewModel;

public class AppointmentsFragment extends Fragment {
    private FragmentAppointmentsBinding binding;

    public static AppointmentsFragment newInstance() {
        return new AppointmentsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AppointmentsViewModel appointmentsViewModel = new ViewModelProvider(this).get(AppointmentsViewModel.class);
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textAppointments;
        appointmentsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}