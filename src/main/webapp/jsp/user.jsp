<%@ page import="mypackage.Const" %>
<%@ page import ="mypackage.DatabaseConfig" %>
<%@ page import ="java.sql.Connection" %>
<%@ page import ="java.sql.ResultSet" %>
<%@ page import = "java.sql.PreparedStatement" %>
<%@ page import = "java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%	

	if (session.getAttribute("unique_id") == null) {
		request.getRequestDispatcher("login.jsp").forward(request, response);
	} else {
		
	}
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Const.app_name %></title>
	<link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/Style.css">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
	<%
		DatabaseConfig db;
		String fName = "John";
		String lName = "Doe";
		String imgName ="fake.jpg";
		String status = "";
		try{
			db = new DatabaseConfig();
			Connection conn = db.getConnection();
			String sql = "SELECT * FROM users WHERE unique_id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			
// 			requested from signupservlet

			String param = ""+session.getAttribute("unique_id");
            System.out.println("param from user.jsp " + param);
			pstmt.setString(1,param);
			ResultSet set = pstmt.executeQuery();
				if(set.next()){
					fName = set.getString("fname");
					lName = set.getString("lname");
					imgName = set.getString("img");
					status = set.getString("status");
				
			}
			
			}catch(ClassNotFoundException | SQLException e){
				out.write("Connection not found due to : " + e.getMessage());
			}
		response.setContentType("text/html");
	
	%>
	<div class="Wrapper">
		<section class ="users">
			<header>
				<div class="content">
				<!-- <a href = "javascript:void(0);" onclick ="profile()"  > -->
				<a href = "http://localhost:8080/ChatAPP/profile" >
					<img alt="" src='<%String imgPath = "uploads/" + imgName; out.print(imgPath); 
					//testing
					System.out.println("image path " +imgPath);
					//image path uploads/1727718674566Copy of Copy of Gradient Colorful Web Development Gantt Chart Graph .png
					%>'>
					</a>
					
					<div class = "details">
						<span><%= fName +" "+lName %></span>
						<p><%= status %>
					</div>
					
					<a href="<%= "logout?logout_id="+session.getAttribute("unique_id") %>" class="logout">Logout</a>
					
					<!-- notice here -->		
				</div>
			</header>
			<div class="search">
				<span class="text"> Select an user to start chat..</span>
				<input type="text" placeholder="Enter name to search...">
				<button><i class="fas fa-search"></i></button>
			</div>
			<!-- list of search user  is here; -->
			<div class="users-list" id="user_list">
			<!-- response is get form UserServlet using user.js -->
			
			</div>
		</section>
	
	</div>
	  
	<script type="text/javascript" src="http://localhost:8080/ChatAPP/js/users.js"></script>
</body>
</html>