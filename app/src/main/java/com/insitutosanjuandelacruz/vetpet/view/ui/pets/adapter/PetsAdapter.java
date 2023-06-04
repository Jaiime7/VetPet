package com.insitutosanjuandelacruz.vetpet.view.ui.pets.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.Timestamp;
import com.insitutosanjuandelacruz.vetpet.R;
import com.insitutosanjuandelacruz.vetpet.model.Pet;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    private List<Pet> petList;

    public PetsAdapter(List<Pet> petList) {
        this.petList = petList;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        Pet pet = petList.get(position);
        holder.bind(pet);
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    static class PetViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPet;
        private TextView textViewName;
        private TextView textViewAge;
        private TextView textViewSpecies;

        public PetViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPet = itemView.findViewById(R.id.imageViewPet);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAge = itemView.findViewById(R.id.textViewPetAge);
            textViewSpecies = itemView.findViewById(R.id.textViewSpecies);
        }

        public void bind(Pet pet) {
            // Set the data to the views
            textViewName.setText(pet.getName());
            textViewAge.setText(calculateAge(pet.getBirthdate()));
            textViewSpecies.setText(pet.getSpecies());

            // Load the pet image using Glide or any other image loading library
            Glide.with(itemView)
                    .load(pet.getPetImage())
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageViewPet);
        }

        private String calculateAge(Timestamp birthdate) {
            // Calculate the age based on the birthdate
            return "Age: " ; // Implement the logic to calculate age
        }
    }
}