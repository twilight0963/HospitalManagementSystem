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

public class PrescriptionPatientDisplay extends JPanel {
    public PrescriptionPatientDisplay(JFrame root, DatabaseManager dbManager, Patient patient, 
                                    int width, int height, Runnable onUpdate) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Name and ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel(patient.full_name);
        nameLabel.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 14));
        add(nameLabel, gbc);

        gbc.gridy = 1;
        JLabel idLabel = new JLabel("ID: " + patient.id);
        idLabel.setForeground(Color.GRAY);
        add(idLabel, gbc);
        
        // Add prescription buttons
        gbc.gridx = 1;
        gbc.gridy = 0;
        HoverButton prescribeButton = new HoverButton("New Prescription", "medicine.png", 130, 30);
        prescribeButton.addActionListener(_ -> {
            PrescriptionMenu dialog = new PrescriptionMenu(root, dbManager, patient);
            dialog.showAddPrescriptionDialog();
            dialog.setVisible(true);
        });
        add(prescribeButton, gbc);
        
        gbc.gridy = 1;
        HoverButton viewButton = new HoverButton("View Prescriptions", "list.png", 130, 30);
        viewButton.addActionListener(_ -> {
            PrescriptionMenu dialog = new PrescriptionMenu(root, dbManager, patient);
            dialog.setVisible(true);
        });
        add(viewButton, gbc);
    }
}