package hospital.management.system;

import net.proteanit.sql.DbUtils;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Ambulance extends JFrame {
    // Theme Colors
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF);
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2);
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5);

    // Fonts (same as Employee_info)
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // UI Components
    private JTable table;
    private JTextField nameField, genderField, carNameField, availableField, locationField;

    public Ambulance() {
        // Frame Setup
        setTitle("Ambulance Information");
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

        // Input Panel
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Frame Styling
        setSize(1000, 700);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table Setup
        table = new JTable();

        // Load Data
        loadTableData();

        // Customize table after setting the model
        customizeTable(table);

        // Add mouse listener to populate fields on row click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    nameField.setText(table.getValueAt(selectedRow, 0).toString());
                    genderField.setText(table.getValueAt(selectedRow, 1).toString());
                    carNameField.setText(table.getValueAt(selectedRow, 2).toString());
                    availableField.setText(table.getValueAt(selectedRow, 3).toString());
                    locationField.setText(table.getValueAt(selectedRow, 4).toString());
                }
            }
        });

        // Scrollpane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadTableData() {
        try {
            conn c = new conn();
            String q = "SELECT * FROM Ambulance";
            ResultSet resultSet = c.statement.executeQuery(q);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
            c.statement.close();
            c.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void customizeTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(ACCENT_COLOR); // Medium teal for header
        header.setForeground(TEXT_COLOR);
        header.setFont(LABEL_FONT);

        table.setBackground(SECONDARY_COLOR); // Light teal for table background
        table.setForeground(TEXT_COLOR);
        table.setFont(INPUT_FONT);
        table.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR);
        table.setDefaultRenderer(Object.class, centerRenderer);

        // Set column widths only if the table has the expected number of columns
        if (table.getColumnCount() >= 5) {
            table.getColumnModel().getColumn(0).setPreferredWidth(100); // Name
            table.getColumnModel().getColumn(1).setPreferredWidth(100); // Gender
            table.getColumnModel().getColumn(2).setPreferredWidth(100); // Car name
            table.getColumnModel().getColumn(3).setPreferredWidth(100); // Available
            table.getColumnModel().getColumn(4).setPreferredWidth(150); // Location
        } else {
            System.out.println("Warning: Table does not have the expected number of columns. Expected 5, but found " + table.getColumnCount());
        }
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        nameField = createTextField("Name");
        genderField = createTextField("Gender");
        carNameField = createTextField("Car Name");
        availableField = createTextField("Available");
        locationField = createTextField("Location");

        inputPanel.add(createLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(createLabel("Gender:"));
        inputPanel.add(genderField);
        inputPanel.add(createLabel("Car Name:"));
        inputPanel.add(carNameField);
        inputPanel.add(createLabel("Available:"));
        inputPanel.add(availableField);
        inputPanel.add(createLabel("Location:"));
        inputPanel.add(locationField);

        return inputPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setBackground(SECONDARY_COLOR);
        textField.setForeground(TEXT_COLOR);
        textField.setFont(INPUT_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Add Driver Button
        JButton addButton = createStyledButton("Add Driver", ACCENT_COLOR, TEXT_COLOR);
        addButton.addActionListener(e -> addDriver());

        // Update Driver Button
        JButton updateButton = createStyledButton("Update Driver", ACCENT_COLOR, TEXT_COLOR);
        updateButton.addActionListener(e -> updateDriver());

        // Delete Driver Button
        JButton deleteButton = createStyledButton("Delete Driver", ACCENT_COLOR, TEXT_COLOR);
        deleteButton.addActionListener(e -> deleteDriver());

        // Back Button
        JButton backButton = createStyledButton("Back", SECONDARY_COLOR, TEXT_COLOR);
        backButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
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

    private void addDriver() {
        try {
            conn c = new conn();
            String query = "INSERT INTO Ambulance (Name, Gender, Car_name, Available, Location) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = c.connection.prepareStatement(query);

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, genderField.getText());
            pstmt.setString(3, carNameField.getText());
            pstmt.setString(4, availableField.getText());
            pstmt.setString(5, locationField.getText());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Driver Added Successfully!");
                loadTableData();
                clearInputFields();
            }
            pstmt.close();
            c.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error adding driver: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDriver() {
        try {
            if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a driver to update (Name field cannot be empty)",
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn c = new conn();
            String query = "UPDATE Ambulance SET Gender = ?, Car_name = ?, Available = ?, Location = ? WHERE Name = ?";
            PreparedStatement pstmt = c.connection.prepareStatement(query);

            pstmt.setString(1, genderField.getText());
            pstmt.setString(2, carNameField.getText());
            pstmt.setString(3, availableField.getText());
            pstmt.setString(4, locationField.getText());
            pstmt.setString(5, nameField.getText());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Driver Updated Successfully!");
                loadTableData();
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No driver found with the given Name",
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
            c.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating driver: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDriver() {
        try {
            if (nameField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select a driver to delete (Name field cannot be empty)",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            conn c = new conn();
            String query = "DELETE FROM Ambulance WHERE Name = ?";
            PreparedStatement pstmt = c.connection.prepareStatement(query);

            pstmt.setString(1, nameField.getText());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Driver Deleted Successfully!");
                loadTableData();
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No driver found with the given Name",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
            c.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error deleting driver: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        genderField.setText("");
        carNameField.setText("");
        availableField.setText("");
        locationField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Ambulance());
    }
}