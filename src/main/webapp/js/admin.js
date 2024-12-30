// Dummy data for logged-in admin user
const loggedInUser = {
    name: "Admin John",
    role: "Administrator",
};

// Dummy data for admins with active/inactive status
const admins = [
    { id: 1, name: "Admin John", status: "Active" },
    { id: 2, name: "Admin Jane", status: "Inactive" },
    { id: 3, name: "Admin Mike", status: "Active" },
];


// Function to render admin table
function renderAdmins() {
    const adminTableBody = document.getElementById("adminTableBody");
    adminTableBody.innerHTML = "";
    admins.forEach(admin => {
        const row = `<tr>
            <td>${admin.id}</td>
            <td>${admin.name}</td>
            <td>${admin.status}</td>
        </tr>`;
        adminTableBody.innerHTML += row;
    });
}



// Function to render group table
async function renderGroups() {
    let groups;

    // Fetch group data
    try {
        const response = await fetch("http://localhost:8080/ChatAPP/ShowGroups");
        if (!response.ok) {
            throw new Error("Failed to fetch group details");
        }
        groups = await response.json();
    } catch (error) {
        console.error("Error in loading data: ", error);
        const groupTableBody = document.getElementById("groupTableBody");
        groupTableBody.innerHTML = `<tr><td colspan="3">Failed to load group details. Please try again later.</td></tr>`;
        return;
    }

    // Safeguard if no groups are fetched
    if (!groups || groups.length === 0) {
        console.warn("No groups available to display.");
        const groupTableBody = document.getElementById("groupTableBody");
        groupTableBody.innerHTML = `<tr><td colspan="3">No groups found.</td></tr>`;
        return;
    }

    // Populate the table with group data
    const groupTableBody = document.getElementById("groupTableBody");
    let rows = "";
    groups.forEach(group => {
        rows += `
            <tr>
                <td>${group.groupId}</td>
                <td>${group.groupName}</td>
                <td><button class="btn" onclick="removeGroup(${group.groupId})">Remove</button></td>
            </tr>`;
    });
    groupTableBody.innerHTML = rows;
}


// Remove group with confirmation

async function removeGroup(groupId) {
	console.log(groupId);
    // Show confirmation alert before removing the group
    const confirmation = confirm("Are you sure you want to remove this group?");
    if (confirmation) {
        // Filter out the group with the given ID
		//groups = groups.filter(group => group.groupId !== groupId);
		   //testing
		   try{

		       const response = await fetch("http://localhost:8080/ChatAPP/DeleteGroup",{
		           method : "POST",
		           headers : {"Content-Type":"application/x-www-form-urlencoded"},
			   	body:`groupId=${encodeURIComponent(groupId)}`
		       });
			   if(!response.ok){
				   throw new Error("Failed to delete the group");
			   }

			   renderGroups();
			   
		   }catch(error){
		       console.log("error in removing  group: ",error);
			   alert("Failed to remove the  group.Please try again");
			   
		   }; // Re-render the group table
    } else {
        // If the user clicks "Cancel," do nothing
        console.log("Group removal canceled");
    }
}






// Function to render user table
async function renderUsers() {
    const userTableBody = document.getElementById("userTableBody");
    let users;

    try {
        // Fetch user details from the server
        const response = await fetch("http://localhost:8080/ChatAPP/ShowUsers");
		console.log(response);
        if (!response.ok) {
            throw new Error("Failed to fetch user details");
        }
        users = await response.json();
    } catch (error) {
        console.error("Error in loading data:", error);
        userTableBody.innerHTML = `<tr><td colspan="3" class="error">Failed to load user details. Please try again later.</td></tr>`;
        return;
    }

    // Handle empty state
    if (!users || users.length === 0) {
        console.warn("No users available to show");
        userTableBody.innerHTML = `<tr><td colspan="3" class="empty">No users available</td></tr>`;
        return;
    }

    // Populate the user table
    const fragment = document.createDocumentFragment();
    users.forEach(user => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${user.uniqueId}</td>
            <td>${user.userName}</td>
            <td><button class="btn" onclick="removeUser('${encodeURIComponent(user.uniqueId)}')">Remove</button></td>
        `;
        fragment.appendChild(row);
    });
    userTableBody.innerHTML = ""; // Clear existing rows
    userTableBody.appendChild(fragment); // Append all rows at once
}


// Remove user with confirmation
async function removeUser(id) {
	console.log(id);
    // Show confirmation alert before removing the user
    const confirmation = confirm("Are you sure you want to remove this user?");
    if (confirmation) {
		   try{
		       const response = await fetch("http://localhost:8080/ChatAPP/DeleteUser",{
		                                   method: "POST",
		                                   headers:{"Content-type":"application/x-www-form-urlencoded"},
		                                   body:`userId=${encodeURIComponent(id)}`
		                                   });
			   console.log("response object: "+response);
		       if(!response.ok){
			       conseole.error("Failed to remove user: ", response.status, response.statusText);
		           throw new Error("failed to remove the users. HTTP status: "+response.status);
		       }
			   
			   const responseData = await response.json();
			   console.log("Response from Server:", responseData);
			   
			   if(responseData.success){
				console.log("user removed suceessfully");
			   }else if(responseData.error){
				console.error("Error from the Server: ", responseData.error);
				
			   }			   
		   }catch(error){
		       console.log("Error while removing user: ", error);
		   }
		  
		
        renderUsers(); // Re-render the user table
    } else {
        // If the user clicks "Cancel," do nothing
        console.log("User removal canceled");
    }
}




// Function to handle tab switching
function showSection(sectionId) {
    // Hide all sections
    document.querySelectorAll(".container").forEach(section => {
        section.classList.remove("active");
    });

    // Show selected section
    const activeSection = document.getElementById(sectionId);
    activeSection.classList.add("active");

    // Render content dynamically based on section
    if (sectionId === "admins") {
        renderAdmins();
    } else if (sectionId === "users") {
        renderUsers();
    } else if (sectionId === "groups") {
        renderGroups();
    }
}






// Initialize page
document.addEventListener("DOMContentLoaded", () => {
    // Set logged-in user name
    document.getElementById("loggedInUser").textContent = `${loggedInUser.name} (${loggedInUser.role})`;

    // Default: Show admins section
    showSection("admins");
});
