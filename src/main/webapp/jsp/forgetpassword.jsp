<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forget Password</title>
<link rel="stylesheet" type="text/css" href="http://localhost:8080/ChatAPP/cssFiles/forgetpassword.css">
</head>
<body>
    <div class="container">
        <h2>Forget Password</h2>
      
        <form action="/ChatAPP/SendOTP" method="GET">
        <%
        session = request.getSession(false);
        Boolean otpGenerated = (Boolean)session.getAttribute("otpGenerated");
        String userEmail = (String)session.getAttribute("email");
        
        %>
        <!-- Email Section -->
        <%if(otpGenerated == null || !otpGenerated){ %>
            <input type="email" name="email" placeholder="Enter your email" required>
            <button type="submit">Send OTP</button>
            <%}else { %>
            
            <!-- OTP Input Section -->
            <input type = "number" name = "otp" placeholder = "Enter OTP" required>
            <button type = "submit" formaction="/ChatAPP/VerifyOTP">Verify OTP</button>
            <%} %>
            
        </form>
        <% 
            String errorMessage = (String) request.getAttribute("error");
            if (errorMessage != null) {
        %>
            <p class="error"><%= errorMessage %></p>
        <% } %>
    </div>
</body>
</html>
