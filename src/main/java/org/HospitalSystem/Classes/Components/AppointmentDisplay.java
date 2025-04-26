package org.HospitalSystem.Classes.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.Appointment;
import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Static.DBService.AppointmentService;

public class AppointmentDisplay extends JPanel {
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    public AppointmentDisplay(JFrame root, DatabaseManager dbManager, Appointment appointment, 
                            int width, int height, Runnable onUpdate) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Title and Time
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Appointment #" + appointment.id);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        add(titleLabel, gbc);

        // Patient ID and Time
        gbc.gridy = 1;
        JLabel timeLabel = new JLabel(String.format("Patient #%d | %s - %s", 
            appointment.patient_id,
            appointment.startTime.format(timeFormatter),
            appointment.endTime.format(timeFormatter)));
        timeLabel.setForeground(Color.GRAY);
        add(timeLabel, gbc);

        // Description
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        JLabel descLabel = new JLabel(appointment.description);
        descLabel.setForeground(new Color(70, 70, 70));
        add(descLabel, gbc);

        // Edit Button
        gbc.gridx = 2;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        HoverButton editButton = new HoverButton("Edit", 100, 30);
        editButton.addActionListener(_ -> {
            AppointmentMenu dialog = new AppointmentMenu(root);
            dialog.setValues(appointment);
            dialog.setTitle("Edit Appointment");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                AppointmentService.updateAppointment(
                    appointment.id,
                    dialog.getPatientId(),
                    dialog.getStartTime(),
                    dialog.getEndTime(),
                    dialog.getDescription(),
                    dbManager
                );
                onUpdate.run();
            }
        });
        add(editButton, gbc);

        // Delete Button
        gbc.gridy = 1;
        HoverButton deleteButton = new HoverButton("X", 100, 30, "#ff4444", "#cc0000");
        deleteButton.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(
                root,
                "Are you sure you want to cancel this appointment?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                if (AppointmentService.deleteAppointment(appointment.id, dbManager)) {
                    onUpdate.run();
                }
            }
        });
        add(deleteButton, gbc);
    }
}