Hospital Management System

```sql
-- Create Tables
CREATE TABLE Patients (
    Patient_ID INT AUTO_INCREMENT PRIMARY KEY,
    Doctor_ID INT,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    Status VARCHAR(50),
    FOREIGN KEY (Doctor_ID) REFERENCES Doctors(Doctor_ID)
);

CREATE TABLE Doctors (
    Doctor_ID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(255),
    LastName VARCHAR(255),
    Password VARCHAR(255),
    Specialisation INT
);

CREATE TABLE Rooms (
    Room_ID INT AUTO_INCREMENT PRIMARY KEY,
    Occupant INT,
    Type VARCHAR(50),
    Price INT,
    FOREIGN KEY (Occupant) REFERENCES Patients(Patient_ID)
);

CREATE TABLE Appointments (
    Appointment_ID INT AUTO_INCREMENT PRIMARY KEY,
    Doctor_ID INT,
    Patient_ID INT,
    StartTime DATETIME,
    EndTime DATETIME,
    Description TEXT,
    FOREIGN KEY (Doctor_ID) REFERENCES Doctors(Doctor_ID),
    FOREIGN KEY (Patient_ID) REFERENCES Patients(Patient_ID)
);

CREATE TABLE Ambulances (
    Ambulance_ID INT AUTO_INCREMENT PRIMARY KEY,
    Location VARCHAR(255),
    Status VARCHAR(20),
    Doctor_ID INT,
    Patient_ID INT,
    FOREIGN KEY (Doctor_ID) REFERENCES Doctors(Doctor_ID),
    FOREIGN KEY (Patient_ID) REFERENCES Patients(Patient_ID)
);

CREATE TABLE Inventory (
    Medicine_ID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(255),
    Icon_Path VARCHAR(500),
    Default_Dosage DOUBLE(6,2),
    Price DOUBLE(12,2)
);

CREATE TABLE Prescriptions (
    Prescription_ID INT AUTO_INCREMENT PRIMARY KEY,
    Patient_ID INT,
    Medicine_ID INT,
    Doctor_ID INT,
    Dosage DOUBLE(6,2),
    End_Date DATETIME,
    FOREIGN KEY (Patient_ID) REFERENCES Patients(Patient_ID),
    FOREIGN KEY (Medicine_ID) REFERENCES Inventory(Medicine_ID),
    FOREIGN KEY (Doctor_ID) REFERENCES Doctors(Doctor_ID)
);
```
