package music_storage.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    private static Connection connection;
    private static final String USER = "FcONqOaHRl";
    private static final String PASSWORD = "tDIlb4HN0M";
    private static final String URL = "jdbc:mysql://remotemysql.com:3306/FcONqOaHRl?useUnicode=yes&characterEncoding=UTF8" +
            "&useSSL=false&serverTimezone=Asia/Omsk";
    
    
    public static void createConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    
    public static Connection getConnection() {
        return connection;
    }
    
    
    public static void closeConnection() throws SQLException {
        if (connection != null)
            connection.close();
    }
}
