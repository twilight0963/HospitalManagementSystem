package org.HospitalSystem.Classes.Pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.Components.ImagePanel;
import org.HospitalSystem.Classes.Components.RegisterContainer;
import org.HospitalSystem.Classes.DatabaseManager;

public class RegisterPage extends JPanel {
    public RegisterPage(JFrame root, JPanel navigatorPanel, int width, int height, DatabaseManager db) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Login container on left
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new RegisterContainer(root, width/2, height, db, navigatorPanel), gbc);
        
        // Image panel on right
        gbc.gridx = 1;
        add(new ImagePanel("dr.png", width/2, height), gbc);
        
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
    }
    public RegisterPage(JFrame dashboard, JPanel navigatorPanel, DatabaseManager db) {
        this(dashboard, navigatorPanel, 700, 350, db);
    }
}
