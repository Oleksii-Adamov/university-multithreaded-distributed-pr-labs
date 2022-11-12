package client.socket;

import client.UserInteraction;
import comm.StringComm;
import entities.City;
import entities.Country;
import entities.Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        Socket socket = null;
        try {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 12345);
            System.out.println("Connected");

            BufferedReader fromServerReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            UserInteraction.printCommands();

            String command = scanner.nextLine();
            while (!Objects.equals(command, "quit")) {
                String baseMsgToServer = "toServer#" + command;
                switch (command) {
                    case "addCountry", "updCountry" -> {
                        toServer.println(baseMsgToServer + "#" + StringComm.countryToMsg(UserInteraction.queryCountryFromUser(scanner)));
                        UserInteraction.printIsSuccess(StringComm.isSuccess(StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "addCity", "updCity" -> {
                        toServer.println(baseMsgToServer + "#" + StringComm.cityToMsg(UserInteraction.queryCityFromUser(scanner)));
                        UserInteraction.printIsSuccess(StringComm.isSuccess(StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "delCity", "delCountry" -> {
                        toServer.println(baseMsgToServer + "#" + UserInteraction.queryCodeFromUser(scanner));
                        UserInteraction.printIsSuccess(StringComm.isSuccess(StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "getCountry" -> {
                        toServer.println(baseMsgToServer + "#" + UserInteraction.queryCodeFromUser(scanner));
                        UserInteraction.printEintity(StringComm.countryFromFields(
                                StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "getCity" -> {
                        toServer.println(baseMsgToServer + "#" + UserInteraction.queryCodeFromUser(scanner));
                        UserInteraction.printEintity(StringComm.cityFromFields(
                                StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "getCountries" -> {
                        toServer.println(baseMsgToServer);
                        UserInteraction.printEntityIterable(StringComm.countriesFromFields(StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "getCities" -> {
                        toServer.println(baseMsgToServer + "#" + UserInteraction.queryCodeFromUser(scanner));
                        UserInteraction.printEntityIterable(StringComm.citiesFromFields(StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "getCitiesAndCountryNames" -> {
                        toServer.println(baseMsgToServer);
                        UserInteraction.printCitiesAndCountryNames(StringComm.citiesAndCountryNamesFromFields(StringComm.msgFields(fromServerReader.readLine())));
                    }
                    case "numCitiesInCountry" -> {
                        toServer.println(baseMsgToServer + "#" + UserInteraction.queryCodeFromUser(scanner));
                        Integer count = StringComm.intFromFields(StringComm.msgFields(fromServerReader.readLine()));
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

            socket.close();
            System.out.println("Closed socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
