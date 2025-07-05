package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PartyService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/onlin";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "piyush";

    public List<String> getAllParties() {
        List<String> parties = new ArrayList<>();
        String query = "SELECT name FROM parties";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                parties.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return parties;
    }
}
