package hospital.management.system;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton;
    private final Color PRIMARY_COLOR = new Color(41, 128, 185); // Modern blue
    private final Color ACCENT_COLOR = new Color(231, 76, 60);  // Modern red
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color TEXT_COLOR = new Color(52, 73, 94);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public Login() {
        setTitle("Health Safari - Login");
        initializeUI();
        setupLayout();
        setupActionListeners();

        // Window settings
        setSize(800, 400);
        setLocationRelativeTo(null); // Center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void initializeUI() {
        // Set the modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Main background
        getContentPane().setBackground(BACKGROUND_COLOR);
        setLayout(new BorderLayout());
    }

    private void setupLayout() {
        // Left Panel - Login Form
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBackground(BACKGROUND_COLOR);
        loginPanel.setBorder(new EmptyBorder(50, 50, 50, 30));

        // Login header
        JLabel headerLabel = new JLabel("Welcome Back");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        headerLabel.setForeground(PRIMARY_COLOR);
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subHeaderLabel = new JLabel("Please enter your credentials to continue");
        subHeaderLabel.setFont(REGULAR_FONT);
        subHeaderLabel.setForeground(TEXT_COLOR);
        subHeaderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add spacing
        loginPanel.add(headerLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(subHeaderLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(HEADER_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = new JTextField();
        usernameField.setFont(REGULAR_FONT);
        usernameField.setMaximumSize(new Dimension(300, 35));
        usernameField.setPreferredSize(new Dimension(300, 35));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(HEADER_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = new JPasswordField();
        passwordField.setFont(REGULAR_FONT);
        passwordField.setMaximumSize(new Dimension(300, 35));
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Add form elements with spacing
        loginPanel.add(usernameLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(usernameField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        loginPanel.add(passwordLabel);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        loginPanel.add(passwordField);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = new JButton("Login");
        loginButton.setFont(HEADER_FONT);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 40));

        cancelButton = new JButton("Cancel");
        cancelButton.setFont(HEADER_FONT);
        cancelButton.setBackground(ACCENT_COLOR);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setPreferredSize(new Dimension(120, 40));

        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        buttonPanel.add(cancelButton);

        loginPanel.add(buttonPanel);

        // Right Panel - Logo and Image
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setBackground(PRIMARY_COLOR);

        // Logo
        ImageIcon logoIcon = new ImageIcon(ClassLoader.getSystemResource("icon/Health Safari bluebg.png"));
        Image img = logoIcon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
        ImageIcon scaledLogo = new ImageIcon(img);
        JLabel logoLabel = new JLabel(scaledLogo);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        logoPanel.add(logoLabel, BorderLayout.CENTER);

        // Add both panels to main frame
        add(loginPanel, BorderLayout.WEST);
        add(logoPanel, BorderLayout.CENTER);
    }

    private void setupActionListeners() {
        loginButton.addActionListener(this);
        cancelButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            try {
                conn c = new conn();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Use prepared statements in a real application to prevent SQL injection
                String query = "SELECT * FROM login WHERE ID='" + username + "' AND PW='" + password + "'";
                ResultSet resultSet = c.statement.executeQuery(query);

                if (resultSet.next()) {
                    new Reception();
                    dispose(); // Close login window
                } else {
                    showErrorDialog("Invalid Credentials", "Username or password is incorrect. Please try again.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorDialog("Database Error", "Could not connect to database. Please try again later.");
            }
        } else if (e.getSource() == cancelButton) {
            System.exit(0);
        }
    }

    private void showErrorDialog(String title, String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login());
    }
}