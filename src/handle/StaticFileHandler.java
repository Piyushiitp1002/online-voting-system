package handle;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;

public class StaticFileHandler implements HttpHandler {
    private final File baseDir;

    public StaticFileHandler(String directory) {
        this.baseDir = new File(directory).getAbsoluteFile();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uri = exchange.getRequestURI().getPath();

        if (uri.equals("/"))
            uri = "/index.html";

        File file = new File(baseDir, URLDecoder.decode(uri, "UTF-8")).getCanonicalFile();

        if (!file.getPath().startsWith(baseDir.getPath()) || !file.exists() || file.isDirectory()) {
            String notFound = "404 (Not Found)\n";
            exchange.sendResponseHeaders(404, notFound.length());
            exchange.getResponseBody().write(notFound.getBytes());
            exchange.close();
            return;
        }

        String mimeType = Files.probeContentType(file.toPath());
        exchange.getResponseHeaders().set("Content-Type", mimeType != null ? mimeType : "application/octet-stream");

        byte[] fileBytes = Files.readAllBytes(file.toPath());
        exchange.sendResponseHeaders(200, fileBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(fileBytes);
        os.close();
    }
}
