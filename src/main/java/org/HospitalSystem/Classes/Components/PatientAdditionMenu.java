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

public class PatientAdditionMenu extends JDialog {
    private  final JTextField firstNameField;
    private final JTextField lastNameField;
    private final JComboBox<String> statusComboBox;
    private boolean submitted = false;

    public PatientAdditionMenu(JFrame parent) {
        super(parent, "Add New Patient", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // First Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("First Name:"), gbc);

        gbc.gridx = 1;
        firstNameField = new JTextField(20);
        add(firstNameField, gbc);

        // Last Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Last Name:"), gbc);

        gbc.gridx = 1;
        lastNameField = new JTextField(20);
        add(lastNameField, gbc);

        // Status Dropdown
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Status:"), gbc);

        gbc.gridx = 1;
        String[] statuses = {"Stable", "Critical", "Under Observation"};
        statusComboBox = new JComboBox<>(statuses);
        add(statusComboBox, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton submitButton = new JButton("");
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
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    private boolean validateInputs() {
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a first name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a last name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public String getFirstName() {
        return firstNameField.getText().trim();
    }

    public void setValues(String firstName, String lastName, String status) {
        firstNameField.setText(firstName);
        lastNameField.setText(lastName);
        statusComboBox.setSelectedItem(status);
    }

    public String getLastName() {
        return lastNameField.getText().trim();
    }

    public String getStatus() {
        return (String) statusComboBox.getSelectedItem();
    }

    public boolean isSubmitted() {
        return submitted;
    }
}