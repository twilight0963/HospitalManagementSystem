package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Reception extends JFrame {

    // Theme colors
    private final Color SIDEBAR_COLOR = new Color(48, 56, 65); // Dark Gray-Blue
    private final Color SIDEBAR_HOVER_COLOR = new Color(69, 79, 91);
    private final Color MAIN_BACKGROUND_COLOR = new Color(245, 247, 249); // Light Gray
    private final Color TEXT_COLOR = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(63, 81, 181); // Material Design Blue

    // Fonts
    private final Font SIDEBAR_FONT = new Font("Roboto", Font.PLAIN, 14);
    private final Font TITLE_FONT = new Font("Roboto", Font.BOLD, 24);
    private final Font HEADER_FONT = new Font("Roboto", Font.PLAIN, 16);

    public Reception() {
        setTitle("Health Safari - Management System");

        initializeUI();
        setupLayout();

        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());// Use system look and feel

        } catch (Exception e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
    }

    private void setupLayout() {
        // Sidebar
        JPanel sideBar = createSideBar();
        add(sideBar, BorderLayout.WEST);

        // Main Content Area
        JPanel mainContent = createMainContentPanel();
        add(mainContent, BorderLayout.CENTER);
    }

    private JPanel createSideBar() {
        JPanel sideBar = new JPanel();
        sideBar.setBackground(SIDEBAR_COLOR);
        sideBar.setPreferredSize(new Dimension(250, getHeight()));
        sideBar.setLayout(new BorderLayout());

        // Logo Panel
        JPanel logoPanel = createLogoPanel();
        sideBar.add(logoPanel, BorderLayout.NORTH);

        // Navigation Panel
        JPanel navigationPanel = createNavigationPanel();
        sideBar.add(navigationPanel, BorderLayout.CENTER);

        return sideBar;
    }

    private JPanel createLogoPanel() {
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(SIDEBAR_COLOR);
        logoPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

//        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/Health Safari bluebg.png"));
//        Image img = logoIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
//        JLabel logoLabel = new JLabel(new ImageIcon(img));

        JLabel titleLabel = new JLabel("HealthSafari");
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 10, 0, 0));

        //logoPanel.add(logoLabel);
        logoPanel.add(titleLabel);

        return logoPanel;
    }

    private JPanel createNavigationPanel() {
        JPanel navigationPanel = new JPanel();
        navigationPanel.setBackground(SIDEBAR_COLOR);
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.Y_AXIS));

        String[][] menuItems = {
                {"Add Patient", "icon/patient.png"},
                {"Room Management", "icon/roomm.png"},
                {"Departments", "icon/department.png"},
                {"Employee Info", "icon/employee.png"},
                {"Patient Info", "icon/patient.png"},
                {"Patient Discharge", "icon/discharge.png"},
                {"Update Patient", "icon/updated.png"},
                {"Ambulance", "icon/ambulance.png"},
                {"Search Room", "icon/searchh.png"}
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
        // ... (Same action handling logic as before) ...
        switch (buttonText) {
            case "Add Patient":
                new NEW_PATIENT();
                break;
            case "Room Management":
                new Room();
                break;
            case "Departments":
                new Department();
                break;
            case "Employee Info":
                new Employee_info();
                break;
            case "Patient Info":
                new ALL_Patient_Info();
                break;
            case "Patient Discharge":
                new patient_discharge();
                break;
            case "Update Patient":
                new update_patient_details();
                break;
            case "Ambulance":
                new Ambulance();
                break;
            case "Search Room":
                new SearchRoom();
                break;
        }
    }

    private JPanel createMainContentPanel() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(MAIN_BACKGROUND_COLOR);

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainContent.add(headerPanel, BorderLayout.NORTH);

        // Dashboard Content
        JPanel dashboardContent = new JPanel(new BorderLayout());
        dashboardContent.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome to Health Safari");
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(Color.DARK_GRAY);
        dashboardContent.add(welcomeLabel, BorderLayout.NORTH);

        mainContent.add(dashboardContent, BorderLayout.CENTER);

        return mainContent;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60));
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(211,47,47)); // Material red
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Reception::new);
    }
}