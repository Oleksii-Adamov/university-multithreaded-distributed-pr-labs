package server.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnectionRunnable implements Runnable {

    Socket socket;

    SocketConnectionRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected, socket info: " + socket.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            String messageFromClient = in.readLine();
            while (messageFromClient != null) {
                System.out.println(messageFromClient);
                out.println(messageFromClient);
                messageFromClient = in.readLine();
            }
            System.out.println("Socket closed by client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
