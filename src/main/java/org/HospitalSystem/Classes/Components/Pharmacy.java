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
import org.HospitalSystem.Classes.Medicine;
import org.HospitalSystem.Classes.Static.DBService.MedicineService;

public final class Pharmacy extends JPanel {
    private JPanel medicineListPanel = new JPanel();

    public void refreshMedicines(JFrame root, DatabaseManager dbManager) {
        medicineListPanel.removeAll();
        Medicine[] medicines = MedicineService.getAllMedicines(dbManager);
        for (Medicine medicine : medicines) {
            MedicineDisplay display = new MedicineDisplay(
                root,
                dbManager,
                medicine,
                820,
                80,
                () -> refreshMedicines(root, dbManager)
            );
            medicineListPanel.add(display);
            medicineListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        medicineListPanel.revalidate();
        medicineListPanel.repaint();
    }

    public Pharmacy(JFrame root, DatabaseManager dbManager) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;

        // Search bar row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        JTextField searchBar = new JTextField();
        searchBar.setText("Search by medicine name");
        searchBar.setPreferredSize(new Dimension(200, 30));
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Search by medicine name")) {
                    searchBar.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText("Search by medicine name");
                }
            }
        });
        add(searchBar, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0;
        HoverButton searchButton = new HoverButton("Search", "search.png", 100, 30);
        searchButton.addActionListener(_ -> {
            String searchText = searchBar.getText();
            if (!searchText.isEmpty() && !searchText.equals("Search by medicine name")) {
                medicineListPanel.removeAll();
                Medicine[] searchResults = MedicineService.searchMedicines(searchText, dbManager);
                for (Medicine medicine : searchResults) {
                    MedicineDisplay display = new MedicineDisplay(
                        root,
                        dbManager,
                        medicine,
                        820,
                        80,
                        () -> refreshMedicines(root, dbManager)
                    );
                    medicineListPanel.add(display);
                    medicineListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
                medicineListPanel.revalidate();
                medicineListPanel.repaint();
            } else {
                refreshMedicines(root, dbManager);
            }
        });
        add(searchButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        HoverButton addButton = new HoverButton("Add Medicine", "medicine.png", 120, 30);
        addButton.addActionListener(_ -> {
            MedicineMenu dialog = new MedicineMenu(root);
            dialog.setTitle("Add New Medicine");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                MedicineService.addMedicine(
                    dialog.getMedicineName(),
                    dialog.getIconPath(),
                    dialog.getDefaultDosage(),
                    dialog.getPrice(),
                    dbManager
                );
                refreshMedicines(root, dbManager);
            }
        });
        add(addButton, gbc);

        // Create scrollable panel for medicine displays
        medicineListPanel = new JPanel();
        medicineListPanel.setLayout(new BoxLayout(medicineListPanel, BoxLayout.Y_AXIS));

        // Initial population
        refreshMedicines(root, dbManager);

        // Create scroll pane
        JScrollPane scrollPane = new JScrollPane(medicineListPanel);
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