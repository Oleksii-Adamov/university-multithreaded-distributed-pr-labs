package server.rmi;

import database.MapDAO;
import entities.City;
import entities.Country;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.AbstractMap;
import java.util.Collection;

public class RMIServerImpl extends UnicastRemoteObject implements RMIServer {

    private MapDAO map;

    public RMIServerImpl() throws RemoteException {
        map = new MapDAO();
    }

    @Override
    public boolean addCountry(Country country) throws RemoteException {
        return map.addCountry(country);
    }

    @Override
    public boolean addCity(City city) throws RemoteException {
        return map.addCity(city);
    }

    @Override
    public boolean delCity(int cityCode) throws RemoteException {
        return map.delCity(cityCode);
    }

    @Override
    public boolean delCountry(int countryCode) throws RemoteException {
        return map.delCountry(countryCode);
    }

    @Override
    public boolean updCity(City newCityData) throws RemoteException {
        return map.updCity(newCityData);
    }

    @Override
    public boolean updCountry(Country newCountryData) throws RemoteException {
        return map.updCountry(newCountryData);
    }

    @Override
    public Country getCountry(int countryCode) throws RemoteException {
        return map.getCountry(countryCode);
    }

    @Override
    public City getCity(int cityCode) throws RemoteException {
        return map.getCity(cityCode);
    }

    @Override
    public Collection<Country> getCountries() throws RemoteException {
        return map.getCountries();
    }

    @Override
    public Collection<City> getCities(int countryCode) throws RemoteException {
        return map.getCities(countryCode);
    }

    @Override
    public Collection<AbstractMap.SimpleEntry<City, String>> getCitiesAndCountryNames() throws RemoteException {
        return map.getCitiesAndCountryNames();
    }

    @Override
    public Integer numCitiesInCountry(int countryCode) throws RemoteException {
        return map.numCitiesInCountry(countryCode);
    }
}
