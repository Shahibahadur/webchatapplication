package group;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import mypackage.DatabaseConfig;

@WebServlet(name = "GroupMessageServlet" ,urlPatterns = {"/GroupMessage"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,     // 10MB
    maxRequestSize = 1024 * 1024 * 50   // 50MB
)
public class GroupMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String senderId = (String) session.getAttribute("unique_id");
        

        // Redirect to login if the user is not logged in
        if (senderId == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }

        // Get form data
        String group_id = request.getParameter("groupId");
        String messageText = request.getParameter("message");
        messageText = messageText != null ? messageText.replaceAll("__5oO84a9__"," "):"";
        Part attachmentPart = request.getPart("image");

        if (group_id == null || group_id.isBlank()) {
            response.getWriter().println("Group ID is required.");
            return;
        }

        String attachmentPath = null;

        // Handle file attachment
        if (attachmentPart != null && attachmentPart.getSize() > 0) {
            String uploadDir = getServletContext().getRealPath("/attachments");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
                response.getWriter().println("Failed to create upload directory.");
                return;
            }

            String fileName = UUID.randomUUID().toString() + "_" + attachmentPart.getSubmittedFileName();
            attachmentPath = "attachments/" + fileName;

            attachmentPart.write(uploadDir + File.separator + fileName);
        }

        // Insert message into the database
        if(!messageText.isEmpty()||attachmentPart!=null) {
        try (Connection connection = new DatabaseConfig().getConnection()) {
            String sql = "INSERT INTO group_messages (group_id, sender_id, message_text, attachment_path) VALUES (?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, Integer.parseInt(group_id));
                stmt.setString(2, senderId);
                if(messageText.isBlank()) {
                	stmt.setNull(3, java.sql.Types.VARCHAR);
                }else {
                stmt.setString(3, messageText);
                }
                if(attachmentPath!=null) {
                    stmt.setString(4, attachmentPath);	
                }else {
                	stmt.setNull(4, java.sql.Types.VARCHAR);
                }
                stmt.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
            return;
        }
        }else {
        	response.sendRedirect("error.jsp");
        }

        response.sendRedirect("jsp/GroupChat.jsp?group_id=" + group_id);
    }
}
