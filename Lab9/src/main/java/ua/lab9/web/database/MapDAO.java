package ua.lab9.web.database;

import ua.lab9.web.entities.City;
import ua.lab9.web.entities.Country;

import java.sql.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MapDAO {

    private MapDAO() {

    }

    private static final Connection con;
    private static final Statement stmt;

    private static Lock countriesReadLock;

    private static Lock countriesWriteLock;

    private static Lock citiesReadLock;

    private static Lock citiesWriteLock;

    static {
        String ip = "localhost";
        String port = "3306";
        String dbName = "map";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbName;
        try {
            con = DriverManager.getConnection(url, "root", "root");
            stmt = con.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ReentrantReadWriteLock countriesReadWriteLock = new ReentrantReadWriteLock();
        countriesReadLock = countriesReadWriteLock.readLock();
        countriesWriteLock = countriesReadWriteLock.writeLock();
        ReentrantReadWriteLock citiesReadWriteLock = new ReentrantReadWriteLock();
        citiesReadLock = citiesReadWriteLock.readLock();
        citiesWriteLock = citiesReadWriteLock.writeLock();
    }

    public static void stop() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean addCountry(Country country) {
        countriesWriteLock.lock();
        boolean success = true;
        String sql = "INSERT INTO COUNTRIES (ID_CO, NAME) " +
                "VALUES (" + country.getCode() + ", '" + country.getName() + "')";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: addCountry " + e.getMessage());
            success = false;
        }
        countriesWriteLock.unlock();
        return success;
    }

    public static boolean addCity(City city) {
        citiesWriteLock.lock();
        boolean success = true;
        String sql = "INSERT INTO CITIES (ID_CI, NAME, ISCAPITAL, COUNT, ID_CO) " +
                "VALUES (" + city.code + ", '" + city.name + "', " + city.isCapital + ", " + city.count + ", " +
                city.countryCode + ")";
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: addCity " + e.getMessage());
            success = false;
        }
        citiesWriteLock.unlock();
        return success;
    }

    public static boolean delCity(int cityCode) {
        citiesWriteLock.lock();
        boolean success = true;
        String sql = "DELETE FROM CITIES WHERE ID_CI =" + cityCode;
        try {
            int c = stmt.executeUpdate(sql);
            success = c > 0;
        } catch (SQLException e) {
            System.out.println("Error: delCity " + cityCode + " " + e.getMessage());
            success = false;
        }
        citiesWriteLock.unlock();
        return success;
    }

    public static boolean delCountry(int countryCode) {
        countriesWriteLock.lock();
        boolean success = true;
        String sql = "DELETE FROM COUNTRIES WHERE ID_CO =" + countryCode;
        try {
            int c = stmt.executeUpdate(sql);
            success = c > 0;
        } catch (SQLException e) {
            System.out.println("Error: delCountry " + countryCode + " " + e.getMessage());
            success = false;
        }
        countriesWriteLock.unlock();
        return success;
    }

    public static boolean updCity(City newCityData) {
        citiesWriteLock.lock();
        boolean success = true;
        String sql = "UPDATE CITIES SET NAME ='" + newCityData.name + "', ISCAPITAL =" + newCityData.isCapital +
                ", COUNT =" + newCityData.count + ", ID_CO =" + newCityData.countryCode +
                " WHERE ID_CI =" + newCityData.code;
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: updCity " + newCityData.code + " " + e.getMessage());
            success = false;
        }
        citiesWriteLock.unlock();
        return success;
    }

    public static boolean updCountry(Country newCountryData) {
        countriesWriteLock.lock();
        boolean success = true;
        String sql = "UPDATE COUNTRIES SET NAME ='" + newCountryData.getName() + "' WHERE ID_CO =" + newCountryData.getCode();
        try {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Error: updCountry " + newCountryData.getCode() + " " + e.getMessage());
            success = false;
        }
        countriesWriteLock.unlock();
        return success;
    }

    public static Country getCountry(int countryCode) {
        countriesReadLock.lock();
        Country retCountry;
        String sql = "SELECT ID_CO, NAME FROM COUNTRIES WHERE ID_CO =" + countryCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            retCountry = new Country(rs.getInt("ID_CO"), rs.getString("NAME"));
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: getCountry" + countryCode + " " + e.getMessage());
            retCountry = null;
        }
        countriesReadLock.unlock();
        return retCountry;
    }

    public static City getCity(int cityCode) {
        citiesReadLock.lock();
        City retCity;
        String sql = "SELECT ID_CI, NAME, ISCAPITAL, COUNT, ID_CO " +
                "FROM CITIES WHERE ID_CI =" + cityCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            retCity = new City(rs.getInt("ID_CI"), rs.getString("NAME"),
                    rs.getInt("ISCAPITAL"), rs.getInt("COUNT"), rs.getInt("ID_CO"));
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: getCity " + cityCode + " " + e.getMessage());
            retCity = null;
        }
        citiesReadLock.unlock();
        return retCity;
    }

    public static List<Country> getCountries() {
        countriesReadLock.lock();
        List<Country> countries = new ArrayList<>();
        String sql = "SELECT ID_CO, NAME FROM COUNTRIES";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                countries.add(new Country(rs.getInt("ID_CO"), rs.getString("NAME")));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: getCountries " + e.getMessage());
            return null;
        }
        countriesReadLock.unlock();
        return countries;
    }

    public static Collection<City> getCities(int countryCode) {
        citiesReadLock.lock();
        ArrayList<City> cities = new ArrayList<>();
        String sql = "SELECT ID_CI, NAME, ISCAPITAL, COUNT, ID_CO " +
                "FROM CITIES WHERE CITIES.ID_CO =" + countryCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cities.add(new City(rs.getInt("ID_CI"), rs.getString("NAME"),
                        rs.getInt("ISCAPITAL"), rs.getInt("COUNT"), rs.getInt("ID_CO")));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: getCities " + countryCode + " " + e.getMessage());
        }
        citiesReadLock.unlock();
        return cities;
    }

    public static Collection<AbstractMap.SimpleEntry<City, String>> getCitiesAndCountryNames() {
        countriesReadLock.lock();
        citiesReadLock.lock();
        ArrayList<AbstractMap.SimpleEntry<City, String>> cities = new ArrayList<>();
        String sql = "SELECT CITIES.ID_CI, CITIES.NAME, CITIES.ISCAPITAL, CITIES.COUNT, CITIES.ID_CO, COUNTRIES.NAME FROM CITIES INNER JOIN COUNTRIES ON CITIES.ID_CO = COUNTRIES.ID_CO";
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cities.add(new AbstractMap.SimpleEntry<>(
                        new City(rs.getInt("CITIES.ID_CI"), rs.getString("CITIES.NAME"),
                                rs.getInt("CITIES.ISCAPITAL"), rs.getInt("CITIES.COUNT"),
                                rs.getInt("CITIES.ID_CO")),
                        rs.getString("COUNTRIES.NAME")));
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: getCitiesAndCountryNames " + e.getMessage());
        }
        countriesReadLock.unlock();
        citiesReadLock.unlock();
        return cities;
    }

    public static Integer numCitiesInCountry(int countryCode) {
        citiesReadLock.lock();
        Integer numCities = null;
        String sql = "SELECT COUNT(ID_CI) as num_cities FROM CITIES WHERE CITIES.ID_CO =" + countryCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                numCities = rs.getInt("num_cities");
            }
            rs.close();
        } catch (SQLException e) {
            System.out.println("Error: numCitiesInCountry " + countryCode + " " + e.getMessage());
        }
        citiesReadLock.unlock();
        return numCities;
    }
}
