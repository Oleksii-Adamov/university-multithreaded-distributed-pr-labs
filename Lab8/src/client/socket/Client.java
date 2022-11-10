package client.socket;

import client.UserInteraction;
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

    private static Country queryCountryFromUser(Scanner scanner) {
        Country country = new Country();
        System.out.print("code: ");
        country.code = Integer.parseInt(scanner.nextLine());
        System.out.print("name: ");
        country.name = scanner.nextLine();
        return country;
    }
    private static City queryCityFromUser(Scanner scanner) {
        City city = new City();
        System.out.print("code: ");
        city.code = Integer.parseInt(scanner.nextLine());
        System.out.print("name: ");
        city.name = scanner.nextLine();
        System.out.print("iscap: ");
        city.isCapital = Integer.parseInt(scanner.nextLine());
        System.out.print("count: ");
        city.count = Integer.parseInt(scanner.nextLine());
        System.out.print("country code: ");
        city.countryCode = Integer.parseInt(scanner.nextLine());
        return city;
    }
    private static int queryCodeFromUser(Scanner scanner) {
        System.out.print("code: ");
        return Integer.parseInt(scanner.nextLine());
    }

    private static void printIsSuccess(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Failure!");
        }
    }

    private static <T extends Entity> void printEntityIterable(Collection<T> entities) {
        for (T entity : entities) {
            entity.print();
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Socket socket = null;
        try {
            System.out.println("Connecting to server...");
            socket = new Socket("localhost", 12345);
            System.out.println("Connected");

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            UserInteraction.printCommands();

            String command = scanner.nextLine();
            while (!Objects.equals(command, "quit")) {
                out.println(command);
                System.out.println(in.readLine());
                command = scanner.nextLine();
            }
            socket.close();
            System.out.println("Closed socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
