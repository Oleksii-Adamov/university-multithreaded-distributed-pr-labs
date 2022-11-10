import database.MapDAO;
import entities.City;

import java.util.AbstractMap;

public class Main {
    public static void main(String[] args) {
        MapDAO map = new MapDAO();
        for (AbstractMap.SimpleEntry<City, String> cityInfo : map.getCitiesAndCountryNames()) {
            cityInfo.getKey().print();
            System.out.println("country name: " + cityInfo.getValue());
            System.out.println();
        }
        System.out.println(map.numCitiesInCountry(1));
    }
}