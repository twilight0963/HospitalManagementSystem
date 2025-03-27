package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class patient_discharge extends JFrame {
    // Color palette as specified in Department class
    private static final Color PRIMARY_COLOR = new Color(0xEE, 0xF7, 0xFF); // #EEF7FF (lightest blue for borders)
    private static final Color BACKGROUND_COLOR = new Color(0x4D, 0x86, 0x9C); // #4D869C (darkest teal for background)
    private static final Color TEXT_COLOR = new Color(0, 0, 0); // Black for text
    private static final Color ACCENT_COLOR = new Color(0x7A, 0xB2, 0xB2); // #7AB2B2 (medium teal for buttons/headers)
    private static final Color SECONDARY_COLOR = new Color(0xCD, 0xE8, 0xE5); // #CDE8E5 (light teal for input backgrounds)

    patient_discharge() {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set frame properties
        setTitle("Patient Discharge");
        setSize(800, 400); // Matches Department class size
        setLocationRelativeTo(null); // Center the window, replacing setLocation(400, 250)
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Explicitly set the content pane background to avoid white screen
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Main Panel with modern styling
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(panel, BorderLayout.CENTER);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel("PATIENT CHECK-OUT");
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        label.setForeground(TEXT_COLOR); // Black for title text
        titlePanel.add(label);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Form Panel
        JPanel formPanel = new JPanel();
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Customer ID Label
        JLabel label2 = new JLabel("Customer ID");
        label2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label2.setForeground(TEXT_COLOR); // Black for label text
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(label2, gbc);

        // Patient ID Choice
        Choice choice = new Choice();
        choice.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        choice.setBackground(SECONDARY_COLOR); // #CDE8E5 for input background
        choice.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        formPanel.add(choice, gbc);

        // Populate patient IDs
        try {
            conn c = new conn();
            ResultSet resultSet = c.statement.executeQuery("select * from Patient_Info");
            while (resultSet.next()) {
                choice.add(resultSet.getString("number"));
            }
            System.out.println("Patient IDs loaded successfully");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading patient IDs: " + e.getMessage());
        }

        // Room Number Label
        JLabel label3 = new JLabel("Room Number");
        label3.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label3.setForeground(TEXT_COLOR); // Black for label text
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(label3, gbc);

        // Room Number Display
        JLabel RNo = new JLabel();
        RNo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        RNo.setForeground(TEXT_COLOR); // Black for display text
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(RNo, gbc);

        // In Time Label
        JLabel label4 = new JLabel("Admission Time");
        label4.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label4.setForeground(TEXT_COLOR); // Black for label text
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(label4, gbc);

        // In Time Display
        JLabel INTime = new JLabel();
        INTime.setFont(new Font("Segoe UI", Font.BOLD, 14));
        INTime.setForeground(TEXT_COLOR); // Black for display text
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(INTime, gbc);

        // Out Time Label
        JLabel label5 = new JLabel("Discharge Time");
        label5.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label5.setForeground(TEXT_COLOR); // Black for label text
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(label5, gbc);

        // Out Time Display
        Date date = new Date();
        JLabel OUTTime = new JLabel("" + date);
        OUTTime.setFont(new Font("Segoe UI", Font.BOLD, 14));
        OUTTime.setForeground(TEXT_COLOR); // Black for display text
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(OUTTime, gbc);

        panel.add(formPanel, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // Discharge Button
        JButton discharge = new JButton("Discharge");
        discharge.setBackground(ACCENT_COLOR); // #7AB2B2 for button background
        discharge.setForeground(TEXT_COLOR); // Black for button text
        discharge.setFont(new Font("Segoe UI", Font.BOLD, 12));
        discharge.setFocusPainted(false);
        discharge.setPreferredSize(new Dimension(120, 35));
        discharge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conn c = new conn();
                try {
                    c.statement.executeUpdate("delete from Patient_Info where number = '" + choice.getSelectedItem() + "'");
                    c.statement.executeUpdate("update room set Availability = 'Available' where room_no = '" + RNo.getText() + "'");

                    JOptionPane.showMessageDialog(null,
                            "Patient Discharged Successfully",
                            "Discharge Confirmation",
                            JOptionPane.INFORMATION_MESSAGE);
                    setVisible(false);
                } catch (Exception E) {
                    JOptionPane.showMessageDialog(null,
                            "Error in Discharge Process",
                            "Discharge Error",
                            JOptionPane.ERROR_MESSAGE);
                    E.printStackTrace();
                }
            }
        });
        buttonPanel.add(discharge);

        // Check Button
        JButton Check = new JButton("Check Details");
        Check.setBackground(ACCENT_COLOR); // #7AB2B2 for button background
        Check.setForeground(TEXT_COLOR); // Black for button text
        Check.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Check.setFocusPainted(false);
        Check.setPreferredSize(new Dimension(120, 35));
        Check.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conn c = new conn();
                try {
                    ResultSet resultSet = c.statement.executeQuery("select * from Patient_Info where number = '" + choice.getSelectedItem() + "'");
                    while (resultSet.next()) {
                        RNo.setText(resultSet.getString("Room_Number"));
                        INTime.setText(resultSet.getString("Time"));
                    }
                } catch (Exception E) {
                    JOptionPane.showMessageDialog(null,
                            "Unable to Retrieve Patient Details",
                            "Retrieval Error",
                            JOptionPane.ERROR_MESSAGE);
                    E.printStackTrace();
                }
            }
        });
        buttonPanel.add(Check);

        // Back Button
        JButton Back = new JButton("Back");
        Back.setBackground(ACCENT_COLOR); // #7AB2B2 for button background
        Back.setForeground(TEXT_COLOR); // Black for button text
        Back.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Back.setFocusPainted(false);
        Back.setPreferredSize(new Dimension(120, 35));
        Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        buttonPanel.add(Back);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ensure the frame is visible
        setVisible(true);
        System.out.println("patient_discharge frame set to visible");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new patient_discharge());
    }
}