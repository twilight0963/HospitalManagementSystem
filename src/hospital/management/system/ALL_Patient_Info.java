package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ALL_Patient_Info extends JFrame {
    // Logger
    private static final Logger LOGGER = Logger.getLogger(ALL_Patient_Info.class.getName());

    // Color Constants as specified
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF); // #EEF7FF (lightest blue for borders)
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C); // #4D869C (darkest teal for background)
    private static final Color TEXT_COLOR = new Color(0, 0, 0); // Black for text
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2); // #7AB2B2 (medium teal for buttons/headers)
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5); // #CDE8E5 (light teal for table/input background)

    // UI Components
    private JTable table;

    public ALL_Patient_Info() {
        // Frame Setup
        setTitle("Patient Information");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Main Panel with Table
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Frame Styling
        setSize(1000, 650); // Updated to match Room class size
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Table Setup
        table = new JTable();
        customizeTable(table);

        // Load Data
        loadPatientData();

        // Scrollpane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR)); // #EEF7FF for border
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void customizeTable(JTable table) {
        // Table Header Styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(ACCENT_COLOR); // #7AB2B2 for header background
        header.setForeground(TEXT_COLOR); // Black for header text
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Table Body Styling
        table.setBackground(SECONDARY_COLOR); // #CDE8E5 for table background
        table.setForeground(TEXT_COLOR); // Black for table text
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(30);

        // Center Align Cell Contents
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR); // Black for table text
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void loadPatientData() {
        conn connection = null;
        try {
            connection = new conn();
            String query = "SELECT * FROM Patient_Info";
            ResultSet resultSet = connection.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading patient data", e);
            JOptionPane.showMessageDialog(this,
                    "Error loading patient data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            // Close the statement and connection directly since conn class doesn't have closeConnection()
            if (connection != null) {
                try {
                    if (connection.statement != null && !connection.statement.isClosed()) {
                        connection.statement.close();
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing statement", e);
                }
                try {
                    if (connection.connection != null && !connection.connection.isClosed()) {
                        connection.connection.close();
                    }
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing connection", e);
                }
            }
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Back Button
        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(backButton);
        return buttonPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(ACCENT_COLOR); // #7AB2B2 for button background
        button.setForeground(TEXT_COLOR); // Black for button text
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
    }

    public static void main(String[] args) {
        // Use SwingUtilities to ensure thread safety
        SwingUtilities.invokeLater(() -> new ALL_Patient_Info());
    }
}