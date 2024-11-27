package group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/JoinGroupServlet")
public class JoinGroupServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("unique_id");
        int groupId = Integer.parseInt(request.getParameter("group_id"));

        try (Connection conn = new DatabaseConfig().getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Group_Join_Requests (group_id, user_id) VALUES (?, ?)");
            stmt.setInt(1, groupId);
            stmt.setString(2, userId);
            stmt.executeUpdate();
            response.sendRedirect("jsp/group.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("join-request-error.jsp");
        }
    }
}
