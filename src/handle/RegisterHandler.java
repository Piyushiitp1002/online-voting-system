package handle;

import com.sun.net.httpserver.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class RegisterHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader reader = new BufferedReader(isr);
            String formData = reader.readLine();
            Map<String, String> params = parseFormData(formData);

            String name = params.get("name");
            String email = params.get("email");
            String password = params.get("password");

            try (Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/onlin", "root", "piyush")) {

                String sql = "INSERT INTO voters (name, email, password) VALUES (?, ?, ?)";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, password);
                ps.executeUpdate();

            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = "<script>alert('Registration Successful'); window.location='login.html';</script>";
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
                    map.put(parts[0], parts[1]);
                }
            }
        }
        return map;
    }
}
