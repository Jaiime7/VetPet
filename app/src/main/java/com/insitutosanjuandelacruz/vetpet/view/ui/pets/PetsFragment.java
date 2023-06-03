package com.insitutosanjuandelacruz.vetpet.view.ui.pets;

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
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentMeBinding;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentPetsBinding;
import com.insitutosanjuandelacruz.vetpet.view.ui.me.MeViewModel;

public class PetsFragment extends Fragment {

    private FragmentPetsBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PetsViewModel petsViewModel = new ViewModelProvider(this).get(PetsViewModel.class);
        binding = FragmentPetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textPets;
        petsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}