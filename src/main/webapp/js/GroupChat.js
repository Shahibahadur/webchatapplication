/**
 * 
 */

window.onload = function () {
    const messagesContainer = document.querySelector('.messages');

    // Function to scroll to the bottom
    function scrollToBottom() {
        messagesContainer.scrollTop = messagesContainer.scrollHeight;
    }

    // Call scrollToBottom initially to ensure it scrolls to the bottom on page load
    scrollToBottom();

    // Simulate receiving a new message (for demonstration purposes)
    setInterval(() => {
        // Example of dynamically adding a new message
        const newMessage = document.createElement('div');
        newMessage.className = 'message';
        newMessage.innerHTML = `
            <div class="sender">Demo User</div>
            <div class="text">This is a new message!</div>
        `;
        messagesContainer.appendChild(newMessage);

        // Scroll to the bottom after adding a new message
        scrollToBottom();
    }, 5000); // Adjust timing as needed
};
