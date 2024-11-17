package mypackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Time;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;


/*
public class InsertChatServlet extends HttpServlet {

	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		
		 
		HttpSession session = req.getSession();
		
		if(session.getAttribute("unique_id") != null) {
			
			try {
				Connection conn = new DatabaseConfig().getConnection();
				
				String incoming_id = MySQLUtils.mysql_real_escape_string(conn, req.getParameter("incoming_id"));
				String outgoing_id = MySQLUtils.mysql_real_escape_string(conn, req.getParameter("outgoing_id"));
				String message = MySQLUtils.mysql_real_escape_string(conn, req.getParameter("message"));
				
				message = message.replaceAll("__5oO84a9__", " ");
				
				
				
				if(!message.isBlank()) {
				
					String query2 = "INSERT INTO  `messages`  (incoming_msg_id, outgoing_msg_id, msg) VALUES (?,?,?)";
					
					PreparedStatement pstmt = conn.prepareStatement(query2);
					pstmt.setString(1, incoming_id);
					pstmt.setString(2, outgoing_id);
					pstmt.setString(3, message);
					
					pstmt.executeUpdate();
				
					
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}else {
			
			req.getRequestDispatcher("user-login").forward(req, resp);
		}
		
	}
	
}
*/
@WebServlet(name = "InsertChatServlet", urlPatterns = { "/insertChat" })
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
    maxFileSize = 1024 * 1024 * 10,      // 10 MB
    maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class InsertChatServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        if (session.getAttribute("unique_id") != null) {
            try {
                Connection conn = new DatabaseConfig().getConnection();
                
                // Retrieving form fields
                String incoming_id = MySQLUtils.mysql_real_escape_string(conn, req.getParameter("incoming_id"));
                String outgoing_id = MySQLUtils.mysql_real_escape_string(conn, req.getParameter("outgoing_id"));
                String message = MySQLUtils.mysql_real_escape_string(conn, req.getParameter("message"));
                message = message.replaceAll("__5oO84a9__", " "); // Decode spaces in message

                String filePath = null;
                String typeOfImage = null;
                String fileName = null;
                InputStream imageInputStream = null;

                // Processing uploaded image
                Part filePart = req.getPart("image"); // "image" is the name of the file input field in the form
                if (filePart != null && filePart.getSize() > 0) {
                    fileName = extractFileName(filePart);
                    imageInputStream = filePart.getInputStream();
                    typeOfImage = fileName.substring(fileName.lastIndexOf(".") + 1);
                    
                    String uploadPath = getServletContext().getRealPath("/images");
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) uploadDir.mkdir(); // Create directory if not exists
                    
                    filePath = uploadPath + File.separator + fileName;
                }

                // Ensure that message is empty if no message is provided, but we have an image
                if (message == null || message.trim().isEmpty()) {
                    message = "";
                }

                // Insert message and image
                if (!message.isEmpty() || filePart != null) {
                    if (filePart != null && containsExtension(typeOfImage)) {
                        // Save image file
                        Time timeObj = new Time(System.currentTimeMillis());
                        long time = timeObj.getTime();
                        String new_image_name = time + fileName;

                        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                        File uploadDirectory = new File(uploadPath);
                        if (!uploadDirectory.exists()) {
                            uploadDirectory.mkdir();
                        }

                        Path destinationPath = Paths.get(uploadDirectory.getAbsolutePath());
                        try (FileOutputStream fout = new FileOutputStream(destinationPath.resolve(new_image_name).toString())) {
                            fout.write(imageInputStream.readAllBytes());

                            String query = "INSERT INTO messages (incoming_msg_id, outgoing_msg_id, msg, imagePath) VALUES(?, ?, ?, ?)";
                            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                                pstmt.setString(1, incoming_id);
                                pstmt.setString(2, outgoing_id);
                                pstmt.setString(3, message); // Insert empty string if no message
                                pstmt.setString(4, new_image_name);

                                pstmt.executeUpdate();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            req.getRequestDispatcher("user-login").forward(req, resp);
        }
    }

    private String extractFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String token : contentDisposition.split(";")) {
            if (token.trim().startsWith("filename")) {
                String fileName = token.substring(token.indexOf('=') + 1).trim().replaceAll("\"", "");
                return fileName;
            }
        }
        return "unknown.jpg";
    }

    private boolean containsExtension(String uploadedFileExtension) {
        String[] extensions = {"png", "jpg", "jpeg"};
        for (String value : extensions) {
            if (value.equalsIgnoreCase(uploadedFileExtension)) {
                return true;
            }
        }
        return false;
    }
}
