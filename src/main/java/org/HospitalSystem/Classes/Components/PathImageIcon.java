package org.HospitalSystem.Classes.Components;

import javax.swing.ImageIcon;

public class PathImageIcon extends ImageIcon{
    private final String path;
    public PathImageIcon(String p){
        super(p);
        this.path = p;
    }
    public PathImageIcon resize(int width, int height){
        ImageIcon resizedIcon = new ImageIcon(this.getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
        this.setImage(resizedIcon.getImage());
        return this;
    }

    // For square icons
    public PathImageIcon resize(int size){
        return this.resize(size, size);
    }
    public String getPath(){
        return this.path;
    }
}
