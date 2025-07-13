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
MIT License

Copyright (c) [2025] [Shahi Bahadur]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
