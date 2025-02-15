package group;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;
import com.google.gson.Gson;

// Display the groups in which user is joined
@WebServlet("/GroupDisplayServlet")
public class GroupDisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");

        System.out.println(userId + " session from GroupDisplayServlet");

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        List<Map<String, String>> joinedGroups = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            String joinedGroupsSql = """
                SELECT g.group_id, g.image, g.group_name, gm.joined_at
                FROM `groups` g
                JOIN group_members gm ON g.group_id = gm.group_id
                WHERE gm.user_id = ? AND gm.status = 'approved' AND g.admin_id != ?
            """;
            try (PreparedStatement joinedGroupsStmt = connection.prepareStatement(joinedGroupsSql)) {
                joinedGroupsStmt.setString(1, userId);
                joinedGroupsStmt.setString(2, userId);
                ResultSet joinedGroupsRs = joinedGroupsStmt.executeQuery();

                while (joinedGroupsRs.next()) {
                    Map<String, String> group = new HashMap<>();
                    group.put("group_id", joinedGroupsRs.getString("group_id"));
                    group.put("group_name", joinedGroupsRs.getString("group_name"));
                    group.put("group_image", joinedGroupsRs.getString("image"));

                    Timestamp joinedAt = joinedGroupsRs.getTimestamp("joined_at");
                    if (joinedAt != null) {
                        String formattedTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(joinedAt);
                        group.put("joined_at", formattedTimestamp);
                    } else {
                        group.put("joined_at", "N/A");
                    }
                    joinedGroups.add(group);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(new Gson().toJson(Map.of("error", e.getMessage())));
            return;
        }

        // Convert joinedGroups to JSON and send it as a response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(joinedGroups));
    }
}
