package org.HospitalSystem.Classes.Components;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AmbulanceMenu extends JDialog {
    private final JTextField locationField;
    private final JComboBox<String> statusComboBox;
    private final NumberField doctorField;
    private final NumberField patientField;
    private boolean submitted = false;

    public AmbulanceMenu(JFrame parent) {
        super(parent, "Add New Ambulance", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Location
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Location:"), gbc);

        gbc.gridx = 1;
        locationField = new JTextField(20);
        add(locationField, gbc);

        // Status Dropdown
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        String[] statuses = {"Available", "In Use", "Under Maintenance"};
        statusComboBox = new JComboBox<>(statuses);
        add(statusComboBox, gbc);

        // Doctor ID
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Doctor ID:"), gbc);

        gbc.gridx = 1;
        doctorField = new NumberField(0, 999, 20);
        add(doctorField, gbc);

        // Patient ID
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Patient ID:"), gbc);

        gbc.gridx = 1;
        patientField = new NumberField(0, 999, 20);
        add(patientField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton submitButton = new JButton("Save");
        submitButton.addActionListener(_ -> {
            if (validateInputs()) {
                submitted = true;
                dispose();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    private boolean validateInputs() {
        if (locationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a location", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public String getAmbulanceLocation() {
        return locationField.getText().trim();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }

    public int getDoctorId() {
        String text = doctorField.getText().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public int getPatientId() {
        String text = patientField.getText().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public void setValues(String location, String status, int doctorId, int patientId) {
        locationField.setText(location);
        statusComboBox.setSelectedItem(status);
        doctorField.setText(doctorId == 0 ? "" : String.valueOf(doctorId));
        patientField.setText(patientId == 0 ? "" : String.valueOf(patientId));
    }

    public boolean isSubmitted() {
        return submitted;
    }
}