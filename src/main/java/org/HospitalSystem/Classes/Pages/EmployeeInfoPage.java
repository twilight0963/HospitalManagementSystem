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
import org.HospitalSystem.Classes.Components.SpecialisationPanel;
import org.HospitalSystem.Classes.Components.StatusPanel;
import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Doctor;
import org.HospitalSystem.Classes.Static.DBService.CurrentUserService;
import org.HospitalSystem.Classes.Static.DBService.PatientService;

public final class EmployeeInfoPage extends JPanel {
    private final JPanel infoContent;
    private final int remainingWidth;
    private final JLabel titleLabel;
    private final DatabaseManager db;
    private Timer refreshTimer;
    private Doctor currentDoctor;

    public void refreshInfo() {
        currentDoctor = CurrentUserService.getInfo(db);
        if (currentDoctor != null) {
            infoContent.removeAll();
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH;
            gbc.weightx = 1.0;
            gbc.weighty = 0.1;

            // Re-add title
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.insets = new java.awt.Insets(10, 10, 20, 10);
            infoContent.add(titleLabel, gbc);

            // Reset constraints for info panels
            gbc.gridwidth = 1;
            gbc.insets = new java.awt.Insets(10, 10, 10, 10);
            gbc.weighty = 1;

            // Update info panels
            String[][] infoPanels = {
                {"Employee ID", String.valueOf(currentDoctor.id)},
                {"Name", currentDoctor.full_name},
                {"Specialisation", currentDoctor.getSpecialisation()},
                {"Active Patients", String.valueOf(PatientService.myPatients(db).length)},
            };

            for (int row = 1; row < 3; row++) {
                for (int col = 0; col < 2; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;
                    
                    int index = (row-1) * 3 + col;
                    if (index < infoPanels.length) {
                        StatusPanel panel = new StatusPanel(
                            (int)(remainingWidth/3.2),
                            infoPanels[index][0],
                            infoPanels[index][1]
                        );
                        infoContent.add(panel, gbc);
                    }
                }
            }
            
            // Re-add specialisation panel
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            SpecialisationPanel specialisationPanel = new SpecialisationPanel(
                (int)(remainingWidth/1.6),
                db,
                currentDoctor
            );
            infoContent.add(specialisationPanel, gbc);

            infoContent.revalidate();
            infoContent.repaint();
        }
    }

    public EmployeeInfoPage(JFrame root, JPanel navigatorPanel, DatabaseManager db, int width, int height) {
        this.db = db;
        this.remainingWidth = width - width/5;
        
        setLayout(new BorderLayout());
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(Color.decode("#f0f0f0"));
        setOpaque(true);
        
        // Create and add the dashboard drawer
        DashboardDrawer drawer = new DashboardDrawer(root, navigatorPanel, width/5);
        add(drawer, BorderLayout.WEST);

        // Create and add the main content area
        JPanel mainContent = new JPanel(new BorderLayout(10,10));
        this.infoContent = new JPanel(new GridBagLayout());
        this.titleLabel = new JLabel();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1;

        // Add title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new java.awt.Insets(10, 10, 20, 10);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.LEFT);
        titleLabel.setForeground(Color.WHITE);
        infoContent.add(titleLabel, gbc);

        // Get doctor info
        currentDoctor = CurrentUserService.getInfo(db);
        if (currentDoctor != null) {
            titleLabel.setText("Employee Information");

            // Reset constraints for info panels
            gbc.gridwidth = 1;
            gbc.insets = new java.awt.Insets(10, 10, 10, 10);
            gbc.weighty = 1;

            // Add doctor information panels
            String[][] infoPanels = {
                {"Employee ID", String.valueOf(currentDoctor.id)},
                {"Name", currentDoctor.full_name},
                {"Specialisation", currentDoctor.getSpecialisation()},
                {"Active Patients", String.valueOf(PatientService.myPatients(db).length)}, // You can add a method to count active patients
            };

            for (int row = 1; row < 3; row++) {
                for (int col = 0; col < 2; col++) {
                    gbc.gridx = col;
                    gbc.gridy = row;
                    
                    int index = (row-1) * 3 + col;
                    if (index < infoPanels.length) {
                        StatusPanel panel = new StatusPanel(
                            (int)(remainingWidth/3.2),
                            infoPanels[index][0],
                            infoPanels[index][1]
                        );
                        infoContent.add(panel, gbc);
                    }
                }
            }
            gbc.gridx = 1;
            gbc.gridy = 2;
            SpecialisationPanel specialisationPanel = new SpecialisationPanel(
                (int)(remainingWidth/1.7), 
                db,
                currentDoctor
            );
            infoContent.add(specialisationPanel, gbc);
        }

        mainContent.add(infoContent, BorderLayout.CENTER);
        infoContent.setBackground(Color.decode("#8abcd1"));
        mainContent.setOpaque(true);
        add(mainContent, BorderLayout.CENTER);

        // Start refresh timer (updates every 5 seconds)
        refreshTimer = new Timer(5000, _ -> refreshInfo());
        refreshTimer.start();
    }

    public EmployeeInfoPage(JFrame root, JPanel navigatorPanel, DatabaseManager db) {
        this(root, navigatorPanel, db, 1280, 720);
    }
}
