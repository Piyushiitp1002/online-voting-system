package services;

import java.sql.*;

public class AdminService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/onlin";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "piyush";

    public boolean login(String email, String password) {
        boolean isAuthenticated = false;

        String query = "SELECT * FROM admins WHERE email = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            isAuthenticated = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }
}
