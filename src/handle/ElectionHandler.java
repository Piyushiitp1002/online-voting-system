package handle;

import java.io.*;
import java.sql.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class ElectionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        StringBuilder html = new StringBuilder();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/onlin", "root", "piyush")) {

            String sql = "SELECT * FROM parties";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String party = rs.getString("name");
            

                html.append("<div class='party-option'>")
                        .append("<label>")
                        .append("<input type='radio' name='party_id' value='")
                        .append(party).append("' required> ")
                        .append(party)
                        .append("</label>")
                        .append("</div>");

            }

        } catch (Exception e) {
            e.printStackTrace();
            html.append("<p>Error loading parties.</p>");
        }

        byte[] response = html.toString().getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "text/html");
        exchange.sendResponseHeaders(200, response.length);
        OutputStream os = exchange.getResponseBody();
        os.write(response);
        os.close();
    }
}
