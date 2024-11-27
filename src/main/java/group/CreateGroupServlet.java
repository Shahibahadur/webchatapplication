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


@WebServlet("/CreateGroupServlet")
public class CreateGroupServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminId = (String) session.getAttribute("unique_id");
        String groupName = request.getParameter("groupName");

        try (Connection conn = new DatabaseConfig().getConnection()) {
        	
        	String sql = "SELECT fname , lname FROM unique_id = ?";
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `groups` (group_name, admin_id) VALUES (?, ?)");
            stmt.setString(1, groupName);
            stmt.setString(2, adminId);
            stmt.executeUpdate();
            response.sendRedirect("jsp/group.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("group-error.jsp");
        }
    }
}
