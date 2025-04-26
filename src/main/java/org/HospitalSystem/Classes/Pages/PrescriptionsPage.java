package org.HospitalSystem.Classes.Pages;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.Components.DashboardDrawer;
import org.HospitalSystem.Classes.Components.PrescriptionPatients;
import org.HospitalSystem.Classes.DatabaseManager;

public final class PrescriptionsPage extends JPanel {
    
    public PrescriptionsPage(JFrame root, JPanel navigatorPanel, DatabaseManager db, int width, int height) {
        setLayout(new BorderLayout());
        setPreferredSize(new java.awt.Dimension(width, height));
        setBackground(Color.decode("#f0f0f0"));
        
        // Create and add the dashboard drawer
        DashboardDrawer drawer = new DashboardDrawer(root, navigatorPanel, width/5);
        add(drawer, BorderLayout.WEST);
        
        // Add the prescriptions panel
        PrescriptionPatients prescriptionPanel = new PrescriptionPatients(root, db);
        add(prescriptionPanel, BorderLayout.CENTER);
    }

    public PrescriptionsPage(JFrame root, JPanel navigatorPanel, DatabaseManager db) {
        this(root, navigatorPanel, db, 1280, 720);
    }
}
