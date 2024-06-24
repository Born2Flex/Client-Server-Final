package ua.edu.ukma;

import ua.edu.ukma.http.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(8080, 5);
        server.start();
    }
}
