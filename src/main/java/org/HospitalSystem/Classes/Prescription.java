package org.HospitalSystem.Classes;

import java.time.LocalDateTime;

public final class Prescription {
    public int id;
    public int patient_id;
    public int medicine_id;
    public int doctor_id;
    public double dosage;
    public LocalDateTime end_date;
    public String medicineName; // For display purposes

    public Prescription(int id, int patient_id, int medicine_id, int doctor_id, 
                       double dosage, LocalDateTime end_date) {
        this.id = id;
        this.patient_id = patient_id;
        this.medicine_id = medicine_id;
        this.doctor_id = doctor_id;
        this.dosage = dosage;
        this.end_date = end_date;
    }

    public boolean isActive() {
        return LocalDateTime.now().isBefore(end_date);
    }
}