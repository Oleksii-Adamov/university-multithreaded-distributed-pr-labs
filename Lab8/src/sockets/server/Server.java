package sockets.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started");
        Socket socket = null;
        try {
            while (true) {
                socket = server.accept();
                Thread thread = new Thread(new SocketConnectionRunnable(socket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
