package org.HospitalSystem.Classes.Static;


public final class Specialisations {
    public static String toName(int id){
        return switch (id) {
            case 0 -> "General Practitioner";
            case 1 -> "Cardiologist";
            case 2 -> "Neurologist";
            case 3 -> "Orthopedic Surgeon";
            case 4 -> "Dermatologist";
            case 5 -> "Pediatrician";
            default -> "-";
        };
    }



    public static int toID(String name) {
        return switch (name) {
            case "General Practitioner" -> 0;
            case "Cardiologist" -> 1;
            case "Neurologist" -> 2;
            case "Orthopedic Surgeon" -> 3;
            case "Dermatologist" -> 4;
            case "Pediatrician" -> 5;
            default -> -1;
        };
    }
}
