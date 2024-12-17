package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ManagingUsers
 */

@WebServlet("/ShowUsers")
public class ManagingUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	class users{
		private String userName;
		private String uniqueId;
		public users(String userName , String uniqueId){
			this.userName = userName;
			this.uniqueId = uniqueId;
		}
		public String getUsername(){
			return userName;
		}
		public String getUniqueId(){
			return uniqueId;
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManagingUsers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Group> usersDetails  = new ArrayList<>();
		try(Connection conn = new DatabaseConfig().getConnection(); 
		    PreparedStatement pstmt = conn.prepareStatemet("SELECT unique_id , fname, lname FROM users");
		    ResultSet rs = pstmt.executeQuery();
		   ){
			while(rs.next()){
				String userName = rs.getString("fname")+" "+rs.getString("lname");
				userDetails.add(new users(userName, rs.getString("unique_id"));
			}
		}
		catch(Exception e){
			e.printStackTrace();
			response.setContentType("application/json; charset=UTF-8");
			respsone.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			try(PrintWriter out = response.getWeiter()){
				out.write("{\"error\": \"Unable to fetch users details. Please try again later.\"}");
			}
			return;
		}
		response.setContentType("application/json; charset=UTF-8");
		try(PrintWriter out = response.getWriter()){
			out.write(new Gson().toJson(userDetails));
		}
		

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
