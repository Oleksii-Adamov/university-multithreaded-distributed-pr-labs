package client;

import entities.City;
import entities.Country;
import entities.Entity;

import java.util.Collection;
import java.util.Scanner;

public class UserInteraction {

    public static void printCommands() {
        System.out.println("Commands:");
        System.out.println("addCountry");
        System.out.println("addCity");
        System.out.println("delCity");
        System.out.println("delCountry");
        System.out.println("updCity");
        System.out.println("updCountry");
        System.out.println("getCountry");
        System.out.println("getCity");
        System.out.println("getCountries");
        System.out.println("getCities");
        System.out.println("getCitiesAndCountryNames");
        System.out.println("numCitiesInCountry");
    }

    public static Country queryCountryFromUser(Scanner scanner) {
        Country country = new Country();
        System.out.print("code: ");
        country.code = Integer.parseInt(scanner.nextLine());
        System.out.print("name: ");
        country.name = scanner.nextLine();
        return country;
    }

    public static City queryCityFromUser(Scanner scanner) {
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

    public static int queryCodeFromUser(Scanner scanner) {
        System.out.print("code: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public static void printIsSuccess(boolean isSuccess) {
        if (isSuccess) {
            System.out.println("Success!");
        }
        else {
            System.out.println("Failure!");
        }
    }

    public static <T extends Entity> void printEntityIterable(Collection<T> entities) {
        for (T entity : entities) {
            entity.print();
            System.out.println();
        }
    }
}
