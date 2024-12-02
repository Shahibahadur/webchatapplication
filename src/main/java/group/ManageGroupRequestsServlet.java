package group;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/ManageGroupRequestsServlet")
public class ManageGroupRequestsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestId = request.getParameter("request_id");
        String action = request.getParameter("action");

        JsonObject jsonResponse = new JsonObject();

        if (requestId == null || action == null || (!action.equals("approve") && !action.equals("reject"))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Invalid request parameters.");
            response.getWriter().write(jsonResponse.toString());
            return;
        }

        // Database operation to update the request status
        try (Connection connection = new DatabaseConfig().getConnection()) {
            String sql = "UPDATE group_join_requests SET request_status = ? WHERE request_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, action.equals("approve") ? "approved" : "rejected");
                stmt.setInt(2, Integer.parseInt(requestId));

                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    jsonResponse.addProperty("success", true);
                } else {
                    jsonResponse.addProperty("success", false);
                    jsonResponse.addProperty("message", "No request found with the provided ID.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.addProperty("success", false);
            jsonResponse.addProperty("message", "Database error: " + e.getMessage());
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(jsonResponse.toString());
    }
}
