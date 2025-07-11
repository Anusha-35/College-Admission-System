import java.sql.*;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/admission_db";
        String user = "root";
        String password = "anush@.10"; // 🔁 Replace with your MySQL password
        return DriverManager.getConnection(url, user, password);
    }
}
