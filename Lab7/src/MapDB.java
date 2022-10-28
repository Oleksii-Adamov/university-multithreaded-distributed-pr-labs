import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

public class MapDB extends MapQueries {
    private final Connection con;
    private final Statement stmt;

    public MapDB() {
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
    }

    public void stop() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean addCountry(Country country) {
        String sql = "INSERT INTO COUNTRIES (ID_CO, NAME) " +
                "VALUES (" + country.code + ", '" + country.name + "')";
        try {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: addCountry " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addCity(City city) {
        String sql = "INSERT INTO CITIES (ID_CI, NAME, ISCAPITAL, COUNT, ID_CO) " +
                "VALUES (" + city.code + ", '" + city.name + "', " + city.isCapital + ", " + city.count + ", " +
                city.countryCode + ")";
        try {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: addCity " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delCity(int cityCode) {
        String sql = "DELETE FROM CITIES WHERE ID_CI =" + cityCode;
        try {
            int c = stmt.executeUpdate(sql);
            return c > 0;
        } catch (SQLException e) {
            System.out.println("Error: delCity " + cityCode + " " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delCountry(int countryCode) {
        String sql = "DELETE FROM COUNTRIES WHERE ID_CO =" + countryCode;
        try {
            int c = stmt.executeUpdate(sql);
            return c > 0;
        } catch (SQLException e) {
            System.out.println("Error: delCountry " + countryCode + " " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updCity(City newCityData) {
        String sql = "UPDATE CITIES SET NAME ='" + newCityData.name + "', ISCAPITAL =" + newCityData.isCapital +
                ", COUNT =" + newCityData.count + ", ID_CO =" + newCityData.countryCode +
                " WHERE ID_CI =" + newCityData.code;
        try {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: updCity " + newCityData.code + " " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updCountry(Country newCountryData) {
        String sql = "UPDATE COUNTRIES SET NAME ='" + newCountryData.name + "' WHERE ID_CO =" + newCountryData.code;
        try {
            stmt.executeUpdate(sql);
            return true;
        } catch (SQLException e) {
            System.out.println("Error: updCountry " + newCountryData.code + " " + e.getMessage());
            return false;
        }
    }

    @Override
    public Country getCountry(int countryCode) {
        Country retCountry;
        String sql = "SELECT ID_CO, NAME FROM COUNTRIES WHERE ID_CO =" + countryCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            retCountry = new Country(rs.getInt("ID_CO"), rs.getString("NAME"));
            rs.close();
            return retCountry;
        } catch (SQLException e) {
            System.out.println("Error: getCountry" + countryCode + " " + e.getMessage());
            return null;
        }
    }

    @Override
    public City getCity(int cityCode) {
        City retCity;
        String sql = "SELECT ID_CI, NAME, ISCAPITAL, COUNT, ID_CO " +
                "FROM CITIES WHERE ID_CI =" + cityCode;
        try {
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            retCity = new City(rs.getInt("ID_CI"), rs.getString("NAME"),
                    rs.getInt("ISCAPITAL"), rs.getInt("COUNT"), rs.getInt("ID_CO"));
            rs.close();
            return retCity;
        } catch (SQLException e) {
            System.out.println("Error: getCity " + cityCode + " " + e.getMessage());
            return null;
        }
    }

    @Override
    public Collection<Country> getCountries() {
        ArrayList<Country> countries = new ArrayList<>();
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
        return countries;
    }

    @Override
    public Collection<City> getCities(int countryCode) {
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
            return null;
        }
        return cities;
    }

    public boolean addFromXML(String filePath) {
        boolean success = true;
        MapXML mapXML = new MapXML(filePath);
        for (Country country : mapXML.getCountries()) {
            success = addCountry(country);
            if (!success) break;
            for (City city : mapXML.getCities(country.code)) {
                success = addCity(city);
                if (!success) break;
            }
            if (!success) break;
        }
        return success;
    }
}
