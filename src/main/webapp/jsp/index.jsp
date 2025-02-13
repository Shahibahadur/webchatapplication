<%@page import = "mypackage.Const" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= Const.app_name %></title>
<link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/Style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
</head>
<body>
    <%
        try {
            if (session.getAttribute("unique_id") != null) {
                request.getRequestDispatcher("user.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    %>
    <div class="wrapper">
        <section class="form signup">
            <header><%= Const.app_name %></header>
			<form id ="form" action="<%= request.getContextPath() %>/signup1" method="POST" id="signupform" enctype="multipart/form-data">
          <!--  request.getContextPath =http://localhost:8080/ChatAPP/signup1 -->
           <!-- which is mapped in web.xml which open SignUpServlet -->
           
           <!--  enctype define how the form data should be encoded while submitting 
            multipart/form-data, used while uploading the data -->
            
                
                
				<%
				    try {
				        if (session.getAttribute("error_message") != null) {
				%>
				            <p style="color: red;"><%=session.getAttribute("error_message")%></p>
				<%
				            // Remove the error message after displaying it
				            session.removeAttribute("error_message");
							session.invalidate();
							
				        }
				    } catch (Exception e) {
				        e.printStackTrace();
				    }
				%>

                <p id = "error" style = "color: red"></p>
                <div class="name-details">
                    <div class="field input">
                        <label>First Name</label>
                        <input id = "fname" type="text" name="fname" placeholder="First Name" required>
                    </div>
                    <div class="field input">
                        <label>Last Name</label>
                        <input id = "lname" type="text" name="lname" id="lname" placeholder="Last Name" required>
                    </div>
                </div>    
                
                <div class="field input">
                    <label>Email Address</label>
                    <input id = "email" type="text" name="email" id="email" placeholder="Enter your email" required>
                </div>
                <div class="field input">
                    <label>Password</label>
                    <input id = "password"  type="password" name="password" id="password" placeholder="Enter new Password" required>
                    <i class="fas fa-eye"></i>
                </div>
                <div class="field image">
                    <label>Select Image</label>
                    <input id = "image" type="file" name="image" accept="image/*">
                </div>
                <div class="field button">
                    <input type="submit" value="Continue to Chat">
                </div>
            </form>
           <!--  here i have to check  -->
            <div class="link">Already signed up? <a href="<%=request.getContextPath()%>/user-login">Login now</a></div>
        </section>
    </div>
    
    	<script type = "text/javascript">
	
	window.addEventListener("pageshow", (event) => {
	    if (event.persisted) {
	        // The page is being loaded from the cache, so refresh it
	        window.location.reload();
	    }
	});

	
	</script>
    <script src = "http://localhost:8080/ChatAPP/js/index.js"></script>
    <script src="http://localhost:8080/ChatAPP/js/pass_show_hode.js"></script>
    
</body>
</html>
