package org.HospitalSystem.Classes.Components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath, int width, int height) {
        
        try {
            // Load and scale the image
            Image originalImage = ImageIO.read(new File("src/main/java/org/HospitalSystem/Resources/" + imagePath));
            this.image = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.err.println("Error loading image: " + imagePath);
        }
        
        // Set the panel's preferred size
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}
