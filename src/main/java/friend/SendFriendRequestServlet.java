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


@WebServlet("/SendFriendRequestServlet")
public class SendFriendRequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String senderId = (String) request.getSession().getAttribute("unique_id");
        String receiverId = request.getParameter("receiver_id");
        
        DatabaseConfig db;
        try {
        	Connection conn = new DatabaseConfig().getConnection();

             PreparedStatement stmt = conn.prepareStatement("INSERT INTO friend_request (sender, receiver,request_date) VALUES (?, ?, NOW())");
            stmt.setString(1, senderId);
            stmt.setString(2, receiverId);

            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("jsp/friend.jsp");
    }
}

