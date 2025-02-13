<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang = "eng">
<head>
    <meta charset="UTF-8">
    <title>Create Group</title>
    <link rel="stylesheet" href="http://localhost:8080/ChatAPP/cssFiles/creategroup.css">
</head>
<body>
    <div class="Wrapper">
        <section class="create-group">
            <header>
                <h2>Create a Group</h2>
            </header>
            <form action="/ChatAPP/CreateGroup" method="post" enctype="multipart/form-data">
                <div>
                    <label for="groupName">Group Name:</label>
                    <input type="text" id="groupName" name="groupName" placeholder="Enter group name" required>
                </div>
                
                <div>
                    <label for="groupImage">Group Image:</label>
                    <input type="file" id="groupImage" name="groupImage" accept="image/*">
                </div>
                
                <div>
                    <label>Friends:</label>
                    <div id="friendsContainer">
                        <!-- Friends list will be populated dynamically -->
                    </div>
                </div>
                
                <button type="submit">Create Group</button>
            </form>
        </section>
    </div>
    <script type="text/javascript" src ="http://localhost:8080/ChatAPP/js/creategroup.js"></script>
    
</body>

	<script type = "text/javascript">
	
	window.addEventListener("pageshow", (event) => {
	    if (event.persisted) {
	        // The page is being loaded from the cache, so refresh it
	        window.location.reload();
	    }
	});

	
	</script>
</html>
