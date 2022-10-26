import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
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
            e.printStackTrace();
        }
        Document doc = null;
        try {
            assert db != null;
            doc = db.parse(new File("input.xml"));
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
        Element root = doc.getDocumentElement();
        System.out.println(root.getTagName());
        if (root.getTagName().equals("map")) {
// Отримуємо колекцію країн
            NodeList listCountries = root.getElementsByTagName("country");
// Проходимо по країнам
            for (int i = 0; i < listCountries.getLength(); i++) {
// Отримуємо поточну країну
                Element country = (Element) listCountries.item(i);
                String countryCode = country.getAttribute("id");
                String countryName = country.getAttribute("name");

                System.out.println(countryCode + "\t" + countryName + ":");
// Отримуємо колекцію міст для країни
                NodeList listCities = country.getElementsByTagName("city");
// Проходимо по містах
                for (int j = 0; j < listCities.getLength(); j++) {
// Отримуємо поточне місто
                    Element city = (Element) listCities.item(j);
                    String cityName = city.getAttribute("name");
                    System.out.println("" + cityName);
                }
            }
        }
    }
}