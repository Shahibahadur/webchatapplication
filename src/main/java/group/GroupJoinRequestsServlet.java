package group;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/GroupJoinRequestsServlet")
public class GroupJoinRequestsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized access.");
            return;
        }

        ArrayList<Map<String, String>> joinRequests = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            String sql = "SELECT r.request_id, r.user_id, u.user_name, g.group_name "
                    + "FROM group_join_requests r "
                    + "JOIN users u ON r.user_id = u.unique_id "
                    + "JOIN `groups` g ON r.group_id = g.group_id "
                    + "WHERE g.admin_id = ? AND r.request_status = 'pending'";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> requests = new HashMap<>();
                        requests.put("request_id", rs.getString("request_id"));
                        requests.put("user_id", rs.getString("user_id"));
                        requests.put("user_name", rs.getString("user_name"));
                        requests.put("group_name", rs.getString("group_name"));
                        joinRequests.add(requests);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error fetching join requests: " + e.getMessage());
            return;
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(new Gson().toJson(joinRequests));
    }
}
