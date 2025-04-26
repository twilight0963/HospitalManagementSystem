package org.HospitalSystem.Classes;

public final class Medicine {
    public int id;
    public String name;
    public String icon_path;
    public double default_dosage;
    public double price;

    public Medicine(int id, String name, double default_dosage, double price) {
        this.id = id;
        this.name = name;
        this.icon_path = "src/main/java/org/HospitalSystem/Resources/default_meds.png";
        this.default_dosage = default_dosage;
        this.price = price;
    }
}