import java.util.List;

public interface Map {
    boolean addCountry(Country country);
    boolean addCity(int countryCode, City city);
    boolean delCity(int cityCode);
    boolean delCountry(int countryCode);
    boolean updCity(City newCityData);
    boolean updCountry(Country newCountryData);
    Country getCountry(int countryCode);
    City getCity(int cityCode);
    List<Country> getCountries();
    List<City> getCities(int countryCode);
}
