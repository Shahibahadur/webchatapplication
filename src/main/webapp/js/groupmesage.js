/*// Function to fetch messages
async function fetchMessages() {
    const messageContainer = document.getElementById("messageContainer");
    const groupId = messageContainer.dataset.groupId;
    const senderId = messageContainer.dataset.sender;

    if (!groupId
 || !senderId) {
        console.error("Missing group ID or sender ID in messageContainer dataset.");
        return;
    }

    try {
        const response = await fetch(`/ChatAPP/GetMessage?groupId=${encodeURIComponent(groupId)}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const messages = await response.json();
            const chatBox = document.querySelector(".chat-box");
            let messageHTML = "";

            messages.forEach((message) => {
                if (!message.messageText && !message.attachmentPath) {
                    return; // Skip messages without text and attachments
                }

                const isOutgoing = senderId === message.senderId;
                const messageContent = `
                    ${message.messageText ? `<p>${message.messageText}</p>` : ""}
                    ${message.attachmentPath ? `<img src="/ChatAPP/${message.attachmentPath}" alt="Sent image">` : ""}
                `;

                messageHTML += `
                    <div class="chat ${isOutgoing ? "outgoing" : "incoming"}">
                        <div class="details">
                            <span>${new Date(message.timestamp).toLocaleString()}</span>
                            <strong>${message.senderName}</strong>
                            ${messageContent}
                        </div>
                    </div>
                `;
            });

            chatBox.innerHTML = messageHTML;

            // Scroll to bottom if not actively scrolling up
            if (!chatBox.classList.contains("active")) {
                scrollToBottom();
            }
        } else {
            console.error("Failed to fetch messages:", response.statusText);
        }
    } catch (error) {
        console.error("Error fetching messages:", error);
    }
}*/




async function fetchMessages() {
    const messageContainer = document.getElementById("messageContainer");
    const groupId = messageContainer.dataset.groupId;
    const senderId = messageContainer.dataset.sender;

    if (!groupId || !senderId) {
        console.error("Missing group ID or sender ID in messageContainer dataset.");
        return;
    }

    try {
        const response = await fetch(`/ChatAPP/GetMessage?groupId=${encodeURIComponent(groupId)}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const messages = await response.json();
            const chatBox = document.querySelector(".chat-box");
            let messageHTML = "";

            messages.forEach((message) => {
                if (!message.messageText && !message.attachmentPath) {
                    return; // Skip messages without text and attachments
                }

                const isOutgoing = senderId === message.senderId;
                let messageContent = "";
                let attachmentContent = "";

                // Build content for both message and attachment
                if (message.messageText) {
                    messageContent = `
                        <p style="margin: 0; padding-right: 10px; flex: 1; border: none;">${message.messageText}</p>
                        <span class="menu-trigger" onclick="toggleMenu(this)" style="cursor: pointer; font-size: 18px; color: #007bff;">...</span>
                        <div class="menu" style="display: none;">
                            <div onclick="deleteMessage('${message.timestamp}', '${message.messageText}')">Delete</div>
                            <div onclick="editMessage('${message.timestamp}', '${message.messageText}')">Edit</div>
                        </div>
                    `;
                }

                if (message.attachmentPath) {
                    attachmentContent = `
                        <img src="/ChatAPP/${message.attachmentPath}" alt="Sent image" style="width: 50px; height: 50px; object-fit: cover; margin-right: 2px;">
                        <span class="menu-trigger" onclick="toggleMenu(this)" style="cursor: pointer; font-size: 18px; color: #007bff;">...</span>
                        <div class="menu" style="display: none;">
                            <div onclick="deleteMessage('${message.timestamp}', '${message.attachmentPath}')">Delete</div>
                        </div>
                    `;
                }

                // Only append content if they are not null
                const messageContentFinal = messageContent ? messageContent : "";
                const attachmentContentFinal = attachmentContent ? attachmentContent : "";

                // Create HTML for the message (either outgoing or incoming)
                const messageHTMLContent = `
                    <div class="chat ${isOutgoing ? 'outgoing' : 'incoming'}">
                        <div class="details">
                            <span>${new Date(message.timestamp).toLocaleString()}</span>
                            <strong>${message.senderName}</strong>
                            <div class="message-container" 
                                onmouseover="this.querySelector('span').style.display='inline-block'" 
                                onmouseout="this.querySelector('span').style.display='none'" 
                                style="display: flex; align-items: center; padding: 10px; margin-bottom: 10px;">
                                ${messageContentFinal}
                                ${attachmentContentFinal}
                            </div>
                        </div>
                    </div>
                `;

                messageHTML += messageHTMLContent;
            });

            chatBox.innerHTML = messageHTML;

            // Scroll to bottom if not actively scrolling up
            if (!chatBox.classList.contains("active")) {
                scrollToBottom();
            }
        } else {
            console.error("Failed to fetch messages:", response.statusText);
        }
    } catch (error) {
        console.error("Error fetching messages:", error);
    }
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
		        messageInterval = setInterval(fetchMessages, 700);
		    }
}

// Close menu if clicked outside
document.addEventListener('click', (e) => {
    if (!e.target.closest('.message-container')) {
        document.querySelectorAll('.menu').forEach((menu) => {
            menu.style.display = 'none';
        });
		
		if (!messageInterval) {
				           messageInterval = setInterval(fetchMessages, 700);
				       }
    }
});

async function deleteMessage(date, text) {
  try {
    // Send the request to the server
    const response = await fetch('/ChatAPP/deleteGroupMessage', {
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
	fetchMessages();
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
            fetch('/ChatAPP/editGroupMessage', {
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
						fetchMessages();
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







// Function to fetch group details
async function fetchGroup() {
    const groupElement = document.getElementById("group");
    const groupId = groupElement.dataset.groupId;

    if (!groupId) {
        console.error("Missing group ID in group element dataset.");
        return;
    }

    try {
        const response = await fetch(`/ChatAPP/GetGroup?groupId=${encodeURIComponent(groupId)}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const groupDetail = await response.json();

            groupElement.innerHTML = `
              <a href="http://localhost:8080/ChatAPP/GroupDisplayServlet" class="back-icon"><i class="fas fa-arrow-left"></i></a>

                <img src="/ChatAPP/groupImages${groupDetail.groupImage}" alt="Group Image">
				
                <strong>${groupDetail.groupName}</strong>
            `;
			console.log(groupDetail.groupImage);
        } else {
            console.error("Failed to fetch group details:", response.statusText);
        }
    } catch (error) {
        console.error("Error fetching group details:", error);
    }
}

// Function to submit form
function submitForm() {
    const FORM = document.getElementById("message_box");
    const FORMDATA = new FormData(FORM);
    let message = document.getElementById("message").value.trim();
    let image = document.getElementById("image-upload").files[0];

    if (!message && !image) {
        alert("No message or image provided. Cannot send an empty message.");
        return;
    }

    if (message) {
        message = message.replaceAll(" ", "__5oO84a9__");
        FORMDATA.append("message", message);
    } else {
        FORMDATA.append("message", "");
    }

    if (image) {
        FORMDATA.append("image", image);
    }

    const groupId = document.getElementById("groupId").value;
    FORMDATA.append("groupId", groupId);

    const XHR = new XMLHttpRequest();
    const URL = "http://localhost:8080/ChatAPP/GroupMessage";

    XHR.open("POST", URL, true);
    XHR.onreadystatechange = function () {
        if (XHR.readyState === 4 && XHR.status === 200) {
            document.getElementById("message").value = "";
            document.getElementById("image-upload").value = "";
            fetchMessages(); // Fetch messages immediately after submission
        } else if (XHR.readyState === 4) {
            console.error("Error in message submission:", XHR.status, XHR.statusText);
        }
    };
    XHR.send(FORMDATA);
}

// Function to scroll to the bottom of the chat box
function scrollToBottom() {
    const CHATBOX = document.querySelector(".chat-box");
    CHATBOX.scrollTop = CHATBOX.scrollHeight;
}

// Initialize the page

let messageInterval;
document.addEventListener("DOMContentLoaded", () => {
    const FORM = document.querySelector(".typing-area");
    const INPUTFIELD = FORM ? FORM.querySelector(".input-field") : null;
    const SENDBUTTON = FORM ? FORM.querySelector("button") : null;
    const CHATBOX = document.querySelector(".chat-box");

    if (!FORM || !INPUTFIELD || !SENDBUTTON || !CHATBOX) {
        console.error("One or more required elements are missing.");
        return;
    }

    FORM.onsubmit = (e) => {
        e.preventDefault();
    };

    INPUTFIELD.addEventListener("keydown", function (event) {
        if (event.key === "Enter" || event.keyCode === 13) {
            event.preventDefault();
            submitForm();
        }
    });

    fetchGroup();
	fetchMessages();
	
   messageInterval = setInterval(fetchMessages, 700);

    CHATBOX.onmouseenter = () => {
        CHATBOX.classList.add("active");
    };

    CHATBOX.onmouseleave = () => {
        CHATBOX.classList.remove("active");
    };

    // Scroll event to detect user scrolling up
    CHATBOX.addEventListener("scroll", () => {
        if (CHATBOX.scrollTop < CHATBOX.scrollHeight - CHATBOX.clientHeight) {
            CHATBOX.classList.add("active");
        } else {
            CHATBOX.classList.remove("active");
        }
    });
});
