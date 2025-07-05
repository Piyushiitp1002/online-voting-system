import com.sun.net.httpserver.HttpServer;
import handle.LoginHandler;
import handle.RegisterHandler;
import handle.ElectionHandler;
import handle.VoteHandler;
import handle.StaticFileHandler;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8001;

        // Start HTTP server
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Backend API endpoints
        server.createContext("/login", new LoginHandler());
        server.createContext("/register", new RegisterHandler());
        server.createContext("/elections", new ElectionHandler());
        server.createContext("/vote", new VoteHandler());

        // Frontend static file handler for HTML, CSS, JS, images
        server.createContext("/", new StaticFileHandler("frontend"));

        // Use thread pool for concurrent requests
        server.setExecutor(Executors.newFixedThreadPool(10));

        System.out.println("Server started on http://localhost:" + port);
        server.start();
    }

}
