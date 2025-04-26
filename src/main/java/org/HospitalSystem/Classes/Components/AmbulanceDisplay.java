package org.HospitalSystem.Classes.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.Ambulance;
import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Static.DBService.AmbulanceService;

public class AmbulanceDisplay extends JPanel {
    public AmbulanceDisplay(JFrame root, DatabaseManager dbManager, Ambulance ambulance, int width, int height, Runnable onUpdate) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // ID and Location
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("ID: " + ambulance.id);
        idLabel.setForeground(Color.GRAY);
        add(idLabel, gbc);

        gbc.gridy = 1;
        JLabel locationLabel = new JLabel("Location: " + ambulance.location);
        add(locationLabel, gbc);

        // Status
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel statusLabel = new JLabel("Status: " + ambulance.status);
        switch (ambulance.status) {
            case "Available" -> statusLabel.setForeground(Color.GREEN);
            case "In Use" -> statusLabel.setForeground(Color.RED);
            default -> statusLabel.setForeground(Color.ORANGE);
        }
        add(statusLabel, gbc);

        // Assignments
        gbc.gridy = 1;
        String assignments = "Assignments: ";
        if (ambulance.doctor_id != 0) {
            assignments += "Doc#" + ambulance.doctor_id;
        }
        if (ambulance.patient_id != 0) {
            assignments += " Pat#" + ambulance.patient_id;
        }
        if (ambulance.doctor_id == 0 && ambulance.patient_id == 0) {
            assignments += "None";
        }
        JLabel assignmentLabel = new JLabel(assignments);
        add(assignmentLabel, gbc);

        // Edit Button
        gbc.gridx = 2;
        gbc.gridy = 0;
        HoverButton editButton = new HoverButton("Edit", 50, 30);
        editButton.addActionListener(_ -> {
            AmbulanceMenu dialog = new AmbulanceMenu(root);
            dialog.setValues(ambulance.location, ambulance.status, ambulance.doctor_id, ambulance.patient_id);
            dialog.setTitle("Edit Ambulance");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                boolean success = AmbulanceService.updateAmbulance(
                    ambulance.id,
                    dialog.getAmbulanceLocation(),
                    dialog.getStatus(),
                    dialog.getDoctorId(),
                    dialog.getPatientId(),
                    dbManager
                );
                if (success) {
                    onUpdate.run();
                }
            }
        });
        add(editButton, gbc);

        // Delete Button
        gbc.gridx = 3;
        HoverButton deleteButton = new HoverButton("Delete", 50, 30, "#ff4444", "#cc0000");
        deleteButton.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(
                root,
                "Are you sure you want to delete this ambulance?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                if (AmbulanceService.deleteAmbulance(ambulance.id, dbManager)) {
                    onUpdate.run();
                }
            }
        });
        
        if (!ambulance.isAvailable()) {
            deleteButton.setEnabled(false);
        }
        
        add(deleteButton, gbc);
    }
}