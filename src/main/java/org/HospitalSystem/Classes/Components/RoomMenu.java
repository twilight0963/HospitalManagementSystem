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
import javax.swing.JPanel;

public class RoomMenu extends JDialog {
    private final JComboBox<String> roomTypeComboBox;
    private final NumberField occupancyField;
    private boolean submitted = false;

    public RoomMenu(JFrame parent) {
        super(parent, "Add New Room", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Room Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Room Type: "), gbc);

        gbc.gridx = 1;
        String[] roomTypes = {"ICU", "General Ward", "Private"};
        roomTypeComboBox = new JComboBox<>(roomTypes);
        add(roomTypeComboBox, gbc);

        // Occupant ID
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Occupant ID:"), gbc);

        gbc.gridx = 1;
        occupancyField = new NumberField(0, 999, 20);
        add(occupancyField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton submitButton = new JButton("Save");
        submitButton.addActionListener(_ -> {
            submitted = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dispose());

        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        add(buttonPanel, gbc);

        pack();
        setLocationRelativeTo(parent);
    }

    public String getRoomType() {
        return roomTypeComboBox.getSelectedItem().toString();
    }

    public int getOccupant() {
        String text = occupancyField.getText().trim();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    public void setValues(String type, int occupant) {
        roomTypeComboBox.setSelectedItem(type);
        occupancyField.setText(occupant == 0 ? "" : String.valueOf(occupant));
    }

    public boolean isSubmitted() {
        return submitted;
    }
}