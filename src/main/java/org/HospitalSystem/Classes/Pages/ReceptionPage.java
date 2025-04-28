package org.HospitalSystem.Classes.Pages;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.HospitalSystem.Classes.Components.DashboardDrawer;
import org.HospitalSystem.Classes.Components.StatusPanel;
import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Static.DBService.AmbulanceService;
import org.HospitalSystem.Classes.Static.DBService.CurrentUserService;
import org.HospitalSystem.Classes.Static.DBService.PatientService;
import org.HospitalSystem.Classes.Static.DBService.RoomService;
import org.HospitalSystem.Classes.Static.DBService.UserAddService;

public final class ReceptionPage extends JPanel {
    private final JPanel statusContent;
    private final int remainingWidth;
    private final JLabel titleLabel;
    private final DatabaseManager db;
    private Timer refreshTimer;

    public void refreshStatistics() {
        statusContent.removeAll();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;

        // Re-add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new java.awt.Insets(10, 10, 20, 10);
        statusContent.add(titleLabel, gbc);

        // Re-add status panels
        gbc.gridwidth = 1;
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);
        gbc.weighty = 1;
        for (int row = 1; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                
                String title = "";
                switch ((row-1) * 3 + col) {
                    case 0 -> title = "Patient Count";
                    case 1 -> title = "Occupied Rooms";
                    case 2 -> title = "Available Rooms";
                    case 3 -> title = "Employees";
                    case 4 -> title = "Ambulances";
                    case 5 -> title = "Critical Patients";
                }
                
                int status = 0;
                switch ((row-1) * 3 + col) {
                    case 0 -> status = PatientService.patientCount(db);
                    case 1 -> status = RoomService.getOccupiedRoomCount(db);
                    case 2 -> status = RoomService.getFreeRoomCount(db);
                    case 3 -> status = UserAddService.getUserCount(db);
                    case 4 -> status = AmbulanceService.getAllAmbulances(db).length;
                    case 5 -> status = PatientService.criticalCount(db);
                }

                statusContent.add(new StatusPanel(
                    (int)(remainingWidth/3.2), 
                    title, 
                    String.valueOf(status)
                ), gbc);
            }
        }
        
        statusContent.revalidate();
        statusContent.repaint();
    }

    public ReceptionPage(JFrame root, JPanel navigatorPanel, DatabaseManager db, int width, int height) {
        this.db = db;
        this.remainingWidth = width - width/5;
        
        setLayout(new java.awt.BorderLayout());
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(java.awt.Color.decode("#f0f0f0"));
        setOpaque(true);
        
        // Create and add the dashboard drawer
        DashboardDrawer drawer = new DashboardDrawer(root, navigatorPanel, width/5);
        add(drawer, BorderLayout.WEST);

        // Create and add the main content area
        JPanel mainContent = new JPanel(new BorderLayout(10,10));
        this.statusContent = new JPanel(new GridBagLayout());
        this.titleLabel = new JLabel("Welcome to Health Safari, Dr. " + CurrentUserService.getInfo(db).full_name + "!");
        
        // Configure GridBagConstraints properly
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;  // Fill both horizontally and vertically
        gbc.weightx = 1.0;  // Allow horizontal expansion
        gbc.weighty = 0.1;

        // Add status panels with proper constraints
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;  // Span all columns for the title
        gbc.insets = new java.awt.Insets(10, 10, 20, 10);  // More vertical padding for title
        titleLabel.setFont(new Font("Roboto", java.awt.Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setForeground(Color.WHITE);

        statusContent.add(titleLabel, gbc);
        gbc.gridwidth = 1;  // Reset gridwidth for the next components
        gbc.insets = new java.awt.Insets(10, 10, 10, 10);  // Equal padding all around
        gbc.weighty = 1;
        for (int row = 1; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                gbc.gridx = col;
                gbc.gridy = row;
                
                String title = "";
                switch ((row-1) * 3 + col) {
                    case 0 -> title = "Patient Count";
                    case 1 -> title = "Occupied Rooms";
                    case 2 -> title = "Avalaible Rooms";
                    case 3 -> title = "Employees";
                    case 4 -> title = "Ambulances";
                    case 5 -> title = "Critical Patients";
                }
                
                int status = 0;
                switch ((row-1) * 3 + col) {
                    case 0 -> status = PatientService.patientCount(db);
                    case 1 -> status = RoomService.getOccupiedRoomCount(db);
                    case 2 -> status = RoomService.getFreeRoomCount(db);
                    case 3 -> status = UserAddService.getUserCount(db);
                    case 4 -> status = AmbulanceService.getAllAmbulances(db).length;
                    case 5 -> status = PatientService.criticalCount(db);
                }

                statusContent.add(new StatusPanel(
                    (int)(remainingWidth/3.2), 
                    title, 
                    String.valueOf(status)
                ), gbc);
            }
        }

        mainContent.add(statusContent, BorderLayout.CENTER);

        statusContent.setBackground(java.awt.Color.decode("#8abcd1"));
        mainContent.setOpaque(true);
        add(mainContent, java.awt.BorderLayout.CENTER);

        // Start refresh timer (updates every 5 seconds)
        refreshTimer = new Timer(5000, _ -> refreshStatistics());
        refreshTimer.start();
    }
    
    public ReceptionPage(JFrame dashboard, JPanel navigatorPanel, DatabaseManager db) {
        this(dashboard, navigatorPanel, db, 1280, 720);
    }
}
