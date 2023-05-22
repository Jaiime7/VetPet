package com.insitutosanjuandelacruz.vetpet.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Appointment {
    private String date;
    private String hour;
    private String clientId;
    private String veterinaryId;
    private String petId;
    private String type;
    private int duration;
    private String remarks;

    // Constructor
    public Appointment(String date, String hour, String clientId, String veterinaryId, String petId,
                       String type, int duration, String remarks) {
        this.date = date;
        this.hour = hour;
        this.clientId = clientId;
        this.veterinaryId = veterinaryId;
        this.petId = petId;
        this.type = type;
        this.duration = duration;
        this.remarks = remarks;
    }

    // Getters and Setters

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getVeterinaryId() {
        return veterinaryId;
    }

    public void setVeterinaryId(String veterinaryId) {
        this.veterinaryId = veterinaryId;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    // Método para guardar la cita en Firebase
    public void saveToFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference appointmentsRef = database.getReference("appointments");

        // Generar una clave única para la cita
        String appointmentId = appointmentsRef.push().getKey();

        // Guardar los datos de la cita en la base de datos
        appointmentsRef.child(appointmentId).setValue(this);
    }
}
