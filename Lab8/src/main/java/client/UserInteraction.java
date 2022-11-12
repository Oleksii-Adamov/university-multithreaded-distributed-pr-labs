package client;

import entities.City;
import entities.Country;
import entities.Entity;

import java.util.AbstractMap;
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
        if (entities == null) {
            System.out.println("Error");
        }
        for (T entity : entities) {
            entity.print();
            System.out.println();
        }
    }

    public static void printEintity(Entity entity) {
        if (entity == null) {
            System.out.println("Error");
        }
        else {
            entity.print();
        }
    }

    public static void printCitiesAndCountryNames(Collection<AbstractMap.SimpleEntry<City, String>> citiesInfo) {
        for (AbstractMap.SimpleEntry<City, String> cityInfo : citiesInfo) {
            cityInfo.getKey().print();
            System.out.println("country name: " + cityInfo.getValue());
            System.out.println();
        }
    }
}
