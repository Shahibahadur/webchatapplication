package friend;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

@WebServlet(name = "RequestReceived", urlPatterns = "/friend-requests-received")
public class RequestReceived extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Get the logged-in user's unique ID from the session
        String loggedInUserId = (String) session.getAttribute("unique_id");
        System.out.println("Logged-in user ID: " + loggedInUserId + " from RequestReceived");

        // Prepare a list to store the friend requests
        List<Map<String, String>> receivedRequests = new ArrayList<>();

        if (loggedInUserId == null || loggedInUserId.trim().isEmpty()) {
            response.getWriter().write("[]");
            return;
        }

        try {
            // Initialize database connection
            DatabaseConfig db = new DatabaseConfig();
            Connection conn = db.getConnection();

            // SQL query with explicit column selection and aliases
            String query = 
                "SELECT " +
                "    fr.request_id, " +
                "    fr.status AS request_status, " +
                "    u.fname, " +
                "    u.lname, " +
                "    u.img AS sender_image, " +
                "    u.email " +
                "FROM " +
                "    friend_request fr " +
                "INNER JOIN " +
                "    users u " +
                "ON " +
                "    fr.sender = u.unique_id " +
                "WHERE " +
                "    fr.receiver = ? AND fr.status = 'pending'";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, loggedInUserId);

            // Execute the query and process the results
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> requestInfo = new HashMap<>();
                requestInfo.put("fname", rs.getString("fname"));
                requestInfo.put("lname", rs.getString("lname"));
                requestInfo.put("sender_image", 
                                rs.getString("sender_image") != null ? rs.getString("sender_image") : "default_image_path_or_placeholder");
                requestInfo.put("request_id", rs.getString("request_id"));
                requestInfo.put("email", rs.getString("email"));
                requestInfo.put("status", rs.getString("request_status"));
                receivedRequests.add(requestInfo);

                // Log each fetched row
                System.out.println("Fetched Row: " + requestInfo);
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert the list of received requests to JSON and send it as the response
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(receivedRequests));
    }
}
