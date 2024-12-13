// Function to fetch messages
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
                    ${message.attachmentPath ? `<img src="/ChatAPP/GroupImage/${message.attachmentPath}" alt="Sent image">` : ""}
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

                <img src="/ChatAPP/groupimge/${groupDetail.groupImage}" alt="Group Image">
                <strong>${groupDetail.groupName}</strong>
            `;
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
    setInterval(fetchMessages, 700);

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
