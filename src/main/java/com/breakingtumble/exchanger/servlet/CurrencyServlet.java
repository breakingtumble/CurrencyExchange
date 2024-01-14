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

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = CurrencyServiceImpl.getService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() == null) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Please provide the currency code.");
            return;
        }
        String currencyCode = req.getPathInfo().substring(1);
        /*
         * Check the code to be exactly 3 symbols long according to standards
         * */
        if (currencyCode.length() != 3) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Wrong currency code format");
        } else if (!currencyCode.matches("^[A-Z]*$")) {
            /*
             * Check if code contains only alphabetic syntax to properly find matching currency without any issues
             * */
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Code must contain only alphabets.");
        } else {
            /*
             * Getting object Currencyy which will be converted to JSON,
             * also need to handle case when object is null and SQL exception when the database is absent*/
            try {
                Currencyy currencyy = currencyService.getCurrencyByCode(currencyCode);
                if (currencyy == null) {
                    ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_NOT_FOUND, "Currency wasn't found");
                } else {
                    JsonResponseSender.sendJsonResponse(resp, currencyy);
                }
            } catch (SQLException e) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
            }
        }
    }
}
