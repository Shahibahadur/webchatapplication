<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Group Members</title>
    <link rel="stylesheet" href="styles.css"> <!-- Add your stylesheet here -->
    <script src="https://kit.fontawesome.com/a076d05399.js" crossorigin="anonymous"></script>
</head>
<body>

<div class="wrapper">
    <!-- Group Information -->
    <header>
        <h1>Group Name: <span id="groupName">Avengers</span></h1>
        <p>Group Admin: <span id="groupAdmin">Admin Name</span></p>
        <a href="GroupListServlet">Back to Groups</a>
    </header>

    <!-- Members List -->
    <section class="members">
        <h2>Group Members</h2>
        <div id="membersContainer">
            <!-- Members will be dynamically added here -->
        </div>
    </section>

    <!-- Leave Group Button -->
    <section class="leave-group">
        <form action="LeaveGroupServlet" method="post">
            <input type="hidden" name="group_id" value="<%= request.getParameter("group_id") %>">
            <button type="submit" class="btn leave">Leave Group</button>
        </form>
    </section>
</div>

<script>

    const members = [
        { id: 1, name: "Pawan Shahi", role: "admin" },
        { id: 2, name: "John Doe", role: "member" },
        { id: 3, name: "Jane Smith", role: "member" }
    ];

    const membersContainer = document.getElementById("membersContainer");
    const groupAdmin = "Pawan Shahi"; // Replace with backend admin name

    function renderMembers() {
        members.forEach(member => {
            const memberDiv = document.createElement("div");
            memberDiv.classList.add("member");

            const memberName = document.createElement("span");
            memberName.textContent = member.name;

            const actions = document.createElement("div");
            actions.classList.add("actions");

            if (member.name !== groupAdmin) {
                // Add Remove Button for Admin
                if (groupAdmin === "Pawan Shahi") { // Replace with session user check
                    const removeBtn = document.createElement("button");
                    removeBtn.textContent = "Remove";
                    removeBtn.classList.add("btn", "remove");
                    removeBtn.addEventListener("click", () => removeMember(member.id));
                    actions.appendChild(removeBtn);
                }

                // Add Leave Button for Members
                const leaveBtn = document.createElement("button");
                leaveBtn.textContent = "Leave";
                leaveBtn.classList.add("btn", "leave");
                leaveBtn.addEventListener("click", () => leaveGroup(member.id));
                actions.appendChild(leaveBtn);
            }

            memberDiv.appendChild(memberName);
            memberDiv.appendChild(actions);
            membersContainer.appendChild(memberDiv);
        });
    }

    function removeMember(memberId) {
        alert(`Removing member with ID: ${memberId}`);
        // Send request to backend to remove member
    }

    function leaveGroup(memberId) {
        alert(`Leaving group for member ID: ${memberId}`);
        // Send request to backend to leave group
    }

    renderMembers();
</script>

<style>
    body {
        font-family: Arial, sans-serif;
        margin: 20px;
        padding: 20px;
    }

    .wrapper {
        max-width: 800px;
        margin: 0 auto;
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
    }

    header {
        margin-bottom: 20px;
    }

    .members .member {
        display: flex;
        justify-content: space-between;
        padding: 10px;
        border: 1px solid #ddd;
        margin-bottom: 10px;
        border-radius: 5px;
    }

    .btn {
        padding: 5px 10px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
    }

    .btn.remove {
        background-color: #e74c3c;
        color: white;
    }

    .btn.leave {
        background-color: #3498db;
        color: white;
    }

    .leave-group {
        text-align: center;
        margin-top: 20px;
    }

    .leave-group .btn {
        background-color: #e74c3c;
        color: white;
    }
</style>

</body>
</html>
