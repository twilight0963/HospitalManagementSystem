package org.HospitalSystem.Classes.Components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import org.HospitalSystem.Classes.Appointment;
import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Static.DBService.AppointmentService;

public final class Appointments extends JPanel {
    private final JPanel appointmentListPanel;
    private final Timer refreshTimer;

    public void refreshAppointments(JFrame root, DatabaseManager dbManager) {
        appointmentListPanel.removeAll();
        Appointment[] appointments = AppointmentService.getMyAppointments(dbManager);
        
        for (Appointment appointment : appointments) {
            if (!appointment.isExpired()) {
                AppointmentDisplay display = new AppointmentDisplay(
                    root,
                    dbManager,
                    appointment,
                    820,
                    80,
                    () -> refreshAppointments(root, dbManager)
                );
                appointmentListPanel.add(display);
                appointmentListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        appointmentListPanel.revalidate();
        appointmentListPanel.repaint();
    }

    public Appointments(JFrame root, DatabaseManager dbManager) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Add Button at top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        HoverButton addButton = new HoverButton("New Appointment", "appointment.png", 150, 30);
        addButton.addActionListener(_ -> {
            AppointmentMenu dialog = new AppointmentMenu(root);
            dialog.setTitle("New Appointment");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                AppointmentService.addAppointment(
                    dialog.getPatientId(),
                    dialog.getStartTime(),
                    dialog.getEndTime(),
                    dialog.getDescription(),
                    dbManager
                );
                refreshAppointments(root, dbManager);
            }
        });
        add(addButton, gbc);

        // Create scrollable panel for appointment displays
        appointmentListPanel = new JPanel();
        appointmentListPanel.setLayout(new BoxLayout(appointmentListPanel, BoxLayout.Y_AXIS));

        // Initial population
        refreshAppointments(root, dbManager);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(appointmentListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Add scroll pane below button
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        // Setup auto-refresh timer (every minute)
        refreshTimer = new Timer(60000, _ -> refreshAppointments(root, dbManager));
        refreshTimer.start();
    }

    public void cleanup() {
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}