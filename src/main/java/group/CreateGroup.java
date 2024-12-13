package group;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

@WebServlet("/CreateGroup")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, 
    maxFileSize = 1024 * 1024 * 10, 
    maxRequestSize = 1024 * 1024 * 50
)
public class CreateGroup extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");

        // Redirect to login if the user is not logged in
        if (userId == null) {
            response.sendRedirect("jsp/login.jsp");
            return;
        }

        // Retrieve form data
        String groupName = request.getParameter("groupName");
        Part groupImagePart = request.getPart("groupImage");
        String[] friendIds = request.getParameterValues("friendIds");

        // Validate inputs
        if (groupName == null || groupName.isBlank()) {
            response.getWriter().println("Group name is required.");
            return;
        }

        // Define image path
        String imagePath = null;

        // Handle group image upload
        if (groupImagePart != null && groupImagePart.getSize() > 0) {
            String uploadDir = getServletContext().getRealPath("/groupImages");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
                response.getWriter().println("Failed to create upload directory.");
                return;
            }

            // Generate unique file name
            String fileName = UUID.randomUUID().toString() + "_" + groupImagePart.getSubmittedFileName();
            imagePath =  fileName;

            // Save file to the server
            groupImagePart.write(uploadDir + File.separator + fileName);
        }

        // Database operations
        try (Connection connection = new DatabaseConfig().getConnection()) {
            String insertGroupSql = "INSERT INTO `groups` (group_name, image, admin_id) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertGroupSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, groupName);
                stmt.setString(2, imagePath);
                stmt.setString(3, userId);
                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int groupId = generatedKeys.getInt(1);

                    // Add members to the group
                    if (friendIds != null) {
                        String addMemberSql = "INSERT INTO group_join_requests (group_id, user_id, request_status) "
                                + "VALUES (?, ?, 'invited')";
                        try (PreparedStatement addMemberStmt = connection.prepareStatement(addMemberSql)) {
                            for (String friendId : friendIds) {
                                addMemberStmt.setInt(1, groupId);
                                addMemberStmt.setString(2, friendId);
                                addMemberStmt.addBatch();
                            }
                            addMemberStmt.executeBatch();
                        }
                    }

                    // Add admin to group_join_requests and group_members
                    String addAdminSql1 = "INSERT INTO group_join_requests (group_id, user_id, request_status) VALUES (?, ?, 'approved')";
                    String addAdminSql2 = "INSERT INTO group_members (group_id, user_id, status) VALUES(?, ?, 'approved')";
                    
                    try (PreparedStatement addAdminStmt1 = connection.prepareStatement(addAdminSql1);
                         PreparedStatement addAdminStmt2 = connection.prepareStatement(addAdminSql2)) {
                        // Add admin to group_join_requests
                        addAdminStmt1.setInt(1, groupId);
                        addAdminStmt1.setString(2, userId);
                        addAdminStmt1.executeUpdate();

                        // Add admin to group_members
                        addAdminStmt2.setInt(1, groupId);
                        addAdminStmt2.setString(2, userId);
                        addAdminStmt2.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
            return;
        }

        // Redirect to group page upon successful creation
        response.sendRedirect("jsp/group.jsp");
    }
}
