package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class AdminLoginServlet
 */
@WebServlet("/adminLogin")
public class AdminLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminLoginServlet() {
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

		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if(!emal.isBlank() && !password.isBlank(){
			String regex =
			Patteren pattern = Pattern.compile(regex);
			if(pattern.matcher(email).matches()){
				try{
					String passwordHash = AppSecurity.encrypt(password);
					Connection conn = new DatabaseConfig().getConnection();
					String sql = "SELECT from admin where password = ? and email = ?";
					PreparedStatement pstmt = conn.getPrepareStatement(sql);
					pstmt.setString(1,passwordHash);
					pstmt.setString(2,email);

					ResultSet rs = stmt.executeQuery();
				if(rs.next()){
					String sq = "UPDATE admin SET status = 'active' WHERE unique_id = ?";
					PreparedStatement ps = conn.getPrepareStatement(sq);
					ps.setString(1,rs.getString("unique_id"));
					int i = ps.executeUpdate();
				
				if(i>0){
					HttpSession session = request.getSession();
					session.setAttribute("adminId",rs.getString("unique_id)"));
					out.write("sucess");
				}
				
				}else{
					out.write("Invalid Email or Password");
				}
					
				}catch(Exception e){
					e.printStackTrace();
				}
				
			}else{
				out.write("Invalid Email");
			}
		}else{
			out.write("All field is required");
		}

		
		doGet(request, response);
	}

}
