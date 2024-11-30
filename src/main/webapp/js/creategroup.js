	

async function fetchFriends() {
		try{
		
        const response = await fetch('http://localhost:8080/ChatAPP/FriendsListServlet');
        const friends = await response.json();  // Assuming the response is a JSON array
        const friendsContainer = document.getElementById('friendsContainer');
        
        // Clear the container before adding friends
        friendsContainer.innerHTML = '';

        friends.forEach(friend => {
            // Check if the friend object contains the necessary properties
            if (friend.friend_id && friend.fname) {
                const checkbox = document.createElement('input');
                checkbox.type = 'checkbox';
                checkbox.name = 'friendIds';
                checkbox.value = friend.unique_id;
				console.log(friend.unique_id);
                checkbox.id = `friend_${friend.friend_id}`;
                
                const label = document.createElement('label');
                label.htmlFor = `friend_${friend.friend_id}`;
                label.textContent = `${friend.fname} ${friend.lname}`;
                
                const div = document.createElement('div');
                div.appendChild(checkbox);
                div.appendChild(label);
                
                friendsContainer.appendChild(div);
            } else {
                console.error("Friend data is missing required properties:", friend);
            }
        });
    } catch (error) {
        console.error('Error fetching friends:', error);
    }
}

window.onload = fetchFriends;
