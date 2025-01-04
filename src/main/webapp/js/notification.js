/**
 * 
 */
$(document).ready(function(){
	const requestsContainer = $('#requestContainer');
	function fetchJoinRequests(){	
	 $.ajax({
			url:"http://localhost:8080/ChatAPP/GetGroupsServlet",
			method: "GET",
			dataType: "json",
			
		});
		}
		
		fetchJoinRequests()
		.done(function(data){
			console.log("Sucess: ", data);
			 displayRequests(data);
		})
		.fail(function(error){
			console.error("Error: ", error);
		})
		
		function displayRequests(requests){
			requestsContainer.html("");
			if (requests.length > 0) {
			            requests.forEach(req => {
			                const requestDiv = $('<div>');
			                requestDiv.innerHTML = `
								<img src='uploads/${req.image}' alt = "profile picture">
			                    <p>${req.user_fname} ${req.user_lname} wants to join ${req.group_name}</p>
			                    <button class = "action-button" data-request-id="${req.request_id}" data-action="approve">Approve</button>
			                    <button class = "action-button" data-request-id="${req.request_id}" data-action="reject">Reject</button>
			                `;
			                requestsContainer.append(requestDiv);
			            });
				
			}else{
				requestsContainer.html("<p>No join request is available </p>");
			}
		}
		
		requestsContainer.on('click','.action-button', async function(event){
			if(event.target.name ==== "BUTTON"){
				const button = event.target;
				const requestId = button.dataset.requestId;
				const action = button.dataset.action;
				try{
				const response = $.ajax({
						url: "/ChatAPP/ManageGroupRequestsServle",
						method: "POST",
						headers:{"content-type": "application/x-www-form-urlencoded"},
						body: `request_id=${encodeURIComponent(requestId)}&action=${encodeURIComponent(action)}`
					});
				}
				
			}
		})

})


 