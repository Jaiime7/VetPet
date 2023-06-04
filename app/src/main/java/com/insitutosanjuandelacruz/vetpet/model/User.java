package com.insitutosanjuandelacruz.vetpet.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {
    private String name;
    private String birthdate;
    private String gender;
    private String email;
    private String userType;
    private String address;
    private String profileImage;

    // Constructor
    public User(String name, String birthdate, String gender, String email, String password,
                String userType, String address, String profileImage) {
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.email = email;
        this.userType = userType;
        this.address = address;
        this.profileImage = profileImage;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
