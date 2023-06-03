package com.insitutosanjuandelacruz.vetpet.view.ui.medicalHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.insitutosanjuandelacruz.vetpet.databinding.FragmentHistoryMedicalBinding;

public class MedicalHistoryFragment extends Fragment {

    private FragmentHistoryMedicalBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MedicalHistoryViewModel medicalHistoryViewModel = new ViewModelProvider(this).get(MedicalHistoryViewModel.class);

        binding = FragmentHistoryMedicalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHistoryMedical;
        medicalHistoryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}