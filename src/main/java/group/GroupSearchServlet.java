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

/*@WebServlet("/GroupSearchServlet")
public class GroupSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchQuery = request.getParameter("searchQuery");
        List<Map<String, String>> searchResults = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                String searchSql = "SELECT group_id, group_name FROM `groups` WHERE group_name LIKE ?";
                try (PreparedStatement searchStmt = connection.prepareStatement(searchSql)) {
                    searchStmt.setString(1, "%" + searchQuery + "%");
                    ResultSet searchRs = searchStmt.executeQuery();

                    while (searchRs.next()) {
                        Map<String, String> group = new HashMap<>();
                        group.put("group_id", searchRs.getString("group_id"));
                        group.put("group_name", searchRs.getString("group_name"));
                        searchResults.add(group);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
            return;
        }

        request.setAttribute("searchResults", searchResults);
        request.getRequestDispatcher("/jsp/group.jsp").forward(request, response);
    }
}
*/



@WebServlet("/GroupSearchServlet")
public class GroupSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get the current user's ID from the session
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");
        
        
        System.out.println(userId + " session from GroupSearchServlet ");

        if (userId == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String searchQuery = request.getParameter("searchQuery");
        List<Map<String, String>> searchResults = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                // Query to find groups not joined or requested by the user
                String searchSql = """
                    SELECT g.group_id, g.group_name
                    FROM `groups` g
                    WHERE g.group_name LIKE ?
                    AND g.group_id NOT IN (
                        SELECT gm.group_id FROM group_members gm WHERE gm.user_id = ?
                        UNION
                        SELECT gr.group_id FROM group_join_requests gr WHERE gr.user_id = ?
                    )
                """;

                try (PreparedStatement searchStmt = connection.prepareStatement(searchSql)) {
                    searchStmt.setString(1, "%" + searchQuery + "%");
                    searchStmt.setString(2, userId);
                    searchStmt.setString(3, userId);

                    ResultSet searchRs = searchStmt.executeQuery();

                    while (searchRs.next()) {
                        Map<String, String> group = new HashMap<>();
                        group.put("group_id", searchRs.getString("group_id"));
                        group.put("group_name", searchRs.getString("group_name"));
                        searchResults.add(group);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error: " + e.getMessage());
            return;
        }

        // Set the search results as a request attribute
        request.setAttribute("searchResults", searchResults);

        // Forward to the JSP for display
        request.getRequestDispatcher("/jsp/group.jsp").forward(request, response);
    }
}








