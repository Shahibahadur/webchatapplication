package admin;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/showLogInAdmin")
public class ShowLogInAdmin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        String admin_id = (String)session.getAttribute("admin_id");

        // Sample logged-in admin details
        String adminName = "Admin "; // You would typically fetch this from a session or database
        String adminRole = "Administrator"; // You would typically fetch this from a session or database

        // Simulate fetching logged-in admin's details (Replace with real logic)
        // For now, we'll send a hardcoded admin object
        try (Connection connection = new DatabaseConfig().getConnection()) {
            // You can modify the query if you have a logged-in admin in the database
            String query = "SELECT admin_name, role FROM admin WHERE unique_id = ?";  // Example query
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, admin_id);  
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                adminName = resultSet.getString("admin_name");
                adminRole = resultSet.getString("role");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Failed to fetch data from the database.\"}");
            out.flush();
            return;
        }

        // Create JSON response
        String jsonResponse = String.format("{\"name\":\"%s\", \"role\":\"%s\"}", adminName, adminRole);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
