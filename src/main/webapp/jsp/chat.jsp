<%@ page import="mypackage.Const" %>
<%@ page import = "mypackage.DatabaseConfig" %>
<%@ page import = "java.sql.Connection" %>
<%@ page import = "java.sql.ResultSet" %>
<%@ page import = "java.sql.PreparedStatement" %>
<%@ page import = "java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=Const.app_name %></title>
<link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/Style.css">
<link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/chat.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>

</head>
<body>
	<%
		if(session.getAttribute("unique_id")== null ){
			request.getRequestDispatcher("login.jsp").forward(request,response);
		}
		DatabaseConfig db;
		String fname = "Jhon";
		String lname = "Doe";
		String imgName = "default.png";
		String status = "";
		String param = "" + request.getParameter("user_id"); //need to check here
		System.out.println("param from chat.jsp "+param);
		System.out.println("session from chat.jsp "+session.getAttribute("unique_id"));
		
		try{
			db = new DatabaseConfig();
			Connection conn = db.getConnection();
			
			String sql = "SELECT * FROM users WHERE unique_id=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, param);
			ResultSet set = pstmt.executeQuery();
			if(set.next()){
				fname = set.getString("fname");
				lname = set.getString("lname");
				imgName = set.getString("img");
				status = set.getString("status");
			}
		}catch(ClassNotFoundException | SQLException e){
			out.write("Connection not found due : " + e.getMessage());
			
		}
		response.setContentType("text/html");
		
	%>
	<div class = "wrapper">
		<section class = "chat-area">
			<header>
				<a href="user-chatbox" class="back-icon"><i class="fas fa-arrow-left"></i></a>
				<img alt = "" src = '<% String imgPath = "uploads/" + imgName ; out.println(imgPath); %>'>
				<div class = "details">
					<span><%= fname + " " + lname %></span>
					<p><%= status %> </p>
				</div>
				
			</header>
			<div class = "chat-box">
			
			<!-- message is shown here -->
			
			</div>
			<form action = "#" id = "message_box" class = "typing-area" autocomplete = "off">
				<input type="hidden" name="outgoing_id" id="outgoing_id" value=<%= session.getAttribute("unique_id") %> >
				<input type = "hidden" name = "incoming_id" id = "incoming_id" value =<%= param %> >
				<input type = "text" name = "message" id = "message" class = "input-field" placeholder = "Type a message here..">
				
				<!-- Emoji icon for selecting an image -->
				 <label for="file-input" class="emoji-icon">
				    <i class="fas fa-smile"></i>
				</label>
				
				<!-- Image Upload Icon -->
				<label for="image-upload" class="custom-file-upload">
				    <i class="fas fa-image"></i> <!-- Image icon -->
				</label>
				<input id="image-upload" type="file" name="image" accept="image/*" style="display: none;"/>
				
				<!-- File Upload Icon -->
				<label for="file-upload" class="custom-file-upload">
				    <i class="fas fa-paperclip"></i> <!-- File icon -->
				</label>
				<input id="file-upload" type="file" name="file" style="display: none;"/>
				
				<!-- Send Button -->
				<button type="button" onclick="submitForm()"><i class="fab fa-telegram-plane"></i></button>
			</form>
			
		</section>
		
	</div>
	<script type = "text/javascript" src = "http://localhost:8080/ChatAPP/js/chat.js" ></script>
	
</body>
</html>