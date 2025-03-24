package hospital.management.system;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Reception extends JFrame {

    // Theme colors
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);  // Primary blue
    private final Color SECONDARY_COLOR = new Color(52, 152, 219); // Secondary blue
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(52, 73, 94);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public Reception() {
        setTitle("Health Safari - Reception Dashboard");
        initializeUI();
        setupComponents();

        setSize(1200, 800);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeUI() {
        // Set the modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main layout
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void setupComponents() {
        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create main content panel
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Hospital logo and name
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        brandPanel.setOpaque(false);

        // Logo image
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/Health Safari bluebg.png"));
        Image img = logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon scaledLogo = new ImageIcon(img);
        JLabel logoLabel = new JLabel(scaledLogo);

        // Hospital name
        JLabel hospitalName = new JLabel("Health Safari");
        hospitalName.setFont(new Font("Segoe UI", Font.BOLD, 24));
        hospitalName.setForeground(Color.WHITE);

        brandPanel.add(logoLabel);
        brandPanel.add(hospitalName);
        headerPanel.add(brandPanel, BorderLayout.WEST);

        // Logout button
        JButton logoutButton = createStyledButton("Logout", new Color(231, 76, 60));
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new Login();
            }
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Welcome message
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(BACKGROUND_COLOR);
        welcomePanel.setBorder(new EmptyBorder(0, 0, 30, 0));

        JLabel welcomeLabel = new JLabel("Welcome to Health Safari Management System");
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(PRIMARY_COLOR);
        welcomePanel.add(welcomeLabel, BorderLayout.WEST);

        // Date and time could go here

        contentPanel.add(welcomePanel, BorderLayout.NORTH);

        // Main dashboard panels
        JPanel dashboardPanel = new JPanel(new BorderLayout());
        dashboardPanel.setBackground(BACKGROUND_COLOR);

        // Action buttons panel - using grid layout
        JPanel buttonsPanel = createButtonsPanel();
        dashboardPanel.add(buttonsPanel, BorderLayout.CENTER);

        // Main image or summary panel
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(BorderFactory.createLineBorder(new Color(225, 225, 225), 1));

        // Hospital image
        ImageIcon hospitalIcon = new ImageIcon(ClassLoader.getSystemResource("icon/Health Safari2.png"));
        Image hospitalImg = hospitalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        ImageIcon scaledHospitalIcon = new ImageIcon(hospitalImg);
        JLabel hospitalImageLabel = new JLabel(scaledHospitalIcon);
        hospitalImageLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setOpaque(false);
        imagePanel.add(hospitalImageLabel, BorderLayout.CENTER);

        JLabel tagline = new JLabel("Providing Quality Healthcare Services", JLabel.CENTER);
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        tagline.setForeground(TEXT_COLOR);
        tagline.setBorder(new EmptyBorder(20, 0, 20, 0));

        summaryPanel.add(imagePanel, BorderLayout.CENTER);
        summaryPanel.add(tagline, BorderLayout.SOUTH);

        dashboardPanel.add(summaryPanel, BorderLayout.EAST);

        contentPanel.add(dashboardPanel, BorderLayout.CENTER);

        return contentPanel;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 3, 20, 20));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        buttonsPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

        // Create dashboard buttons with icons
        JButton addPatientBtn = createDashboardButton("Add New Patient", "icon/patient.png");
        addPatientBtn.addActionListener(e -> new NEW_PATIENT());

        JButton roomBtn = createDashboardButton("Room Management", "icon/roomm.png");
        roomBtn.addActionListener(e -> new Room());

        JButton departmentBtn = createDashboardButton("Department", "icon/department.png");
        departmentBtn.addActionListener(e -> new Department());

        JButton employeeInfoBtn = createDashboardButton("Employee Info", "icon/employee.png");
        employeeInfoBtn.addActionListener(e -> new Employee_info());

        JButton patientInfoBtn = createDashboardButton("Patient Info", "icon/patient.png");
        patientInfoBtn.addActionListener(e -> new ALL_Patient_Info());

        JButton dischargeBtn = createDashboardButton("Patient Discharge", "icon/discharge.png");
        dischargeBtn.addActionListener(e -> new patient_discharge());

        JButton updatePatientBtn = createDashboardButton("Update Patient", "icon/updated.png");
        updatePatientBtn.addActionListener(e -> new update_patient_details());

        JButton ambulanceBtn = createDashboardButton("Ambulance", "icon/ambulance.png");
        ambulanceBtn.addActionListener(e -> new Ambulance());

        JButton searchRoomBtn = createDashboardButton("Search Room", "icon/searchh.png");
        searchRoomBtn.addActionListener(e -> new SearchRoom());

        // Add buttons to panel
        buttonsPanel.add(addPatientBtn);
        buttonsPanel.add(roomBtn);
        buttonsPanel.add(departmentBtn);
        buttonsPanel.add(employeeInfoBtn);
        buttonsPanel.add(patientInfoBtn);
        buttonsPanel.add(dischargeBtn);
        buttonsPanel.add(updatePatientBtn);
        buttonsPanel.add(ambulanceBtn);
        buttonsPanel.add(searchRoomBtn);

        return buttonsPanel;
    }

    private JButton createDashboardButton(String text, String iconPath) {
        JButton button = new JButton(text);

        // Use default icon if specific icon not found
        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            button.setIconTextGap(10);
        } catch (Exception e) {
            // If icon not found, just use text
        }

        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(SECONDARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SECONDARY_COLOR);
            }
        });

        return button;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));

        // Hover effect
        Color darkerColor = new Color(
                Math.max((int)(bgColor.getRed() * 0.8), 0),
                Math.max((int)(bgColor.getGreen() * 0.8), 0),
                Math.max((int)(bgColor.getBlue() * 0.8), 0)
        );

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkerColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Reception());
    }
}