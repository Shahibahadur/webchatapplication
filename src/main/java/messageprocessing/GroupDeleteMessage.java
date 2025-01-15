package messageprocessing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/deleteGroupMessage")
public class GroupDeleteMessage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        HttpSession session = req.getSession(false);
	        String unique_id = (String) session.getAttribute("unique_id");

	        resp.setContentType("application/json");
	        PrintWriter out = resp.getWriter();
	        Gson gson = new Gson();

	        try {
	            // Parse JSON from the request body
	            StringBuilder sb = new StringBuilder();
	            BufferedReader reader = req.getReader();
	            String line;
	            while ((line = reader.readLine()) != null) {
	                sb.append(line);
	            }
	            JsonObject json = gson.fromJson(sb.toString(), JsonObject.class);

	            // Extract date and text from JSON
	            String date = json.get("date").getAsString().trim();
	            String text = json.get("text").getAsString().trim();
	            System.out.println("Text to delete: " + text);

	            // Check if the text is an image file based on extension
	            boolean isImage = isImage(text);

	            // Database operation to update the message to null
	            Connection conn = new DatabaseConfig().getConnection();
	            
	            String sql;
	            if (isImage) {
	                sql = "UPDATE group_messages SET attachment_path = DEFAULT WHERE timestamp = ? AND attachment_path = ? AND sender_id = ?";
	            } else {
	                sql = "UPDATE group_messages SET message_text = 'you deleted message' WHERE timestamp = ? AND message_text = ? AND sender_id = ?";
	            }

	            PreparedStatement pstmt = conn.prepareStatement(sql);
	            pstmt.setString(1, date);
	            pstmt.setString(2, text);
	            pstmt.setString(3, unique_id);

	            int rowsAffected = pstmt.executeUpdate();
	            System.out.println(rowsAffected);
	            pstmt.close();
	            conn.close();

	            // Response
	            JsonObject response = new JsonObject();
	            if (rowsAffected > 0) {
	                response.addProperty("status", "success");
	                response.addProperty("message", "Message updated to null successfully.");
	            } else {
	                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                response.addProperty("status", "error");
	                response.addProperty("message", "No matching message found to update.");
	            }
	            out.write(gson.toJson(response));
	        } catch (Exception e) {
	            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            JsonObject response = new JsonObject();
	            response.addProperty("status", "error");
	            response.addProperty("message", "An error occurred while updating the message.");
	            out.write(gson.toJson(response));
	            e.printStackTrace();
	        }
	    }

	    // Helper method to check if the text represents an image based on file extension
	    private boolean isImage(String text) {
	        String[] imageExtensions = {".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff"};
	        for (String ext : imageExtensions) {
	            if (text.toLowerCase().endsWith(ext)) {
	                return true;
	            }
	        }
	        return false;
	    }
	}
