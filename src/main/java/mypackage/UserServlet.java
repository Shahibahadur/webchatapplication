package mypackage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name="UserServlet", urlPatterns="/users") //from user.js //sendGetRequest();

/*public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		HttpSession session = req.getSession();
		String output = "";
		DatabaseConfig db ;
		try {
		   db = new DatabaseConfig();
		   Connection con = db.getConnection();
		   String sql = "SELECT * FROM users WHERE NOT unique_id='"+session.getAttribute("unique_id") + "'";

		   ResultSet set = con.prepareStatement(sql).executeQuery();
 
		   ResultSet countSet = con.prepareStatement(sql).executeQuery();
		   int row_count = 0;
		   while(countSet.next()) {
			   row_count += 1;
		   }
		
		   if(row_count==0) {
			   output += "No users are available to chat";
		   }
		   else if(row_count>0){
				String sender_id = req.getSession().getAttribute("unique_id")+"";
				//user unique_id 

			   while(set.next()) {
				   
				   String reciever_id = set.getString("unique_id");
				   //unique_id of another user
				   
					ResultSet latestMsgSet = getMessagesResultSet(reciever_id, sender_id, con);
					int numRow = numOfRows(reciever_id, sender_id, con);
					
					String result="No message available.";

					String pre = "";
					
					if(numRow > 0) {
					
						if(latestMsgSet.next()) {						
							result = latestMsgSet.getString("msg");
							System.out.println("Session_id from UsreServlet" +session.getAttribute("unique_id"));
							//here if message is send then outgoing_message_id = sender_id(session)
							
							if(sender_id.equalsIgnoreCase(latestMsgSet.getString("outgoing_msg_id"))) {
								pre = "You : ";
							}else {
								pre = "";
							}
							
						}
						
					}else {
						result = "No message available.";
					}
					String msg;
					if(result!=null && result.length() > 26) {
						msg = result.substring(0, 25) + "...";
					}else {
						msg = result;
					}
					
					// check user in online or offline
					String offline;
				
					if(set.getString("status").equalsIgnoreCase("Offline now")) {
						offline = "offline";
					}else {
						offline = "";
					}
				
					
					output += "<a href=\"chats?user_id=" + set.getString("unique_id") + "\">\n" 
							//from here to chat.jsp
					   		+ "					<div class=\"content\">\n"
					   		+ "						<img alt=\"\" src=\"uploads/"+ set.getString("img")+"\">\n"
					   		+ "						<div class=\"details\">\n"
					   		+ "							<span>"+ set.getString("fname") + " " + set.getString("lname") +"</span>\n"
					   		+ "							<p>"+pre+msg+"</p>\n"
					   		+ "						</div>\n"
					   		+ "					</div>\n"
					   		+ "				<div class=\"status-dot "+ offline +" \"><i class=\"fas fa-circle\"></i></div>\n"
					   		+ "				</a>";
			   }
			 
		   }

		   resp.getWriter().write(output);
		   //back to user.jsp

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

private int numOfRows(String incoming_id, String outgoing_id, Connection conn) throws SQLException {
		
		ResultSet set = getMessagesResultSet(incoming_id, outgoing_id, conn);
		int i = 0;
		while(set.next()) {
			i += 1;
		}
		return i;
	}

	private ResultSet getMessagesResultSet(String incoming_id, String outgoing_id, Connection conn) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM messages "
				+ " WHERE (outgoing_msg_id=? AND incoming_msg_id=?)"
				+ " OR (outgoing_msg_id=? AND incoming_msg_id=?) "
				+ "ORDER BY msg_id DESC LIMIT 1;";
		
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, outgoing_id);
		pstmt.setString(2, incoming_id);
		pstmt.setString(3, incoming_id);
		pstmt.setString(4, outgoing_id);
		
		ResultSet set = pstmt.executeQuery();
		
		return set;
		
	}
	
	
}

*/



public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        String output = "";
        DatabaseConfig db;

        try {
            db = new DatabaseConfig();
            Connection conn = db.getConnection();

            // Query to fetch friends
            String sql = "SELECT u.* FROM users u INNER JOIN friends f "
                    + "ON (f.sender = u.unique_id OR f.receiver = u.unique_id) "
                    + "WHERE (f.sender = ? OR f.receiver = ?) AND u.unique_id != ?;";
            PreparedStatement pstmt = conn.prepareStatement(sql);

            // Get the logged-in user's unique_id from the session
            String userId = (String) session.getAttribute("unique_id");
            System.out.println("User ID from session: " + userId);

            // Set parameters in the query
            pstmt.setString(1, userId); // For f.sender = ?
            pstmt.setString(2, userId); // For f.receiver = ?
            pstmt.setString(3, userId); // To exclude the logged-in user itself

            ResultSet set = pstmt.executeQuery();

            // Process the result set
            int row_count = 0;
            while (set.next()) {
                row_count++;
                String receiver_id = set.getString("unique_id");

                ResultSet latestMsgSet = getMessagesResultSet(receiver_id, userId, conn);
                int numRow = numOfRows(receiver_id, userId, conn);

                String result = "No message available.";
                String pre = "";

                if (numRow > 0 && latestMsgSet.next()) {
                    result = latestMsgSet.getString("msg");

                    if (userId.equalsIgnoreCase(latestMsgSet.getString("outgoing_msg_id"))) {
                        pre = "You: ";
                    }
                }

                String msg = result.length() > 26 ? result.substring(0, 25) + "..." : result;

                String offline = "offline";
                if (!set.getString("status").equalsIgnoreCase("Offline now")) {
                    offline = "";
                }

                output += "<a href=\"chats?user_id=" + receiver_id + "\">\n"
                        + "    <div class=\"content\">\n"
                        + "        <img alt=\"\" src=\"uploads/" + set.getString("img") + "\">\n"
                        + "        <div class=\"details\">\n"
                        + "            <span>" + set.getString("fname") + " " + set.getString("lname") + "</span>\n"
                        + "            <p>" + pre + msg + "</p>\n"
                        + "        </div>\n"
                        + "    </div>\n"
                        + "    <div class=\"status-dot " + offline + "\"><i class=\"fas fa-circle\"></i></div>\n"
                        + "</a>";
            }

            if (row_count == 0) {
                output = "No users are available to chat";
            }

            resp.getWriter().write(output);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Helper method to count the number of rows in the messages result set
    private int numOfRows(String incoming_id, String outgoing_id, Connection conn) throws SQLException {
        ResultSet set = getMessagesResultSet(incoming_id, outgoing_id, conn);
        int i = 0;
        while (set.next()) {
            i += 1;
        }
        return i;
    }

    // Helper method to fetch the latest message between two users
    private ResultSet getMessagesResultSet(String incoming_id, String outgoing_id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM messages "
                + "WHERE (outgoing_msg_id = ? AND incoming_msg_id = ?) "
                + "OR (outgoing_msg_id = ? AND incoming_msg_id = ?) "
                + "ORDER BY msg_id DESC LIMIT 1;";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, outgoing_id);
        pstmt.setString(2, incoming_id);
        pstmt.setString(3, incoming_id);
        pstmt.setString(4, outgoing_id);

        return pstmt.executeQuery();
    }
}

