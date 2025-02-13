<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Group Management</title>
    <link rel = "stylesheet" href = "/ChatAPP/cssFiles/group.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
    
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

    <!-- Link to create group page -->
	<p>
	    <a href="http://localhost:8080/ChatAPP/jsp/CreateGroup.jsp">
	        <i class="fas fa-plus-circle"></i>
	    </a>
	    <span>Create Group</span>
	</p>    
    <hr/>

    <!-- Group Search Form -->
    <h2>Search For Groups</h2>
   <form action="/ChatAPP/GroupSearchServlet" method="get">
	    <input type="text" name="searchQuery" placeholder="Search for groups..." required>
	    <button type="submit">
	        <i class="fas fa-search"></i>
	    </button>
  </form>

    <!-- Display Search Results -->
    <h3>Search Results</h3>
    <%
        List<Map<String, String>> searchResults = (List<Map<String, String>>) request.getAttribute("searchResults");
        		
        		System.out.println(searchResults +" Search result from goup.jsp");
        		
        if (searchResults != null && !searchResults.isEmpty()) {
        	System.out.println(searchResults +" Search result from group.jsp");
            for (Map<String, String> group : searchResults) {
    %>
        <div>
            <img src="<%= request.getContextPath() + "/groupImages/" + group.get("image") %>" alt="Group Image" width="100" height="100">
            <div class="searchGroup">
                <h3><%= group.get("group_name") %></h3>
            </div>
            <form action="/ChatAPP/JoinGroupServlet" method="post">
                <input type="hidden" name="group_id" value="<%= group.get("group_id") %>">
                <input type="hidden" name="user_id" value="<%= user_id %>">
                <button type="submit"> Join</button>
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
    
<!--     first this page is loaded after request is sent  from GroupDisplayServlet -->  
	<hr/>
  
	<h2>Joined Groups</h2>
    <%
        List<Map<String, String>> joinedGroups = (List<Map<String, String>>) request.getAttribute("joinedGroups");
   		
    	System.out.println(joinedGroups + "joined groups from group.jsp");
    
        if (joinedGroups != null && !joinedGroups.isEmpty()) {
        	%>
        	<div id="groupsContainer">
        	<% 
            for (Map<String, String> group : joinedGroups) {
    %>
    <div class="group">
      <img src="<%= request.getContextPath() + "/groupImages/" + group.get("group_image") %>" alt="Group Image"
		onclick="window.location.href = '/ChatAPP/jsp/GroupChat.jsp?group_id=<%=group.get("group_id")%>'">
      
   
            <h3><%= group.get("group_name") %></h3>
        </div>
    <% 
            }
        } else { 
    %>
        <p>You have not joined any groups.</p>
    <% 
        } 
    %>
    </div>
    
    <!-- <hr/>

    Display join requests
    <div id="requestsContainer">
	dynamically show the list of join request to the group created by the users
	</div>	 -->	

        <script src="http://localhost:8080/ChatAPP/js/srj.js"></script>    
</body>

	<script type = "text/javascript">
	
	window.addEventListener("pageshow", (event) => {
	    if (event.persisted) {
	        // The page is being loaded from the cache, so refresh it
	        window.location.reload();
	    }
	});

	
	</script>
</html>






