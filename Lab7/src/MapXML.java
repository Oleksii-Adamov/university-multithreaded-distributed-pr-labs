import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class MapXML implements Map {
    //private List<Country> countries = new ArrayList<>();
    private HashMap<Integer, Country> countries = new HashMap<>();

    //private List<City> cities = new ArrayList<>();
    private HashMap<Integer, City> cities = new HashMap<>();

    public MapXML(String filePath) {
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(true);
        try {
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException e) throws SAXException {
                    throw new SAXException("Error: " + "Line" + e.getLineNumber() + ": " + e.getMessage());
                }
                @Override
                public void fatalError(SAXParseException e) throws SAXException {
                    throw new SAXException("Fatal error: " + "Line" + e.getLineNumber() + ": " + e.getMessage());
                }

                @Override
                public void warning(SAXParseException e) throws SAXException {
                    System.out.print("XML parsing warning: ");
                    System.out.print("Line" + e.getLineNumber() + ": ");
                    System.out.println(e.getMessage());
                }
            });
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = null;
        try {
            doc = db.parse(new File(filePath));
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        Element root = doc.getDocumentElement();
        NodeList listCountries = root.getElementsByTagName("country");
        for (int i = 0; i < listCountries.getLength(); i++) {
            Element country = (Element) listCountries.item(i);
            Country curCountry = new Country(Integer.parseInt(country.getAttribute("id")), country.getAttribute("name"));
            countries.put(curCountry.code, curCountry);

            NodeList listCities = country.getElementsByTagName("city");
            for (int j = 0; j < listCities.getLength(); j++) {
                Element city = (Element) listCities.item(j);
                City curCity = new City(Integer.parseInt(city.getAttribute("id")), city.getAttribute("name"),
                        Boolean.parseBoolean(city.getAttribute("iscap")), Integer.parseInt(city.getAttribute("count")),
                        curCountry.code);
                cities.put(curCity.code, curCity);
            }
        }
    }

    @Override
    public boolean addCountry(Country country) {
        if (countries.containsKey(country.code)) {
            return false;
        }
        countries.put(country.code, country);
        return true;
    }

    @Override
    public boolean addCity(City city) {
        if (countries.containsKey(city.countryCode) && !cities.containsKey(city.code)) {
            cities.put(city.code, city);
            return true;
        }
        return false;
    }

    @Override
    public boolean delCity(int cityCode) {
        return cities.remove(cityCode) != null;
    }

    @Override
    public boolean delCountry(int countryCode) {
        if (countries.remove(countryCode) != null) {
            ArrayList<Integer> cityCodesToRemove = new ArrayList<>();
            for (City city: cities.values()) {
                if (city.countryCode == countryCode) {
                    cityCodesToRemove.add(city.code);
                }
            }
            for (int cityCodeToRemove : cityCodesToRemove) {
                cities.remove(cityCodeToRemove);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean updCity(City newCityData) {
        if (cities.containsKey(newCityData.code) && countries.containsKey(newCityData.countryCode)) {
            cities.put(newCityData.code, newCityData);
            return true;
        }
        return false;
    }

    @Override
    public boolean updCountry(Country newCountryData) {
        if (countries.containsKey(newCountryData.code)) {
            countries.put(newCountryData.code, newCountryData);
            return true;
        }
        return false;
    }

    @Override
    public Country getCountry(int countryCode) {
        return countries.get(countryCode);
    }

    @Override
    public City getCity(int cityCode) {
        return cities.get(cityCode);
    }

    @Override
    public Collection<Country> getCountries() {
        return countries.values();
    }

    @Override
    public Collection<City> getCities(int countryCode) {
        ArrayList<City> retCities = new ArrayList<>();
        for (City city : cities.values()) {
            if (city.countryCode == countryCode) {
                retCities.add(city);
            }
        }
        return retCities;
    }

    @Override
    public void toXML(String filePath) {
        // creating DOM
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
        Document doc = builder.newDocument();
        // creating root element
        Element root = doc.createElement("map");
        doc.appendChild(root);
        // adding countries and cities
        for (Country country : countries.values()) {
            Element country_elem = doc.createElement("country");
            country_elem.setAttribute("id", Integer.toString(country.code));
            country_elem.setAttribute("name", country.name);
            root.appendChild(country_elem);
            for (City city : cities.values()) {
                if (city.countryCode == country.code) {
                    Element city_elem = doc.createElement("city");
                    city_elem.setAttribute("id", Integer.toString(city.code));
                    city_elem.setAttribute("name", city.name);
                    city_elem.setAttribute("iscap", Boolean.toString(city.isCapital));
                    city_elem.setAttribute("count", Integer.toString(city.count));
                    country_elem.appendChild(city_elem);
                }
            }
        }
        // writing DOM to XML file
        Source domSource = new DOMSource(doc);
        Result fileResult = new StreamResult(new File(filePath));
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-16");
            transformer.transform(domSource, fileResult);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
