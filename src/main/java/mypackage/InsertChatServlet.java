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


@WebServlet(name = "InsertChatServlet", urlPatterns = {"/insertChat"})
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
                message = message != null ? message.replaceAll("__5oO84a9__", " ") : ""; // Decode spaces

                Part filePart = req.getPart("image"); // "image" is the name of the file input field
                String fileName = null;
                String typeOfImage = null;
                String new_image_name = null;

                if (filePart != null && filePart.getSize() > 0) {
                    fileName = extractFileName(filePart);
                    typeOfImage = fileName.substring(fileName.lastIndexOf(".") + 1);

                    if (containsExtension(typeOfImage)) {
                        Time timeObj = new Time(System.currentTimeMillis());
                        long time = timeObj.getTime();
                        new_image_name = time + fileName;

                        String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
                        
                        System.out.println(uploadPath+"from insertChat");
                        File uploadDirectory = new File(uploadPath);
                        if (!uploadDirectory.exists()) {
                            uploadDirectory.mkdir();
                        }

                        Path destinationPath = Paths.get(uploadDirectory.getAbsolutePath());
                        try (FileOutputStream fout = new FileOutputStream(destinationPath.resolve(new_image_name).toString());
                             InputStream imageInputStream = filePart.getInputStream()) {
                        	
                            fout.write(imageInputStream.readAllBytes());
                            System.out.println(imageInputStream.readAllBytes()+"from insertChat fileinputStream");
                        }
                    }
                }

                // Insert message and/or image
                if (!message.isEmpty() || filePart != null) {
                    String query = "INSERT INTO messages (incoming_msg_id, outgoing_msg_id, msg, imagePath) VALUES(?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, incoming_id);
                        pstmt.setString(2, outgoing_id);

                        // Handle message
                        if (message.isBlank()) {
                            pstmt.setNull(3, java.sql.Types.VARCHAR);
                        } else {
                            pstmt.setString(3, message);
                        }

                        // Handle image path
                        if (new_image_name != null) {
                            pstmt.setString(4, new_image_name); // Image path
                        } else {
                            pstmt.setNull(4, java.sql.Types.VARCHAR); // Null if no image
                        }

                        pstmt.executeUpdate();
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
                return token.substring(token.indexOf('=') + 1).trim().replaceAll("\"", "");
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
