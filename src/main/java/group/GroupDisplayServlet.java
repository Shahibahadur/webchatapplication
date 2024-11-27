package group;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

@WebServlet("/GroupDisplayServlet")
public class GroupDisplayServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");
        
        System.out.println(userId + "session from GroupDisplayServlet");

	
		/*
		 * if (userId == null) { response.sendRedirect("jsp/login.jsp"); return; }
		 */
		 

        List<Map<String, String>> joinedGroups = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            String joinedGroupsSql = """
                SELECT g.group_id, g.group_name
                FROM `groups` g
                JOIN group_members gm ON g.group_id = gm.group_id
                WHERE gm.user_id = ? AND gm.status = 'approved'
            """;
            try (PreparedStatement joinedGroupsStmt = connection.prepareStatement(joinedGroupsSql)) {
                joinedGroupsStmt.setString(1, userId);
                ResultSet joinedGroupsRs = joinedGroupsStmt.executeQuery();

                while (joinedGroupsRs.next()) {
                    Map<String, String> group = new HashMap<>();
                    group.put("group_id", joinedGroupsRs.getString("group_id"));
                    group.put("group_name", joinedGroupsRs.getString("group_name"));
                    joinedGroups.add(group);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
            return;
        }

        request.setAttribute("joinedGroups", joinedGroups);
        request.getRequestDispatcher("jsp/group.jsp").forward(request, response);
    }
}
