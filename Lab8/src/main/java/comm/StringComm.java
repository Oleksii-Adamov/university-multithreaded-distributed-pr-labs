package comm;

import entities.City;
import entities.Country;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class StringComm {

    private StringComm() {}

    public static String[] msgFields(String string) {
        return string.split("#");
    }
    public static String countryToMsg(Country country) {
        return country.code + "#" + country.name;
    }

    public static String cityToMsg(City city) {
        return city.code + "#" + city.name + "#" + city.isCapital + "#" + city.count + "#" + city.countryCode;
    }

    public static String successToMsg(boolean success) {
        return success ? "1" : "0";
    }

    public static String countriesToMsg(Collection<Country> countries) {
        StringBuilder msg = new StringBuilder(String.valueOf(countries.size()));
        for (Country country : countries) {
            msg.append("#").append(countryToMsg(country));
        }
        return msg.toString();
    }

    public static String citiesToMsg(Collection<City> cities) {
        StringBuilder msg = new StringBuilder(String.valueOf(cities.size()));
        for (City city : cities) {
            msg.append("#").append(cityToMsg(city));
        }
        return msg.toString();
    }

    public static String citiesAndCountryNamesToMsg(Collection<AbstractMap.SimpleEntry<City, String>> citiesInfo) {
        StringBuilder msg = new StringBuilder(String.valueOf(citiesInfo.size()));
        for (AbstractMap.SimpleEntry<City, String> cityInfo : citiesInfo) {
            msg.append("#").append(cityToMsg( cityInfo.getKey())).append("#").append(cityInfo.getValue());
        }
        return msg.toString();
    }

    public static boolean isSuccess(String msg) {
        String[] fields = msg.split("#");
        return Objects.equals(fields[0], "toClient") && Objects.equals(fields[1], "1");
    }

    public static boolean isValidMsgToServer(String[] fields) {
        return fields.length > 1 && Objects.equals(fields[0], "toServer");
    }

    public static boolean isValidAndSucceful(String[] fields) {
        return (Objects.equals(fields[0], "toClient") && Objects.equals(fields[1], "1")) ||
                Objects.equals(fields[0], "toServer");
    }

    public static Country countryFromFieldsPos(String[] fields, int pos) {
        Country retCountnry = null;
        if (pos + 1 < fields.length) {
            retCountnry = new Country();
            retCountnry.code = Integer.parseInt(fields[pos]);
            retCountnry.name = fields[pos + 1];
        }
        return retCountnry;
    }

    public static Country countryFromFields(String[] fields) {
        Country retCountnry = null;
        if (isValidAndSucceful(fields) && fields.length == 4) {
            retCountnry = countryFromFieldsPos(fields, 2);
        }
        return retCountnry;
    }

    public static City cityFromFieldsPos(String[] fields, int pos) {
        City retCity = null;
        if (pos + 4 < fields.length) {
            retCity = new City();
            retCity.code = Integer.parseInt(fields[pos]);
            retCity.name = fields[pos + 1];
            retCity.isCapital = Integer.parseInt(fields[pos + 2]);
            retCity.count = Integer.parseInt(fields[pos + 3]);
            retCity.countryCode = Integer.parseInt(fields[pos + 4]);
        }
        return retCity;
    }

    public static City cityFromFields(String[] fields) {
        City retCity = null;
        if (isValidAndSucceful(fields) && fields.length == 7) {
            retCity = cityFromFieldsPos(fields, 2);
        }
        return retCity;
    }

    public static Collection<Country> countriesFromFields(String[] fields) {
        Collection<Country> countries = new ArrayList<>();
        if (isValidAndSucceful(fields) && fields.length > 2) {
            int size = Integer.parseInt(fields[2]);
            for (int i = 3, it = 0; i < fields.length - 1 && it < size; i+=2, it++) {
                countries.add(countryFromFieldsPos(fields, i));
            }
        }
        return countries;
    }

    public static Collection<City> citiesFromFields(String[] fields) {
        Collection<City> cities = new ArrayList<>();
        if (isValidAndSucceful(fields) && fields.length > 2) {
            int size = Integer.parseInt(fields[2]);
            for (int i = 3, it = 0; i < fields.length - 1 && it < size; i+=5, it++) {
                cities.add(cityFromFieldsPos(fields, i));
            }
        }
        return cities;
    }

    public static Collection<AbstractMap.SimpleEntry<City, String>> citiesAndCountryNamesFromFields(String[] fields) {
        Collection<AbstractMap.SimpleEntry<City, String>> citiesInfo = new ArrayList<>();
        if (isValidAndSucceful(fields) && fields.length > 2) {
            int size = Integer.parseInt(fields[2]);
            for (int i = 3, it = 0; i < fields.length - 1 && it < size; i+=6, it++) {
                citiesInfo.add(new AbstractMap.SimpleEntry<>(cityFromFieldsPos(fields, i), fields[i+5]));
            }
        }
        return citiesInfo;
    }

    public static Integer intFromFields(String[] fields) {
        if (isValidAndSucceful(fields) && fields.length == 3) {
            return Integer.parseInt(fields[2]);
        }
        return null;
    }

}
