package server.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerRmiTask1 {
    public static void main(String[] args) {
        try {
            Registry r = LocateRegistry.createRegistry(1099);
            RMIServerImpl server = new RMIServerImpl();
            r.rebind("Task1", server);
            System.out.println("Server started");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
