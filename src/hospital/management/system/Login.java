package hospital.management.system;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {

    JTextField textField;
    JPasswordField jPasswordField;
    JButton b1,b2;


    Login(){

        //Label for username
        JLabel namelabel = new JLabel("Username");
        namelabel.setBounds(40,20,100,30);
        namelabel.setFont(new Font("Tahoma",Font.BOLD,16));
        namelabel.setForeground(Color.BLACK);
        add(namelabel);

        //Label for Password
        JLabel password = new JLabel("Password");
        password.setBounds(40,70,100,30);
        password.setFont(new Font("Tahoma",Font.BOLD,16));
        password.setForeground(Color.BLACK);
        add(password);

        //textField = textBox to enter the value
        textField = new JTextField();
        textField.setBounds(150,20,150,30);
        textField.setFont(new Font("Tahoma",Font.PLAIN,15));
        textField.setBackground(new Color(255,179,0));
        add(textField);

        //passwordField = textBox to enter the password
        jPasswordField = new JPasswordField();
        jPasswordField.setBounds(150,70,150,30);
        jPasswordField.setFont(new Font("Tahoma",Font.PLAIN,15));
        jPasswordField.setBackground(new Color(255,179,0));
        add(jPasswordField);

        //login image
        ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("icon/health safari login.png"));
        Image i1 = imageIcon.getImage().getScaledInstance(450,450,Image.SCALE_DEFAULT);
        ImageIcon imageIcon1 = new ImageIcon(i1);
        JLabel label = new JLabel(imageIcon1);
        label.setBounds(320,-30,400,300);
        add(label);

        //button(login)
        b1 = new JButton("Login");
        b1.setBounds(40,140,120,30);
        b1.setFont(new Font("serif",Font.BOLD,15));
        b1.setBackground(Color.BLACK);
        b1.setForeground(Color.WHITE);
        add(b1);

        //button(cancel)
        b2 = new JButton("Cancel");
        b2.setBounds(180,140,120,30);
        b2.setFont(new Font("serif",Font.BOLD,15));
        b2.setBackground(Color.BLACK);
        b2.setForeground(Color.WHITE);
        add(b2);

        //background
        getContentPane().setBackground(new Color(109,164,170));
        setSize(750,300);
        setLocation(400,270);
        setLayout(null);
        setVisible(true);
    }
    public static void main(String[] args) {
        new Login();
    }
}
