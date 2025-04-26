package org.HospitalSystem.Classes;

import java.time.LocalDateTime;

public final class Appointment {
    public int id;
    public int doctor_id;
    public int patient_id;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public String description;

    public Appointment(int id, int doctor_id, int patient_id, LocalDateTime startTime, 
                      LocalDateTime endTime, String description) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(endTime);
    }
}