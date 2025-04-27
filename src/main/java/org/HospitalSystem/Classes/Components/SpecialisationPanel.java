package org.HospitalSystem.Classes.Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Doctor;
import org.HospitalSystem.Classes.Static.DBService.CurrentUserService;
import org.HospitalSystem.Classes.Static.Specialisations;

public class SpecialisationPanel extends JPanel {
    private final JLabel specialisationLabel;
    private final DatabaseManager db;
    private final Doctor currentDoctor;

    public SpecialisationPanel(int width, DatabaseManager db, Doctor doctor) {
        this.db = db;
        this.currentDoctor = doctor;
        
        setLayout(new BorderLayout(10, 5));
        setPreferredSize(new Dimension(width, 80));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Title
        JLabel titleLabel = new JLabel("Specialisation");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        titleLabel.setForeground(Color.GRAY);
        add(titleLabel, BorderLayout.NORTH);

        // Current specialisation label
        specialisationLabel = new JLabel(doctor.getSpecialisation());
        specialisationLabel.setFont(new Font("Roboto", Font.BOLD, 36));
        specialisationLabel.setForeground(Color.BLACK);

        
        
        // Edit button
        HoverButton editButton = new HoverButton("Edit", 100, 25);
        editButton.addActionListener(_ -> showEditDialog());

        // Control panel for label and button
        JPanel controlPanel = new JPanel(new BorderLayout(5, 0));
        controlPanel.setOpaque(false);
        controlPanel.add(specialisationLabel, BorderLayout.CENTER);
        controlPanel.add(editButton, BorderLayout.EAST);
        
        add(controlPanel, BorderLayout.CENTER);
    }

    private void showEditDialog() {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Edit Specialisation", true);
        dialog.setLayout(new BorderLayout(10, 10));
        ((JPanel)dialog.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] specialisations = {
            "General Practitioner",
            "Cardiologist",
            "Neurologist",
            "Orthopedic Surgeon",
            "Dermatologist",
            "Pediatrician"
        };
        JComboBox<String> specialisationCombo = new JComboBox<>(specialisations);
        specialisationCombo.setSelectedItem(currentDoctor.getSpecialisation());

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            String newSpec = (String) specialisationCombo.getSelectedItem();
            int specId = Specialisations.toID(newSpec);
            
            if (CurrentUserService.updateSpecialisation(specId, db)) {
                currentDoctor.specialise(specId);
                specialisationLabel.setText(newSpec);
                dialog.dispose();
                JOptionPane.showMessageDialog(this, 
                    "Specialisation updated successfully", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, 
                    "Failed to update specialisation", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(specialisationCombo, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}