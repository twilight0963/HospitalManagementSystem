<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Add Ward</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
<h2>Add New Ward</h2>
<form action="wardServlet" method="post">
    Ward ID: <input type="number" name="wardId"><br>
    Ward Name: <input type="text" name="wardName"><br>
    Capacity: <input type="number" name="capacity"><br>
    <input type="submit" value="Add Ward">
</form>
<a href="wardServlet">View Ward List</a>
</body>
</html>