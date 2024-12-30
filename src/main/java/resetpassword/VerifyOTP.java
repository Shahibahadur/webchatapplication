package resetpassword;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/VerifyOTP")
public class VerifyOTP extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	 @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        HttpSession session = request.getSession(false);
	        if (session == null) {
	            request.setAttribute("error", "Session expired. Please restart the process.");
	            request.getRequestDispatcher("/ChatAPP/jsp/forgetpassword.jsp").forward(request, response);
	            return;
	        }

	        // Retrieve OTP and generation time
	        Integer generatedOtp = (Integer) session.getAttribute("otp");
	        Long otpGeneratedTime = (Long) session.getAttribute("otpGeneratedTime");

	        if (generatedOtp == null || otpGeneratedTime == null) {
	            request.setAttribute("error", "No OTP found. Please request a new one.");
	            request.getRequestDispatcher("/ChatAPP/jsp/forgetpassword.jsp").forward(request, response);
	            return;
	        }

	        // Validate OTP expiry (5 minutes = 300,000 milliseconds)
	        long currentTime = System.currentTimeMillis();
	        if ((currentTime - otpGeneratedTime) > 300000) {
	            session.invalidate(); // Clear session on expiry
	            request.setAttribute("error", "OTP expired. Please request a new one.");
	            request.getRequestDispatcher("forgetpassword.jsp").forward(request, response);
	            return;
	        }

	        // Validate the entered OTP
	        try {
	            int enteredOtp = Integer.parseInt(request.getParameter("otp"));
	            if (generatedOtp == enteredOtp) {
	                //session.invalidate(); // Invalidate session after successful validation
	                response.getWriter().write("OTP verified successfully. Proceed to reset your password.");
	                response.sendRedirect("/ChatAPP/jsp/setnewpassword.jsp");
	                
	            } else {
	                request.setAttribute("error", "Invalid OTP. Please try again.");
	                request.getRequestDispatcher("/ChatAPP/jsp/forgetpassword.jsp").forward(request, response);
	            }
	        } catch (NumberFormatException e) {
	            request.setAttribute("error", "Invalid OTP format. Please enter numbers only.");
	            request.getRequestDispatcher("ChatAPP/jsp/forgetpassword.jsp").forward(request, response);
	        }
	    }
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
