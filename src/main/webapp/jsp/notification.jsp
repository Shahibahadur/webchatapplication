<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
 <link rel="stylesheet" type="text/css" href="http://localhost:8080/ChatAPP/cssFiles/notification.css">
</head>
<body>
 <div class="notification-container">
	       <div>
		       <p>Group Join Request</p>
		        <div id = "gjrNotification" >
					<!-- display group join request -->       
		        </div>
	        </div>
	        <div>
		        <p>Friend Request <p>
		        <div id = "frNotification">
		        	<!-- display friend request -->
		        </div>
	        </div>
        
        </div>
        
        	<script type = "text/javascript">
	
	window.addEventListener("pageshow", (event) => {
	    if (event.persisted) {
	        // The page is being loaded from the cache, so refresh it
	        window.location.reload();
	    }
	});

	
	</script>
   
<script type = "text/javascript" src="/ChatAPP/js/notification.js"></script>
</body>
</html>