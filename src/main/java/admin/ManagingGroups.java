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

/**
 * Servlet implementation class ManagingGroups
 */
@WebServlet("/ShowGroups")
public class ManagingGroups extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Inner class to represent Group entity
    class Group {
        private int groupId;
        private String groupName;

        public Group(int groupId, String groupName) {
            this.groupId = groupId;
            this.groupName = groupName;
        }

        public int getGroupId() {
            return groupId;
        }

        public String getGroupName() {
            return groupName;
        }
    }

    public ManagingGroups() {
        super();
    }

    /**
     * Handle GET requests to retrieve group details.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Group> groupDetail = new ArrayList<>();
        
        try (Connection conn = new DatabaseConfig().getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT group_id, group_name FROM `groups`");
             ResultSet rs = pstmt.executeQuery()) {
            	while (rs.next()) {
                groupDetail.add(new Group(rs.getInt("group_id"), rs.getString("group_name")));
                
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("application/json; charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                out.write("{\"error\": \"Unable to fetch group details. Please try again later.\"}");
            }
            return;
        }

        // Return the group details as JSON
        response.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.write(new Gson().toJson(groupDetail)); // Serialize group list to JSON
        }
    }

    /**
     * Redirect POST requests to GET for consistency.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
