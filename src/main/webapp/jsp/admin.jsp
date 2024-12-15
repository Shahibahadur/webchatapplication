<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/admin.css">
</head>
<body>
    <header>
        <h1>Admin Dashboard</h1>
        <p>Logged in as: <span id="loggedInUser"></span></p>
    </header>
    <nav>
        <a href="#" onclick="showSection('admins')">Admins</a>
        <a href="#" onclick="showSection('users')">Users</a>
        <a href="#" onclick="showSection('groups')">Groups</a>
    </nav>
    <div id="admins" class="container active">
        <h2>Admin List</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody id="adminTableBody"></tbody>
        </table>
    </div>
    <div id="users" class="container">
        <h2>User List</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody id="userTableBody"></tbody>
        </table>
    </div>
    <div id="groups" class="container">
        <h2>Group List</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody id="groupTableBody"></tbody>
        </table>
    </div>
    <script src="http://localhost:8080/ChatAPP/js/admin.js"></script>
</body>
</html>
