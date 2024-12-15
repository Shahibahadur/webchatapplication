// admin.js

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

// Dummy data for users and groups

/*let users = [
    { id: 1, name: "John Doe" },
    { id: 2, name: "Jane Smith" },
    { id: 3, name: "Alice Johnson" },
];

let groups = [
    { id: 1, name: "Science Club" },
    { id: 2, name: "Literature Group" },
];*/



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

// Function to render user table
async function renderUsers() {

    //testing code 
    let users;

   try{
    const response = await fetch("http://localhost:8080/ChatApp/ManagingUsers");
        if(!response.ok){
            throw new  Error("failed to fetch manage user request");
        }
         users = await response.json();
    }catch(error){
        console.error("Error in loading data :", error);
        throw error;
    }


    //testing code 


    
    const userTableBody = document.getElementById("userTableBody");
    userTableBody.innerHTML = "";
    users.forEach(user => {
        const row = `<tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td><button class="btn" onclick="removeUser(${user.id})">Remove</button></td>
        </tr>`;
        userTableBody.innerHTML += row;
    });
}

// Function to render group table
async function renderGroups() {

    let groups;
    //testing
    try{
    const response = await fetch("http://localhost:8080/ChatAPP/ManageGroups");
        if(!response.ok){

            throw new Error("Failed to fetch groups details");
            
        }
        groups = response.json();
    }catch(error){
        console.error("error in loading data: ", error);
    }


    
    const groupTableBody = document.getElementById("groupTableBody");
    groupTableBody.innerHTML = "";
    groups.forEach(group => {
        const row = `<tr>
            <td>${group.groupId}</td>
            <td>${group.groupName}</td>
            <td><button class="btn" onclick="removeGroup(${group.groupId})">Remove</button></td>
        </tr>`;
        groupTableBody.innerHTML += row;
    });
}

// Remove user
async function removeUser(id) {
    users = users.filter(user => user.id !== id);
    try{
        const response = await fetch("http://localhost:8080/ChatAPP/DeleteUSers",{
                                    method: "POST",
                                    headers:{"Content_type":"application/x-www-form-urlencoded"},
                                    body:`user_id=${encodeURIComponent(id)}`
                                    });
        if(!response.ok){
            throw new Error("failed to remove the users");
        }
    }catch(error){
        console.log("user not removed: ", error);
    }
    //testing
    renderUsers();
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


// Remove user with confirmation
/*function removeUser(id) {
    // Show confirmation alert before removing the user
    const confirmation = confirm("Are you sure you want to remove this user?");
    if (confirmation) {
        // Filter out the user with the given ID
        users = users.filter(user => user.id !== id);
        renderUsers(); // Re-render the user table
    } else {
        // If the user clicks "Cancel," do nothing
        console.log("User removal canceled");
    }
}*/

// Remove group with confirmation
async function removeGroup(groupId) {
    // Show confirmation alert before removing the group
    const confirmation = confirm("Are you sure you want to remove this group?");
    if (confirmation) {
        // Filter out the group with the given ID
		groups = groups.filter(group => group.groupId !== groupId);
		   //testing
		   try{

		       const response = await fetch("http://localhost:8080/ChatAPP/DeleteGroup",{
		           method : "POST",
		           headers : {"Content-Type":"application/x-www-form-urlencoded"}
		       });
		   }catch(error){
		       console.log("error in loading data: ",error);
		   }; // Re-render the group table
    } else {
        // If the user clicks "Cancel," do nothing
        console.log("Group removal canceled");
    }
}


// Initialize page
document.addEventListener("DOMContentLoaded", () => {
    // Set logged-in user name
    document.getElementById("loggedInUser").textContent = `${loggedInUser.name} (${loggedInUser.role})`;

    // Default: Show admins section
    showSection("admins");
});
