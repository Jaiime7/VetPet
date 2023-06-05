package com.insitutosanjuandelacruz.vetpet.view.ui.pets.adapter;

import android.content.Intent;
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
import com.insitutosanjuandelacruz.vetpet.view.ui.pets.editor.EditPetActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    private List<Pet> petList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la mascota seleccionada
                Pet selectedPet = petList.get(holder.getAdapterPosition());

                // Crear un Intent para abrir el Activity de edición de mascotas
                Intent intent = new Intent(v.getContext(), EditPetActivity.class);
                intent.putExtra("petId", selectedPet.getId()); // Pasar el ID de la mascota seleccionada al Activity
                v.getContext().startActivity(intent);
            }
        });
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
            textViewAge.setText((calculateAge(pet.getBirthdate())));
            textViewSpecies.setText(pet.getSpecie());


            Glide.with(itemView)
                    .load("gs://androiddb-d2083.appspot.com/pet/default_pet_image.png")
                    .placeholder(R.drawable.placeholder_image)
                    .into(imageViewPet);
        }

        private String calculateAge(Timestamp birthdate) {
            Date birthdateDate = birthdate.toDate();
            Calendar birthdateCalendar = Calendar.getInstance();
            birthdateCalendar.setTime(birthdateDate);

            Calendar currentCalendar = Calendar.getInstance();

            int years = currentCalendar.get(Calendar.YEAR) - birthdateCalendar.get(Calendar.YEAR);
            int months = currentCalendar.get(Calendar.MONTH) - birthdateCalendar.get(Calendar.MONTH);
            int days = currentCalendar.get(Calendar.DAY_OF_MONTH) - birthdateCalendar.get(Calendar.DAY_OF_MONTH);

            if (months < 0 || (months == 0 && days < 0)) {
                years--;
            }

            return years+" years";
        }
    }
}