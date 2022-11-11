package server.socket;

import client.UserInteraction;
import comm.StringComm;
import database.MapDAO;
import entities.City;
import entities.Country;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.AbstractMap;
import java.util.Collection;

public class SocketConnectionRunnable implements Runnable {

    Socket socket;

    MapDAO map;

    SocketConnectionRunnable(Socket socket, MapDAO map) {
        this.socket = socket;
        this.map = map;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected, socket info: " + socket.toString());
            BufferedReader fromClientReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter toClient = new PrintWriter(socket.getOutputStream(), true);
            String msgFromClient = fromClientReader.readLine();
            while (msgFromClient != null) {
                StringBuilder msgToClient = new StringBuilder("toClient#");
                String[] fields = StringComm.msgFields(msgFromClient);
                if (StringComm.isValidMsgToServer(fields)) {
                    switch (fields[1]) {
                        case "addCountry" -> {
                            Country country = StringComm.countryFromFields(fields);
                            msgToClient.append(StringComm.successToMsg(map.addCountry(country)));
                        }
                        case "addCity" -> {
                            City city = StringComm.cityFromFields(fields);
                            msgToClient.append(StringComm.successToMsg(map.addCity(city)));
                        }
                        case "delCity" -> {
                            Integer code = StringComm.intFromFields(fields);
                            boolean success = code != null && map.delCity(code);
                            msgToClient.append(StringComm.successToMsg(success));
                        }
                        case "delCountry" -> {
                            Integer code = StringComm.intFromFields(fields);
                            boolean success = code != null && map.delCountry(code);
                            msgToClient.append(StringComm.successToMsg(success));
                        }
                        case "updCity" -> {
                            City city = StringComm.cityFromFields(fields);
                            msgToClient.append(StringComm.successToMsg(map.updCity(city)));
                        }
                        case "updCountry" -> {
                            Country country = StringComm.countryFromFields(fields);
                            msgToClient.append(StringComm.successToMsg(map.updCountry(country)));
                        }
                        case "getCountry" -> {
                            Integer code = StringComm.intFromFields(fields);
                            Country country = null;
                            if (code != null) {
                                country = map.getCountry(code);
                            }
                            if (country == null) {
                                msgToClient.append(StringComm.successToMsg(false));
                            }
                            else {
                                msgToClient.append(StringComm.successToMsg(true)).append("#").append(StringComm.countryToMsg(country));
                            }
                        }
                        case "getCity" -> {
                            Integer code = StringComm.intFromFields(fields);
                            City city = null;
                            if (code != null) {
                                city = map.getCity(code);
                            }
                            if (city == null) {
                                msgToClient.append(StringComm.successToMsg(false));
                            }
                            else {
                                msgToClient.append(StringComm.successToMsg(true)).append("#").append(StringComm.cityToMsg(city));
                            }
                        }
                        case "getCountries" -> {
                            Collection<Country> countries = map.getCountries();
                            msgToClient.append(StringComm.successToMsg(true)).append("#").append(StringComm.countriesToMsg(countries));
                        }
                        case "getCities" -> {
                            Integer code = StringComm.intFromFields(fields);
                            if (code == null) {
                                msgToClient.append(StringComm.successToMsg(false));
                            }
                            else {
                                Collection<City> cities = map.getCities(code);
                                msgToClient.append(StringComm.successToMsg(true)).append("#").append(StringComm.citiesToMsg(cities));
                            }
                        }
                        case "getCitiesAndCountryNames" -> {
                            Collection<AbstractMap.SimpleEntry<City, String>> citiesInfo = map.getCitiesAndCountryNames();
                            msgToClient.append(StringComm.successToMsg(true)).append("#").append(StringComm.citiesAndCountryNamesToMsg(citiesInfo));
                        }
                        case "numCitiesInCountry" -> {
                            Integer code = StringComm.intFromFields(fields);
                            Integer count = null;
                            if (code != null) {
                                count = map.numCitiesInCountry(code);
                            }
                            if (count == null) {
                                msgToClient.append(StringComm.successToMsg(false));
                            }
                            else {
                                msgToClient.append(StringComm.successToMsg(true)).append("#").append(count);
                            }
                        }
                        default -> msgToClient.append(StringComm.successToMsg(false));
                    }
                }
                else {
                    msgToClient.append(StringComm.successToMsg(false));
                }
                toClient.println(msgToClient);
                msgFromClient = fromClientReader.readLine();
            }
            System.out.println("Socket closed by client");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
