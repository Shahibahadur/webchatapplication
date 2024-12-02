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
    <style>
        body { font-family: Arial, sans-serif; }
        .chat-container { max-width: 600px; margin: 20px auto; }
        .message { margin-bottom: 10px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; }
        .message .sender { font-weight: bold; }
        .message .text { margin-top: 5px; }
    </style>
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/Style.css">
    
    <link rel="stylesheet" href = "http://localhost:8080/ChatAPP/cssFiles/GroupChat.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
    
</head>
<body>
    <div class="chat-container">
        <h2>Messages</h2>
        <%
            try (Connection connection = new DatabaseConfig().getConnection()) {
                String sql = "SELECT m.message_text, m.attachment_path, m.timestamp, u.fname, u.lname "
                           + "FROM group_messages m "
                           + "JOIN users u ON m.sender_id = u.unique_id "
                           + "WHERE m.group_id = ? ORDER BY m.timestamp ASC";
                try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1, Integer.parseInt(groupId));
                    ResultSet rs = stmt.executeQuery();

                    while (rs.next()) {
                        String senderName = rs.getString("fname") + " " + rs.getString("lname");
                        String messageText = rs.getString("message_text");
                        String timestamp = rs.getString("timestamp");
                        String attachmentPath = rs.getString("attachment_path");
        %>
        <div class="message">
            <div class="sender"><%= senderName %> at <%= timestamp %></div>
            <div class="text"><%= messageText != null ? messageText : "No text provided" %></div>
            <% if (attachmentPath != null) { %>
                <div class="attachment"><a href="<%= attachmentPath %>" target="_blank">View Attachment</a></div>
            <% } %>
        </div>
        <%
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("Error: " + e.getMessage());
            }
        %>
        
        <h1>Group Chat</h1>
        <form action="/ChatAPP/GroupMessageServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="groupId" value="<%= groupId %>">
            <input type ="text" name="messageText" id = "message" class= "input-field"placeholder="Type your message here..." required></textarea><br>
            
            
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
            
            <label for ="file-upload" class="custom-file-upload">
            	<i class="fas fa-paperclip"></i>
            </label>
            <input id ="file-upload" type="file" name="attachment" style = "display: none; ">
            
            <!-- send button -->
            <button type="submit" ><i class ="fab fa-telegram-plane"></i></button>
        </form>
    </div>
    
    <script type="text/javascript" src="http://localhost:8080/ChatAPP/js/GroupChat.js"></script>
</body>
</html>
 --%>
 
 
 <%@ page import="java.sql.*" %>
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
    <link rel="stylesheet" href = "http://localhost:8080/ChatAPP/cssFiles/GroupChat.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <div class="chat-container">
        <!-- Chat Header -->
        <div class="chat-header">
            <div class="user-info">
                <img src="profile.jpg" alt="Profile Picture">
                <div>
                    <div class="name">Group Chat</div>
                    <div class="status">Active now</div>
                </div>
            </div>
        </div>

        <!-- Messages Section -->
        <div class="chat-messages">
            <%
                try (Connection connection = new DatabaseConfig().getConnection()) {
                    String sql = "SELECT m.message_text, m.attachment_path, m.timestamp, u.fname, u.lname "
                               + "FROM group_messages m "
                               + "JOIN users u ON m.sender_id = u.unique_id "
                               + "WHERE m.group_id = ? ORDER BY m.timestamp ASC";
                    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                        stmt.setInt(1, Integer.parseInt(groupId));
                        ResultSet rs = stmt.executeQuery();

                        while (rs.next()) {
                            String senderName = rs.getString("fname") + " " + rs.getString("lname");
                            String messageText = rs.getString("message_text");
                            String timestamp = rs.getString("timestamp");
                            String attachmentPath = rs.getString("attachment_path");
            %>
            <div class="message received">
                <div class="sender"><%= senderName %> at <%= timestamp %></div>
                <div class="text"><%= messageText != null ? messageText : "No text provided" %></div>
                <% if (attachmentPath != null) { %>
                    <div class="attachment"><a href="<%= attachmentPath %>" target="_blank">View Attachment</a></div>
                <% } %>
            </div>
            <%
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    out.println("Error: " + e.getMessage());
                }
            %>
        </div>

        <!-- Input Section -->
        <div class="typing-area">
            <form action="/ChatAPP/GroupMessageServlet" method="post" enctype="multipart/form-data">
                <input type="hidden" name="groupId" value="<%= groupId %>">
                <input type="text" name="messageText" class="input-field" placeholder="Type a message here..." required>
                
                <label for="image-upload" class="icon"><i class="fas fa-image"></i></label>
                <input id="image-upload" type="file" name="image" accept="image/*" style="display: none;">
                
                <label for="file-upload" class="icon"><i class="fas fa-paperclip"></i></label>
                <input id="file-upload" type="file" name="attachment" style="display: none;">
                
                <button type="submit" class="send-btn"><i class="fas fa-paper-plane"></i></button>
            </form>
        </div>
    </div>
    <script src="https://kit.fontawesome.com/a076d05399.js"></script>
</body>
</html>
 