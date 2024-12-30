package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

/**
 * Servlet implementation class DeleteUser
 */
@WebServlet("/DeleteUser") // Ensure the URL mapping matches your application setup
public class DeleteUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUser() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uniqueId = request.getParameter("userId");

        // Validate the uniqueId parameter
        if (uniqueId == null || uniqueId.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
           // SC_BAD_REQUEST (400):
            //response.getWriter().write("{\"error\": \"Invalid or missing uniqueId parameter\"}");
            return;
        }

        try (Connection conn = new DatabaseConfig().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE unique_id = ?")) {

            pstmt.setString(1, uniqueId); // Set the uniqueId parameter
            System.out.println(uniqueId);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("{\"success\": \"User deleted successfully\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                //SC_NOT_FOUND (404):
                response.getWriter().write("{\"error\": \"User not found\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            //SC_INTERNAL_SERVER_ERROR (500):
            response.getWriter().write("{\"error\": \"An error occurred while deleting the user\"}");
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
