package org.HospitalSystem.Classes.Components;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Patient;
import org.HospitalSystem.Classes.Static.DBService.PatientService;

public class PrescriptionPatients extends JPanel {
    private final JPanel patientListPanel;
    private static Timer refreshTimer;

    private void refreshPatients(JFrame root, DatabaseManager dbManager) {
        patientListPanel.removeAll();
        Patient[] patients = PatientService.myPatients(dbManager);
        
        for (Patient patient : patients) {
            PrescriptionPatientDisplay display = new PrescriptionPatientDisplay(
                root,
                dbManager,
                patient,
                820,
                80,
                () -> refreshPatients(root, dbManager)
            );
            patientListPanel.add(display);
            patientListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        patientListPanel.revalidate();
        patientListPanel.repaint();
    }

    public PrescriptionPatients(JFrame root, DatabaseManager dbManager) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create title label
        JLabel titleLabel = new JLabel("My Patients' Prescriptions");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Create scrollable panel for patient displays
        patientListPanel = new JPanel();
        patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        
        // Initial population
        refreshPatients(root, dbManager);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(patientListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Add refresh timer (updates every 5 seconds)
        refreshTimer = new Timer(5000, _ -> refreshPatients(root, dbManager));
        refreshTimer.start();
    }
    public static void stopRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}