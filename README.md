# Web-Based Chat Application

## Overview
This is a simple web-based chat application that allows users to:
- Send direct messages to other users
- Participate in group chats
- Basic chat functionality without complex features

The application uses a straightforward architecture without any frameworks, focusing on core functionality.

## Technologies Used
- **Backend**: Java
- **Database**: MySQL
- **Frontend**: HTML, CSS, JavaScript
- **Communication**: Fetch API (instead of WebSockets)

## Key Features
1. **User-to-User Messaging**: Send private messages to other registered users
2. **Group Chat**: Participate in conversations with multiple users
3. **Simple Interface**: Clean and straightforward UI
4. **Lightweight**: No framework overhead

## Installation & Setup

### Prerequisites
- Java JDK (version X or higher)
- MySQL Server
- Web server (like Apache Tomcat)

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/Shahibahadur/webchatapplication.git
   ```

2. Set up the database:
   - Create a MySQL database
   - Import the provided SQL schema file
   - Update database connection details in the Java code

3. Compile and run the Java backend

4. Deploy the frontend files to your web server

5. Access the application through your web browser

## Configuration
Modify the following files for your environment:
- `config.properties` (or equivalent) - for database connection details
- Adjust any server-related settings in the Java code as needed

## Usage
1. Register a new user account or login with existing credentials
2. View available users and groups
3. Select a user or group to start chatting
4. Type your message and send

## Limitations
- Uses HTTP polling (Fetch API) rather than WebSockets for real-time communication
- Basic functionality without advanced features like message history, file sharing, etc.
- No framework means some manual handling of requests/responses

## Future Enhancements
- Implement WebSocket for real-time communication
- Add message persistence and history
- Include user presence indicators
- Add file sharing capability
- Implement end-to-end encryption

## Contributing
Contributions are welcome! Please fork the repository and submit pull requests.

## License
