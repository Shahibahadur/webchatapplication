$(document).ready(function () {
    const gjrContainer = $('#gjrNotification'); // Group Join Requests
    const frContainer = $('#frNotification');  // Friend Requests

    function clearContainer(container, message) {
        container.html(`<p>${message}</p>`);
    }

    function fetchJoinRequests() {
        return $.ajax({
            url: "/ChatAPP/GroupJoinRequestsServlet",
            method: "GET",
            dataType: "json",
        });
    }

    function fetchFriendRequest() {
        return $.ajax({
            url: "/ChatAPP/friend-requests-received",
            method: "GET",
            dataType: "json",
        });
    }

    function displayRequests(requests, container) {
        if (requests.length > 0) {
            container.html(""); // Clear existing content
            requests.forEach(req => {
                const requestDiv = $(`
                    <div class="request-item">
                        <img src="/ChatAPP/uploads/${req.image}" alt="Profile Picture" class="profile-picture">
                        <p>${req.user_fname} ${req.user_lname} wants to join ${req.group_name}</p>
                        <button class="action-button" data-request-id="${req.request_id}" data-action="approve">Approve</button>
                        <button class="action-button" data-request-id="${req.request_id}" data-action="reject">Reject</button>
                    </div>
                `);
                container.append(requestDiv);
            });
        } else {
            clearContainer(container, "No join requests are available.");
        }
    }

    function displayFriendRequest(requests) {
        if (requests.length > 0) {
            frContainer.html(""); // Clear existing content
            requests.forEach(req => {
                const frDiv = $(`
                    <div class="friend-request">
                        <img src="/ChatAPP/uploads/${req.sender_image}" alt="Profile Picture" class="profile-picture">
                        <span class="friend-name">${req.fname} ${req.lname}</span>
                        <button class="action-button" data-request-id="${req.request_id}" data-action="approve">Approve</button>
                        <button class="action-button" data-request-id="${req.request_id}" data-action="reject">Reject</button>
                    </div>
                `);
                frContainer.append(frDiv);
            });
        } else {
            clearContainer(frContainer, "No friend requests at the moment.");
        }
    }

    async function handleAction(url, button) {
        const requestId = button.data('request-id');
        const action = button.data('action');

        try {
            const response = await $.ajax({
                url,
                method: "POST",
                contentType: "application/x-www-form-urlencoded",
                data: `request_id=${encodeURIComponent(requestId)}&action=${encodeURIComponent(action)}`,
            });

            if (response.status) {
                alert(`Request ${action}ed successfully!`);
                refreshFriendRequest();
                refreshRequests();
            } else {
                alert(response.message || "Failed to manage the request. Try again.");
            }
        } catch (error) {
            console.error(`Error managing ${action}:`, error);
            alert("An error occurred while processing the request.");
        }
    }

    function refreshRequests() {
        fetchJoinRequests().done(data => {
            console.log("Join Requests:", data);
            displayRequests(data, gjrContainer);
        });
    }

    function refreshFriendRequest() {
        fetchFriendRequest().done(data => {
            console.log("Friend Requests:", data);
            displayFriendRequest(data);
        });
    }

    gjrContainer.on('click', '.action-button', function (event) {
        handleAction("/ChatAPP/ManageGroupRequestsServlet", $(event.target));
    });

    frContainer.on('click', '.action-button', function (event) {
        handleAction("/ChatAPP/ProcessRequestServlet", $(event.target));
    });

    refreshFriendRequest();
    refreshRequests();
});
