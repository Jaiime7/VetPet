package com.insitutosanjuandelacruz.vetpet.view.ui.pets;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.insitutosanjuandelacruz.vetpet.databinding.FragmentPetsBinding;
import com.insitutosanjuandelacruz.vetpet.model.Pet;
import com.insitutosanjuandelacruz.vetpet.view.ui.pets.adapter.PetsAdapter;
import com.insitutosanjuandelacruz.vetpet.view.ui.pets.adder.AddPetActivity;

import java.util.ArrayList;
import java.util.List;

public class PetsFragment extends Fragment {

    private FragmentPetsBinding binding;
    private PetsAdapter petsAdapter;
    private List<Pet> petList;
    private FirebaseUser mCurrentUser;

    FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PetsViewModel petsViewModel = new ViewModelProvider(this).get(PetsViewModel.class);
        binding = FragmentPetsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configure RecyclerView
        RecyclerView recyclerView = binding.recyclerViewPets;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petList = new ArrayList<>();
        petsAdapter = new PetsAdapter(petList);
        recyclerView.setAdapter(petsAdapter);

        // Fetch pets from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference petsRef = db.collection("pet");
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        String userId = mCurrentUser.getUid(); // Replace with appropriate method to get the current user's ID

        petsRef.whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Pet pet = document.toObject(Pet.class);
                            petList.add(pet);
                        }
                        petsAdapter.notifyDataSetChanged();
                    } else {
                    }
                });
        binding.btnAddPet.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddPetActivity.class);
            startActivity(intent);
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.with(this).pauseRequests();
        Glide.with(this).onDestroy();
        binding = null;
    }

}
