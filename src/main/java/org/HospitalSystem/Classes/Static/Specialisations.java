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
    public static int toID(String name){
        return switch (name.toLowerCase()) {
            case "gp" -> 0;
            case "cardio" -> 1;
            case "neuro" -> 2;
            case "ortho" -> 3;
            case "derma" -> 4;
            case "pedia" -> 5;
            default -> -1;
        };
    }
}
