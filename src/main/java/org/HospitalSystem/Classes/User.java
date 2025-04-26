package org.HospitalSystem.Classes;

import org.HospitalSystem.Classes.Components.PathImageIcon;
import org.HospitalSystem.Classes.Exceptions.AuthError;
import org.HospitalSystem.Classes.Static.SharedFunctions;

public abstract class User {
    public final int id;
    protected String pass;
    private PathImageIcon picture;
    protected String first_name;
    protected String last_name;
    public String full_name;
    public String short_name;

    public User(int ID, String pass, String fName, String lName, PathImageIcon pfp){
        this.id = ID;
        this.pass = pass;
        this.full_name = SharedFunctions.capitalize(fName) + " " + SharedFunctions.capitalize(lName);
        this.short_name = SharedFunctions.firstLetter(fName) + " " + SharedFunctions.capitalize(lName);
        this.first_name = SharedFunctions.capitalize(fName);
        this.last_name = SharedFunctions.capitalize(lName);
        this.picture = SharedFunctions.resizeIcon(pfp);
    }

    public User(int ID, String pass, String fName, String lName){
        PathImageIcon pfp = new PathImageIcon("src/main/java/org/HospitalSystem/Resources/default_user.jpg");
        this.id = ID;
        this.pass = pass;
        this.full_name = SharedFunctions.capitalize(fName) + " " + SharedFunctions.capitalize(lName);
        this.short_name = SharedFunctions.firstLetter(fName) + " " + SharedFunctions.capitalize(lName);
        this.picture = SharedFunctions.resizeIcon(pfp);
    }
    public void changePass(String old_pass, String new_pass) throws AuthError{
        if (this.pass.equals(old_pass)){
            this.pass = new_pass;
        }else{
            throw new AuthError();
        }
    }
    public boolean checkPass(String pass) throws AuthError{
        if (this.pass.equals(pass)){
            return true;
        }else{
            throw new AuthError();
        }
    }

    public void changePic(String pass, PathImageIcon new_pfp) throws AuthError{
        if (checkPass(pass)){
            this.picture = SharedFunctions.resizeIcon(new_pfp);
        }
    }

    public PathImageIcon getPic(String pass) throws AuthError{
        if (checkPass(pass)){
            return this.picture;
        }
            return null;
    }

    public void updateName(String fName, String lName, String password) throws AuthError{
        if (checkPass(pass)){
            this.full_name = SharedFunctions.capitalize(fName) + " " + SharedFunctions.capitalize(lName);
            this.short_name = SharedFunctions.firstLetter(fName) + " " + SharedFunctions.capitalize(lName);
        }
    }
    // abstract public void saveInfo(DatabaseManager db) throws SQLException, AuthError;
}
