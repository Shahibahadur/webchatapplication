<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Group Management</title>
</head>
<body>
    <!-- Form to create a group -->
    <form action="/ChatAPP/CreateGroupServlet" method="post">
        <input type="text" name="groupName" placeholder="Enter group name" required>
        <button type="submit">Create Group</button>
    </form>
    
    <!-- Group Search Form -->
    <form action="/ChatAPP/GroupSearchServlet" method="get">
        <input type="text" name="searchQuery" placeholder="Search for groups..." required>
        <button type="submit">Search</button>
    </form>
	<p>Available Group </p>
    <!-- Display Search Results -->
    <%
        List<Map<String, String>> searchResults = (List<Map<String, String>>) request.getAttribute("searchResults");
        if (searchResults != null && !searchResults.isEmpty()) {
            for (Map<String, String> group : searchResults) {
    %>
        <div>
            <h3>Search Result: <%= group.get("group_name") %></h3>
            <form action="/ChatAPP/JoinGroupServlet" method="post">
                <input type="hidden" name="group_id" value="<%= group.get("group_id") %>">
                <button type="submit">Send Join Request</button>
            </form>
        </div>
    <% 
            }
        } else if (searchResults != null) { 
    %>
        <p>No groups match your search.</p>
    <% 
        }
    %>

 <!-- Display list of available groups -->
    <h2>Available Groups</h2>
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

    <!-- Display joined groups -->
    <h2>Joined Groups</h2>
    <% 
        List<Map<String, String>> joinedGroups = (List<Map<String, String>>) request.getAttribute("joinedGroups");
        if (joinedGroups != null && !joinedGroups.isEmpty()) {
            for (Map<String, String> group : joinedGroups) {
    %>
        <div>
            <h3><%= group.get("group_name") %></h3>
            <p>Joined on: <%= group.get("joined_at") %></p>
        </div>
    <% 
            }
        } else { 
    %>
        <p>You have not joined any groups.</p>
    <% 
        } 
    %>

    <!-- Display join requests -->
    <h2>Join Requests</h2>
    <% 
        List<Map<String, String>> requests = (List<Map<String, String>>) request.getAttribute("requests");
        if (requests != null && !requests.isEmpty()) {
            for (Map<String, String> req : requests) {
    %>
        <div>
            <p><%= req.get("user_name") %> wants to join <%= req.get("group_name") %></p>
            <form action="ManageGroupRequestsServlet" method="post">
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


<!--  9824500600 -->
