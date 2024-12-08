package group;
import java.sql.Connection;
import java.sql.PreparedStatement;


import java.util.Map;
import java.util.Hashmap;
import com.google.
import jakarta.servlet.http.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

@WebServlet("/GetGroup")
public class GetSpecificGroup extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest request , HttpServletResponse response) {
		int groupId = Integer.parseInt(request.getAttribute("group_id").toString());
		Connection conn= null;
		Map<String,String> groupDetails = new HashMap<>();
		try {
			conn = new DatabaseConfig().getConnection();
			String sq1 = "SELECT group_name,image FROM `groups` where group_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInteger(1,group_id);
			ResutSet rs = stmt.executeQuery();
			while(rs.next()){
				
				groupDetails.put("groupName",rs.getString("group_name"));
				groupDetails.put("groupImage", rs.getString("image"));	
			}
			response.setContentType("application/json");
			PrintWriter out = request.getWriter();
			out.write(new Gson().toJson(groupDetails))
				
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}

}
