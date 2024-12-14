package admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mypackage.DatabaseConfig;

/**
 * Servlet implementation class ManagingGroups
 */
public class ManagingGroups extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagingGroups() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		Map<Integer,String> groups = new HashMap<>();
		try{
			Connection conn = new DatabaseConfig().getConnection();

			String sql = "SELECT group_id, groupName FROM `groups`";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				groups.put(rs.getInt("group_id"),rs.getString("group_name"));
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
			
		}
		response.setContentType("application/json; charset = UTF-8");
		PrintWriter out = response.getWriter();
		out.write(new Gson().toJson(groups));
	}

}
