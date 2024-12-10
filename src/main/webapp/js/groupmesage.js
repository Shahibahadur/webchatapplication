// Fetch messages for the group
async function fetchMessages() {
    const messageContainer = document.getElementById("messageContainer");
    const groupId = messageContainer.dataset.groupId;

    messageContainer.innerHTML = ""; // Clear the message container

    try {
        const response = await fetch(`/ChatAPP/GetMessage?groupId=${encodeURIComponent(groupId)}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const messages = await response.json();

            messages.forEach((message) => {
                const messageElement = document.createElement("div");
                messageElement.classList.add("message");
                messageElement.innerHTML = `
                    <strong>${message.senderName}</strong>
                    <span>${new Date(message.timestamp).toLocaleString()}</span>
                    <p>${message.messageText}</p>
                `;
                messageContainer.appendChild(messageElement);
            });
        } else {
            console.error("Failed to fetch messages:", response.statusText);
        }
    } catch (error) {
        console.error("Error fetching messages:", error);
    }
}

// Fetch group details
async function fetchGroup() {
    const groupElement = document.getElementById("group");
    const groupId = groupElement.dataset.groupId;
	console.log(groupId);


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


function submitForm() {
    const FORM = document.getElementById("message_box");
    const FORMDATA = new FormData(FORM);
    let message = document.getElementById("message").value.trim();
    let image = document.getElementById("image-upload").files[0];

    if (!message && !image) {
        alert("No message or image provided. Cannot send an empty message.");
        return;
    }

    if (message && message.trim() !== "") {
        message = message.replaceAll(" ", "__5oO84a9__");
        FORMDATA.append("message", message);
    } else {
        FORMDATA.append("message", "");
    }

    if (image) {
        FORMDATA.append("image", image);
    }

    const groupID = document.getElementById("groupID").value;
    FORMDATA.append("groupID", groupID);

    const XHR = new XMLHttpRequest();
    const URL = "http://localhost:8080/ChatAPP/GroupMessage";

    XHR.open("POST", URL, true);
    XHR.onreadystatechange = function () {
        if (XHR.readyState === 4 && XHR.status === 200) {
            document.getElementById("message").value = "";
            document.getElementById("image-upload").value = "";
            scrollToBottom();
        } else if (XHR.readyState === 4) {
            console.error("Error in message submission:", XHR.status, XHR.statusText);
        }
    };
    XHR.send(FORMDATA);

    function scrollToBottom() {
        const CHATBOX = document.querySelector(".chat-box");
        CHATBOX.scrollTop = CHATBOX.scrollHeight;
    }
}



// Initialize the page
document.addEventListener("DOMContentLoaded", () => {
	
	const FORM = document.querySelector(".typing-area"),
	INPUTFIELD = FORM ? FORM.querySelector(".input-field") : null,
	SENDBUTTON = FORM ? FORM.querySelector("button") : null,
	CHATBOX = document.querySelector(".chat-box");
	
	if(!FORM || !INPUTFIELD || !SENDBUTTON || !CHATBOX){
	console.error("one or more elements could not found. Please check the selector");
	}
	
	
 	FORM.onsubmit = (e)=>{
		
	/*	e->It represents the event object associated with the submit event.*/

		e.preventDefault();
	};
	
	
	INPUTFIELD.addEventListener('keydown',function(event){
		
		// keydown event is triggred when key is pressed down
		if(event.key === 'Enter'|| event.keyCode === 13){
			event.preventDefault();
			submitForm();
		}
		
	});
	
    fetchGroup();
   fetchMessages();
    setInterval(fetchMessages,2000)
    // Refresh messages every 2 seconds

});
