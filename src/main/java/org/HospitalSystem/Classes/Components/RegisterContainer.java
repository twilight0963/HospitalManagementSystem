package org.HospitalSystem.Classes.Components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Static.DBService.UserAddService;

public class RegisterContainer extends JPanel {
    
    public RegisterContainer(JFrame root, int width, int height, DatabaseManager dbManager, JPanel navigatorPanel) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new GridBagLayout());
        
        // Create a panel for the form contents
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add components to the form panel
        JLabel fNameLabel = new JLabel("First Name:");
        JTextField fnameField = new JTextField(20);
        fnameField.setPreferredSize(new Dimension(200, 30));
        JLabel lNameLabel = new JLabel("Last Name:");
        JTextField lnameField = new JTextField(20);
        lnameField.setPreferredSize(new Dimension(200, 30));
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        HoverButton signupButton = new HoverButton("Sign Up", "signup.png", 100, 30, "#6ca5aa", "#4A7E85");
        HoverButton backButton = new HoverButton("I already have an account.", 100, 30);

        signupButton.addActionListener(_ -> {
            String password = String.valueOf(passwordField.getPassword());
            String fname = fnameField.getText();
            String lname = lnameField.getText();
            if (password.isEmpty() || fname.isEmpty() || lname.isEmpty()) {
                System.out.println("Please fill in all fields.");
                return;
            }
            int doctor_id = UserAddService.addDoctor(fname, lname, password, dbManager);
            if (doctor_id > 0) {
                System.out.println("User added successfully.");
                // Clear the fields
                fnameField.setText("");
                lnameField.setText("");
                passwordField.setText("");
                CardLayout navigator = (CardLayout)navigatorPanel.getLayout();
                navigator.show(navigatorPanel, "login");
                root.pack();
                JOptionPane.showMessageDialog(this, "User added successfully. Please log in with ID " + String.valueOf(doctor_id) + ".", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "User already exists. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(_ -> {
            CardLayout navigator = (CardLayout)navigatorPanel.getLayout();
            navigator.show(navigatorPanel, "login");
            root.pack();
        });
        
        
        // Setup constraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);

        // First Name row
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(fNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(fnameField, gbc);

        // Last Name row
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lNameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(lnameField, gbc);
        
        // Password row
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        // Sign up button row
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(signupButton, gbc);
        
        // Padding row
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        formPanel.add(Box.createVerticalStrut(15), gbc);

        // Login button row
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(backButton, gbc);


        // Add the form panel to the container
        add(formPanel);
    }
}
