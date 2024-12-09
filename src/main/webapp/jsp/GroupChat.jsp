<%-- <%@ page import="java.sql.*" %>
<%@ page import="mypackage.DatabaseConfig" %>
<%
    String groupId = request.getParameter("group_id");
    if (groupId == null || groupId.isEmpty()) {
        out.println("Group ID is required to view this page.");
        return;
    }

    String userId = (String) session.getAttribute("unique_id");
    if (userId == null) {
        response.sendRedirect("jsp/login.jsp");
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
    <link rel="stylesheet" href = "http://localhost:8080/ChatAPP/cssFiles/GroupChat.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>

		<div id = "group" dataset-group_id ='<%=groupId%>'>
		
		</div>



		<div dataset-group_id ="<%=groupId%>" id = "messageContainer" class = "message_container">


		</div>
        <!-- Input Section -->
        <div class="typing-area">
            <form action="/ChatAPP/GroupMessageServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="groupId" value="<%= groupId %>">
                <input type="text" name="messageText" class="input-field" placeholder="Type a message here..." >
                
                <!-- Emoji icon for selecting an image -->
				 <label for="file-input" class="emoji-icon">
				    <i class="fas fa-smile"></i>
				</label>
                
                <label for="image-upload" class="icon"><i class="fas fa-image"></i></label>
                <input id="image-upload" type="file" name="image" accept="image/*" style="display: none;">
                
                <label for="file-upload" class="icon"><i class="fas fa-paperclip"></i></label>
                <input id="file-upload" type="file" name="attachment" style="display: none;">
                
                <button type="submit" class="send-btn"><i class="fas fa-paper-plane"></i></button>
            </form>
        </div>
    </div>
    <script src ="http://localhost:8080/ChatAPP/js/groupmesage.js"></script>
</body>
</html>
 
 --%>
 
 
 <%@ page import="java.sql.*" %>
<%@ page import="mypackage.DatabaseConfig" %>
<%
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
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/GroupChat.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
    <div id="group" data-group-id="<%= groupId %>"></div>

    <div id="messageContainer" data-group-id="<%= groupId %>" class="message_container"></div>

    <!-- Typing Area -->
    <div class="typing-area">
        <form action="/ChatAPP/GroupMessageServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="groupId" value="<%= groupId %>">
            <input type="text" name="messageText" class="input-field" placeholder="Type a message here..." required>

            <!-- Emoji Icon -->
            <label for="file-input" class="emoji-icon">
                <i class="fas fa-smile"></i>
            </label>

            <!-- Image Upload -->
            <label for="image-upload" class="icon">
                <i class="fas fa-image"></i>
            </label>
            <input id="image-upload" type="file" name="image" accept="image/*" style="display: none;">

            <!-- File Upload -->
            <label for="file-upload" class="icon">
                <i class="fas fa-paperclip"></i>
            </label>
            <input id="file-upload" type="file" name="attachment" style="display: none;">

            <!-- Send Button -->
            <button type="submit" class="send-btn">
                <i class="fas fa-paper-plane"></i>
            </button>
        </form>
    </div>

    <script src="http://localhost:8080/ChatAPP/js/groupmesage.js"></script>
</body>
</html>
 