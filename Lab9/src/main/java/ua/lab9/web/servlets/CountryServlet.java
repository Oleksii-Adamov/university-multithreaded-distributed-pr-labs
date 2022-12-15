package ua.lab9.web.servlets;

import ua.lab9.web.database.MapDAO;
import ua.lab9.web.entities.City;
import ua.lab9.web.entities.Country;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/country")
public class CountryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardCountryInfo(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "add":
                addCityAction(req, resp);
                break;
            case "editCountry":
                editCountryAction(req, resp);
                break;
            case "editCity":
                editCityAction(req, resp);
                break;
            case "deleteCity":
                deleteCityAction(req, resp);
                break;
        }
    }
    private void forwardCountryInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nextJSP = "/jsp/country.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        int code = Integer.parseInt(req.getParameter("countryCode"));
        String countryName = MapDAO.getCountry(code).getName();
        List<City> cities = MapDAO.getCities(code);
        req.setAttribute("countryName", countryName);
        req.setAttribute("cities", cities);
        req.setAttribute("countryCode", code);
        dispatcher.forward(req, resp);
    }

    private void editCountryAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("name");
        int code = Integer.parseInt(req.getParameter("countryCode"));
        boolean success = MapDAO.updCountry(new Country(code, name));
        String message = null;
        if (success) {
            message = "The country has been successfully updated.";
        }
        req.setAttribute("countryCode", code);
        req.setAttribute("message", message);
        forwardCountryInfo(req, resp);
    }

    private void addCityAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("code"));
        String name = req.getParameter("name");
        int isCapital = Integer.parseInt(req.getParameter("isCapital"));
        int count = Integer.parseInt(req.getParameter("count"));
        int countryCode = Integer.parseInt(req.getParameter("countryCode"));
        boolean success = MapDAO.addCity(new City(code, name, isCapital, count, countryCode));
        if (success) {
            req.setAttribute("cityCode", code);
            String message = "The new city has been successfully created.";
            req.setAttribute("message", message);
            forwardCountryInfo(req, resp);
        }
        else {
            String message = "City with this code already exists or country with this code doesn't exist";
            req.setAttribute("message", message);
            String nextJSP = "/jsp/new-city.jsp";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(req, resp);
        }
    }

    private void editCityAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    private void deleteCityAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("cityCode"));
        int countryCode = Integer.parseInt(req.getParameter("countryCode"));
        boolean success = MapDAO.delCity(code);
        if (success){
            String message = "The city has been successfully deleted.";
            req.setAttribute("message", message);
            req.setAttribute("countryCode", countryCode);
        }
        forwardCountryInfo(req, resp);
    }
}
