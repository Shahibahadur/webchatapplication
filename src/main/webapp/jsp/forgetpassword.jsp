<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Forget Password</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 300px;
            text-align: center;
        }
        .container h2 {
            margin-bottom: 20px;
        }
        .container input[type="email"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .container button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            border: none;
            color: white;
            border-radius: 5px;
            cursor: pointer;
        }
        .container button:hover {
            background-color: #0056b3;
        }
        .error {
            color: red;
            font-size: 0.9em;
        }
    </style>
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
