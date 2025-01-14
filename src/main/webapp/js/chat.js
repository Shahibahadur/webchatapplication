
// Ensure the script runs after the DOM is fully loaded
let messageInterval;

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

	
	sendGetRequest();
	
    messageInterval = setInterval(sendGetRequest, 700);

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
			sendGetRequest()
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


function toggleMenu(trigger,type) {
    const menu = trigger.nextElementSibling;
    // Close other menus if open
	
	clearInterval(messageInterval);

    document.querySelectorAll('.menu').forEach((m) => {
        if (m !== menu) m.style.display = 'none';
		
    });

    // Toggle current menu
    menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
	
	// Restart the interval when the menu is closed
	    if (menu.style.display === 'none') {
	        messageInterval = setInterval(sendGetRequest, 700);
	    }
}

// Close menu if clicked outside
document.addEventListener('click', (e) => {
    if (!e.target.closest('.message-container')) {
        document.querySelectorAll('.menu').forEach((menu) => {
            menu.style.display = 'none';
        });
		
		if (!messageInterval) {
		           messageInterval = setInterval(sendGetRequest, 700);
		       }
    }
});

async function deleteMessage(date, text) {
  try {
    // Send the request to the server
    const response = await fetch('/ChatAPP/deleteMessage', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json', // Set content type as JSON
      },
      body: JSON.stringify({ date, text }), // Pass the data in the body
    });

    // Check if the request was successful
    if (!response.ok) {
      throw new Error(`Failed to delete message: ${response.statusText}`);
    }

    // Parse the server response
    const result = await response.json();
    console.log('Message deleted successfully:', result);

    // Update the UI or notify the user
    alert('Message deleted successfully!');
	
	sendGetRequest(); // Refresh chat box after deletion

  } catch (error) {
    console.error('Error deleting message:', error);
    alert('Failed to delete message. Please try again.');
  }
}

// edit message
function editMessage(timestamp, originalMessage) {
    const messageElement = event.target.closest(".message-container").querySelector("p");

    // Save the original message in case the user cancels
    const oldMessage = messageElement.textContent;

    // Create an input element
    const input = document.createElement("input");
    input.type = "text";
    input.value = oldMessage;
    input.style.flex = "1"; // Ensures it matches the original `p` element's layout
    input.style.border = "1px solid #ccc";
    input.style.padding = "5px";
    input.style.margin = "0";

    // Replace the <p> element with the input
    messageElement.replaceWith(input);

    // Flag to prevent multiple replacements
    let isReplaced = false;

    // Focus on the input field and move the cursor to the end
    input.focus();
    input.setSelectionRange(input.value.length, input.value.length);

    function saveMessage() {
        // Prevent this logic from running multiple times
        if (isReplaced) return;
        isReplaced = true;

        const newMessage = input.value.trim();

        if (newMessage === "") {
            alert("Message cannot be empty.");
            input.focus();
            isReplaced = false; // Allow retry
            return;
        }

        // Replace the input back with a <p> tag
        const updatedP = document.createElement("p");
        updatedP.textContent = newMessage;
        updatedP.style.margin = "0";
        updatedP.style.paddingRight = "10px";
        updatedP.style.flex = "1";

        input.replaceWith(updatedP);

        // Call the edit function using fetch
        if (newMessage !== oldMessage) {
            fetch('/ChatAPP/editMessage', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    timestamp: timestamp,
                    newMessage: newMessage,
                }),
            })
                .then((response) => {
                    if (response.ok) {
                        console.log("Message updated successfully.");
						sendGetRequest(); // Refresh chat box after editing

                    } else {
                        console.error("Failed to update message.");
                    }
                })
                .catch((error) => {
                    console.error("Error updating message:", error);
                });
        }
    }

    // Handle input blur or pressing Enter
    input.addEventListener("blur", saveMessage);
    input.addEventListener("keydown", (e) => {
        if (e.key === "Enter") {
            saveMessage();
        }
    });
}





