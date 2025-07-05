package server;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class SimpleHttpServer {

    private int port;
    private Map<String, BiConsumer<String, OutputStream>> postHandlers = new HashMap<>();
    private Map<String, BiConsumer<String, OutputStream>> getHandlers = new HashMap<>();


    public SimpleHttpServer(int port) {
        this.port = port;
    }

    public void addPostHandler(String path, BiConsumer<String, OutputStream> handler) {
        postHandlers.put(path, handler);
    }

    public void addGetHandler(String path, BiConsumer<String, OutputStream> handler) {
        getHandlers.put(path, handler);
    }
    

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("🚀 Server running at http://localhost:" + port);

            while (true) {
                Socket client = serverSocket.accept();
                handleClient(client);
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }

    private void handleClient(Socket client) {
        try (
            InputStream input = client.getInputStream();
            OutputStream output = client.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))
        ) {
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) return;

            String[] parts = requestLine.split(" ");
            String method = parts[0];
            String path = parts[1];

            // Read headers and body
            int contentLength = 0;
            String line;
            while (!(line = reader.readLine()).isEmpty()) {
                if (line.startsWith("Content-Length:")) {
                    contentLength = Integer.parseInt(line.substring("Content-Length:".length()).trim());
                }
            }

            if (method.equals("POST") && postHandlers.containsKey(path)) {
                char[] bodyChars = new char[contentLength];
                reader.read(bodyChars);
                String requestBody = new String(bodyChars);
                postHandlers.get(path).accept(requestBody, output);

            } else if (method.equals("GET") && getHandlers.containsKey(path)) {
                getHandlers.get(path).accept(null, output);

            } else if (method.equals("GET")) {
                serveStaticFile(path, output);
            } else {
                sendNotFound(output);
            }

            client.close();

        } catch (IOException e) {
            System.err.println("❌ Client error: " + e.getMessage());
        }
    }

    private void serveStaticFile(String path, OutputStream output) throws IOException {
        if (path.equals("/")) path = "/index.html";
        File file = new File("frontend" + path);

        if (file.exists() && !file.isDirectory()) {
            String contentType = path.endsWith(".html") ? "text/html" :
                                 path.endsWith(".css") ? "text/css" :
                                 path.endsWith(".js") ? "application/javascript" :
                                 "text/plain";

            byte[] data = java.nio.file.Files.readAllBytes(file.toPath());

            PrintWriter writer = new PrintWriter(output);
            writer.print("HTTP/1.1 200 OK\r\n");
            writer.print("Content-Type: " + contentType + "\r\n");
            writer.print("Content-Length: " + data.length + "\r\n");
            writer.print("\r\n");
            writer.flush();

            output.write(data);
            output.flush();
        } else {
            sendNotFound(output);
        }
    }

    private void sendNotFound(OutputStream output) throws IOException {
        PrintWriter writer = new PrintWriter(output);
        writer.print("HTTP/1.1 404 Not Found\r\n");
        writer.print("Content-Type: text/html\r\n");
        writer.print("\r\n");
        writer.print("<h1>404 - Page Not Found</h1>");
        writer.flush();
    }
}
