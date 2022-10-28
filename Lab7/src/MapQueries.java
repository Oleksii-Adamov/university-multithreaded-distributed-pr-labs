import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class MapQueries {
    public abstract boolean addCountry(Country country);
    public abstract boolean addCity(City city);
    public abstract boolean delCity(int cityCode);
    public abstract boolean delCountry(int countryCode);
    public abstract boolean updCity(City newCityData);
    public abstract boolean updCountry(Country newCountryData);
    public abstract Country getCountry(int countryCode);
    public abstract City getCity(int cityCode);
    public abstract Collection<Country> getCountries();
    public abstract Collection<City> getCities(int countryCode);

    protected static boolean toXMLFromMap(HashMap<Country, Collection<City>> map, String filePath) {
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
        for (Map.Entry<Country, Collection<City>> entry : map.entrySet()) {
            Country country = entry.getKey();
            Element country_elem = doc.createElement("country");
            country_elem.setAttribute("id", Integer.toString(country.code));
            country_elem.setAttribute("name", country.name);
            root.appendChild(country_elem);
            for (City city : entry.getValue()) {
                if (city.countryCode == country.code) {
                    Element city_elem = doc.createElement("city");
                    city_elem.setAttribute("id", Integer.toString(city.code));
                    city_elem.setAttribute("name", city.name);
                    city_elem.setAttribute("iscap", Integer.toString(city.isCapital));
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
            DOMImplementation domImpl = doc.getImplementation();
            DocumentType doctype = domImpl.createDocumentType("doctype",
                    "SYSTEM",
                    "map.dtd");
            //transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, /*doctype.getSystemId()*/"map.dtd");
            transformer.transform(domSource, fileResult);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean toXML(String filePath) {
        HashMap<Country, Collection<City>> map = new HashMap<>();
        for (Country country : getCountries()) {
            map.put(country, getCities(country.code));
        }
        return toXMLFromMap(map, filePath);
    }
}
