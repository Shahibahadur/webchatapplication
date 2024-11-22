package friend;

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

public class FriendSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		String searchQuery = request.getParameter("searchQuery");
		List<Map<String, String>> searchResults = new ArrayList<>();

		DatabaseConfig db;

		try {

			db = new DatabaseConfig();
			Connection conn = db.getConnection();

			
			/*
			 * PreparedStatement stmt = conn.prepareStatement(
			 * "SELECT * FROM users WHERE (fname LIKE ? OR lname LIKE ?) AND NOT unique_id='"
			 * +session.getAttribute("unique_id")+"';"); stmt.setString(1, "%" + searchQuery
			 * + "%"); stmt.setString(2, "%" + searchQuery + "%");
			 */
			
			
			PreparedStatement stmt = conn.prepareStatement(
				    "SELECT u.* " +
				    "FROM users u " +
				    "WHERE u.unique_id != ? " + // Exclude the logged-in user
				    "AND NOT EXISTS (" + // Ensure the logged-in user hasn't sent a request to this user
				    "    SELECT 1 FROM friend_request fr1 " +
				    "    WHERE fr1.sender = ? AND fr1.receiver = u.unique_id " +
				    ") " +
				    "AND NOT EXISTS (" + // Ensure the logged-in user hasn't received a request from this user
				    "    SELECT 1 FROM friend_request fr2 " +
				    "    WHERE fr2.receiver = ? AND fr2.sender = u.unique_id " +
				    ") " +
				    "AND (u.fname LIKE ? OR u.lname LIKE ?);"
				);
			
			// note here u.unique_id is unique id of other not of current logged in user;
			// Assuming the session contains the logged-in user's unique_id
			String loggedInUserId = (String) session.getAttribute("unique_id");

			// Set the parameters for the prepared statement
			stmt.setString(1, loggedInUserId); // Exclude the logged-in user
			stmt.setString(2, loggedInUserId); // Check if the logged-in user has sent a request to this user
			stmt.setString(3, loggedInUserId); // Check if the logged-in user has received a request from this user
			stmt.setString(4, "%" + searchQuery + "%"); // First name search
			stmt.setString(5, "%" + searchQuery + "%"); // Last name search



			  
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
			    Map<String, String> user = new HashMap<>();
			    user.put("fname", rs.getString("fname"));
			    user.put("lname", rs.getString("lname"));
			    user.put("image", rs.getString("img"));
			    user.put("email", rs.getString("email"));
			    user.put("unique_id", rs.getString("unique_id"));
			    searchResults.add(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		request.setAttribute("searchResults", searchResults);
		request.getRequestDispatcher("jsp/friend.jsp").forward(request, response);

	}
}
