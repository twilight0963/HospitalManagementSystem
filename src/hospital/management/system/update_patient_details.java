package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class update_patient_details extends JFrame {
    // Theme Colors (same as Employee_info)
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF); // #EEF7FF (lightest blue for borders)
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C); // #4D869C (darkest teal for background)
    private static final Color TEXT_COLOR = new Color(0, 0, 0); // Black for text (as in Employee_info)
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2); // #7AB2B2 (medium teal for buttons/headers)
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5); // #CDE8E5 (light teal for input background)

    // Fonts (same as Employee_info)
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    // UI Components
    private Choice patientChoice;
    private JTextField roomTextField, inTimeTextField, amountTextField, pendingTextField;

    public update_patient_details() {
        // Frame Setup
        setTitle("Update Patient Details");
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

        // Title Label
        JLabel titleLabel = new JLabel("Update Patient Details", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Panel (Left Side)
        JPanel formPanel = createFormPanel();
        mainPanel.add(formPanel, BorderLayout.WEST);

        // Image Panel (Right Side)
        JPanel imagePanel = createImagePanel();
        mainPanel.add(imagePanel, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        // Frame Styling
        setSize(950, 500); // Match the size of the original
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setPreferredSize(new Dimension(400, 300));

        // Name Selection
        JLabel nameLabel = createLabel("Name:");
        patientChoice = new Choice();
        patientChoice.setFont(INPUT_FONT);
        patientChoice.setBackground(SECONDARY_COLOR);
        patientChoice.setForeground(TEXT_COLOR);

        // Populate patient names
        try {
            conn connection = new conn();
            ResultSet resultSet = connection.statement.executeQuery("select * from Patient_Info");
            while (resultSet.next()) {
                patientChoice.add(resultSet.getString("Name"));
            }
            connection.statement.close();
            connection.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Room Number
        JLabel roomLabel = createLabel("Room Number:");
        roomTextField = createTextField("");

        // In-Time
        JLabel inTimeLabel = createLabel("In-Time:");
        inTimeTextField = createTextField("");

        // Amount Paid
        JLabel amountLabel = createLabel("Amount Paid (Rs):");
        amountTextField = createTextField("");

        // Pending Amount
        JLabel pendingLabel = createLabel("Pending Amount (Rs):");
        pendingTextField = createTextField("");
        pendingTextField.setEditable(false); // Pending amount is calculated, not editable

        // Add components to panel
        panel.add(nameLabel);
        panel.add(patientChoice);
        panel.add(roomLabel);
        panel.add(roomTextField);
        panel.add(inTimeLabel);
        panel.add(inTimeTextField);
        panel.add(amountLabel);
        panel.add(amountTextField);
        panel.add(pendingLabel);
        panel.add(pendingTextField);

        return panel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Image loading
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/updated.png"));
        Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(image);
        JLabel label = new JLabel(imageIcon1);
        panel.add(label);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        // Check Button
        JButton checkButton = createStyledButton("CHECK", ACCENT_COLOR, TEXT_COLOR);
        checkButton.addActionListener(e -> {
            String patientName = patientChoice.getSelectedItem();
            String query = "select * from Patient_Info where Name = '" + patientName + "'";
            try {
                conn connection = new conn();
                ResultSet resultSet = connection.statement.executeQuery(query);

                if (resultSet.next()) {
                    roomTextField.setText(resultSet.getString("Room_Number"));
                    inTimeTextField.setText(resultSet.getString("Time"));
                    amountTextField.setText(resultSet.getString("Deposite"));
                }

                ResultSet roomResultSet = connection.statement.executeQuery(
                        "select * from room where room_no = '" + roomTextField.getText() + "'"
                );

                if (roomResultSet.next()) {
                    String price = roomResultSet.getString("Price");
                    int amountPaid = Integer.parseInt(price) - Integer.parseInt(amountTextField.getText());
                    pendingTextField.setText("" + amountPaid);
                }

                connection.statement.close();
                connection.connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Update Button
        JButton updateButton = createStyledButton("UPDATE", ACCENT_COLOR, TEXT_COLOR);
        updateButton.addActionListener(e -> {
            try {
                conn connection = new conn();
                String patientName = patientChoice.getSelectedItem();
                String updateQuery = "update Patient_Info set Room_Number = '" + roomTextField.getText() +
                        "', Time = '" + inTimeTextField.getText() +
                        "', Deposite = '" + amountTextField.getText() +
                        "' where name = '" + patientName + "'";

                connection.statement.executeUpdate(updateQuery);
                JOptionPane.showMessageDialog(null, "Updated Successfully");
                setVisible(false);
                connection.statement.close();
                connection.connection.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Back Button
        JButton backButton = createStyledButton("BACK", SECONDARY_COLOR, TEXT_COLOR);
        backButton.addActionListener(e -> setVisible(false));

        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
        buttonPanel.add(checkButton);

        return buttonPanel;
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

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(LABEL_FONT);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new update_patient_details());
    }
}