package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class Reception extends JFrame {

    // Theme colors
    private final Color SIDEBAR_COLOR = new Color(77, 134, 156);
    private final Color SIDEBAR_HOVER_COLOR = new Color(122, 178, 178);
    private final Color MAIN_BACKGROUND_COLOR = new Color(205, 232, 229);
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(122, 178, 178);

    // Fonts
    private final Font SIDEBAR_FONT = new Font("Roboto", Font.PLAIN, 14);
    private final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 24);
    private final Font HEADER_FONT = new Font("Roboto", Font.PLAIN, 16);

    // Database connection
    private Connection conn;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/hospital_management_system";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "vilenop1234";

    public Reception() {
        setTitle("Health Safari - Management System");

        initializeDatabase();
        initializeUI();
        setupLayout();

        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initializeUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
    }

    private void setupLayout() {
        JPanel sideBar = createSideBar();
        add(sideBar, BorderLayout.WEST);

        JPanel mainContent = createMainContentPanel();
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createSideBar() {
        JPanel sideBar = new JPanel();
        sideBar.setBackground(SIDEBAR_COLOR);
        sideBar.setPreferredSize(new Dimension(250, getHeight()));
        sideBar.setLayout(new BorderLayout());

        JPanel logoPanel = createLogoPanel();
        sideBar.add(logoPanel, BorderLayout.NORTH);

        JPanel navigationPanel = createNavigationPanel();
        sideBar.add(navigationPanel, BorderLayout.CENTER);

        return sideBar;
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_COLOR);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel titleLabel = new JLabel("HealthSafari");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        logoPanel.add(titleLabel);
        return logoPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(SIDEBAR_COLOR);
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));

        String[][] menuItems = {
                {"Add Patient", "icon/add_patient.png"},
                {"Room Management", "icon/roomm.png"},
                {"Departments", "icon/department.png"},
                {"Employee Info", "icon/employee.png"},
                {"Patient Info", "icon/patient_info.png"},
                {"Patient Discharge", "icon/discharge.png"},
                {"Update Patient", "icon/update.png"},
                {"Ambulance", "icon/ambulance.png"},
                {"Search Room", "icon/search.png"}
        };

        for (String[] item : menuItems) {
            navigationPanel.add(createSidebarButton(item[0], item[1]));
        }
        return navigationPanel;
    }

    private JButton createSidebarButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(SIDEBAR_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(SIDEBAR_COLOR);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(0, 20, 0, 0));
        button.setFocusPainted(false);

        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
            Image img = icon.getImage().getScaledInstance(24, 24, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            button.setIconTextGap(15);
        } catch (Exception e) {
            // Icon not found, continue with text only
        }

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SIDEBAR_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(SIDEBAR_COLOR);
            }
        });

        button.addActionListener(e -> handleButtonAction(text));
        return button;
    }

    private void handleButtonAction(String buttonText) {
        switch (buttonText) {
            case "Add Patient": new NEW_PATIENT(); break;
            case "Room Management": new Room(); break;
            case "Departments": new Department(); break;
            case "Employee Info": new Employee_info(); break;
            case "Patient Info": new ALL_Patient_Info(); break;
            case "Patient Discharge": new patient_discharge(); break;
            case "Update Patient": new update_patient_details(); break;
            case "Ambulance": new Ambulance(); break;
            case "Search Room": new SearchRoom(); break;
        }
    }

    private JPanel createMainContentPanel() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(MAIN_BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();
        mainContent.add(headerPanel, BorderLayout.NORTH);

        JPanel dashboardContent = new JPanel(new BorderLayout());
        dashboardContent.setBorder(new EmptyBorder(20, 20, 20, 20));
        dashboardContent.setBackground(MAIN_BACKGROUND_COLOR);

        JLabel welcomeLabel = new JLabel("Welcome to Health Safari");
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(Color.DARK_GRAY);
        dashboardContent.add(welcomeLabel, BorderLayout.NORTH);

        JPanel quickStatsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        quickStatsPanel.setBackground(MAIN_BACKGROUND_COLOR);
        quickStatsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        try {
            String[][] statsData = {
                    {"Total Patients", "icon/patient_info.png", getCountFromTable("patient_info")},
                    {"Occupied Rooms", "icon/roomm.png", getOccupiedRoomsCount()},
                    {"Departments", "icon/department.png", getCountFromTable("department")},
                    {"Employees", "icon/employee.png", getCountFromTable("EMP_INFO")},
                    {"Ambulances", "icon/ambulance.png", getCountFromTable("Ambulance")},
                    {"Discharges Today", "icon/discharge.png", getDischargesToday()}
            };

            for (String[] stat : statsData) {
                JPanel statCard = createStatCard(stat[0], stat[1], stat[2]);
                quickStatsPanel.add(statCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            String[][] statsData = {
                    {"Total Patients", "icon/patient_info.png", "0"},
                    {"Occupied Rooms", "icon/roomm.png", "0"},
                    {"Departments", "icon/department.png", "0"},
                    {"Employees", "icon/employee.png", "0"},
                    {"Ambulances", "icon/ambulance.png", "0"},
                    {"Discharges Today", "icon/discharge.png", "0"}
            };

            for (String[] stat : statsData) {
                JPanel statCard = createStatCard(stat[0], stat[1], stat[2]);
                quickStatsPanel.add(statCard);
            }
        }

        dashboardContent.add(quickStatsPanel, BorderLayout.CENTER);
        mainContent.add(dashboardContent, BorderLayout.CENTER);
        return mainContent;
    }

    private String getCountFromTable(String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        }
        return "0";
    }

    private String getOccupiedRoomsCount() throws SQLException {
        String query = "SELECT COUNT(*) FROM Room WHERE Availability = 'Occupied'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        }
        return "0";
    }

    private String getDischargesToday() throws SQLException {
        String query = "SELECT COUNT(*) FROM patient_info WHERE DATE(Time) = CURDATE()";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return String.valueOf(rs.getInt(1));
            }
        }
        return "0";
    }

    private JPanel createStatCard(String title, String iconPath, String value) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));

        try {
            ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource(iconPath));
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel iconLabel = new JLabel(new ImageIcon(img));
            card.add(iconLabel, BorderLayout.WEST);
        } catch (Exception e) {
            // Icon not found, skip adding
        }

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Roboto", Font.PLAIN, 14));
        titleLabel.setForeground(Color.DARK_GRAY);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        valueLabel.setForeground(PRIMARY_COLOR);

        textPanel.add(titleLabel, BorderLayout.NORTH);
        textPanel.add(valueLabel, BorderLayout.CENTER);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(211,47,47));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(HEADER_FONT);
        logoutButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        logoutButton.addActionListener(e -> {
            dispose();
            new Login();
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutButton);
        headerPanel.add(logoutPanel, BorderLayout.EAST);
        return headerPanel;
    }

    @Override
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Reception::new);
    }
}