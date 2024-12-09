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

// Initialize the page
document.addEventListener("DOMContentLoaded", () => {
    fetchGroup();
   fetchMessages();

    // Refresh messages every 2 seconds

});
