package group;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/GetGroupsServlet")
public class GetGroupsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        String admin_id = (String)session.getAttribute("unique_id");
        

        List<Map<String, String>> groups = new ArrayList<>();
        try (Connection connection = new DatabaseConfig().getConnection()) {
            String sql = "SELECT group_id, group_name, image FROM `groups` WHERE admin_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            	stmt.setString(1, admin_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    Map<String, String> group = new HashMap<>();
                    group.put("group_id", String.valueOf(rs.getInt("group_id")));
                    group.put("group_name", rs.getString("group_name"));
                    group.put("image", rs.getString("image"));
                    System.out.println(rs.getString("group_name")+"from get groupServlet");
                    groups.add(group);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Convert the groups list to JSON
        response.getWriter().write(new com.google.gson.Gson().toJson(groups));
    }
}
