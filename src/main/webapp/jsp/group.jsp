<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <!-- Form to create a group -->
    <form action="/ChatAPP/CreateGroupServlet" method="GET">
        <input type="text" name="groupName" placeholder="Enter group name" required>
        <button type="submit">Create Group</button>
    </form>
    
    <!-- Display list of groups -->
    <% 
        List<Map<String, String>> groups = (List<Map<String, String>>) request.getAttribute("groups");
        if (groups != null && !groups.isEmpty()) {
            for (Map<String, String> group : groups) {
    %>
        <div>
            <h3><%= group.get("group_name") %></h3>
            <form action="/ChatAPP/JoinGroupServlet" method="post">
                <input type="hidden" name="group_id" value="<%= group.get("group_id") %>">
                <button type="submit">Join Group</button>
            </form>
        </div>
    <% 
            }
        } else { 
    %>
        <p>No groups available.</p>
    <% 
        } 
    %>

    <!-- Display join requests -->
    <% 
        List<Map<String, String>> requests = (List<Map<String, String>>) request.getAttribute("requests");
        if (requests != null && !requests.isEmpty()) {
            for (Map<String, String> req : requests) {
    %>
        <div>
            <p><%= req.get("user_name") %> wants to join <%= req.get("group_name") %></p>
            <form action="/ChatAPP/ManageGroupRequestsServlet" method="post">
                <input type="hidden" name="request_id" value="<%= req.get("request_id") %>">
                <button type="submit" name="action" value="approve">Approve</button>
                <button type="submit" name="action" value="reject">Reject</button>
            </form>
        </div>
    <% 
            }
        } else { 
    %>
        <p>No join requests available.</p>
    <% 
        } 
    %>
</body>
</html>
