package org.HospitalSystem.Classes.Components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StatusPanel extends JPanel{
    private final JLabel statusLabel;
    private final String RESOURCE_PATH = "src/main/java/org/HospitalSystem/Resources/";
    public StatusPanel(int sizes, String title, String iconPath, String status){
        Dimension size = new Dimension(sizes, sizes);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setLayout(new BorderLayout(10,10));
        setBorder(BorderFactory.createLineBorder(Color.white, 10));


        JLabel titleLabel = new JLabel(title);
        titleLabel.setForeground(Color.GRAY);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(titleLabel, BorderLayout.NORTH);

        if (!iconPath.isEmpty()){
            JLabel iconLabel = new JLabel(new PathImageIcon(RESOURCE_PATH + iconPath).resize(sizes/4));
            add(iconLabel, BorderLayout.WEST);
        }

        statusLabel = new JLabel(status);
        statusLabel.setFont(new Font("Roboto", Font.PLAIN, 72));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setForeground(Color.decode("#4d869c"));
        add(statusLabel, BorderLayout.CENTER);
        

        setBackground(Color.WHITE);
    }

    public void updateStatus(String newStatus) {
        statusLabel.setText(newStatus);
        revalidate();
        repaint();
    }

    public StatusPanel(int sizes, String title, String status){
        this(sizes, title, "", status);
    }
}
