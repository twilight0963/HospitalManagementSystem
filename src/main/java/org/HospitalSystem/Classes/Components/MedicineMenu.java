package org.HospitalSystem.Classes.Components;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

import org.HospitalSystem.Classes.Medicine;

public class MedicineMenu extends JDialog {
    private final JTextField nameField;
    private final JTextField iconPathField;
    private final NumberField dosageField;
    private final NumberField priceField;
    private boolean submitted = false;

    public MedicineMenu(JFrame parent) {
        super(parent, "Add New Medicine", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Medicine Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(20);
        add(nameField, gbc);

        // Icon Path
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Icon Path:"), gbc);

        gbc.gridx = 1;
        iconPathField = new JTextField(20);
        add(iconPathField, gbc);

        // Default Dosage
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Dosage (mg):"), gbc);

        gbc.gridx = 1;
        dosageField = new NumberField(0, 999999, 20);
        dosageField.setAllowDecimals(true);
        add(dosageField, gbc);

        // Price
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Price (₹):"), gbc);

        gbc.gridx = 1;
        priceField = new NumberField(0, 999999, 20);
        priceField.setAllowDecimals(true);
        add(priceField, gbc);

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
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a medicine name", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (dosageField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a default dosage", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (priceField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a price", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public String getMedicineName() {
        return nameField.getText().trim();
    }

    public String getIconPath() {
        return iconPathField.getText().trim();
    }

    public double getDefaultDosage() {
        String text = dosageField.getText().trim();
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }

    public double getPrice() {
        String text = priceField.getText().trim();
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }

    public void setValues(Medicine medicine) {
        nameField.setText(medicine.name);
        iconPathField.setText(medicine.icon_path);
        dosageField.setText(String.format("%.2f", medicine.default_dosage));
        priceField.setText(String.format("%.2f", medicine.price));
    }

    public boolean isSubmitted() {
        return submitted;
    }
}
