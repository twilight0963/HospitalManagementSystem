package hospital.management.system;

import net.proteanit.sql.DbUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Department extends JFrame {
    // Theme Colors
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF);
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C);
    private static final Color TEXT_COLOR = new Color(0, 0, 0);
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2);
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5);

    // Fonts
    private final Font HEADER_FONT = new Font("Roboto", Font.BOLD, 16);
    private final Font TABLE_FONT = new Font("Roboto", Font.PLAIN, 14);
    private final Font BUTTON_FONT = new Font("Roboto", Font.BOLD, 12);

    private JTable table;

    public Department() {
        System.out.println("Department constructor started");
        initializeFrame();
        setupUI();
        System.out.println("Department constructor finished");
    }

    private void initializeFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Hospital Management - Departments");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel tablePanel = createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);

        table = createDepartmentTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_COLOR));

        JLabel titleLabel = new JLabel("Hospital Departments", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR); // Black for title text
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        tablePanel.add(titleLabel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JTable createDepartmentTable() {
        table = new JTable();
        table.setFont(TABLE_FONT);
        table.setRowHeight(30);
        table.setBackground(SECONDARY_COLOR);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(TEXT_COLOR);

        JTableHeader header = table.getTableHeader();
        header.setBackground(ACCENT_COLOR);
        header.setForeground(TEXT_COLOR);
        header.setFont(HEADER_FONT);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        centerRenderer.setForeground(TEXT_COLOR);
        table.setDefaultRenderer(Object.class, centerRenderer);

        refreshTableData();

        return table;
    }

    private void refreshTableData() {
        try {
            conn connection = new conn();
            String query = "SELECT * FROM department";
            ResultSet resultSet = connection.statement.executeQuery(query);
            table.setModel(DbUtils.resultSetToTableModel(resultSet));
            connection.statement.close();
            connection.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading department data: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addButton = new JButton("Add Department");
        addButton.setFont(BUTTON_FONT);
        addButton.setBackground(ACCENT_COLOR);
        addButton.setForeground(TEXT_COLOR);
        addButton.setFocusPainted(false);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDepartment();
            }
        });

        JButton removeButton = new JButton("Remove Department");
        removeButton.setFont(BUTTON_FONT);
        removeButton.setBackground(ACCENT_COLOR);
        removeButton.setForeground(TEXT_COLOR);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeDepartment();
            }
        });

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(BUTTON_FONT);
        backButton.setBackground(ACCENT_COLOR);
        backButton.setForeground(TEXT_COLOR);
        backButton.setFocusPainted(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(backButton);

        return buttonPanel;
    }

    private void addDepartment() {
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField deptField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JLabel deptLabel = new JLabel("Department Name:");
        JLabel phoneLabel = new JLabel("Phone Number:");
        inputPanel.add(deptLabel);
        inputPanel.add(deptField);
        inputPanel.add(phoneLabel);
        inputPanel.add(phoneField);

        int result = JOptionPane.showConfirmDialog(this,
                inputPanel,
                "Add New Department",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String deptName = deptField.getText().trim();
            String phoneNo = phoneField.getText().trim();

            if (!deptName.isEmpty() && !phoneNo.isEmpty()) {
                try {
                    conn connection = new conn();
                    String query = "INSERT INTO department (Department, Phone_no) VALUES ('" + deptName + "', '" + phoneNo + "')";
                    connection.statement.executeUpdate(query);
                    connection.statement.close();
                    connection.connection.close();

                    JOptionPane.showMessageDialog(this,
                            "Department added successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);

                    refreshTableData();
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error adding department: " + e.getMessage(),
                            "Database Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please fill in all fields",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void removeDepartment() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a department to remove",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String deptName = (String) table.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove '" + deptName + "'?",
                "Confirm Removal",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                conn connection = new conn();
                String query = "DELETE FROM department WHERE Department = '" + deptName + "'";
                connection.statement.executeUpdate(query);
                connection.statement.close();
                connection.connection.close();

                JOptionPane.showMessageDialog(this,
                        "Department removed successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                refreshTableData();
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error removing department: " + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Department());
    }
}