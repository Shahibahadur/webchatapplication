<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Group Management</title>
</head>
<body>

    <%
        String user_id = null;

        // Check if the user is logged in
        if (session.getAttribute("unique_id") == null) {
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        } else {
            user_id = (String) session.getAttribute("unique_id");
        }
    %>

    <!-- Form to create a group -->
    <h2>Create a Group</h2>
    <form action="/ChatAPP/CreateGroupServlet" method="post">
        <input type="text" name="groupName" placeholder="Enter group name" required>
        <button type="submit">Create Group</button>
    </form>

    <!-- Link to create group page -->
    <p><a href="http://localhost:8080/ChatAPP/jsp/CreateGroup.jsp">Create Group</a></p>

    <!-- Group Search Form -->
    <h2>Search for Groups</h2>
    <form action="/ChatAPP/GroupSearchServlet" method="get">
        <input type="text" name="searchQuery" placeholder="Search for groups..." required>
        <button type="submit">Search</button>
    </form>

    <!-- Display Search Results -->
    <h3>Search Results</h3>
    <%
        List<Map<String, String>> searchResults = (List<Map<String, String>>) request.getAttribute("searchResults");
        if (searchResults != null && !searchResults.isEmpty()) {
            for (Map<String, String> group : searchResults) {
    %>
        <div>
            <img src="<%= request.getContextPath() + "/uploads/" + group.get("image") %>" alt="Group Image" width="100" height="100">
            <div class="searchGroup">
                <h3><%= group.get("group_name") %></h3>
            </div>
            <form action="/ChatAPP/JoinGroupServlet" method="post">
                <input type="hidden" name="group_id" value="<%= group.get("group_id") %>">
                <input type="hidden" name="user_id" value="<%= user_id %>">
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

    <!-- Display list of created groups -->
    <h2>Created Groups</h2>
    <div id="groupsContainer">
        <!-- Dynamically populated list of created groups -->
    </div>

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
    <div id="requestsContainer">
    <h2>Join Requests</h2>
    <% 
        List<Map<String, String>> requests = (List<Map<String, String>>) request.getAttribute("requests");
        if (requests != null && !requests.isEmpty()) {
            for (Map<String, String> req : requests) {
    %>
        <div>
            <p><%= req.get("user_name") %> wants to join <%= req.get("group_name") %></p>
            <form>
                <input type="hidden" name="request_id" value="<%= req.get("request_id") %>">
                <button type="button" value="approve">Approve</button>
                <button type="button" value="reject">Reject</button>
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
</div>

    <script src="http://localhost:8080/ChatAPP/js/fetch&navigate.js"></script>
</body>
</html>
