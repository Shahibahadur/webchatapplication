<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set New Password</title>
    <link rel = "stylesheet" type = "text/css" href = "/ChatAPP/cssFiles/setnewpassword.css">
</head>
<body>
    <div class="container">
        <h2>Set New Password</h2>
        <form action="/ChatAPP/UpdatePassword" method="post">
            <label for="newPassword">New Password</label>
            <input type="password" id="newPassword" name="newPassword" required placeholder="Enter new password">

            <label for="confirmPassword">Confirm Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Confirm new password">

            <!-- Error message placeholder -->
            <% String errorMessage = (String) request.getAttribute("error"); %>
            <% if (errorMessage != null) { %>
                <p class="error"><%= errorMessage %></p>
            <% } %>

            <button type="submit" class="btn">Submit</button>
        </form>
    </div>
</body>
</html>
