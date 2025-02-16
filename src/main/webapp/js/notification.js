$(document).ready(function () {
    const gjrContainer = $('#gjrNotification'); // Group Join Requests
    const frContainer = $('#frNotification');  // Friend Requests
	const inviteContainer = $("#gInvite");
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
	
	
	function fetchInvite(){
		return $.ajax({
			url : "/ChatAPP/group-invites-received",
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
	
	
	
	
	
	function displayInvite(invites) {
	    console.log("Processing invites:", invites);

	    if (invites.length > 0) {
	        inviteContainer.empty();

	        invites.forEach(invite => {
	            console.log("Processing invite:", invite); // Log each invite object
	            const inDiv = $(`
	                <div class="group-invite">
	                    <img src="/ChatAPP/uploads/${invite.image ? invite.image : 'default-profile.png'}"
	                         alt="Profile Picture" class="profile-picture">
	                    <p>${invite.user_fname} ${invite.user_lname} invited you to join ${invite.group_name}</p>
	                    <button class="action-button" data-request-id="${invite.request_id}" data-action="approve">Approve</button>
	                    <button class="action-button" data-request-id="${invite.request_id}" data-action="reject">Reject</button>
	                </div>
	            `);
	            inviteContainer.append(inDiv);
	        });
	    } else {
	        console.warn("No invites available");
	        clearContainer(inviteContainer, "No Group Invites at the moment.");
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
				refreshInvite();
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
	
	function refreshInvite() {
	    fetchInvite().done(data => {
	        console.log("Group Invite API Response:", data); // Debugging
	        if (!Array.isArray(data)) {
	            console.error("Unexpected response format:", data);
	            return;
	        }
	        displayInvite(data);
	    }).fail(error => {
	        console.error("Failed to fetch invites:", error);
	    });
	}


    gjrContainer.on('click', '.action-button', function (event) {
        handleAction("/ChatAPP/ManageGroupRequestsServlet", $(event.target));
    });

    frContainer.on('click', '.action-button', function (event) {
        handleAction("/ChatAPP/ProcessRequestServlet", $(event.target));
    });
	
	inviteContainer.on('click','.action-button', function(event){
		handleAction("/ChatAPP/ManageGroupInviteServlet", $(event.target));
	})
	
	refreshInvite();
    refreshFriendRequest();
    refreshRequests();
});
