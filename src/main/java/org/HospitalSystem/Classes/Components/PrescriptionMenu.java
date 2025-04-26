package org.HospitalSystem.Classes.Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Medicine;
import org.HospitalSystem.Classes.Patient;
import org.HospitalSystem.Classes.Prescription;
import org.HospitalSystem.Classes.Static.DBService.MedicineService;
import org.HospitalSystem.Classes.Static.DBService.PrescriptionService;

public class PrescriptionMenu extends JDialog {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
    private final JPanel prescriptionListPanel;
    private final Patient patient;
    private final DatabaseManager dbManager;

    public PrescriptionMenu(JFrame parent, DatabaseManager dbManager, Patient patient) {
        super(parent, "Prescriptions", true);
        this.patient = patient;
        this.dbManager = dbManager;

        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);
        setLocationRelativeTo(parent);

        // Add new prescription button
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        HoverButton addButton = new HoverButton("Add Prescription", "medicine.png", 150, 30);
        addButton.addActionListener(_ -> showAddPrescriptionDialog());
        topPanel.add(addButton);
        add(topPanel, BorderLayout.NORTH);

        // Prescriptions list
        prescriptionListPanel = new JPanel();
        prescriptionListPanel.setLayout(new BoxLayout(prescriptionListPanel, BoxLayout.Y_AXIS));
        refreshPrescriptions();

        JScrollPane scrollPane = new JScrollPane(prescriptionListPanel);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void refreshPrescriptions() {
        prescriptionListPanel.removeAll();
        Prescription[] prescriptions = PrescriptionService.getPatientPrescriptions(patient.id, dbManager);
        
        for (Prescription prescription : prescriptions) {
            addPrescriptionPanel(prescription);
        }
        
        prescriptionListPanel.revalidate();
        prescriptionListPanel.repaint();
    }

    private void addPrescriptionPanel(Prescription prescription) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panel.setBackground(Color.WHITE);

        // Medicine info
        JLabel medicineLabel = new JLabel(prescription.medicineName);
        medicineLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        panel.add(medicineLabel, BorderLayout.NORTH);

        // Dosage and end date
        JLabel detailsLabel = new JLabel(String.format("%.2f mg daily until %s", 
            prescription.dosage, prescription.end_date.format(formatter)));
        detailsLabel.setForeground(Color.GRAY);
        panel.add(detailsLabel, BorderLayout.CENTER);

        // Delete button
        if (prescription.doctor_id == DatabaseManager.user_id) {
            HoverButton deleteButton = new HoverButton("Stop", 60, 25, "#ff4444", "#cc0000");
            deleteButton.addActionListener(_ -> {
                if (PrescriptionService.deletePrescription(prescription.id, dbManager)) {
                    refreshPrescriptions();
                }
            });
            panel.add(deleteButton, BorderLayout.EAST);
        }

        prescriptionListPanel.add(panel);
        prescriptionListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    public void showAddPrescriptionDialog() {
        JDialog dialog = new JDialog(this, "Add New Prescription", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Get all medicines
        Medicine[] medicines = MedicineService.getAllMedicines(dbManager);
        
        // Create medicine combo box
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Select Medicine:"), gbc);
        
        gbc.gridx = 1;
        JComboBox<Medicine> medicineCombo = new JComboBox<>(medicines);
        medicineCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Medicine medicine) {
                    setText(medicine.name);
                }
                return this;
            }
        });
        dialog.add(medicineCombo, gbc);

        // Dosage field
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Dosage (mg):"), gbc);
        
        gbc.gridx = 1;
        NumberField dosageField = new NumberField(0, 999, 20);
        dosageField.setAllowDecimals(true);
        // Set default dosage when medicine selected
        medicineCombo.addActionListener(_ -> {
            Medicine selected = (Medicine) medicineCombo.getSelectedItem();
            if (selected != null) {
                dosageField.setText(String.format("%.2f", selected.default_dosage));
            }
        });
        dialog.add(dosageField, gbc);

        // End date spinner
        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("End Date:"), gbc);
        
        gbc.gridx = 1;
        Calendar calendar = Calendar.getInstance();
        Date initDate = calendar.getTime();
        calendar.add(Calendar.YEAR, 1);
        Date latestDate = calendar.getTime();
        calendar.add(Calendar.YEAR, -2);
        Date earliestDate = calendar.getTime();
        
        SpinnerDateModel dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.DAY_OF_MONTH);
        JSpinner endDateSpinner = new JSpinner(dateModel);
        endDateSpinner.setEditor(new JSpinner.DateEditor(endDateSpinner, "MMM dd, yyyy"));
        dialog.add(endDateSpinner, gbc);

        // Buttons panel
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(_ -> {
            Medicine selectedMedicine = (Medicine) medicineCombo.getSelectedItem();
            if (selectedMedicine == null || dosageField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "Please fill all fields", "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            double dosage = Double.parseDouble(dosageField.getText().trim());
            LocalDateTime endDate = ((Date) endDateSpinner.getValue())
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .withHour(23)
                .withMinute(59);

            int result = PrescriptionService.addPrescription(
                patient.id,
                selectedMedicine.id,
                dosage,
                endDate,
                dbManager
            );

            if (result > 0) {
                dialog.dispose();
                refreshPrescriptions();
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(_ -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, gbc);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}