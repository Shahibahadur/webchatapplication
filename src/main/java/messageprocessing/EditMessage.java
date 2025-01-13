package messageprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/editMessage")
public class EditMessage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String unique_id = (String) session.getAttribute("unique_id");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        // Read JSON payload from request
        StringBuilder jsonPayload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                jsonPayload.append(line);
            }
        }

        try {
            // Parse JSON payload
            JsonObject jsonObject = JsonParser.parseString(jsonPayload.toString()).getAsJsonObject();
            String timestamp = jsonObject.get("timestamp").getAsString();
            String newMessage = jsonObject.get("newMessage").getAsString();

            if (timestamp == null || newMessage == null || newMessage.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("{\"status\":\"error\",\"message\":\"Invalid input data.\"}");
                return;
            }

            // Database update logic
            try (Connection conn = new DatabaseConfig().getConnection()) {
                String updateQuery = "UPDATE messages SET msg = ? WHERE time = ? AND outgoing_msg_id = ?";
                try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                    stmt.setString(1, newMessage);
                    stmt.setString(2, timestamp);
                    stmt.setString(3, unique_id);

                    int rowsUpdated = stmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.write("{\"status\":\"success\",\"message\":\"Message updated successfully.\"}");
                    } else {
                        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        out.write("{\"status\":\"error\",\"message\":\"Message not found.\"}");
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.write("{\"status\":\"error\",\"message\":\"An error occurred: " + e.getMessage() + "\"}");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("{\"status\":\"error\",\"message\":\"Invalid JSON payload.\"}");
        }
    }
}
