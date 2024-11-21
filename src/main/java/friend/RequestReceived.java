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

@WebServlet("/friend-requests-received") // URL mapping
public class RequestReceived extends HttpServlet {
	private static final long serialVersionUID = 1L;

    // Handles displaying the received friend requests
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Get the logged-in user's unique ID
        String loggedInUserId = (String) session.getAttribute("unique_id");
        List<Map<String, String>> receivedRequests = new ArrayList<>();

        DatabaseConfig db;

        try {
            db = new DatabaseConfig();
            Connection conn = db.getConnection();

            // SQL query to fetch friend requests where the logged-in user is the receiver
            PreparedStatement stmt = conn.prepareStatement(
            	    "SELECT * FROM friend_request fr " +
            	    "INNER JOIN users u ON fr.sender = u.unique_id " +
            	    "WHERE fr.receiver = ? AND fr.status = 'pending';"
            	);

            stmt.setString(1, loggedInUserId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> requestInfo = new HashMap<>();
                requestInfo.put("fname", rs.getString("fname"));
                requestInfo.put("lname", rs.getString("lname"));
                requestInfo.put("sender_image", rs.getString("img"));
                requestInfo.put("request_id", rs.getString("request_id"));
                requestInfo.put("email", rs.getString("email"));
                requestInfo.put("status", rs.getString("status"));
                receivedRequests.add(requestInfo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Pass the received requests to the JSP for displaying
        request.setAttribute("requestsReceived", receivedRequests);
        request.getRequestDispatcher("jsp/friend.jsp").forward(request, response);
    }
}