document.addEventListener("DOMContentLoaded", async () => {
    // Containers for displaying data
    const groupsContainer = document.getElementById("groupsContainer");
    const requestsContainer = document.getElementById("requestsContainer");

    try {
        // Fetch groups and join requests sequentially
        const groups = await fetchGroups();
        const requests = await fetchJoinRequests();

        // Display the fetched data
        displayGroups(groups);
        displayRequests(requests);
    } catch (error) {
        console.error("Error loading data:", error);
    }

    // Function to fetch groups
    async function fetchGroups() {
        try {
            const response = await fetch("http://localhost:8080/ChatAPP/GetGroupsServlet");
            if (!response.ok) {
                throw new Error("Failed to fetch groups");
            }
            return await response.json(); // Parse response as JSON
        } catch (error) {
            console.error("Error fetching groups:", error);
            throw error;
        }
    }

    // Function to fetch join requests
    async function fetchJoinRequests() {
        try {
            const response = await fetch("http://localhost:8080/ChatAPP/GroupJoinRequestsServlet");
            if (!response.ok) {
                throw new Error("Failed to fetch join requests");
            }
            return await response.json(); // Parse response as JSON
        } catch (error) {
            console.error("Error fetching join requests:", error);
            throw error;
        }
    }

    // Function to display groups
    function displayGroups(groups) {
        groupsContainer.innerHTML = ""; // Clear existing content
        if (groups.length > 0) {
            groups.forEach(group => {
                const groupDiv = document.createElement("div");
                groupDiv.className = "group";

                // Add group image
                const img = document.createElement("img");
                img.src = `/ChatAPP/images/${group.image}`;
                img.alt = "Group Image";
                img.style.width = "100px";
                img.style.height = "100px";
                img.style.objectFit = "cover";
                groupDiv.appendChild(img);

                // Add group name
                const groupName = document.createElement("h3");
                groupName.textContent = group.group_name;
                groupDiv.appendChild(groupName);

                // Add click event to redirect to group chat
                groupDiv.addEventListener("click", () => {
                    window.location.href = `/ChatAPP/jsp/GroupChat.jsp?group_id=${group.group_id}`;
                });

                groupsContainer.appendChild(groupDiv);
            });
        } else {
            groupsContainer.innerHTML = "<p>No groups available.</p>";
        }
    }

    // Function to display join requests
    function displayRequests(requests) {
        requestsContainer.innerHTML = ""; // Clear existing content
        if (requests.length > 0) {
            requests.forEach(req => {
                const requestDiv = document.createElement("div");
                requestDiv.innerHTML = `
                    <p>${req.user_fname} ${req.user_lname} wants to join ${req.group_name}</p>
                    <button data-request-id="${req.request_id}" data-action="approve">Approve</button>
                    <button data-request-id="${req.request_id}" data-action="reject">Reject</button>
                `;
                requestsContainer.appendChild(requestDiv);
            });
        } else {
            requestsContainer.innerHTML = "<p>No join requests available.</p>";
        }
    }

    // Handle approve/reject actions for join requests
    requestsContainer.addEventListener("click", async (event) => {
        if (event.target.tagName === "BUTTON") {
            const button = event.target;
            const requestId = button.dataset.requestId;
            const action = button.dataset.action;

            try {
                const response = await fetch("/ChatAPP/ManageGroupRequestsServlet", {
                    method: "POST",
                    headers: { "Content-Type": "application/x-www-form-urlencoded" },
                    body: `request_id=${encodeURIComponent(requestId)}&action=${encodeURIComponent(action)}`
                });

                if (response.ok) {
					console.log('testing');
                    const result = await response.json();
					console.log(result.status);
                    if (result.status) {
                        alert(`Request ${result.status} successfully!`);
					
                        const updatedRequests = await fetchJoinRequests();
                        displayRequests(updatedRequests); // Refresh the list
						
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
});
