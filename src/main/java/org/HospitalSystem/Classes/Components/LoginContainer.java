package org.HospitalSystem.Classes.Components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Exceptions.AuthError;
import org.HospitalSystem.Classes.Static.DBService.AuthService;
import org.HospitalSystem.HMS;

public class LoginContainer extends JPanel {
    
    public LoginContainer(JFrame root, int width, int height, DatabaseManager dbManager, JPanel navigatorPanel) {
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setOpaque(true);
        setLayout(new GridBagLayout());
        
        // Create a panel for the form contents
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Add components to the form panel
        JLabel usernameLabel = new JLabel("User ID:");
        NumberField usernameField = new NumberField(1, 999999, 20);
        usernameField.setPreferredSize(new Dimension(200, 30));
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(200, 30));
        HoverButton loginButton = new HoverButton("Login", "login.png", 100, 30);

        loginButton.addActionListener(_ -> {
            int userid = usernameField.getValue().intValue();
            String password = String.valueOf(passwordField.getPassword());
            if (userid < 1 || password.isEmpty()) {
                System.out.println("Please fill in all fields.");
                return;
            }
            try {
                if (AuthService.login(userid, password, dbManager)) {
                    // Clear the fields
                    usernameField.setText("");
                    passwordField.setText("");
                    System.out.println("Login successful!");

                    // Proceed to the next page
                    CardLayout navigator = (CardLayout)navigatorPanel.getLayout();
                    navigator.show(navigatorPanel, "dashboard");
                    root.dispose();
                    HMS.showDashboard();
                }
            } catch (AuthError e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JLabel orLabel = new JLabel("-- or --");
        orLabel.setHorizontalAlignment(JLabel.CENTER);
        orLabel.setForeground(Color.GRAY);


        HoverButton registerButton = new HoverButton("Register", "signup.png", 100, 30);
        registerButton.addActionListener(_ -> {
            // Clear the fields
            usernameField.setText("");
            passwordField.setText("");
            // Proceed to the registration page
            CardLayout navigator = (CardLayout)navigatorPanel.getLayout();
            navigator.show(navigatorPanel, "register");
            root.pack();
        });
        
        // Setup constraints
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 5, 5, 5);
        
        // Username row
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        // Password row
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        // Login button row
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(orLabel, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(registerButton, gbc);
        
        // Add the form panel to the container
        add(formPanel);
    }
}
