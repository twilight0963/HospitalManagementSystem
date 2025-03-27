package hospital.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.Date;

public class NEW_PATIENT extends JFrame implements ActionListener {
    // Color palette from the provided theme
    private static final Color PRIMARY_COLOR = new Color(77, 134, 156); // #4D869C (deeper blue-teal for accents)
    private static final Color BACKGROUND_COLOR = new Color(205, 232, 229); // #CDE8E5 (light mint green for background)
    private static final Color TEXT_COLOR = new Color(77, 134, 156); // #4D869C (deeper blue-teal for text)
    private static final Color SECONDARY_COLOR = new Color(122, 178, 178); // #7AB2B2 (muted teal for secondary elements)

    // Fonts (unchanged)
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font INPUT_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    JComboBox<String> comboBox;
    JTextField textFieldNumber, textName, textFieldDisease, textFieldDeposite;
    JRadioButton r1, r2;
    Choice c1;
    JLabel date;
    JButton b1, b2;

    NEW_PATIENT() {
        // Modern panel setup
        JPanel panel = new JPanel();
        panel.setBounds(5, 5, 840, 540);
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(null);
        add(panel);

        // Update panel with a softer, more modern look
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR, 2), // #7AB2B2 for border
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Patient image with smoother scaling
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/patient.png"));
        Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon imageIcon1 = new ImageIcon(image);
        JLabel label = new JLabel(imageIcon1);
        label.setBounds(550, 150, 200, 200);
        panel.add(label);

        // Title
        JLabel labelName = new JLabel("NEW PATIENT REGISTRATION");
        labelName.setBounds(118, 11, 400, 53);
        labelName.setFont(TITLE_FONT);
        labelName.setForeground(PRIMARY_COLOR); // #4D869C for title
        panel.add(labelName);

        // Method to standardize label creation
        createLabel(panel, "ID :", 35, 76, labelID -> {
            labelID.setFont(LABEL_FONT);
            labelID.setForeground(TEXT_COLOR);
        });

        // Modernized combo box
        comboBox = new JComboBox<>(new String[]{"Aadhar Card", "Voter Id", "Driving License"});
        comboBox.setBounds(271, 73, 250, 30);
        comboBox.setFont(INPUT_FONT);
        comboBox.setBackground(new Color(238, 255, 255)); // #EEFFFF for input background
        comboBox.setForeground(TEXT_COLOR);
        panel.add(comboBox);

        // Consistent label and field creation
        createLabelAndField(panel, "Number :", 35, 111, textFieldNumber = new JTextField());
        createLabelAndField(panel, "Name :", 35, 151, textName = new JTextField());

        // Gender section
        createLabel(panel, "Gender :", 35, 191, labelGender -> {
            labelGender.setFont(LABEL_FONT);
            labelGender.setForeground(TEXT_COLOR);
        });

        // Radio buttons
        ButtonGroup genderGroup = new ButtonGroup();
        r1 = createRadioButton(panel, "Male", 271, 191, genderGroup);
        r2 = createRadioButton(panel, "Female", 350, 191, genderGroup);

        // Remaining fields
        createLabelAndField(panel, "Disease :", 35, 231, textFieldDisease = new JTextField());

        // Room selection
        createLabel(panel, "Room :", 35, 274, labelRoom -> {
            labelRoom.setFont(LABEL_FONT);
            labelRoom.setForeground(TEXT_COLOR);
        });

        c1 = new Choice();
        try {
            conn c = new conn();
            ResultSet resultSet = c.statement.executeQuery("select * from Room");
            while (resultSet.next()) {
                c1.add(resultSet.getString("room_no"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        c1.setBounds(271, 274, 250, 30);
        c1.setFont(INPUT_FONT);
        c1.setBackground(new Color(238, 255, 255)); // #EEFFFF for input background
        panel.add(c1);

        // Time display
        createLabel(panel, "Time :", 35, 316, labelDate -> {
            labelDate.setFont(LABEL_FONT);
            labelDate.setForeground(TEXT_COLOR);
        });

        Date date1 = new Date();
        date = new JLabel("" + date1);
        date.setBounds(271, 316, 250, 30);
        date.setForeground(TEXT_COLOR);
        date.setFont(INPUT_FONT);
        panel.add(date);

        // Deposit field
        createLabelAndField(panel, "Deposit :", 35, 359, textFieldDeposite = new JTextField());

        // Buttons
        b1 = createStyledButton(panel, "ADD", 100, 430, SECONDARY_COLOR, Color.WHITE); // #7AB2B2 for ADD button
        b2 = createStyledButton(panel, "Back", 260, 430, new Color(238, 255, 255), PRIMARY_COLOR); // #EEFFFF and #4D869C for Back button

        // Frame settings
        setUndecorated(false);
        setSize(850, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    // Helper method to create labels with custom styling
    private void createLabel(JPanel panel, String text, int x, int y,
                             java.util.function.Consumer<JLabel> styler) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, 200, 14);
        styler.accept(label);
        panel.add(label);
    }

    // Helper method to create labels and text fields together
    private void createLabelAndField(JPanel panel, String labelText,
                                     int labelX, int labelY, JTextField textField) {
        createLabel(panel, labelText, labelX, labelY, label -> {
            label.setFont(LABEL_FONT);
            label.setForeground(TEXT_COLOR);
        });

        textField.setBounds(271, labelY, 250, 30);
        textField.setFont(INPUT_FONT);
        textField.setBackground(new Color(238, 255, 255)); // #EEFFFF for input background
        panel.add(textField);
    }

    // Create radio buttons
    private JRadioButton createRadioButton(JPanel panel, String text,
                                           int x, int y, ButtonGroup group) {
        JRadioButton radioButton = new JRadioButton(text);
        radioButton.setBounds(x, y, 80, 30);
        radioButton.setFont(INPUT_FONT);
        radioButton.setBackground(BACKGROUND_COLOR);
        radioButton.setForeground(TEXT_COLOR);
        group.add(radioButton);
        panel.add(radioButton);
        return radioButton;
    }

    // Create styled buttons
    private JButton createStyledButton(JPanel panel, String text,
                                       int x, int y, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBounds(x, y, 120, 35);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(LABEL_FONT);
        button.setFocusPainted(false);
        button.addActionListener(this);
        panel.add(button);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            conn c = new conn();
            String radioBTN = null;
            if (r1.isSelected()) {
                radioBTN = "Male";
            } else if (r2.isSelected()) {
                radioBTN = "Female";
            }
            String s1 = (String) comboBox.getSelectedItem();
            String s2 = textFieldNumber.getText();
            String s3 = textName.getText();
            String s4 = radioBTN;
            String s5 = textFieldDisease.getText();
            String s6 = c1.getSelectedItem();
            String s7 = date.getText();
            String s8 = textFieldDeposite.getText();

            try {
                String q = "insert into Patient_Info values ('" + s1 + "', '" + s2 + "','" + s3 + "','" + s4 + "', '" + s5 + "', '" + s6 + "', '" + s7 + "', '" + s8 + "')";
                String q1 = "update room set Availability = 'Occupied' where room_no = " + s6;
                c.statement.executeUpdate(q);
                c.statement.executeUpdate(q1);
                JOptionPane.showMessageDialog(null, "Added Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                setVisible(false);
            } catch (Exception E) {
                E.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error Adding Patient", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            setVisible(false);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NEW_PATIENT());
    }
}