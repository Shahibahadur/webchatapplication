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

@WebServlet("/RejectRequestServlet")
public class RejectRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestId = request.getParameter("request_id");
        DatabaseConfig db;

        try {
        	db = new DatabaseConfig();
        	Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE friend_request SET status = 'reject' where request_id = ? ");
            stmt.setString(1, requestId);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("jsp/friend.jsp");
    }
}

