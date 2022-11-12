package client.rmi;

import client.UserInteraction;
import server.rmi.RMIServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws MalformedURLException, NotBoundException, RemoteException {
        RMIServer server = (RMIServer) Naming.lookup("//127.0.0.1/Map");

        UserInteraction.printCommands();
        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine();
        while (!Objects.equals(command, "quit")) {
            switch (command) {
                case "addCountry" -> UserInteraction.printIsSuccess(server.addCountry(UserInteraction.queryCountryFromUser(scanner)));
                case "addCity" -> UserInteraction.printIsSuccess(server.addCity(UserInteraction.queryCityFromUser(scanner)));
                case "delCity" -> UserInteraction.printIsSuccess(server.delCity(UserInteraction.queryCodeFromUser(scanner)));
                case "delCountry" -> UserInteraction.printIsSuccess(server.delCountry(UserInteraction.queryCodeFromUser(scanner)));
                case "updCity" -> UserInteraction.printIsSuccess(server.updCity(UserInteraction.queryCityFromUser(scanner)));
                case "updCountry" -> UserInteraction.printIsSuccess(server.updCountry(UserInteraction.queryCountryFromUser(scanner)));
                case "getCountry" -> UserInteraction.printEintity(server.getCountry(UserInteraction.queryCodeFromUser(scanner)));
                case "getCity" -> UserInteraction.printEintity(server.getCity(UserInteraction.queryCodeFromUser(scanner)));
                case "getCountries" -> UserInteraction.printEntityIterable(server.getCountries());
                case "getCities" -> UserInteraction.printEntityIterable(server.getCities(UserInteraction.queryCodeFromUser(scanner)));
                case "getCitiesAndCountryNames" -> UserInteraction.printCitiesAndCountryNames(server.getCitiesAndCountryNames());
                case "numCitiesInCountry" -> {
                    Integer count = server.numCitiesInCountry(UserInteraction.queryCodeFromUser(scanner));
                    if (count == null) {
                        System.out.println("Error");
                    }
                    else {
                        System.out.println(count);
                    }
                }
                default -> System.out.println("Wrong command!");
            }
            command = scanner.nextLine();
        }
    }
}
