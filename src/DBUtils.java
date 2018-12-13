import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
	private Connection conn;
	private String url = "jdbc:mysql://127.0.0.1:3306/cart_ccs" + "?serverTimezone=GMT%2B8";
	private String user = "root";
	private String password = "007";

	private Statement statement;
	private ResultSet rSet;

	public void openConnect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, password);
			if (conn != null) {
				System.out.println("数据库连接成功");
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.toString());
		}
	}

	public ResultSet getUser() {
		try {
			statement = conn.createStatement();
			rSet = statement.executeQuery("select * from user");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rSet;
	}

	public boolean isExistInDB(String username, String password) {
		boolean isFlag = false;
		try {
			statement = conn.createStatement();
			rSet = statement.executeQuery("select * from user");
			if (rSet != null) {
				while (rSet.next()) {
					if (rSet.getString("user_name").equals(username)) {
						if (rSet.getString("user_pwd").equals(password)) {
							isFlag = true;
							break;
						}
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isFlag = false;
		}
		return isFlag;
	}

	public boolean insertDataToDB(String username, String password) {
		String sql = " insert into user ( user_name , user_pwd ) values ( " + "'" + username + "', " + "'" + password
				+ "' )";

		try {
			statement = conn.createStatement();
			return statement.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public void closeConnect() {
		try {
			if (rSet != null) {
				rSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (conn != null) {
				conn.close();
			}
			System.out.println("关闭数据库连接成功");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}
