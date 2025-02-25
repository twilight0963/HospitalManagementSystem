package hms.Classes;

import javax.swing.ImageIcon;

public class Doctor {
    private final String name;
    private final String bio;
    private final ImageIcon pfp; 
    public Doctor(String bio, String firstName, String lastName, String imgPath){
        this.pfp = new ImageIcon(imgPath);
        this.name = String.format("Dr. %s %s",firstName,lastName);
        this.bio = bio;
    }
    public ImageIcon getImage(){
        return this.pfp;
    }
    public String getName(){
        return this.name;
    }
    public String getBio(){
        return this.bio;
    }
}
