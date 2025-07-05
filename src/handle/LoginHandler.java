package handle;

import java.io.*;
import java.sql.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            Map<String, String> formData = parseFormData(exchange);

            String email = formData.get("email");
            String password = formData.get("password");

            boolean isAuthenticated = false;

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/onlin", "root", "piyush")) {

                String sql = "SELECT * FROM voters WHERE email = ? AND password = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, email);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    isAuthenticated = true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            String redirect = isAuthenticated ? "voters.html" : "login.html";
            String response = "<script>alert('" + (isAuthenticated ? "Login successful!" : "Invalid login") + "');"
                    + "window.location='" + redirect + "';</script>";

            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    private Map<String, String> parseFormData(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
        Map<String, String> formData = new HashMap<>();

        if (query != null) {
            for (String pair : query.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    formData.put(URLDecoder.decode(parts[0], "UTF-8"),
                            URLDecoder.decode(parts[1], "UTF-8"));
                }
            }
        }
        return formData;
    }
}
