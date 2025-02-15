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
    	String groupId = "1";
    	String sender = null;
        String user_id = null;

        // Check if the user is logged in
        if (session.getAttribute("unique_id") == null) {
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            return;
        } else {
            user_id = (String) session.getAttribute("unique_id");
            sender = user_id;
            
        }
    %>

    

 <!-- Groups Sidebar -->
    <div class="groups-sidebar">
        <h2>Groups</h2>
        
        <!-- Group Search -->
   <form class="group-search" action="/ChatAPP/GroupSearchServlet" method="get">
	    <input type="text" name="searchQuery" placeholder="Search for groups..." required>
	    <button type="submit">
	        <i class="fas fa-search"></i>
	    </button>
  </form>

    <%
        List<Map<String, String>> searchResults = (List<Map<String, String>>) request.getAttribute("searchResults");
        		
        		
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

      <!-- Group List -->
     <!--  display created groups using js  groups -->
        <div class="group-list" id="createdContainer">
     
        <!-- Dynamically populated list of created groups -->

		</div>
		
		
    <!-- Display joined groups -->
    
    <div class = "group-list" id = "joinedContainer">
    <!-- Dynamically populated lists of joined groups -->
    </div>
    
    
    
	<div class="create-group">
	    <a href="http://localhost:8080/ChatAPP/jsp/CreateGroup.jsp">
	        <i class="fas fa-plus-circle"></i>Create Group
	    </a>
	   
	</div>
	
    </div>
    
	

<!-- Chat Container -->
    <div class="chat-container">
        

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
    </div>
<!-- 
    <script>
        // Add click handlers for groups
        document.querySelectorAll('.group-item').forEach(item => {
            item.addEventListener('click', function() {
                // Remove active class from all items
                document.querySelectorAll('.group-item').forEach(i => i.classList.remove('active'));
                // Add active class to clicked item
                this.classList.add('active');
                // Load group chat here
                const groupName = this.querySelector('h3').textContent;
                document.querySelector('.chat-header h2').textContent = `#${groupName}`;
            });
        });
    </script> -->

        <script src="http://localhost:8080/ChatAPP/js/srj.js"></script> 
        <script src="http://localhost:8080/ChatAPP/js/groupmesage.js"></script>
         
        
        
          
</body>



</html>
