package org.HospitalSystem.Classes.Components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Patient;
import org.HospitalSystem.Classes.Static.DBService.PatientService;

public final class Patients extends JPanel {
    private Patient[] patients;
    private JPanel patientListPanel = new JPanel();
    
    public void refreshPatients(JFrame root, DatabaseManager dbManager, Runnable onStatusUpdate) {
        patientListPanel.removeAll();
        patients = PatientService.myPatients(dbManager);
        for (Patient patient : patients) {
            PatientDisplay display = new PatientDisplay(
                root, 
                dbManager, 
                patient, 
                820, 
                80,
                () -> {
                    refreshPatients(root, dbManager, onStatusUpdate);
                    if (onStatusUpdate != null) onStatusUpdate.run();
                }
            );
            patientListPanel.add(display);
            patientListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        patientListPanel.revalidate();
        patientListPanel.repaint();
    }

    public Patients(JFrame root, DatabaseManager dbManager) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        
        // Search bar row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        JTextField searchBar = new JTextField();
        searchBar.setText("Search for a patient");
        searchBar.setPreferredSize(new java.awt.Dimension(200, 30));
        // Clear default text on focus
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Search for a patient")) {
                    searchBar.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText("Search for a patient");
                }
            }
        });
        add(searchBar, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        HoverButton searchButton = new HoverButton("Search", "search.png", 100, 30);
        searchButton.addActionListener(_ -> {
            String searchTerm = searchBar.getText();
            if (!searchTerm.isEmpty() && !searchTerm.equals("Search for a patient")) {
                patientListPanel.removeAll();
                Patient[] searchResults = PatientService.searchPatients(searchTerm, dbManager);
                for (Patient patient : searchResults) {
                    PatientDisplay display = new PatientDisplay(
                        root, 
                        dbManager, 
                        patient, 
                        820, 
                        80,
                        () -> refreshPatients(root, dbManager, null)
                    );
                    patientListPanel.add(display);
                    patientListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
                
                patientListPanel.revalidate();
                patientListPanel.repaint();
            }
            else{
                refreshPatients(root, dbManager, null);
            }
        });
        add(searchButton, gbc);
        gbc.gridx = 2;
        gbc.weightx = 0.0;
        HoverButton addButton = new HoverButton("Add Patient", "patient.png", 120, 30);
        addButton.addActionListener(_ -> {
            PatientAdditionMenu dialog = new PatientAdditionMenu(root);
            dialog.setTitle("Add New Patient");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                String firstName = dialog.getFirstName();
                String lastName = dialog.getLastName();
                String status = dialog.getStatus();
                PatientService.addPatient(firstName, lastName, status, dbManager);
                refreshPatients(root, dbManager, null);  // Call the refresh method
            }
        });
        add(addButton, gbc);

        // Create scrollable panel for patient displays
        patientListPanel = new JPanel();  // Initialize the class field
        patientListPanel.setLayout(new BoxLayout(patientListPanel, BoxLayout.Y_AXIS));
        
        // Initial population of patients
        refreshPatients(root, dbManager, null);

        // Create scroll pane and add patient list
        JScrollPane scrollPane = new JScrollPane(patientListPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // Add scroll pane below search bar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);
    }
    
}
