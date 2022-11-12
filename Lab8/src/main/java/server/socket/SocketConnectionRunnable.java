package server.socket;

import comm.StringComm;
import database.MapDAO;
import server.ServerActionFromFields;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnectionRunnable implements Runnable {

    Socket socket;

    MapDAO map;

    SocketConnectionRunnable(Socket socket, MapDAO map) {
        this.socket = socket;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected, socket info: " + socket.toString());
            BufferedReader fromClientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
            String msgFromClient = fromClientReader.readLine();
            while (msgFromClient != null) {
                String[] fields = StringComm.msgFields(msgFromClient);
                String msgToClient = ServerActionFromFields.actAndGetRespond(map, fields, "toClient#");
                toClient.println(msgToClient);
                msgFromClient = fromClientReader.readLine();
            }
            System.out.println("Socket closed by client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
