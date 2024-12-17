package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class DeleteUsers
 */
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUser() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String uniqueId = (String)request.getParameter("uniqueId");
		if(!uniqueId || uniqueId.isBlank()){
			response.setStatus(HttpServletResponse_SC_BAD_REQUEST);
		}
		try(Connection conn = new DatabaseConfig().getConnection();
		PreparedStatement pstmt = conn.prepareStarement("DELETE FROM users WHERE unique_id = 'uniqueId'");){
			int i = pstmt.executeUpdate();
			if(i>0){
				response.setStatus(HttpServletResponse.SC_OK);
			}else{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
		}catch(Exception e){
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
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
