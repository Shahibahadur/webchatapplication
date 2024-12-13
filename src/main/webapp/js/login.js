/*//select form element here
const form = document.querySelector(".signup form");
const errorText = form.querySelector(".error-txt");
console.log(form.querySelector(".error-txt").value);
console.log("testing");
   function submitForm() {
            // Serialize the form data
            var formData = $("#loginForm").serialize();
			console.log(formData);
            // Send data to the JSP page using AJAX
            $.ajax({
                type: "POST",
                url: "http://localhost:8080/ChatAPP/Login",
				//noted
				//request sent LoginServlet where session is set
				
                data: formData,
                success: function(response) {
                    // Handle the response from the JSP page
                    $("#error_text").html(response);
                    console.log("before success");
                     if(response == "success"){
						console.log('able to connect sucess');
						
						console.log(location.href);
						location.href = "/ChatAPP/user-chatbox";
						//move to url_user_servlet and from there
						//
					 	errorText.style.background = "lightgreen";
					 	errorText.style.color = "green";
					 
					 }else
					 {
						errorText.style.background = "#f8d7da";
						errorText.style.color = "#721c24";
                        errorText.textContent = response;
						errorText.style.display = "block";
					 }
                },
                error: function(error) {
                    console.error('Error:', error);
                }
            });
       } */

 // this one is working js for user

//finished
/*const form = document.querySelector(".signup form");
const errorText = form.querySelector("#error_text");

function submitForm(event) {
    event.preventDefault(); // Prevent the default form submission

    // Serialize form data using FormData
    const formData = new FormData(form);
    const formParams = new URLSearchParams(formData).toString();  // Convert formData to URL-encoded string

    console.log("Form Data:", formParams);

    // Use Fetch API for making the POST request
    fetch("login", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded", // Set header to form-urlencoded
        },
        body: formParams,  // Send URL-encoded string as body
    })
    .then(response => response.text())  // Parse response as text
    .then(responseText => {
        // Display the response in the errorText element
        errorText.innerHTML = responseText;

        if (responseText === "success") {
            // Redirect to the chatbox on success
            location.href = "user-chatbox";
            errorText.style.background = "lightgreen";
            errorText.style.color = "green";
        } else {
            // Show error message on failure
            errorText.style.background = "#f8d7da";  // Correct the color code
            errorText.style.color = "#721c24";
            errorText.textContent = responseText;
            errorText.style.display = "block";
        }
    })
    .catch(error => {
        // Handle any error that occurred during the request
        console.error('Error occurred:', error);
    });
}

// Attach the submitForm function to the form's submit event
form.addEventListener('submit', submitForm);*/




const form = document.querySelector(".signup form");
const errorText = form.querySelector(".error-txt");
console.log(form.querySelector(".error-txt").value);
console.log("testing");

function submitForm() {
    // Serialize the form data
    var formData = $("#loginForm").serialize();
    console.log(formData);

    // Get the selected role (user or admin)
    const role = document.getElementById('userRole').value; // Fetch the selected role (user or admin)
    let loginUrl = "";

    // Set the URL based on the role selected
    if (role === "user") {
        loginUrl = "http://localhost:8080/ChatAPP/Login"; // URL for user login
		console.log(role);
    } else if (role === "admin") {
        loginUrl = "http://localhost:8080/ChatAPP/adminLoginServlet"; // URL for admin login
    }

    // Send data to the appropriate servlet based on role
    $.ajax({
        type: "POST",
        url: loginUrl, // Use the appropriate URL for user or admin
        data: formData,
        success: function(response) {
            // Handle the response from the JSP page
            $("#error_text").html(response);
            console.log("before success");
            if (response === "success") {
                console.log('Login successful');
                console.log(location.href);

                // Redirect based on role
                if (role === "user") {
                    location.href = "/ChatAPP/user-chatbox"; // Redirect to user chatbox
                } else if (role === "admin") {
                    location.href = "/ChatAPP/admin-dashboard"; // Redirect to admin dashboard
                }

                // Style the success message
                errorText.style.background = "lightgreen";
                errorText.style.color = "green";
            } else {
                // Display error message for failed login
                errorText.style.background = "#f8d7da";
                errorText.style.color = "#721c24";
                errorText.textContent = response;
                errorText.style.display = "block";
            }
        },
        error: function(error) {
            console.error('Error:', error);
        }
    });
}

