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

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Room;
import org.HospitalSystem.Classes.Static.DBService.RoomService;

public final class Rooms extends JPanel {
    private Room[] rooms;
    private JPanel roomListPanel = new JPanel();

    public void refreshRooms(JFrame root, DatabaseManager dbManager) {
        roomListPanel.removeAll();
        rooms = RoomService.getAllRooms(dbManager);
        if (rooms != null) {
            for (Room room : rooms) {
                if (room != null) {  // Check for null since array might have empty slots
                    RoomDisplay display = new RoomDisplay(
                        root,
                        dbManager,
                        room,
                        820,
                        80,
                        () -> refreshRooms(root, dbManager)
                    );
                    roomListPanel.add(display);
                    roomListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }
        roomListPanel.revalidate();
        roomListPanel.repaint();
    }

    public Rooms(JFrame root, DatabaseManager dbManager) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        
        // Search bar row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.0;
        
        // Create a NumericTextField for ID search
        NumberField searchBar = new NumberField(0, 9999, 20);
        searchBar.setText("Search by room ID");
        searchBar.setPreferredSize(new Dimension(200, 30));
        searchBar.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchBar.getText().equals("Search by room ID")) {
                    searchBar.setText("");
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (searchBar.getText().isEmpty()) {
                    searchBar.setText("Search by room ID");
                }
            }
        });
        add(searchBar, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.0;
        HoverButton searchButton = new HoverButton("Search", "search.png", 100, 30);
        searchButton.addActionListener(_ -> {
            int searchId;
            try {
            searchId = searchBar.getValue().intValue();
            } catch (Exception e) {
                refreshRooms(root, dbManager);
                return;
            }
            if (searchId > 0) {
                roomListPanel.removeAll();
                Room[] searchResults = RoomService.searchRooms(searchId, dbManager);
                for (Room room : searchResults) {
                    if (room != null) {
                        RoomDisplay display = new RoomDisplay(
                            root,
                            dbManager,
                            room,
                            820,
                            80,
                            () -> refreshRooms(root, dbManager)
                        );
                        roomListPanel.add(display);
                        roomListPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
                roomListPanel.revalidate();
                roomListPanel.repaint();
            } else {
                // If search field is empty or invalid, show all rooms
                refreshRooms(root, dbManager);
            }
        });
        add(searchButton, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0.0;
        HoverButton addButton = new HoverButton("Add Room", "room.png", 120, 30);
        addButton.addActionListener(_ -> {
            RoomMenu dialog = new RoomMenu(root);
            dialog.setTitle("Add New Room");
            dialog.setVisible(true);

            if (dialog.isSubmitted()) {
                String type = dialog.getRoomType();
                Room newRoom = new Room(
                    0,
                    type,
                    type.equals("Private") ? 5000 : 2000  // Basic price logic
                );
                newRoom.occupant_id = dialog.getOccupant();
                RoomService.addRoom(newRoom, dbManager);
                refreshRooms(root, dbManager);
            }
        });
        add(addButton, gbc);

        // Create scrollable panel for room displays
        roomListPanel = new JPanel();
        roomListPanel.setLayout(new BoxLayout(roomListPanel, BoxLayout.Y_AXIS));
        
        // Initial population of rooms
        refreshRooms(root, dbManager);

        // Create scroll pane and add room list
        JScrollPane scrollPane = new JScrollPane(roomListPanel);
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
