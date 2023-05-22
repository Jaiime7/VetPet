package com.insitutosanjuandelacruz.vetpet.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MedicalHistory {
    private String petId;
    private String date;
    private String veterinaryId;
    private String diagnosis;
    private String treatment;
    private String notes;

    // Constructor
    public MedicalHistory(String petId, String date, String veterinaryId, String diagnosis, String treatment,
                          String notes) {
        this.petId = petId;
        this.date = date;
        this.veterinaryId = veterinaryId;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    // Getters and Setters

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVeterinaryId() {
        return veterinaryId;
    }

    public void setVeterinaryId(String veterinaryId) {
        this.veterinaryId = veterinaryId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Método para guardar el historial médico en Firebase
    public void saveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference medicalHistoryRef = database.getReference("medicalHistory");

        // Generar una clave única para el historial médico
        String historyId = medicalHistoryRef.push().getKey();

        // Guardar los datos del historial médico en la base de datos
        medicalHistoryRef.child(historyId).setValue(this);
    }
}
