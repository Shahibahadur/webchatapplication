package admin;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/ShowUsers")
public class ManagingUsers extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // DTO Class
    static class Users {
        private String userName;
        private String uniqueId;

        public Users(String userName, String uniqueId) {
            this.userName = userName;
            this.uniqueId = uniqueId;
        }

        public String getUserName() {
            return userName;
        }

        public String getUniqueId() {
            return uniqueId;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("ManagingUsers");
        List<Users> usersDetails = new ArrayList<>();
        try (Connection conn = new DatabaseConfig().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT unique_id, fname, lname FROM users");
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String userName = rs.getString("fname") + " " + rs.getString("lname");
                usersDetails.add(new Users(userName, rs.getString("unique_id")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch user details.");
            return;
        }

        response.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(new Gson().toJson(usersDetails));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
