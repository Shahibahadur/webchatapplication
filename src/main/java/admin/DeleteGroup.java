package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class DeleteGroup
 */
@WebServlet("/DeleteGroup")
public class DeleteGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteGroup() {
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
	tey{
		int groupId = Integer.parseInt(request.getParameter("groupId"));

		boolean success = delete(groupId);
		if(success){
			response.setStatus(HttpServleteResponse.SC_OK);
		}else{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
	}catch(Exception e){
		e.printStackTrace();
		response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
		//doGet(request, response);
	}
	}

	private boolean delete(int groupId){

		try{
			Connection conn = new DatabaeConfig().getConnection();

			String sql = "DELETE FROM `groups` WHERE group_id = ?";
			PreparedStatement stmt= conn.prepareStatement(sql);
			stmt.setInt(1, groupId);
			int result  = stmt.executeUpdate();
			return  result > 0;
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}

}
