package com.insitutosanjuandelacruz.vetpet.model;

public class Pet {
    private String name;
    private String species;
    private String race;
    private String birthdate;
    private String gender;
    private double weight;
    private String userId;
    private String petImage;

    // Constructor
    public Pet(String name, String species, String race, String birthdate, String gender, double weight,
               String userId, String petImage) {
        this.name = name;
        this.species = species;
        this.race = race;
        this.birthdate = birthdate;
        this.gender = gender;
        this.weight = weight;
        this.userId = userId;
        this.petImage = petImage;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
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
}
