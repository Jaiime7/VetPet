package com.insitutosanjuandelacruz.vetpet.model;

import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Pet {
    private String name;
    private String specie;
    private String race;
    private Timestamp birthdate;
    private String gender;
    private double weight;
    private String userId;
    private String petImage;

    // Constructor
    public Pet(String name, String species, String race, Timestamp birthdate, String gender, double weight,
               String userId, String petImage) {
        this.name = name;
        this.specie = species;
        this.race = race;
        this.birthdate = birthdate;
        this.gender = gender;
        this.weight = weight;
        this.userId = userId;
        this.petImage = petImage;
    }
    public Pet() {
        // Constructor vacío
    }
    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return specie;
    }

    public void setSpecies(String species) {
        this.specie = species;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public Timestamp getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPetImage() {
        return petImage;
    }

    public void setPetImage(String petImage) {
        this.petImage = petImage;
    }

    // Método para guardar la mascota en Firebase
    public void saveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference petsRef = database.getReference("pets");

        // Generar una clave única para la mascota
        String petId = petsRef.push().getKey();

        // Guardar los datos de la mascota en la base de datos
        petsRef.child(petId).setValue(this);
    }
}
