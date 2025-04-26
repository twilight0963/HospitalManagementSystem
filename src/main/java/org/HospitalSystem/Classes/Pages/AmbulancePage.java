package org.HospitalSystem.Classes.Pages;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.Components.Ambulances;
import org.HospitalSystem.Classes.Components.DashboardDrawer;
import org.HospitalSystem.Classes.DatabaseManager;

public class AmbulancePage extends JPanel {
    public AmbulancePage(JFrame root, JPanel cardPanel, DatabaseManager db, int width, int height) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        add(new DashboardDrawer(root, cardPanel, width/5), BorderLayout.WEST);
        add(new Ambulances(root, db), BorderLayout.CENTER);
    }

    public AmbulancePage(JFrame root, JPanel cardPanel, DatabaseManager db) {
        this(root, cardPanel, db, 1280, 720);
    }
}
