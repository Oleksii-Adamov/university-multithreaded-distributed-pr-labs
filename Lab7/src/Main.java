import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Objects;
import java.util.Scanner;

public class Main {

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
        city.isCapital = Boolean.parseBoolean(scanner.nextLine());
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

    private static String queryFileNameFromUser(Scanner scanner) {
        System.out.print("file name: ");
        return scanner.nextLine();
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
        System.out.println(entities.size());
        for (T entity : entities) {
            entity.print();
            System.out.println();
        }
    }
    public static void main(String[] args) throws IOException {
        System.out.println("Commands:");
        System.out.println("DB");
        System.out.println("XML");
        System.out.println("quit");
        Scanner scanner = new Scanner( System.in );
        String command = scanner.nextLine();
        while (!Objects.equals(command, "quit")) {
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
            System.out.println("toXML");
            System.out.println("quit");

            command = scanner.nextLine();
            Map map = new MapXML("input.xml");
            while (!Objects.equals(command, "quit")) {
                switch (command) {
                    case "addCountry" -> printIsSuccess(map.addCountry(queryCountryFromUser(scanner)));
                    case "addCity" -> printIsSuccess(map.addCity(queryCityFromUser(scanner)));
                    case "delCity" -> printIsSuccess(map.delCity(queryCodeFromUser(scanner)));
                    case "delCountry" -> printIsSuccess(map.delCountry(queryCodeFromUser(scanner)));
                    case "updCity" -> printIsSuccess(map.updCity(queryCityFromUser(scanner)));
                    case "updCountry" -> printIsSuccess(map.updCountry(queryCountryFromUser(scanner)));
                    case "getCountry" -> map.getCountry(queryCodeFromUser(scanner)).print();
                    case "getCity" -> map.getCity(queryCodeFromUser(scanner)).print();
                    case "getCountries" -> printEntityIterable(map.getCountries());
                    case "getCities" -> printEntityIterable(map.getCities(queryCodeFromUser(scanner)));
                    case "toXML" -> map.toXML(queryFileNameFromUser(scanner));
                    //default -> System.out.println("Wrong command!");
                    //default -> ;
                }
                command = scanner.nextLine();
            }
        }
    }
}