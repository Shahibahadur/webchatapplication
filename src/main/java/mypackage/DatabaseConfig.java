package mypackage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConfig {
	public static String driverPath = "com.mysql.cj.jdbc.Driver";
	public static String host = "localhost";
	public static String username = "root";
	public static String password = "admin";
	public static String dbname = "chat";
	public static int portNo = 3306;
	public String url = "jdbc:mysql://" + host + ":" + portNo + "/" + dbname;
	static Connection conn=null;
	public DatabaseConfig() throws ClassNotFoundException , SQLException{
		//load the driver
		com.mysql.cj.jdbc.Driver set = new com.mysql.cj.jdbc.Driver();
		//register Driver;
		// establish the connection to database
		DriverManager.registerDriver(set);
		//Class.forName(driverPath);
		conn = DriverManager.getConnection(url,username,password);		
	}
	public void closeConnection(Connection conn) throws SQLException{
		if(conn!=null && !conn.isClosed()) {
		conn.close();
		}
	}
	public Connection getConnection() {
		return conn;
	}

}
//finished