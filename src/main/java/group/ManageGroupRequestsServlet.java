package group;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import mypackage.DatabaseConfig;

@WebServlet("/ManageGroupRequestsServlet")
public class ManageGroupRequestsServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String adminId = (String) session.getAttribute("unique_id");
        int requestId = Integer.parseInt(request.getParameter("request_id"));
        String action = request.getParameter("action"); // "approve" or "reject"

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
            } else if ("reject".equals(action)) {
                PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE Group_Join_Requests SET request_status='Rejected' WHERE request_id=?"
                );
                stmt.setInt(1, requestId);
                stmt.executeUpdate();
            }
           
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("manage-error.jsp");
        }
    }
}
