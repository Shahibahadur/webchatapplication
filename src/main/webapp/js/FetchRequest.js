document.addEventListener("DOMContentLoaded", () => {
    const requestsContainer = document.getElementById("requestsContainer");

    // Fetch and display join requests
    async function fetchJoinRequests() {
        try {
            const response = await fetch("/ChatAPP/GroupJoinRequestsServlet");
            if (response.ok) {
                const requests = await response.json();
                displayRequests(requests);
            } else {
                console.error("Failed to fetch join requests.");
            }
        } catch (error) {
            console.error("Error fetching join requests:", error);
        }
    }

    // Display requests in the container
    function displayRequests(requests) {
        requestsContainer.innerHTML = ""; // Clear existing content
        if (requests.length > 0) {
            requests.forEach((req) => {
                const requestDiv = document.createElement("div");
                requestDiv.innerHTML = `
                    <p>${req.user_name} wants to join ${req.group_name}</p>
                    <button data-request-id="${req.request_id}" data-action="approve">Approve</button>
                    <button data-request-id="${req.request_id}" data-action="reject">Reject</button>
                `;
                requestsContainer.appendChild(requestDiv);
            });
        } else {
            requestsContainer.innerHTML = "<p>No join requests available.</p>";
        }
    }

    // Handle approve/reject actions
    requestsContainer.addEventListener("click", async (event) => {
        if (event.target.tagName === "BUTTON") {
            const button = event.target;
            const requestId = button.dataset.requestId;
            const action = button.dataset.action;

            try {
                const response = await fetch("/ChatAPP/ManageGroupRequestsServlet", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: `request_id=${encodeURIComponent(requestId)}&action=${encodeURIComponent(action)}`,
                });

                if (response.ok) {
                    const result = await response.json();
                    if (result.success) {
                        alert(`Request ${action}d successfully!`);
                        fetchJoinRequests(); // Refresh the list
                    } else {
                        alert("Failed to manage the request. Try again.");
                    }
                } else {
                    console.error("Error managing request:", await response.text());
                }
            } catch (error) {
                console.error("Error:", error);
            }
        }
    });

    // Initial fetch
    fetchJoinRequests();
});
