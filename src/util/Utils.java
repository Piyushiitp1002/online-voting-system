package util;

import java.io.PrintWriter;

public class Utils {
    public static void sendResponse(PrintWriter writer, String message) {
        writer.print("HTTP/1.1 200 OK\r\n");
        writer.print("Content-Type: text/html\r\n\r\n");
        writer.print("<html><body>" + message + "</body></html>");
        writer.flush();
    }

    public static void sendRedirect(PrintWriter writer, String location) {
        writer.print("HTTP/1.1 302 Found\r\n");
        writer.print("Location: " + location + "\r\n");
        writer.print("\r\n");
        writer.flush();
    }

    public static java.util.Map<String, String> parseFormData(String raw) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        String[] pairs = raw.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                map.put(parts[0], parts[1]);
            }
        }
        return map;
    }
}
