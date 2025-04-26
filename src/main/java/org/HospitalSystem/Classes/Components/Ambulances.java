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

import org.HospitalSystem.Classes.Ambulance;
import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Static.DBService.AmbulanceService;

public final class Ambulances extends JPanel {
    private Ambulance[] ambulances;
    private JPanel ambulanceListPanel = null;

    public void refreshAmbulances(JFrame root, DatabaseManager dbManager) {
        ambulanceListPanel.removeAll();
        ambulances = AmbulanceService.getAllAmbulances(dbManager);
        for (Ambulance ambulance : ambulances) {
            AmbulanceDisplay display = new AmbulanceDisplay(
                root,
                dbManager,
                ambulance,
                820,
                80,
                () -> refreshAmbulances(root, dbManager)
            );
            ambulanceListPanel.add(display);
            ambulanceListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        ambulanceListPanel.revalidate();
        ambulanceListPanel.repaint();
    }

    public Ambulances(JFrame root, DatabaseManager dbManager) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Search bar row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        NumberField searchBar = new NumberField(0, 999, 20);
        searchBar.setText("Search by ambulance ID");
        searchBar.setPreferredSize(new Dimension(200, 30));
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Search by ambulance ID")) {
                    searchBar.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText("Search by ambulance ID");
                }
            }
        });
        add(searchBar, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        HoverButton searchButton = new HoverButton("Search", "search.png", 100, 30);
        searchButton.addActionListener(_ -> {
            String searchText = searchBar.getText();
            if (!searchText.isEmpty() && !searchText.equals("Search by ambulance ID")) {
                int searchId = Integer.parseInt(searchText);
                ambulanceListPanel.removeAll();
                Ambulance[] searchResults = AmbulanceService.searchAmbulances(searchId, dbManager);
                for (Ambulance ambulance : searchResults) {
                    if (ambulance != null) {
                        AmbulanceDisplay display = new AmbulanceDisplay(
                            root,
                            dbManager,
                            ambulance,
                            820,
                            80,
                            () -> refreshAmbulances(root, dbManager)
                        );
                        ambulanceListPanel.add(display);
                        ambulanceListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
                ambulanceListPanel.revalidate();
                ambulanceListPanel.repaint();
            } else {
                refreshAmbulances(root, dbManager);
            }
        });
        add(searchButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        HoverButton addButton = new HoverButton("Add Ambulance", "amb.png", 120, 30);
        addButton.addActionListener(_ -> {
            AmbulanceMenu dialog = new AmbulanceMenu(root);
            dialog.setTitle("Add New Ambulance");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                String location = dialog.getAmbulanceLocation();
                String status = dialog.getStatus();
                int doctorId = dialog.getDoctorId();
                int patientId = dialog.getPatientId();
                
                    
                AmbulanceService.addAmbulance(location, status, doctorId, patientId, dbManager);
                refreshAmbulances(root, dbManager);
            }
        });
        add(addButton, gbc);

        // Create scrollable panel for ambulance displays
        ambulanceListPanel = new JPanel();
        ambulanceListPanel.setLayout(new BoxLayout(ambulanceListPanel, BoxLayout.Y_AXIS));

        // Initial population of ambulances
        refreshAmbulances(root, dbManager);

        // Create scroll pane and add ambulance list
        JScrollPane scrollPane = new JScrollPane(ambulanceListPanel);
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
