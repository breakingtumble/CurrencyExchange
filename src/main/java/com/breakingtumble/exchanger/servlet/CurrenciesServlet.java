package com.breakingtumble.exchanger.servlet;

import com.breakingtumble.exchanger.model.Currencyy;
import com.breakingtumble.exchanger.service.CurrencyService;
import com.breakingtumble.exchanger.service.impl.CurrencyServiceImpl;
import com.breakingtumble.exchanger.util.ErrorSender;
import com.breakingtumble.exchanger.util.JsonResponseSender;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private static final int UNIQUE_CONSTRAINT_FAILED_CODE = 19;
    private CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = CurrencyServiceImpl.getService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            /*
             * Invoking currencyService getAll() method to receive a list of currencies
             * there could be an empty list or list with Curencyy objects.
             * */
            JsonResponseSender.sendJsonResponse(resp, currencyService.getAllCurrencies());
        } catch (SQLException e) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
         * Checking if user provided all the fields to instantiating a Currency object,
         * otherwise sending json response with BAD REQUEST status code
         * */
        if (req.getParameter("name") == null || req.getParameter("code") == null || req.getParameter("sign") == null) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Some fields are absent, please, check the request body");
            return;
        }

        if (req.getParameter("code").length() != 3) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Wrong currency code format");
            return;
        } else if (!req.getParameter("code").matches("^[A-Z]*$")) {
            /*
             * Check if code contains only alphabetic syntax to properly find matching currency without any issues
             * */
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Code must contain only alphabets.");
            return;
        }
        /*
         *  If the previous checks were passed, creating Currency instance
         * to be able to work with DAO layer.
         * */
        Currencyy currencyy = new Currencyy(
                req.getParameter("name"),
                req.getParameter("code"),
                req.getParameter("sign"));
        /*
         *  Trying to obtain already existing instance in database,
         * if it exists sending a creation conflict code and json response with message.
         * Otherwise, create a json object of inserted currency and send it to client
         * */
        try {
            JsonResponseSender.sendJsonResponse(resp, currencyService.createCurrency(currencyy));
        } catch (SQLException e) {
            if (e.getErrorCode() == UNIQUE_CONSTRAINT_FAILED_CODE) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_CONFLICT, "Currency with provided code already exists");
            } else {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
            }
        }
    }
}
