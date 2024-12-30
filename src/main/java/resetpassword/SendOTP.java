package resetpassword;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import mypackage.DatabaseConfig;

@WebServlet("/SendOTP")
public class SendOTP extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userEmail = request.getParameter("email");
        
        

        // Validate email format
        
        
        try {
            InternetAddress emailAddr = new InternetAddress(userEmail);
            emailAddr.validate();
        } catch (AddressException ex) {
            response.getWriter().write("Invalid email address format.");
            request.setAttribute("error", "Invalid email address format");
            request.getRequestDispatcher("/ChatAPP/jsp/forgetpassword.jsp").forward(request,response);        
            return;
        }
        
        try (Connection conn = new DatabaseConfig().getConnection(); 
        	     PreparedStatement pstmt = conn.prepareStatement("SELECT email FROM users WHERE email = ?")) {
        	    
        	    // Set the email parameter
        	    String email = request.getParameter("email");
        	    pstmt.setString(1, email);
        	    
        	    // Execute the query
        	    try (ResultSet rs = pstmt.executeQuery()) {
        	        if (!rs.next()) { // Check if the result set is empty
        	            request.setAttribute("error", "Email not found!");
        	            request.getRequestDispatcher("/jsp/forgetpassword.jsp").forward(request, response);
        	            return;
        	        }
        	    }
        	} catch (Exception e) {
        	    e.printStackTrace();
        	    request.setAttribute("error", "An error occurred while processing your request.");
        	    request.getRequestDispatcher("/jsp/forgetpassword.jsp").forward(request, response);
        	}


        // Generate OTP
        int otp = (int) (Math.random() * 900000) + 100000;

        // Save OTP and timestamp in session
        HttpSession session = request.getSession();
        session.setAttribute("otp", otp);
        session.setAttribute("email", userEmail);
        session.setAttribute("otpGeneratedTime", System.currentTimeMillis()); // Store OTP generation time
        session.setMaxInactiveInterval(300); // Set session timeout to 5 minutes

        // Send OTP via Email
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");

            String senderEmail = "shahiharinna3@gmail.com";
            String senderPassword = "rkzs pxmo cqgw mtla";

            // Validate environment variables
            if (senderEmail == null || senderPassword == null) {
                response.getWriter().write("Sender email or password not configured.");
                return;
            }

            Session mailSession = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            // Enable debug mode for troubleshooting
            mailSession.setDebug(true);

            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
            message.setSubject("Password Reset OTP");
            message.setText("Your OTP is: " + otp);

            Transport.send(message);
            session.setAttribute("otpGenerated", true);
            response.getWriter().write("OTP sent to your email. It will expire in 5 minutes.");
            response.sendRedirect("/ChatAPP/jsp/forgetpassword.jsp");
            
        } catch (MessagingException e) {
            e.printStackTrace(); // Log the error
            response.getWriter().write("Failed to send OTP: " + e.getMessage());
            request.setAttribute("error", "Failed to Send OTP");
            request.getRequestDispatcher("/ChatAPP/jsp/forgetpassword.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
