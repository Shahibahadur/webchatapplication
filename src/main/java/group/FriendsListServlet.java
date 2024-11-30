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

@WebServlet("/FriendsListServlet")
public class FriendsListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");

        if (userId == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"User not logged in.\"}");
            return;
        }

        List<Map<String, String>> friends = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            String sql = "SELECT u.unique_id, u.fname, u.lname, u.email, u.img, f.friendship_id FROM users u "
                    + "INNER JOIN friends f "
                    + "ON (f.sender = u.unique_id OR f.receiver = u.unique_id) "
                    + "WHERE (f.sender = ? OR f.receiver = ?) AND u.unique_id != ?";

            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                // Set parameters in the query
                pstmt.setString(1, userId); // For f.sender = ?
                pstmt.setString(2, userId); // For f.receiver = ?
                pstmt.setString(3, userId); // To exclude the logged-in user itself

                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    Map<String, String> friend = new HashMap<>();
                    friend.put("unique_id", rs.getString("unique_id"));
                    friend.put("friend_id", rs.getString("friendship_id"));
                    friend.put("fname", rs.getString("fname"));
                    friend.put("lname", rs.getString("lname"));
                    friend.put("image", rs.getString("img")); // Fixed typo
                    friend.put("email", rs.getString("email"));
                    friends.add(friend);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        }

        // Convert the list to JSON using Gson
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(friends));
    }
}
