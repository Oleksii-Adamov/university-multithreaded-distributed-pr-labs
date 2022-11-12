package server.rmi;

import entities.City;
import entities.Country;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.AbstractMap;
import java.util.Collection;

public interface RMIServer extends Remote {
    boolean addCountry(Country country) throws RemoteException;
    boolean addCity(City city) throws RemoteException;
    boolean delCity(int cityCode) throws RemoteException;
    boolean delCountry(int countryCode) throws RemoteException;
    boolean updCity(City newCityData) throws RemoteException;
    boolean updCountry(Country newCountryData) throws RemoteException;
    Country getCountry(int countryCode) throws RemoteException;
    City getCity(int cityCode) throws RemoteException;
    Collection<Country> getCountries() throws RemoteException;
    Collection<City> getCities(int countryCode) throws RemoteException;
    Collection<AbstractMap.SimpleEntry<City, String>> getCitiesAndCountryNames() throws RemoteException;
    Integer numCitiesInCountry(int countryCode) throws RemoteException;
}
