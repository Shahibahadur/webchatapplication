
<%@page import="mypackage.Const"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html5>
<html>
<head>
<meta charset="UTF-8">
<title><%= Const.app_name %></title>
<link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/Style.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css"/>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>

<style type="text/css">
    #submitButton {
        color:white;
        border-radius: 5px;
        margin-top: 13px;
        height: 45px;
        font-size: 16px;
        text-align: center;
        color: #fff;
        background-color: #333;
        cursor: pointer;
        font-weight: 400;
        border: none;
    }
    .role-select {
        margin-top: 15px;
        font-size: 16px;
    }
</style>

</head>
<body>

<%
    try {
        if (session.getAttribute("unique_id") != null) {
            String userRole = (String) session.getAttribute("role");
            if ("Administrator".equals(userRole)) {
                request.getRequestDispatcher("admin-dashboard.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("user-dashboard.jsp").forward(request, response);
            }
        }
    } catch (Exception e) {
        // Handle exception if necessary
    }
%>

<div class="wrapper">
    <section class="form signup">
        <header><%= Const.app_name %></header>
        <form id="loginForm">
            <div class="error-txt" id="error_text" style="display:none;">This is an error message!</div>
            <div class="field input">
                <label for="email">Email Address</label>
                <input type="text" name="email" id="email" placeholder="Enter your email">
            </div>
            <div class="field input">
                <label>Password</label>
                <input type="password" name="password" id="password" placeholder="Enter your Password">
                <i class="fas fa-eye"></i>
            </div>
            <!-- Role selection for admin or user -->
            <div class="role-select">
                <label>Login as:</label>
                <select id="userRole" name="userRole">
                    <option value="user">User</option>
                    <option value="admin">Administrator</option>
                </select>
            </div>
            <div>
            <a href="/ChatAPP/jsp/forgetpassword.jsp">Forget Password</a>
           </div>
            <div class="field button">
                <button type="button" id="submitButton" onclick="submitForm()">Continue to Chat</button>
            </div>
        </form>
        <div class="link">Not yet signed up? <a href="<%=request.getContextPath()%>/signup-now">Signup now</a></div>
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
<script>
    // Function to create a cookie
    function createCookie(name, value, days) {
        const date = new Date();
        date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
        const expires = "expires=" + date.toUTCString();
        document.cookie = name + "=" + encodeURIComponent(value) + "; " + expires + "; path=/; SameSite=Strict";
    }

    // Function to read a cookie
    function getCookie(name) {
        const cookies = document.cookie.split(';');
        for (let cookie of cookies) {
            const [cookieName, cookieValue] = cookie.trim().split('=');
            if (cookieName === name) {
                return decodeURIComponent(cookieValue);
            }
        }
        return null;
    }

    // Function to delete a cookie
    function deleteCookie(name) {
        document.cookie = `${name}=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;`;
    }

    // Handle login
    function handleLogin() {
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        fetch("/ChatAPP/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ email, password }),
        })
            .then((response) => response.text())
            .then((data) => {
                if (data === "success") {
                    // Create a cookie to remember the user
                    createCookie("user_id", "12345", 7); // Expires in 7 days
                    window.location.href = "/ChatAPP/user-dashboard.jsp";
                } else {
                    alert("Login failed: " + data);
                }
            })
            .catch((error) => {
                console.error("Error:", error);
            });
    }

    // Check for cookie on page load
    window.onload = function () {
        const userId = getCookie("user_id");
        if (userId) {
            // Automatically log the user in
            fetch("/ChatAPP/auto-login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ userId }),
            })
                .then((response) => response.text())
                .then((data) => {
                    if (data === "success") {
                        window.location.href = "/ChatAPP/user-dashboard.jsp";
                    } else {
                        deleteCookie("user_id"); // Delete the invalid cookie
                    }
                })
                .catch((error) => {
                    console.error("Error:", error);
                });
        }
    };
</script>
<script src="http://localhost:8080/ChatAPP/js/login.js"></script>
<script src="http://localhost:8080/ChatAPP/js/pass_show_hode.js"></script>

</body>
</html>

