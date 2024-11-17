<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="mypackage.DatabaseConfig" %>
<%@ page import = "mypackage.Const" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.PreparedStatement" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.SQLException" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/profile.css">
    <title><%=Const.app_name %></title>
</head>
<body>
	<%
		DatabaseConfig db;
		String fName = "John";
		String lName = "Doe";
		String imgName ="fake.jpg";
		String status = "";
		String email = "unknown@gmail.com";
		try{
			db = new DatabaseConfig();
			Connection conn = db.getConnection();
			String sql = "SELECT * FROM users WHERE unique_id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
			String param = ""+session.getAttribute("unique_id");
            System.out.println("param from user.jsp " + param);
			pstmt.setString(1,param);
			ResultSet set = pstmt.executeQuery();
				if(set.next()){
					fName = set.getString("fname");
					lName = set.getString("lname");
					imgName = set.getString("img");
					email = set.getString("email");
				
			}
			
			}catch(ClassNotFoundException | SQLException e){
				out.write("Connection not found due to : " + e.getMessage());
			}
		response.setContentType("text/html");
	
	%>
	
	<div class="profile-container">
        <h1>Profile</h1>

        <!-- Profile Image -->
        <div class="profile-section">
            <h2>Profile Image</h2>
            <img src='<%String imgPath = "uploads/" + imgName; out.print(imgPath); 
					//testing
					System.out.println("image path " +imgPath);
					//image path uploads/1727718674566Copy of Copy of Gradient Colorful Web Development Gantt Chart Graph .png
					%>' alt="Profile Image" id="profile-img">
            <button onclick="editProfileImage()">Edit</button>
        </div>

        <!-- First Name Section -->
        <div class="profile-section">
            <h2>First Name</h2>
            <p id="fname-display"><%= fName %></p>
            <input type="text" id="fname-edit" value="<%= fName %>" style="display: none;">
            <button onclick="editName('fname')">Edit</button>
        </div>

        <!-- Last Name Section -->
        <div class="profile-section">
            <h2>Last Name</h2>
            <p id="lname-display"><%= lName %></p>
            <input type="text" id="lname-edit" value="<%= lName %>" style="display: none;">
            <button onclick="editName('lname')">Edit</button>
        </div>

        <!-- Email Section -->
        <div class="profile-section">
            <h2>Email</h2>
            <p id="email-display"><%= email %></p>
            <input type="email" id="email-edit" value="<%= email %>" style="display: none;">
            <button onclick="editEmail()">Edit</button>
        </div>
        
        <!-- Save Button to Save changes -->
        <div class="profile-section">
            <button id="save-btn" onclick="saveProfile()">Save Changes</button>
        </div>

    </div>

    <script src="profile.js"></script>
	

</body>
</html>