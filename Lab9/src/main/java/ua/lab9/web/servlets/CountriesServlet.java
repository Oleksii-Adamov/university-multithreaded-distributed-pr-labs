package ua.lab9.web.servlets;


import ua.lab9.web.database.MapDAO;
import ua.lab9.web.entities.Country;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/countries")
public class CountriesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        forwardCountries(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "add":
                addCountryAction(req, resp);
                break;
            case "delete":
                deleteCountryAction(req, resp);
                break;
        }
    }

    private void forwardCountries(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String nextJSP = "/jsp/countries.jsp";
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
        List<Country> countries = MapDAO.getCountries();
        req.setAttribute("countries", countries);
        dispatcher.forward(req, resp);
    }

    private void addCountryAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("code"));
        String name = req.getParameter("name");
        boolean success = MapDAO.addCountry(new Country(code, name));
        if (success) {
            req.setAttribute("countryCode", code);
            String message = "The new country has been successfully created.";
            req.setAttribute("message", message);
            forwardCountries(req, resp);
        }
        else {
            String message = "Country with this code already exists!";
            req.setAttribute("message", message);
            String nextJSP = "/jsp/new-country.jsp";
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(nextJSP);
            dispatcher.forward(req, resp);
        }
    }

    private void deleteCountryAction(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int code = Integer.parseInt(req.getParameter("countryCode"));
        boolean success = MapDAO.delCountry(code);
        if (success){
            String message = "The employee has been successfully removed.";
            req.setAttribute("message", message);
        }
        forwardCountries(req, resp);
    }
}
