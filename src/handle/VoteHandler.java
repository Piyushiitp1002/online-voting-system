package handle;

import com.sun.net.httpserver.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class VoteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader reader = new BufferedReader(isr);
            String formData = reader.readLine();
            Map<String, String> params = parseFormData(formData);

            String partyId = params.get("party_id");
            String voterId = params.get("voter_id");

            String response;

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/onlin", "root", "piyush")) {

                // Check if voter has already voted
                String checkSql = "SELECT * FROM votes WHERE voter_id = ?";
                PreparedStatement checkPs = con.prepareStatement(checkSql);
                checkPs.setString(1, voterId);
                ResultSet rs = checkPs.executeQuery();

                if (rs.next()) {
                    response = "<script>alert('You have already voted.'); window.location='voters.html';</script>";
                } else {
                    // Insert vote
                    String insertSql = "INSERT INTO votes (voter_id, party_id) VALUES (?, ?)";
                    PreparedStatement insertPs = con.prepareStatement(insertSql);
                    insertPs.setString(1, voterId);
                    insertPs.setString(2, partyId);
                    insertPs.executeUpdate();

                    response = "<script>alert('Vote cast successfully'); window.location='voters.html';</script>";
                }

            } catch (Exception e) {
                e.printStackTrace();
                response = "<script>alert('Error while submitting vote'); window.location='voters.html';</script>";
            }

            exchange.getResponseHeaders().set("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        if (formData != null) {
            for (String pair : formData.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    map.put(parts[0], java.net.URLDecoder.decode(parts[1], java.nio.charset.StandardCharsets.UTF_8));
                }
            }
        }
        return map;
    }
}
