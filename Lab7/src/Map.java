import java.util.Collection;

public interface Map {
    boolean addCountry(Country country);
    boolean addCity(City city);
    boolean delCity(int cityCode);
    boolean delCountry(int countryCode);
    boolean updCity(City newCityData);
    boolean updCountry(Country newCountryData);
    Country getCountry(int countryCode);
    City getCity(int cityCode);
    Collection<Country> getCountries();
    Collection<City> getCities(int countryCode);
    void toXML(String filePath);
}
