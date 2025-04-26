package org.HospitalSystem.Classes;

import org.HospitalSystem.Classes.Components.PathImageIcon;

public class Patient extends User{
    public int room_id;
    public String status;
    public int prescription_id;

    public Patient(int ID, String pass, String fName, String lName, PathImageIcon pfp){
        super(ID, pass, fName, lName, pfp);
    }
    public Patient(int ID, String pass, String fName, String lName){
        super(ID, pass, fName, lName);
    }
}
