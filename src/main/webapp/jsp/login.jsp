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
</style>

</head>
<body>

<%
    try {
        if (session.getAttribute("unique_id") != null) {
            request.getRequestDispatcher("user.jsp").forward(request, response);
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
            <div class="field button">
                <button type="button" id="submitButton" onclick="submitForm()">Continue to Chat</button>
            </div>
        </form>
        <div class="link">Not yet signed up? <a href="<%=request.getContextPath()%>/signup-now">Signup now</a></div>
       <!--  <div class="link">Not yet signed up? <a href="index.jsp">Signup now</a></div> -->
    </section>
</div>

<script src="http://localhost:8080/ChatAPP/js/login.js"></script>
<script src="http://localhost:8080/ChatAPP/js/pass_show_hode.js"></script>
</body>
</html>
