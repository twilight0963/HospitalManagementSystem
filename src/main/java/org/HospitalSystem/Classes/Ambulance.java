package org.HospitalSystem.Classes;

public final class Ambulance {
    public int id;
    public String location;
    public String status;
    public int doctor_id;
    public int patient_id;

    public Ambulance(int id, String location, String status) {
        this.id = id;
        this.location = location;
        this.status = status;
        this.doctor_id = 0;  // Default to 0 when no doctor assigned
        this.patient_id = 0; // Default to 0 when no patient assigned
    }

    public Ambulance setDoctor(int doctor_id) {
        this.doctor_id = doctor_id;
        return this;
    }

    public Ambulance setPatient(int patient_id) {
        this.patient_id = patient_id;
        return this;
    }

    public Ambulance clearAssignments() {
        this.doctor_id = 0;
        this.patient_id = 0;
        return this;
    }

    public boolean isAvailable() {
        return status.equals("Available") && doctor_id == 0 && patient_id == 0;
    }
}