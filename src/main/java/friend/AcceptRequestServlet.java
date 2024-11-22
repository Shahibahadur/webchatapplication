package friend;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;


@WebServlet("/AcceptRequestServlet")
public class AcceptRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("request_id");
        DatabaseConfig db;

        try {
        	db = new DatabaseConfig();
        	
        	Connection conn = db.getConnection();
             PreparedStatement stmtUpdate = conn.prepareStatement("UPDATE friend_request SET status = 'accepted' where request_id = ?");
             PreparedStatement stmtInsert = conn.prepareStatement("INSERT INTO friends (sender, receiver, friendship_date) VALUES ((SELECT sender FROM friend_request WHERE request_id = ?), (SELECT receiver FROM friend_request WHERE request_id = ?), NOW());");
	         stmtInsert.setString(1, requestId);
	         stmtInsert.setString(2, requestId);

            stmtInsert.executeUpdate();
            stmtUpdate.setString(1, requestId);
            stmtUpdate.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("jsp/friend.jsp");
    }
}

