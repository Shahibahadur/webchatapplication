package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/showAdmin")
public class ShowAdmins extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonArray adminsArray = new JsonArray();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            String query = "SELECT unique_id, admin_name, status FROM admin"; // Replace with your table and column names
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                JsonObject adminObject = new JsonObject();
                adminObject.addProperty("id", resultSet.getString("unique_id"));
                adminObject.addProperty("name", resultSet.getString("admin_name"));
                adminObject.addProperty("status", resultSet.getString("status"));
                adminsArray.add(adminObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            PrintWriter out = response.getWriter();
            out.print("{\"error\":\"Failed to fetch data from the database.\"}");
            out.flush();
            return;
        }

        PrintWriter out = response.getWriter();
        out.print(adminsArray.toString());
        out.flush();
    }
}
