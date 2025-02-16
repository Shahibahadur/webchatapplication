package group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet(name = "GroupInviteReceived", urlPatterns = "/group-invites-received")
public class GroupInviteReceived extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        // Get the logged-in user's unique ID from the session
        String loggedInUserId = (String) session.getAttribute("unique_id");
        System.out.println("Logged-in user ID: " + loggedInUserId + " from GroupInviteReceived");

        // List to store the received group invitations
        List<Map<String, String>> receivedInvites = new ArrayList<>();

        if (loggedInUserId == null || loggedInUserId.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        try {
        	Connection conn = new DatabaseConfig().getConnection();
            String query =
                "SELECT " +
                "    gjr.request_id, " +
                "    gjr.request_status, " +
                "    g.group_name, " +
                "    g.group_image, " +
                "    g.group_admin, " +
                "    u.fname AS admin_fname, " +
                "    u.lname AS admin_lname " +
                "FROM " +
                "    group_join_requests gjr " +
                "INNER JOIN " +
                "    groups g " +
                "ON " +
                "    gjr.group_id = g.group_id " +
                "INNER JOIN " +
                "    users u " +
                "ON " +
                "    g.group_admin = u.unique_id " +
                "WHERE " +
                "    gjr.user_id = ? AND gjr.request_status = 'invited'";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, loggedInUserId);

            // Execute query and process results
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> inviteInfo = new HashMap<>();
                inviteInfo.put("request_id", rs.getString("request_id"));
                inviteInfo.put("request_status", rs.getString("request_status"));
                inviteInfo.put("group_name", rs.getString("group_name"));
                inviteInfo.put("group_image", rs.getString("group_image") != null ? rs.getString("group_image") : "default_group_image.png");
                inviteInfo.put("group_admin", rs.getString("group_admin"));
                inviteInfo.put("admin_fname", rs.getString("admin_fname"));
                inviteInfo.put("admin_lname", rs.getString("admin_lname"));
                receivedInvites.add(inviteInfo);

                // Log each fetched row
                System.out.println("Fetched Group Invite: " + inviteInfo);
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert the list of received invitations to JSON and send as the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(receivedInvites));
    }
}
