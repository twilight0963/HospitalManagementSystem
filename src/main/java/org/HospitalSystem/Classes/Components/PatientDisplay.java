package org.HospitalSystem.Classes.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Patient;
import org.HospitalSystem.Classes.Static.DBService.PatientService;

public class PatientDisplay extends JPanel {
    public PatientDisplay(JFrame root, DatabaseManager dbManager, Patient patient, int width, int height, Runnable onUpdate) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBackground(java.awt.Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name: " + patient.short_name);
        add(nameLabel, gbc);
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID: " + patient.id);
        idLabel.setForeground(Color.GRAY);
        add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel statusLabel = new JLabel("Status: " + patient.status);

        switch (patient.status) {
            case "Stable" -> statusLabel.setForeground(Color.GREEN);
            case "Critical" -> statusLabel.setForeground(Color.RED);
            default -> statusLabel.setForeground(Color.ORANGE);
        }
        add(statusLabel, gbc);
        gbc.gridx = 2;
        HoverButton editButton = new HoverButton("Edit", 50, 30);
        editButton.addActionListener(_ -> {
            PatientAdditionMenu dialog = new PatientAdditionMenu(root);
            // Pre-fill the form with current values
            dialog.setValues(patient.full_name.split(" ")[0], patient.full_name.split(" ")[1], patient.status);
            dialog.setTitle("Edit Patient");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                String firstName = dialog.getFirstName();
                String lastName = dialog.getLastName();
                String status = dialog.getStatus();
                PatientService.updatePatient(patient.id, firstName, lastName, status, dbManager);
                onUpdate.run();  // Call the refresh callback
            }
        });
        add(editButton, gbc);

        gbc.gridx = 3;
        HoverButton dischargeButton = new HoverButton("Discharge", 50, 30);
        dischargeButton.addActionListener(_ -> {
            PatientService.dischargePatient(patient.id, dbManager);
            onUpdate.run();  // Call the refresh callback
        });
        add(dischargeButton, gbc);

        if (!patient.status.equals("Stable")){
            dischargeButton.setEnabled(false);
        }
        
    }
}
