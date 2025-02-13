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
      
        <form id = "form" action="/ChatAPP/SendOTP" method="GET">
        <p id = "error" style = "display: none; color: red;"></p>
        <%
        session = request.getSession(false);
        Boolean otpGenerated = (Boolean)session.getAttribute("otpGenerated");
        String userEmail = (String)session.getAttribute("email");
        
        %>
        <!-- Email Section -->
        <%if(otpGenerated == null || !otpGenerated){ %>
            <input id = 'email' type="email" name="email" placeholder="Enter your email" required>
            <button type="submit">Send OTP</button>
            <%}else { %>
            
            <!-- OTP Input Section -->
            <input id = 'otp' type = "number" name = "otp" placeholder = "Enter OTP" required>
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
  <script>
    document.addEventListener('DOMContentLoaded', () => {
        let message = [];
        let form = document.getElementById('form'); // Declare form variable

        form.addEventListener('submit', (e) => {
            e.preventDefault(); // Fix typo here
            let error = document.getElementById('error');
            let email = document.getElementById('email');
            let otp = document.getElementById('otp');
            message = []; // Reset the message array for each submission

            // Validate Email
            if (email && email.style.display !== 'none') {
                let pattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
                if (!pattern.test(email.value)) {
                    message.push("Invalid Email");
                }
            }

            // Validate OTP
            if (otp && otp.style.display !== 'none') {
                let pattern = /^\d{6}$/;
                if (!pattern.test(otp.value)) {
                    message.push("Invalid OTP");
                }
            }
            

            // Display Errors or Submit Form
            if (message.length > 0) {
            	error.style.display = 'block';
                error.innerText = message.join(', ');
            } else {
            	if(otp && otp.style.display !== 'none'){
            		form.action = "/ChatAPP/VerifyOTP";
            	}
                form.submit(); // Submit form if no errors
            }
        });
    });

    </script>
    
    	<script type = "text/javascript">
	
	window.addEventListener("pageshow", (event) => {
	    if (event.persisted) {
	        // The page is being loaded from the cache, so refresh it
	        window.location.reload();
	    }
	});

	
	</script>
</body>
</html>
