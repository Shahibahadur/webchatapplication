<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Set New Password</title>
    <link rel="stylesheet" type="text/css" href="/ChatAPP/cssFiles/setnewpassword.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
</head>
<body>
    <div class="container">
        <h2>Set New Password</h2>
        <form id="form" action="/ChatAPP/UpdatePassword" method="post">
            <label for="newPassword">New Password</label>
            <input type="password" id="newPassword" name="newPassword" required placeholder="Enter new password">

            <label for="conformPassword">Confirm Password</label>
            <input type="password" id="conformPassword" name="conformPassword" required placeholder="Confirm new password">

            <!-- Error message placeholder -->
            <p id="error" style="display: none; color: red;"></p>
            
            <% String errorMessage = (String) request.getAttribute("error"); %>
            <% if (errorMessage != null) { %>
                <p class="error"><%= errorMessage %></p>
            <% } %>

            <button type="submit" class="btn">Submit</button>
        </form>
    </div>

    <script>
    $(document).ready(function() {
        $('#form').on('submit', function(e) {
            e.preventDefault(); // Prevent default form submission

            var message = [];
            var newPassword = $('#newPassword').val().trim();
            var confirmPassword = $('#conformPassword').val().trim();
            var error = $('#error');

            console.log("New Password:", newPassword); // Debugging
            console.log("Confirm Password:", confirmPassword); // Debugging

            // Password validation pattern
            let pattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[^\s]{8,16}$/;

            // Check if passwords match
            if (newPassword !== confirmPassword) {
                console.log("Passwords do not match.");
                message.push("Passwords do not match.");
            } else if (!pattern.test(newPassword)) {
                // Check if password meets requirements
                message.push("Password must contain at least one lowercase letter, one uppercase letter, one digit, one special character, no spaces, and be 8-16 characters long.");
            }

            if (message.length > 0) {
                // Display validation errors
                error.css('display', 'block').text(message.join(' '));
            } else {
                // Validation passed, submit the form
                $(this).off('submit').submit();
            }
        });
    });

    </script>
</body>
</html>
