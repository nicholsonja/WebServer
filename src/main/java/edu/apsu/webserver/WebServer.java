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

        System.out.println("Request from " + exchange.getRemoteAddress());

        int number = RANDOM.nextInt(100) + 1;
        String message = "Hello!!\n" + number + "\n";
        
        byte[] body = message.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(200, body.length);

        try (OutputStream out = exchange.getResponseBody()) {
            out.write(body);
        }
    }
}