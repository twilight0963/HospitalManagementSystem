package hospital.management.system;

import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Room extends JFrame {
    // Theme
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF);
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2);
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5);

    // Fonts
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private JTable table;

    Room() {
        // Frame Setup
        setTitle("Room Management");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setOpaque(false);

        // Create Table
        table = new JTable();
        customizeTable();

        // Scroll Pane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR, 2));
        scrollPane.setBackground(BACKGROUND_COLOR);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Header Panel with Labels
        JPanel headerPanel = createHeaderPanel();
        tablePanel.add(headerPanel, BorderLayout.NORTH);

        // Image Panel
        JPanel imagePanel = createImagePanel();

        // Button Panel
        JPanel buttonPanel = createButtonPanel();

        // Combine Panels
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(imagePanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Populate Table
        populateTable();

        setVisible(true);
    }

    private void customizeTable() {
        table.setFont(TABLE_FONT);
        table.setRowHeight(25);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);

        // Customize Table Header
        JTableHeader header = table.getTableHeader();
        header.setBackground(ACCENT_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(HEADER_FONT);

        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        headerPanel.setOpaque(false);

        String[] headers = {"Room No", "Availability", "Price", "Bed Type"};
        for (String header : headers) {
            JLabel label = new JLabel(header, SwingConstants.CENTER);
            label.setFont(HEADER_FONT);
            label.setForeground(TEXT_COLOR);
            headerPanel.add(label);
        }

        return headerPanel;
    }

    private JPanel createImagePanel() {
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);

        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/roomm.png"));
        Image image = imageIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(image));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        return imagePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        JButton backButton = new JButton("Back");
        backButton.setFont(HEADER_FONT);
        backButton.setBackground(ACCENT_COLOR);
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());

        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private void populateTable() {
        try {
            conn c = new conn();
            String q = "select * from room";
            ResultSet resultSet = c.statement.executeQuery(q);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Room());
    }
}