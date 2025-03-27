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
import java.sql.SQLException;

public class Employee_info extends JFrame {
    // Theme Colors
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF);
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2);
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5);

    // Fonts
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // UI Components
    private JTable table;
    private JTextField nameField, ageField, phoneField, salaryField, gmailField, aadharField;

    public Employee_info() {
        // Frame Setup
        setTitle("Employee Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout(10, 10));

        // Main Panel with Table and Input Fields
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
        setSize(900, 600);
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

        // Add mouse listener to populate fields on row click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    nameField.setText(table.getValueAt(selectedRow, 0).toString());
                    ageField.setText(table.getValueAt(selectedRow, 1).toString());
                    phoneField.setText(table.getValueAt(selectedRow, 2).toString());
                    salaryField.setText(table.getValueAt(selectedRow, 3).toString());
                    gmailField.setText(table.getValueAt(selectedRow, 4).toString());
                    aadharField.setText(table.getValueAt(selectedRow, 5).toString());
                }
            }
        });

        // Load Data
        loadEmployeeData();

        // Scrollpane for Table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void customizeTable(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setBackground(ACCENT_COLOR); // Teal for header
        header.setForeground(TEXT_COLOR);
        header.setFont(LABEL_FONT);

        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setFont(INPUT_FONT);
        table.setRowHeight(30);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }

    private void loadEmployeeData() {
        conn connection = null;
        try {
            connection = new conn();
            String query = "SELECT * FROM EMP_INFO";
            ResultSet resultSet = connection.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading employee data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (connection != null) {
                try {
                    if (connection.statement != null) connection.statement.close();
                    if (connection.connection != null) connection.connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        inputPanel.setBackground(BACKGROUND_COLOR);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        nameField = createTextField("Name");
        ageField = createTextField("Age");
        phoneField = createTextField("Phone Number");
        salaryField = createTextField("Salary");
        gmailField = createTextField("Gmail");
        aadharField = createTextField("Aadhar Number");

        inputPanel.add(createLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(createLabel("Age:"));
        inputPanel.add(ageField);
        inputPanel.add(createLabel("Phone:"));
        inputPanel.add(phoneField);
        inputPanel.add(createLabel("Salary:"));
        inputPanel.add(salaryField);
        inputPanel.add(createLabel("Gmail:"));
        inputPanel.add(gmailField);
        inputPanel.add(createLabel("Aadhar:"));
        inputPanel.add(aadharField);

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
        textField.setBackground(SECONDARY_COLOR); // Dark gray for input fields
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

        JButton addButton = createStyledButton("Add Employee", ACCENT_COLOR, TEXT_COLOR);
        addButton.addActionListener(e -> addEmployee());

        JButton updateButton = createStyledButton("Update Employee", ACCENT_COLOR, TEXT_COLOR);
        updateButton.addActionListener(e -> updateEmployee());

        JButton deleteButton = createStyledButton("Delete Employee", ACCENT_COLOR, TEXT_COLOR);
        deleteButton.addActionListener(e -> deleteEmployee());

        JButton backButton = createStyledButton("Back", SECONDARY_COLOR, TEXT_COLOR);
        backButton.addActionListener(e -> dispose());

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

    private void addEmployee() {
        conn connection = null;
        try {
            connection = new conn();
            String query = "INSERT INTO EMP_INFO (Name, Age, Phone_Number, Salary, Gmail, Aadhar_Number) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = connection.connection.prepareStatement(query);

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, ageField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setString(4, salaryField.getText());
            pstmt.setString(5, gmailField.getText());
            pstmt.setString(6, aadharField.getText());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Employee Added Successfully!");
                loadEmployeeData();
                clearInputFields();
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error adding employee: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    if (connection.statement != null) connection.statement.close();
                    if (connection.connection != null) connection.connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateEmployee() {
        conn connection = null;
        try {
            if (aadharField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter Aadhar Number to update an employee",
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            connection = new conn();
            String query = "UPDATE EMP_INFO SET Name=?, Age=?, Phone_Number=?, Salary=?, Gmail=? WHERE Aadhar_Number=?";
            PreparedStatement pstmt = connection.connection.prepareStatement(query);

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, ageField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setString(4, salaryField.getText());
            pstmt.setString(5, gmailField.getText());
            pstmt.setString(6, aadharField.getText());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Employee Updated Successfully!");
                loadEmployeeData();
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No employee found with the given Aadhar Number",
                        "Update Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error updating employee: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    if (connection.statement != null) connection.statement.close();
                    if (connection.connection != null) connection.connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void deleteEmployee() {
        conn connection = null;
        try {
            if (aadharField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please enter Aadhar Number to delete an employee",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            connection = new conn();
            String query = "DELETE FROM EMP_INFO WHERE Aadhar_Number=?";
            PreparedStatement pstmt = connection.connection.prepareStatement(query);

            pstmt.setString(1, aadharField.getText());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Employee Deleted Successfully!");
                loadEmployeeData();
                clearInputFields();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No employee found with the given Aadhar Number",
                        "Delete Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting employee: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    if (connection.statement != null) connection.statement.close();
                    if (connection.connection != null) connection.connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearInputFields() {
        nameField.setText("");
        ageField.setText("");
        phoneField.setText("");
        salaryField.setText("");
        gmailField.setText("");
        aadharField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Employee_info());
    }
}