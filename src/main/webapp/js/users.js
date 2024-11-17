
const searchBar =  document.querySelector(".users .search input"),
//input element
searchBtn = document.querySelector(".users .search button");

const usersList = document.getElementById("user_list");
					
searchBtn.onclick = ()=>{

  searchBar.classList.toggle("active");
  searchBar.focus();
  searchBtn.classList.toggle("active");
  searchBar.value = "";
}

searchBar.onkeyup = ()=>{
	//this event trigers when user release a key on the keyboard after pressing it
	let searchTerm = searchBar.value; 
	if(searchTerm != ""){
		searchBar.classList.add("active");
	}else{
		searchBar.classList.remove("active");
	}
	
	let xhr = new XMLHttpRequest();
	xhr.open("POST","http://localhost:8080/ChatAPP/search",true);
	xhr.onload = ()=> {
		if(xhr.readyState === XMLHttpRequest.DONE){
			if(xhr.status === 200){
				let data = xhr.response;
				usersList.innerHTML = data;
			}
		}
	}
	xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xhr.send("searchTerm=" + searchTerm);
}
 
function sendGetRequest() {
  // Define the URL of your servlet page
  const servletUrl = 'http://localhost:8080/ChatAPP/users';
  //here to UserServlet.java

  // Send a GET request using the fetch API
  fetch(servletUrl, {
    method: 'GET',
    // You can set headers and other options here if needed
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.text(); // or response.json() if the servlet returns JSON
    })
    .then(data => {
      // Process the response data here
      //console.log(data); 
      if(!searchBar.classList.contains("active")){
		  usersList.innerHTML = data;
	  }
      
      //usersList.innerHTML = data;
    })
    .catch(error => {
      console.error('Error:', error);
    });
}
/*console.log("users.js is loaded");
function profile() {
            location.href = "http://localhost:8080/ChatAPP/jsp/profile.jsp";
        }*/
	
const intervalId = setInterval(sendGetRequest, 500);
