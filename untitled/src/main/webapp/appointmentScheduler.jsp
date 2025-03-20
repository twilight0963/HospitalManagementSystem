<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<h2>Schedule Appointment</h2>
<form action="appointmentServlet" method="post">
    Patient ID: <input type="number" name="patientId"><br>
    Doctor ID: <input type="number" name="doctorId"><br>
    Date: <input type="date" name="date"><br>
    <input type="submit" value="Book Appointment">
</form>
</body>
</html>