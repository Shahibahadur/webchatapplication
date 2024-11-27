<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import = "java.util.ArrayList"  %>
<%@ page import = "java.util.HashMap" %>
<%@ page import = "java.util.Map" %>
<%@ page import = "java.util.List" %>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Friend Requests</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/friend.css">
</head>
<body>
    <div class="friend-requests-container">
        <!-- Search Bar -->
        <div class="search-section">
            <form action="/ChatAPP/FriendSearchServlet" method="GET">
                <input type="text" name="searchQuery" placeholder="Search for friends..." required>
                <button type="submit"><i class="fas fa-search"></i> Search</button>
            </form>
        </div>

        <!-- Search Results -->
        <div class="search-results">
            <h3>Search Results</h3>
            <%
                List<Map<String, String>> searchResults = (List<Map<String, String>>) request.getAttribute("searchResults");
                if (searchResults != null && !searchResults.isEmpty()) {
                    for (Map<String, String> user : searchResults) {
            %>
            <div class="friend-request-item">
                <img src="<%= request.getServletContext()+"/uploads/"+user.get("image") %>" alt="Profile Picture">
                <div class="friend-info">
                    <p class="name"><%= user.get("fname")+" "+user.get("lname") %></p>
                    <p class="details"><%= user.get("email") %></p>
                </div>
p                <form action="SendFriendRequestServlet" method="post">
                
                <!-- unique_id of another people -->
                
                    <input type="hidden" name="receiver_id" value="<%= user.get("unique_id") %>">
                    <button type="submit" class="send-request">Send Request</button>
                    
                </form>
            </div>
            <% } } else { %>
            <p>No results found.</p>
            <% } %>
        </div>
        

        <!-- Friend Requests Received -->
        
       <!--  check here request_id from table friend_rerquest -->
       
        <div class="friend-requests-received">
            <h3>Friend Requests Received</h3>
            <%
        	System.out.println("Nothing has get from Requestreceived");

                List<Map<String, String>> requestsReceived = (List<Map<String, String>>) request.getAttribute("requestsReceived");
                if (requestsReceived != null && !requestsReceived.isEmpty()) {
                    for (Map<String, String> req : requestsReceived) {
                    	System.out.println("something has get from Requestreceived");
            %>
            <div class="friend-request-item">
                <img src="<%= request.getServletContext()+"/uploads/"+req.get("sender_image") %>" alt="Profile Picture">
                <div class="friend-info">
                
                    <p class="name"><%= req.get("fname")+" "+req.get("lname") %></p>
                    <p class="details"><%= req.get("email") %></p>
                </div>
                <div class="actions">
                    <form action="/ChatAPP/AcceptRequestServlet" method="post">
                        <input type="hidden" name="request_id" value="<%= req.get("request_id") %>">
                        <button type="submit" class="confirm">Accept</button>
                    </form>
                    <form action="RejectRequestServlet" method="post">
                        <input type="hidden" name="request_id" value="<%= req.get("request_id") %>">
                        <button type="submit" class="delete">Reject</button>
                    </form>
                </div>
            </div>
            <% } } else { %>
            <p>No friend requests received.</p>
            <% } %>
        </div>
    </div>
</body>
</html>
