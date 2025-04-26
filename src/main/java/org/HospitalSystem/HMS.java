package org.HospitalSystem;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.HospitalSystem.Classes.DatabaseManager;
import org.HospitalSystem.Classes.Pages.AmbulancePage;
import org.HospitalSystem.Classes.Pages.AppointmentsPage;
import org.HospitalSystem.Classes.Pages.EmployeeInfoPage;
import org.HospitalSystem.Classes.Pages.LogInPage;
import org.HospitalSystem.Classes.Pages.PatientPage;
import org.HospitalSystem.Classes.Pages.PharmacyPage;
import org.HospitalSystem.Classes.Pages.PrescriptionsPage;
import org.HospitalSystem.Classes.Pages.ReceptionPage;
import org.HospitalSystem.Classes.Pages.RegisterPage;
import org.HospitalSystem.Classes.Pages.RoomPage;

public class HMS {
    private static final DatabaseManager db = new DatabaseManager();
    public static void showLoginFrame() {
        JFrame root = new JFrame("Health Safari");
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setLayout(new BorderLayout());

        // Create card panel for switching between pages
        JPanel cardPanel = new JPanel(new CardLayout());
        
        
        // Create pages
        JPanel loginPage = new LogInPage(root, cardPanel, db);
        JPanel registerPage = new RegisterPage(root, cardPanel, db);
        
        // Add pages to card panel
        cardPanel.add(loginPage, "login");
        cardPanel.add(registerPage, "register");
        
        // Add card panel to center of border layout
        root.add(cardPanel, BorderLayout.CENTER);
        
        // Show initial page
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "login");
        
        root.pack();
        root.setLocationRelativeTo(null);
        root.setResizable(false);
        root.setVisible(true);
    }
    public static void main(String[] args) {
        showLoginFrame();
    }
    public static void showDashboard() {
        JFrame root = new JFrame("Health Safari");
        root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        root.setLayout(new BorderLayout());

        JPanel cardPanel = new JPanel(new CardLayout());
        
        JPanel dashboardPage = new ReceptionPage(root, cardPanel, db);
        JPanel patientPage = new PatientPage(root, cardPanel, db);
        JPanel roomPage = new RoomPage(root, cardPanel, db);
        JPanel employeeInfo = new EmployeeInfoPage(root, cardPanel, db);
        JPanel ambulancePage = new AmbulancePage(root, cardPanel, db);
        JPanel appointmentsPage = new AppointmentsPage(root, cardPanel, db);
        JPanel pharmacyPage = new PharmacyPage(root, cardPanel, db);
        JPanel prescriptionPage = new PrescriptionsPage(root, cardPanel, db);

        cardPanel.add(dashboardPage, "dashboard");
        cardPanel.add(patientPage, "patients");
        cardPanel.add(roomPage, "rooms");
        cardPanel.add(employeeInfo, "employeeInfo");
        cardPanel.add(ambulancePage, "ambulance");
        cardPanel.add(appointmentsPage, "appointments");
        cardPanel.add(pharmacyPage, "pharmacy");
        cardPanel.add(prescriptionPage, "prescriptions");

        root.add(cardPanel, BorderLayout.CENTER);
        
        root.pack();
        root.setLocationRelativeTo(null);
        root.setResizable(false);
        root.setVisible(true);
    }
}
