package friend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/ProcessRequestServlet")
public class ProcessRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("request_id");
        String action = request.getParameter("action"); // Either 'approve' or 'reject'

        if (requestId == null || action == null || requestId.isEmpty() || action.isEmpty()) {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\": false, \"message\": \"Invalid request parameters\"}");
            return;
        }

        try {
            DatabaseConfig db = new DatabaseConfig();
            Connection conn = db.getConnection();

            if ("approve".equalsIgnoreCase(action)) {
                String updateRequestStatusSQL = "UPDATE friend_request SET status = 'accepted' WHERE request_id = ?";
                String insertFriendshipSQL =
                        "INSERT INTO friends (sender, receiver, friendship_date) " +
                        "VALUES ((SELECT sender FROM friend_request WHERE request_id = ?), " +
                        "(SELECT receiver FROM friend_request WHERE request_id = ?), NOW())";

                try (PreparedStatement stmtUpdate = conn.prepareStatement(updateRequestStatusSQL);
                     PreparedStatement stmtInsert = conn.prepareStatement(insertFriendshipSQL)) {

                    stmtUpdate.setString(1, requestId);
                    stmtUpdate.executeUpdate();

                    stmtInsert.setString(1, requestId);
                    stmtInsert.setString(2, requestId);
                    stmtInsert.executeUpdate();

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"status\": true, \"message\": \"Request approved successfully\"}");
                }

            } else if ("reject".equalsIgnoreCase(action)) {
                String updateRequestStatusSQL = "UPDATE friend_request SET status = 'rejected' WHERE request_id = ?";

                try (PreparedStatement stmtUpdate = conn.prepareStatement(updateRequestStatusSQL)) {
                    stmtUpdate.setString(1, requestId);
                    stmtUpdate.executeUpdate();

                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("{\"status\": true, \"message\": \"Request rejected successfully\"}");
                }

            } else {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"status\": false, \"message\": \"Invalid action parameter\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\": false, \"message\": \"An error occurred while processing the request\"}");
        }
    }
}
