package  group;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/GetMessage")
public class GetMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String groupId = request.getParameter("groupId"); // Correctly fetch parameter from the request

        System.out.println(groupId + " from GetMessage");

        List<Map<String, String>> detail = new ArrayList<>();

        try (Connection connection = new DatabaseConfig().getConnection()) {
            String sql = "SELECT m.message_text, m.attachment_path, m.timestamp, u.fname, u.lname "
                       + "FROM group_messages m "
                       + "JOIN users u ON m.sender_id = u.unique_id "
                       + "WHERE m.group_id = ? ORDER BY m.timestamp ASC";

            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupId));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, String> information = new HashMap<>();
                information.put("senderName", rs.getString("fname") + " " + rs.getString("lname"));
                information.put("messageText", rs.getString("message_text"));
                information.put("timestamp", rs.getString("timestamp"));
                information.put("attachmentPath", rs.getString("attachment_path"));
                detail.add(information);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error: " + e.getMessage());
            return;
        }

        response.setContentType("application/json; charset=UTF-8"); // Ensure UTF-8 encoding
        PrintWriter out = response.getWriter();
        out.write(new Gson().toJson(detail));
    }
}
