<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set New Password</title>
    <link rel = "stylesheet" type = "text/css" href = "/ChatAPP/cssFiles/setnewpassword.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
</head>
<body>
    <div class="container">
    <p id = "error" style = "display: none; color: red;">
        <h2>Set New Password</h2>
        <form id = "form" action="/ChatAPP/UpdatePassword" method="post">
            <label for="newPassword">New Password</label>
            <input type="password"  id="newPassword" name="newPassword" required placeholder="Enter new password">

            <label for="confirmPassword">Confirm Password</label>
            <input type="password" id="conformPassword" name="conformPassword" required placeholder="Confirm new password">

            <!-- Error message placeholder -->
            <% String errorMessage = (String) request.getAttribute("error"); %>
            <% if (errorMessage != null) { %>
                <p class="error"><%= errorMessage %></p>
            <% } %>

            <button type="submit" class="btn">Submit</button>
        </form>
    </div>
    <script>
    $(document).ready(function(){
    	$('#form').on('submit', function(e){
    		e.preventDefault();
    		var message = [];
    		var newPassword = $('#newPassword').val().trim();
    		var conformPassword = $('#conformPassword').val().trim();
    		var error = $('#error');
    		
    		let pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\d])(?=.*\W)[^\s]{8,16}$/;
    		if(newPassword !== conformPassword){
    		message.push("passwords do not matched");
    		}else{
    			if(!pattern.test(newPassword)){
    				message.push('Password must conatins at least one lower case, one digit, one uppercase, specail character, no space allowed and between 8 and 16 character in total');
    				
    				
    			}
    		}
    		
    		if(message.length>0){
    			error.css('display','block');
    			error.text(message.join(', '));
    		}else{
    			$(this).submit();
    		}
    	});
    });
    
    </script>
</body>
</html>
