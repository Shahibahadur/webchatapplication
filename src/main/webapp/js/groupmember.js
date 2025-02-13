document.addEventListener('DOMContentLoaded',  function(){
	
	const fetchGroupMember = new Promisse((resolve,reject)=>{
		fetch('/ChatAPP/groupMember')
		.then((response)=>{
			if(response.ok){
				return response.json();
			}else{
				throw new Error("Failed to fetch the data");
			}
			
		}
		)
		.then((data)=>{
			resolve(data);
			
		})
		.catch((error)=>{
			reject(error);
		})
		
	});
	
	fetchGroupMember
	.then((data)=>{
		const membersContainer = document.getElementById("membersContainer");
		const groupAdmin = document.gtElementById('groupAdmin').value;
		data.forEach(member=>{
			const memberDiv = document.createElement("div");
			memberDiv.classList.add("member");
			const memberName = document.createElement("span");
			memberName.textContent=member.name;
			const actions = document.createElement('div');
			actions.classList.add("actions");
			if(member.id !==groupAdminId){
				
			}
			
		})
	})
	
})