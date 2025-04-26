package org.HospitalSystem.Classes.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Medicine;
import org.HospitalSystem.Classes.Static.DBService.MedicineService;

public class MedicineDisplay extends JPanel {
    private static final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.of("en", "IN"));

    public MedicineDisplay(JFrame root, DatabaseManager dbManager, Medicine medicine, int width, int height, Runnable onUpdate) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Icon (if available)
        if (medicine.icon_path != null && !medicine.icon_path.isEmpty()) {
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 2;
            gbc.weightx = 0;
            add(new JLabel(new PathImageIcon(medicine.icon_path).resize(40)), gbc);
            gbc.gridx = 1;
        }

        // Name and ID
        gbc.gridheight = 1;
        gbc.weightx = 1;
        JLabel nameLabel = new JLabel(medicine.name);
        nameLabel.setFont(new Font("Roboto", Font.BOLD, 14));
        add(nameLabel, gbc);

        // Price and Dosage
        gbc.gridy = 1;
        JLabel detailsLabel = new JLabel(String.format("%s | %.2f mg", 
            currencyFormatter.format(medicine.price), medicine.default_dosage));
        detailsLabel.setForeground(Color.GRAY);
        add(detailsLabel, gbc);

        // Edit Button
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0;
        HoverButton editButton = new HoverButton("Edit", 120, 30);
        editButton.addActionListener(_ -> {
            MedicineMenu dialog = new MedicineMenu(root);
            dialog.setValues(medicine);
            dialog.setTitle("Edit Medicine");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                MedicineService.updateMedicine(
                    medicine.id,
                    dialog.getMedicineName(),
                    dialog.getIconPath(),
                    dialog.getDefaultDosage(),
                    dialog.getPrice(),
                    dbManager
                );
                onUpdate.run();
            }
        });
        add(editButton, gbc);

        // Delete Button
        gbc.gridy = 1;
        HoverButton deleteButton = new HoverButton("Delete", 120, 30, "#ff4444", "#cc0000");
        deleteButton.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(
                root,
                "Are you sure you want to delete this medicine?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (result == JOptionPane.YES_OPTION) {
                if (MedicineService.deleteMedicine(medicine.id, dbManager)) {
                    onUpdate.run();
                }
            }
        });
        add(deleteButton, gbc);
    }
}