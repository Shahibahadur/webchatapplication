package mypackage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnectionTest {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		DatabaseConfig db = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			db = new DatabaseConfig();
			conn = db.getConnection();
			if (conn != null && !conn.isClosed()) {
				System.out.println("connection to database is sucessFull");

				String query = "SELECT COUNT(*) AS table_count FROM information_schema.tables WHERE table_schema = ?";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, "chat");
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					int tableNumber = rs.getInt("table_count");
					System.out.println("Number of table: " + tableNumber);
				}
				String query1 = "Show Tables;";
				pstmt = conn.prepareStatement(query1);
				rs = pstmt.executeQuery();
				System.out.println("Name of Table");
				while (rs.next()) {
					System.out.println(rs.getString(1));
				}

			} else {
				System.out.println("Connection to datbase is failed");
			}
		} catch (ClassNotFoundException e) {
			System.out.println("database is not found " + e.getMessage());

		} catch (SQLException e) {
			System.out.println("Failed to connect database" + e.getMessage());
		} finally {
			try {
				db.closeConnection(conn);
				System.out.println("database connection clsoed");
			} catch (SQLException e) {
				System.out.println("Failed to close the database connection: " + e.getMessage());
			}
		}
	}

}
