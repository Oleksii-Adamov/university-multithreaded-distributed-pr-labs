package ua.lab9.web.servlets;

import ua.lab9.web.database.MapDAO;
import ua.lab9.web.entities.Country;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/new-country")
public class CreateCountryServlet  extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        getServletContext().getRequestDispatcher("/jsp/new-country.jsp").forward(req, res);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        try {
            Integer code = Integer.parseInt(req.getParameter("code"));
            String name = req.getParameter("name");
            MapDAO.addCountry(new Country(code, name));
            req.setAttribute("countryCode", code);
            String message = "The new country has been successfully created.";
            req.setAttribute("message", message);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/countries");
            dispatcher.forward(req,res);
//            response.sendRedirect(request.getContextPath()+"/index");
        }
        catch(Exception ex) {
            getServletContext().getRequestDispatcher("/create.jsp").forward(req, res);
        }
    }
}
