document.addEventListener("DOMContentLoaded", async () => {
    const groupsContainer = document.getElementById("groupsContainer");

    try {
        const response = await fetch("http://localhost:8080/ChatAPP/GetGroupsServlet");
        const groups = await response.json();

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
                window.location.href = `/ChatAPP/jspGroupChat.jsp?group_id=${group.group_id}`;
            });

            groupsContainer.appendChild(groupDiv);
        });
    } catch (error) {
        console.error("Error fetching groups:", error);
    }
});
