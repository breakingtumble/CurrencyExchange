package com.breakingtumble.exchanger.servlet;

import com.breakingtumble.exchanger.exception.RateNotFoundException;
import com.breakingtumble.exchanger.service.ExchangeRateService;
import com.breakingtumble.exchanger.service.impl.ExchangeRateServiceImpl;
import com.breakingtumble.exchanger.util.ErrorSender;
import com.breakingtumble.exchanger.util.JsonResponseSender;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private ExchangeRateService exchangeRateService;

    @Override
    public void init() {
        exchangeRateService = ExchangeRateServiceImpl.getService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String fromCurrencyCode = req.getParameter("from");
        String toCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");
        if (fromCurrencyCode == null || toCurrencyCode == null || amount == null) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Provide fields 'from', 'to', 'amount' in query params.");
            return;
        }
        if (fromCurrencyCode.length() != 3 && toCurrencyCode.length() != 3) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Wrong currency code format");
        }
        if (!fromCurrencyCode.matches("^[A-Z]*$") || !toCurrencyCode.matches("^[A-Z]*$")) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Code must contain uppercase-only alphabets.");
        }
        float floatAmount;
        try {
            floatAmount = Float.parseFloat(amount);
            JsonResponseSender.sendJsonResponse(resp, exchangeRateService.convertAmount(floatAmount, fromCurrencyCode, toCurrencyCode));
        } catch (RuntimeException | SQLException e) {
            if (e instanceof RateNotFoundException) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_NOT_FOUND, "Can't find specified exchange rate.");
                return;
            }
            if (e instanceof NumberFormatException) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Field 'rate' must be decimal number.");
                return;
            }
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
