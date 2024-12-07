package group;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/ManageGroupRequestsServlet")
public class ManageGroupRequestsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminId = (String) session.getAttribute("unique_id");
        int requestId = Integer.parseInt(request.getParameter("request_id"));
        String action = request.getParameter("action"); // "approve" or "reject"

        Map<String, String> status = new HashMap<>();

        try (Connection conn = new DatabaseConfig().getConnection()) {
            if ("approve".equals(action)) {
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Group_Join_Requests SET request_status='approved' WHERE request_id=?"
                );
                stmt.setInt(1, requestId);
                stmt.executeUpdate();

                PreparedStatement addMemberStmt = conn.prepareStatement(
                    "INSERT INTO Group_Members (group_id, user_id, status) " +
                    "SELECT group_id, user_id, 'Approved' FROM Group_Join_Requests WHERE request_id=?"
                );
                addMemberStmt.setInt(1, requestId);
                addMemberStmt.executeUpdate();
                status.put("status", "approved");
            } else if ("reject".equals(action)) {
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Group_Join_Requests SET request_status='rejected' WHERE request_id=?"
                );
                stmt.setInt(1, requestId);
                stmt.executeUpdate();
                status.put("status", "rejected");
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(new Gson().toJson(status));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-error.jsp");
        }
    }
}
