package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class SearchRoom extends JFrame {

    // Theme Colors (from Employee_info)
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF); // #EEF7FF (lightest blue for borders)
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C); // #4D869C (darkest teal for background)
    private static final Color TEXT_COLOR = new Color(0, 0, 0); // White for text
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2); // #7AB2B2 (medium teal for buttons/headers)
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5); // #CDE8E5 (light teal for table/input background)

    // Fonts (retaining from Employee_info)
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    private Choice choice;
    private JTable table;

    SearchRoom() {
        // Frame Setup
        setTitle("Search Room");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Table Panel
        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Input and Button Panel
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Frame Styling
        setSize(700, 500);
        setLocationRelativeTo(null);

        setVisible(true);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table Setup
        table = new JTable();
        customizeTable(table);

        // Load Data
        loadRoomData();

        // Scrollpane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void customizeTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(ACCENT_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(LABEL_FONT);

        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setFont(INPUT_FONT);
        table.setRowHeight(30);
    }

    private void loadRoomData() {
        conn connection = null;
        try {
            connection = new conn();
            String query = "SELECT * FROM room";
            ResultSet resultSet = connection.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(LABEL_FONT);
        statusLabel.setForeground(TEXT_COLOR);

        choice = new Choice();
        choice.add("Available");
        choice.add("Occupied");

        inputPanel.add(statusLabel);
        inputPanel.add(choice);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton searchButton = createStyledButton("Search", ACCENT_COLOR, TEXT_COLOR);
        searchButton.addActionListener(e -> searchRooms());

        JButton backButton = createStyledButton("Back", SECONDARY_COLOR, TEXT_COLOR);
        backButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(searchButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(LABEL_FONT);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    private void searchRooms() {
        conn connection = null;
        try {
            connection = new conn();
            String query = "SELECT * FROM room WHERE Availability = '" + choice.getSelectedItem() + "'";
            ResultSet resultSet = connection.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SearchRoom();
    }
}
