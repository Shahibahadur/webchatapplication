

// Ensure the script runs after the DOM is fully loaded
document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector(".typing-area"),
        inputField = form ? form.querySelector(".input-field") : null,
        sendBtn = form ? form.querySelector("button") : null,
        chatBox = document.querySelector(".chat-box");

    // Log the elements to check if they are correctly selected
    console.log("Form:", form);
    console.log("Input Field:", inputField);
    console.log("Send Button:", sendBtn);
    console.log("Chat Box:", chatBox);

    // Ensure elements are not null before proceeding
    if (!form || !inputField || !sendBtn || !chatBox) {
        console.error("One or more elements could not be found. Please check the selectors or ensure the DOM is fully loaded.");
        return;
    }

    // Get the outgoing and incoming IDs
    let out_id = document.getElementById("outgoing_id").value;
    let in_id = document.getElementById("incoming_id").value;

    // Prevent the default form submission
    form.onsubmit = (e) => {
        e.preventDefault();
    };

    // Add event listener to handle 'Enter' key submission
    inputField.addEventListener('keydown', function(event){
        if (event.key === 'Enter' || event.keyCode === 13) {
            event.preventDefault();
            submitForm(); // Call the submitForm function when 'Enter' is pressed
        }
    });

    // Add event listener for button click
  /*  sendBtn.addEventListener("click", (event) => {
		event.preventDefault();//prevent from default submission
        submitForm(); // Call the submitForm function when the button is clicked
    });*/

    // Update the chat box every 700ms
	
	sendGetRequest();
	
    //const intervalId = setInterval(sendGetRequest, 700);

    // Handle scrolling to bottom of the chat box
    chatBox.onmouseenter = () => {
        chatBox.classList.add("active");
    };

    chatBox.onmouseleave = () => {
        chatBox.classList.remove("active");
    };

    // Define the function to scroll to the bottom of the chat box
    function scrollToBottom() {
        chatBox.scrollTop = chatBox.scrollHeight;
    }
});


function submitForm() {
    const form1 = document.getElementById("message_box");
    const formData = new FormData(form1);
    let msg = document.getElementById("message").value.trim();
	let img = document.getElementById("image-upload").files[0];
	

    // Prevent sending empty messages
    if (!msg && !img) {
		console.warn("No message and image are provided. Cannot send an empty message.");
        return;
    }

    // Replace spaces with a unique string to handle spaces correctly in URLs
	
	if(msg && msg.trim()!==""){
    msg = msg.replaceAll(" ", "__5oO84a9__");
	
	formData.append("message", msg);
	}else{
		formData.append("message","");
	}
	
	if(img){
		
		formData.append("image", img);
	}
	

    // Get the outgoing and incoming IDs
    let out_id = document.getElementById("outgoing_id").value;
    let in_id = document.getElementById("incoming_id").value;
	
	formData.append("outgoing_id", out_id);
	formData.append("incoming_id", in_id);
	

    // Create an AJAX request to send the message
    const xhr = new XMLHttpRequest();
 	const url = "insertChat";
	
	//from here to insertChatServlet
	
    console.log("Sending POST request to:", url);

    xhr.open("POST", url, true);
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            document.getElementById("message").value = ""; // Clear the input field
			document.getElementById("image-upload").value="";
            scrollToBottom(); // Scroll to the bottom of the chat box
        } else if (xhr.readyState === 4) {
            console.error("Error in message submission:", xhr.status, xhr.statusText);
        }
    };
    xhr.send(formData);
}

// Define scrollToBottom function globally if needed
function scrollToBottom() {
    const chatBox = document.querySelector(".chat-box");
    chatBox.scrollTop = chatBox.scrollHeight;
}

// Define sendGetRequest function to fetch messages periodically
function sendGetRequest() {
    let out_id = document.getElementById("outgoing_id").value;
    let in_id = document.getElementById("incoming_id").value;
    const servletURL = `get_chat?outgoing_id=${encodeURIComponent(out_id)}&incoming_id=${encodeURIComponent(in_id)}`;

    fetch(servletURL, {
        method: 'GET',
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    }).then(data => {
        const chatBox = document.querySelector(".chat-box");
        chatBox.innerHTML = data;
		console.log(data);
        if (!chatBox.classList.contains("active")) {
            scrollToBottom();
        }
    }).catch(error => {
        console.error('Error:', error);
    });
}
