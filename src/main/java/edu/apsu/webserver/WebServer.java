package edu.apsu.webserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class WebServer {

    private static final int PORT = 8000;
    
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/", WebServer::handle);
        server.start();

        System.out.println("Listening on port " + PORT);
        System.out.println("Open http://localhost:" + PORT + " in a browser");
    }

    private static void handle(HttpExchange exchange) throws IOException {

        System.out.println("Request from " + exchange.getRemoteAddress() + " for " + exchange.getRequestURI());

        if (!exchange.getRequestURI().getPath().equals("/")) {
            exchange.sendResponseHeaders(404, -1);
            exchange.close();
            return;
        }

        final String[] quotes = {
            "To be, or not to be. - William Shakespeare",
            "I came, I saw, I conquered. - Julius Caesar",
            "Knowledge is power. - Francis Bacon"
        };

        int quoteNum = RANDOM.nextInt(quotes.length);
        String message = "<html>"
                + "<h1 style='text-align:center'>Quote " + (quoteNum + 1) + "</h1>"
                + "<p style='text-align:center'>" + quotes[quoteNum] + "</p>"
                + "</html>";
        
        System.out.println("Sent quote: " + quotes[quoteNum]);

        
        byte[] body = message.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(200, body.length);

        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }
}