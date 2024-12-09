package group;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/GetGroup")
public class GetSpecificGroup extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request , HttpServletResponse response) {
		int groupId = Integer.parseInt(request.getParameter("groupId").toString());
		System.out.println(groupId+" from GetSpecificGroup");

		Connection conn= null;
		Map<String,String> groupDetails = new HashMap<>();
		try {
			conn = new DatabaseConfig().getConnection();
			String sql = "SELECT group_name,image FROM `groups` where group_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1,groupId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()){
				
				groupDetails.put("groupName",rs.getString("group_name"));
				groupDetails.put("groupImage", rs.getString("image"));	
			}
			response.setContentType("application/json; charset = UTF-8 ");
			PrintWriter out = response.getWriter();
			out.write(new Gson().toJson(groupDetails));
				
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
