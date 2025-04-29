package org.HospitalSystem.Classes.Components;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

import org.HospitalSystem.Classes.Appointment;

public class AppointmentMenu extends JDialog {
    private final NumberField patientIdField;
    private final JSpinner startTimeSpinner;
    private final JSpinner endTimeSpinner;
    private final JTextField descriptionField;
    private boolean submitted = false;

    public AppointmentMenu(JFrame parent) {
        super(parent, "Add New Appointment", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Patient ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Patient ID:"), gbc);

        gbc.gridx = 1;
        patientIdField = new NumberField(0, 999, 20);
        add(patientIdField, gbc);

        // Start Time
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Start Time:"), gbc);

        gbc.gridx = 1;
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -1);
        Date earliestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 2);
        Date latestDate = calendar.getTime();
        
        SpinnerDateModel startModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.MINUTE);
        startTimeSpinner = new JSpinner(startModel);
        startTimeSpinner.setEditor(new JSpinner.DateEditor(startTimeSpinner, "dd MMM yyyy HH:mm"));
        add(startTimeSpinner, gbc);

        // End Time
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("End Time:"), gbc);

        gbc.gridx = 1;
        SpinnerDateModel endModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.MINUTE);
        endTimeSpinner = new JSpinner(endModel);
        endTimeSpinner.setEditor(new JSpinner.DateEditor(endTimeSpinner, "MMM dd, yyyy HH:mm"));
        add(endTimeSpinner, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Description:"), gbc);

        gbc.gridx = 1;
        descriptionField = new JTextField(20);
        add(descriptionField, gbc);

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
        if (patientIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a patient ID", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Date startDate = (Date) startTimeSpinner.getValue();
        Date endDate = (Date) endTimeSpinner.getValue();
        if (endDate.before(startDate)) {
            JOptionPane.showMessageDialog(this, "End time must be after start time", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (descriptionField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a description", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public int getPatientId() {
        return Integer.parseInt(patientIdField.getText().trim());
    }

    public LocalDateTime getStartTime() {
        return ((Date) startTimeSpinner.getValue())
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    public LocalDateTime getEndTime() {
        return ((Date) endTimeSpinner.getValue())
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    public String getDescription() {
        return descriptionField.getText().trim();
    }

    public void setValues(Appointment appointment) {
        patientIdField.setText(String.valueOf(appointment.patient_id));
        startTimeSpinner.setValue(Date.from(appointment.startTime.atZone(ZoneId.systemDefault()).toInstant()));
        endTimeSpinner.setValue(Date.from(appointment.endTime.atZone(ZoneId.systemDefault()).toInstant()));
        descriptionField.setText(appointment.description);
    }

    public boolean isSubmitted() {
        return submitted;
    }
}