package org.HospitalSystem.Classes.Components;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.HospitalSystem.Classes.Static.DBService.CurrentUserService;
import org.HospitalSystem.HMS;

public class DashboardDrawer extends JPanel {

    private static void switchPage(String page_name, JPanel navigatorPanel, CardLayout navigator, JFrame root) {
        // Switch to new page
        navigator.show(navigatorPanel, page_name);
        root.pack();
    }

    public DashboardDrawer(JFrame root, JPanel navigatorPanel, int width) {
        setPreferredSize(new java.awt.Dimension(width, 720));
        setBackground(java.awt.Color.decode("#4d869c"));
        setOpaque(true);
        setLayout(new java.awt.GridBagLayout());
        
        java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(10, 5, 10, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Add Dashboard button at top
        Font font = new Font("Roboto", Font.BOLD, 24);
        JLabel titleLabel = new JLabel("Health Safari", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(font);
        add(titleLabel, gbc);
        gbc.gridy++;
        HoverButton dashboardButton = new HoverButton("Dashboard", width-10, 50);
        dashboardButton.addActionListener(_ -> {
            // Handle button click
            System.out.println("Dashboard clicked");
            switchPage("dashboard", navigatorPanel, (CardLayout) navigatorPanel.getLayout(), root);
        });
        add(dashboardButton, gbc);
        
        // Create a panel for scrollable content
        JPanel scrollContent = new JPanel(new java.awt.GridBagLayout());
        scrollContent.setBackground(java.awt.Color.decode("#4d869c"));
        
        // Create buttons in scroll content
        java.awt.GridBagConstraints scrollGbc = new java.awt.GridBagConstraints();
        scrollGbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        scrollGbc.insets = new java.awt.Insets(10, 5, 10, 5);
        scrollGbc.gridx = 0;
        scrollGbc.gridy = 0;
        gbc.weightx = 1.0;

        CardLayout navigator = (CardLayout) navigatorPanel.getLayout();
        
        for (int i = 0; i < 8; i++) {
            final int index = i;
            String[] buttonLabels = {"Your Patients", "Rooms", "Employee info", 
                                    "Ambulance", "Appointments", "Pharmacy", "Prescriptions", "Log out"};
            scrollGbc.gridy++;
            HoverButton drawerButton = new HoverButton(buttonLabels[i], width-10, 50);
            drawerButton.addActionListener(_ -> {
                // Handle button click
                System.out.println(buttonLabels[index] + " clicked");
                switch (buttonLabels[index]) {
                    case "Your Patients" -> switchPage("patients", navigatorPanel, navigator, root);
                    case "Rooms" -> switchPage("rooms", navigatorPanel, navigator, root);
                    case "Departments" -> switchPage("departments", navigatorPanel, navigator, root);
                    case "Employee info" -> switchPage("employeeInfo", navigatorPanel, navigator, root);
                    case "Ambulance" -> switchPage("ambulance", navigatorPanel, navigator, root);
                    case "Appointments" -> switchPage("appointments", navigatorPanel, navigator, root);
                    case "Pharmacy" -> switchPage("pharmacy", navigatorPanel, navigator, root);
                    case "Prescriptions" -> switchPage("prescriptions", navigatorPanel, navigator, root);
                    case "Log out" -> {
                        CurrentUserService.logout();
                        root.dispose();
                        HMS.showLoginFrame();
                    }
                }
            });
            scrollContent.add(drawerButton, scrollGbc);
        }
        
        // Create scroll pane and add the content
        JScrollPane scrollPane = new JScrollPane(scrollContent);
        scrollContent.setPreferredSize(new java.awt.Dimension(width-20, 680));
        scrollPane.setPreferredSize(new java.awt.Dimension(width-20, 680)); // Adjust height to leave room for dashboard
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(java.awt.Color.decode("#4d869c"));
        
        // Add scroll pane below dashboard button with modified constraints
        gbc.gridy++;
        gbc.weighty = 1.0;  // Make scroll pane take up remaining vertical space
        gbc.fill = java.awt.GridBagConstraints.BOTH;  // Fill both horizontally and vertically
        add(scrollPane, gbc);
    }
}
