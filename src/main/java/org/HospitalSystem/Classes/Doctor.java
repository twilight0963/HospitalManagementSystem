package org.HospitalSystem.Classes;


import org.HospitalSystem.Classes.Components.PathImageIcon;
import org.HospitalSystem.Classes.Exceptions.AuthError;
import org.HospitalSystem.Classes.Static.Specialisations;

public class Doctor extends User{
    private int specialisation = -1;

    public Doctor(int ID, String pass, String fName, String lName, PathImageIcon pfp) {
        super(ID, pass, fName, lName, pfp);
    }
    public Doctor(int ID, String pass, String fName, String lName) {
        super(ID, pass, fName, lName);
    }
    public void specialise(int specialID, String pass) throws AuthError{
        if (checkPass(pass)){
            this.specialisation = specialID;
        }
    }
    public void specialise(String special, String pass) throws AuthError{
        if (checkPass(pass)){
            this.specialisation = Specialisations.toID(special);
        }
    }
    public String getSpecialisation(){
            return Specialisations.toName(this.specialisation);
    }
    public int getSpecialisationID(String pass) throws AuthError{
        if (checkPass(pass)){
            return this.specialisation;
        }
        return -1;
    }
}
