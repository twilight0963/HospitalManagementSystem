package org.HospitalSystem.Classes.Static;

import java.awt.Image;

import org.HospitalSystem.Classes.Components.PathImageIcon;
public final class SharedFunctions {

    
    public static PathImageIcon resizeIcon(PathImageIcon icon, int width, int height) {
        Image img = icon.getImage(); // Extract the Image from the ImageIcon
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH); 
        icon.setImage(resizedImage);
        return icon; // Return resized ImageIcon
    }
    public static PathImageIcon resizeIcon(PathImageIcon icon){
        return resizeIcon(icon, 100, 100);
    }
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return original string if null or empty
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    public static String firstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str; // Return original string if null or empty
        }
        return str.substring(0, 1).toUpperCase() + ".";
    }
}
