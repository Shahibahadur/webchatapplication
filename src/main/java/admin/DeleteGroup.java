package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/DeleteGroup")
public class DeleteGroup extends HttpServlet {
    private static final long serialVersionUID = 1L;


/*    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String groupIdParam = request.getParameter("groupId");

        // Validate the groupId parameter
        if (groupIdParam == null || groupIdParam.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Invalid group ID\"}");
            return;
        }

        int groupId;
        try {
            groupId = Integer.parseInt(groupIdParam);
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Group ID must be a number\"}");
            return;
        }

        try (Connection conn = new DatabaseConfig().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM `groups` WHERE group_id = ?")) {

            pstmt.setInt(1, groupId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": \"Group deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Group not found\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"Failed to delete the group\"}");
        }
    }

    */
@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String groupIdParam = request.getParameter("groupId");

    // Validate the groupId parameter
    if (groupIdParam == null || groupIdParam.isEmpty()) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"error\": \"Invalid group ID\"}");
        return;
    }

    int groupId;
    try {
        groupId = Integer.parseInt(groupIdParam);
    } catch (NumberFormatException e) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("{\"error\": \"Group ID must be a number\"}");
        return;
    }

    try (Connection conn = new DatabaseConfig().getConnection()) {
        conn.setAutoCommit(false); // Start transaction

        // Delete related records from other tables first
        try (PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM group_messages WHERE group_id = ?");
             PreparedStatement pstmt2 = conn.prepareStatement("DELETE FROM group_members WHERE group_id = ?");
             PreparedStatement pstmt3 = conn.prepareStatement("DELETE FROM `groups` WHERE group_id = ?")) {

            pstmt1.setInt(1, groupId);
            pstmt1.executeUpdate();

            pstmt2.setInt(1, groupId);
            pstmt2.executeUpdate();

            pstmt3.setInt(1, groupId);
            int rowsAffected = pstmt3.executeUpdate();

            if (rowsAffected > 0) {
                conn.commit(); // Commit transaction
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": \"Group deleted successfully\"}");
            } else {
                conn.rollback(); // Rollback transaction
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\": \"Group not found\"}");
            }
        } catch (Exception e) {
            conn.rollback(); // Rollback transaction on error
            throw e;
        }
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("{\"error\": \"Failed to delete the group\"}");
    }
}
    
}
