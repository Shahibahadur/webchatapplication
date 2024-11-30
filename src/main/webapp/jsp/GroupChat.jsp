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
    <style>
        body { font-family: Arial, sans-serif; }
        .chat-container { max-width: 600px; margin: 20px auto; }
        .message { margin-bottom: 10px; padding: 10px; border: 1px solid #ccc; border-radius: 5px; }
        .message .sender { font-weight: bold; }
        .message .text { margin-top: 5px; }
    </style>
</head>
<body>
    <div class="chat-container">
        <h1>Group Chat</h1>
        <form action="/ChatAPP/GroupMessageServlet" method="post" enctype="multipart/form-data">
            <input type="hidden" name="groupId" value="<%= groupId %>">
            <textarea name="messageText" placeholder="Type your message here..." required></textarea><br>
            <input type="file" name="attachment"><br>
            <button type="submit">Send</button>
        </form>

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
    </div>
</body>
</html>
