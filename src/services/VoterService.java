package services;

import java.sql.*;

public class VoterService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/onlin";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "piyush"; // Change if needed

    // Method to authenticate user
    public boolean login(String email, String password) {
        boolean isAuthenticated = false;
        String query = "SELECT * FROM voters WHERE email = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            isAuthenticated = rs.next(); // true if user found

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    // Method to register new voter
    public boolean registerVoter(String email, String name, String password) {
        String query = "INSERT INTO voters (email, name, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email);
            stmt.setString(2, name);
            stmt.setString(3, password);

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to cast a vote (prevents double voting)
    public boolean castVote(String voterId, String partyName) {
        String checkQuery = "SELECT * FROM votes WHERE voter_id = ?";
        String insertQuery = "INSERT INTO votes (voter_id, party_id) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {

            // Check if this voter already voted
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setString(1, voterId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    return false; // Already voted
                }
            }

            // Insert the vote
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setString(1, voterId);
                insertStmt.setString(2, partyName);
                int rows = insertStmt.executeUpdate();
                return rows > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
