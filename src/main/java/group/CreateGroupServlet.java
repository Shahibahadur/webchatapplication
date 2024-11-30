package group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;


@WebServlet("/CreateGroupServlet")
public class CreateGroupServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminId = (String) session.getAttribute("unique_id");
        String groupName = request.getParameter("groupName");
        String firstName=null;
        String lastName=null;

        try (Connection conn = new DatabaseConfig().getConnection()) {
        	
        	String sql = "SELECT fname, lname FROM users WHERE unique_id = ?";
        	PreparedStatement ps = conn.prepareStatement(sql);
        	ps.setString(1, adminId);
        	ResultSet rs = ps.executeQuery();
        	
        	while(rs.next()) {
        		firstName = rs.getString("fname");
        		lastName = rs.getString("lname");
        		
        	}
        	
        	
        	
        	
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO `groups` (group_name, admin_id, admin_fname, admin_lname) VALUES (?, ?, ?, ?)");
   
            stmt.setString(1, groupName);
            stmt.setString(2, adminId);
            stmt.setString(3,firstName);
            stmt.setString(3, lastName);
            stmt.executeUpdate();
            
            response.sendRedirect("jsp/group.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("group-error.jsp");
        }
    }
}
