package org.HospitalSystem.Classes.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Room;
import org.HospitalSystem.Classes.Static.DBService.RoomService;

public class RoomDisplay extends JPanel {
    public RoomDisplay(JFrame root, DatabaseManager dbManager, Room room, int width, int height, Runnable onUpdate) {
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setBackground(java.awt.Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel nameLabel = new JLabel("Name: " + room.type + " - " + room.id);
        add(nameLabel, gbc);
        gbc.gridy = 1;
        JLabel idLabel = new JLabel("Price: " + room.price);
        idLabel.setForeground(Color.GRAY);
        add(idLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        String status = room.occupant_id == 0 ? "Available" : "Occupied";
        JLabel statusLabel = new JLabel("Status: " + status);

        switch (status) {
            case "Available" -> statusLabel.setForeground(Color.GREEN);
            case "Occupied" -> statusLabel.setForeground(Color.RED);
        }
        add(statusLabel, gbc);
        gbc.gridx = 2;
        HoverButton editButton = new HoverButton("Edit", 50, 30);
        add(editButton, gbc);
        editButton.addActionListener(_ -> {
            RoomMenu dialog = new RoomMenu(root);
            // Pre-fill the form with current values
            dialog.setValues(room.type, room.occupant_id);
            dialog.setTitle("Edit Room");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                String roomType = dialog.getRoomType();
                int occupantId = dialog.getOccupant();
                String roomStatus = occupantId==0 ? "Vacant" : "Occupied";
                
                // Update room status
                if (roomStatus.equals("Vacant")) {
                    room.emptyRoom();
                } else {
                    room.setOccupant(occupantId);
                }
                
                RoomService.updateRoom(room.id, roomType, occupantId, dbManager);
                onUpdate.run();  // Call the refresh callbacklback
            }
        });
    }
}

