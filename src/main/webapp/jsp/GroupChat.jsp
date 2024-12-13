
 
<%@ page import="java.sql.*" %>
<%@ page import="mypackage.DatabaseConfig" %>
<%
	String sender = (String)session.getAttribute("unique_id");

    String groupId = request.getParameter("group_id");
    if (groupId == null || groupId.isEmpty()) {
        out.println("<p>Group ID is required to view this page.</p>");
        return;
    }

    String userId = (String) session.getAttribute("unique_id");
    if (userId == null) {
        response.sendRedirect(response.encodeRedirectURL("jsp/login.jsp"));
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Group Chat</title>
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/Style.css">
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/chat.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
	
	<div class = "wrapper">
	<section class = "chat-area">
		<header id="group" data-group-id="<%= groupId %>">
	
		
		</header>    

	    <div id="messageContainer" data-group-id="<%= groupId %>" data-sender="<%=sender%>" class="chat-box">
	    
	    	<!-- message is shown here -->
	    </div>

    <!-- Typing Area -->
   
			<form action = "#" id = "message_box" class = "typing-area" autocomplete = "off">
            <input type="hidden" id = "groupId" name="groupId" value="<%= groupId %>">
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
    <script src="http://localhost:8080/ChatAPP/js/groupmesage.js"></script>
</body>
</html>
 