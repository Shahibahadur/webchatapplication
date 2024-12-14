package admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.regex.Pattern;

import mypackage.DatabaseConfig;
import mypackage.AppSecurity;

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
		if(!email.isBlank() && !password.isBlank()){
			String regex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
			Pattern pattern = Pattern.compile(regex);
			if(pattern.matcher(email).matches()){
				try{
					String passwordHash = AppSecurity.encript(password);;
					Connection conn = new DatabaseConfig().getConnection();
					String sql = "SELECT from admin where password = ? and email = ?";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1,passwordHash);
					pstmt.setString(2,email);

					ResultSet rs = pstmt.executeQuery();
				if(rs.next()){
					String sq = "UPDATE admin SET status = 'active' WHERE unique_id = ?";
					PreparedStatement ps = conn.prepareStatement(sq);
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

		
	}

}
