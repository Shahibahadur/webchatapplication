// Validation functions defined outside the event listener
function validateName(name, fieldName, message) {
  const namePattern = /^[a-zA-Z\s]{3,20}$/;
  if (!name) {
    message.push(`${fieldName} cannot be empty`);
  } else if (!namePattern.test(name)) {
    message.push(`${fieldName} should be 3-20 characters long and contain only letters`);
  }
}

function validatePassword(password, message) {
  const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/;
  if (!password) {
    message.push("Password cannot be null");
  } else if (!passwordPattern.test(password)) {
    message.push(
      "Password must be 8-20 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character."
    );
  }
}

function validateEmail(email, message) {
  const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
  if (!email) {
    message.push("Email cannot be null");
  } else if (!emailPattern.test(email)) {
    message.push("Please enter a valid email address");
  }
}

function validateImage(fileInput, message) {
  const allowedExtensions = /(\.jpg|\.jpeg|\.png|\.gif)$/i;
  const MAXSIZE = 5 * 1024 * 1024; // 5 MB

  if (!fileInput.files.length) {
    message.push("Please select an image file");
    return;
  }
  const file = fileInput.files[0];
  if (!allowedExtensions.test(file.name)) {
    message.push("Only image files (jpg, jpeg, png, gif) are allowed");
  } else if (file.size > MAXSIZE) {
    message.push("Image size should not exceed 5 MB");
  }
}

// Event listener and form submission handling
document.addEventListener('DOMContentLoaded', () => {
  let form = document.getElementById("form");
  let error = document.getElementById("error");

  form.addEventListener('submit', (e) => {
    e.preventDefault(); // Prevent form submission

    // Reset error messages
    let message = [];

    // Get current input values at the time of submission
    let fname = document.getElementById("fname").value;
    let lname = document.getElementById("lname").value;
    let email = document.getElementById("email").value;
    let password = document.getElementById("password").value;
    let image = document.getElementById("image"); // For file inputs, fetch the element itself

    // Validate inputs
    validateName(fname, "First Name", message);
    validateName(lname, "Last Name", message);
    validatePassword(password, message);
    validateEmail(email, message);
    validateImage(image, message);

    // Display errors
    if (message.length > 0) {
      error.innerText = message.join(", ");
    } else {
      error.innerText = "";
      alert("Form is valid! Submitting...");
      form.submit(); // You can remove this if you want to handle submission manually
    }
  });
});
