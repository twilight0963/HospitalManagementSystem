package org.HospitalSystem.Classes.Pages;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.Components.DashboardDrawer;
import org.HospitalSystem.Classes.Components.Rooms;
import org.HospitalSystem.Classes.DatabaseManager;

public class RoomPage extends JPanel {
    public RoomPage(JFrame root, JPanel cardPanel, DatabaseManager db, int width, int height) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(width, height));
        add(new DashboardDrawer(root, cardPanel, width/5), BorderLayout.WEST);
        add(new Rooms(root, db), BorderLayout.CENTER);
    }

    public RoomPage(JFrame root, JPanel cardPanel, DatabaseManager db) {
        this(root, cardPanel, db, 1280, 720);
    }
}

