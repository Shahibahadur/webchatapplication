package group;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/ManageGroupInviteServlet")
public class ManageGroupInviteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ManageGroupInviteServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String requestId = request.getParameter("request_id");
        String action = request.getParameter("action");

        if (requestId == null || action == null) {
            out.write("{\"status\": false, \"message\": \"Invalid request parameters\"}");
            return;
        }

        // Validate action value (only allow approve or reject)
        if (!"approve".equals(action) && !"reject".equals(action)) {
            out.write("{\"status\": false, \"message\": \"Invalid action\"}");
            return;
        }

        try (Connection conn = new DatabaseConfig().getConnection()) {
            String sql = "UPDATE group_invites SET status = ? WHERE invite_id = ?";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, action); // safe parameter insertion
                pstmt.setInt(2, Integer.parseInt(requestId));

                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    out.write("{\"status\": true, \"message\": \"Invite " + action + "d successfully\"}");
                } else {
                    out.write("{\"status\": false, \"message\": \"Invite not found\"}");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database error occurred", e);
            out.write("{\"status\": false, \"message\": \"Database error occurred\"}");
        }
    }
}
