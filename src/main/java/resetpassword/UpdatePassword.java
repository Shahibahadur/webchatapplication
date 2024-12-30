package resetpassword;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.AppSecurity;
import mypackage.DatabaseConfig;

@WebServlet("/UpdatePassword")
public class UpdatePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html"); // Ensure the response type is HTML


		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("email") == null) {
			response.sendRedirect("/jsp/forgetpassword.jsp");
			return;
		}
		
		String email = (String) session.getAttribute("email");
		
		String newPassword = (String)request.getParameter("newPassword");
		
		String conformPassword =(String)request.getParameter("confirmPassword");
		
		System.out.println(newPassword);
		System.out.println(conformPassword);

		if (newPassword == null || conformPassword == null || !newPassword.equals(conformPassword)) {
			request.setAttribute("error", "Passwords do not match.");
			// Use relative path here
			request.getRequestDispatcher("/jsp/setnewpassword.jsp").forward(request, response);
			return;
		}

		Boolean isUpdated = null;
		try {
			isUpdated = updatePassword(email.trim(), newPassword.trim());
		} catch (NoSuchAlgorithmException e) { //
			e.printStackTrace();
		}
		if (isUpdated) {
			session.invalidate();
			response.getWriter().write("password Updated Successfully <a href='/ChatAPP/jsp/login.jsp'>LOGIN </a> ");
		} else {
			request.setAttribute("error", "passwsord is not update. please try again");
			request.getRequestDispatcher("/jsp/setnewpassword.jsp").forward(request, response);
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	private Boolean updatePassword(String email, String password) throws NoSuchAlgorithmException {

		String newHashedPassword = AppSecurity.encript(password);
		try (Connection conn = new DatabaseConfig().getConnection();
				PreparedStatement pstmt = conn.prepareStatement("UPDATE users SET password = ? where email = ?");) {
			pstmt.setString(1, newHashedPassword);
			pstmt.setString(2, email);
			return pstmt.executeUpdate() > 0;

		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}

	}

}
