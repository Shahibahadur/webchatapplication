async function fetchMessages(){
	const messageContainer = document.getElementById("messageContainer");
	let id = messageConatiner.dataset.group_id;
	messageContainer.innerHTML="";
	
	try{
		const response = await fetch(`/ChattAPP/GetMessage?group_id={encodeURIComponent(id)}`,{
			
			method : "POST",
			headers: {
				"Content-Type" : "application/json" 
				
			}
		});
		
		if(response.ok){
			const messages = await response.json();
			
			messages.forEach((message)=>{
				
				const messageElement = document.createElement("div");
				messageElement.classList.add("message");
				messageElement.innerHTML=`
				<strong>${message.senderName}</strong>
				<span>${new Date(message.timestamp).toLocaleString()}</span>
				<span>${message.messageText}</span>
				`;
				messageContainer.appendChild(messageElement);
				
			});
			
		}else{
			console.error("Failed to fetch messages: ", response.statusText);
		}
	}catch(error){
		console.error("Error fetching messages:", error)
	}
}

document.addEventListner("DOMContentLoaded",()=>{
	fetchMessages();
})
