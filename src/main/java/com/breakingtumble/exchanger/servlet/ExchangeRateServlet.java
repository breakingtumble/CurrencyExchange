package com.breakingtumble.exchanger.servlet;

import com.breakingtumble.exchanger.model.ExchangeRate;
import com.breakingtumble.exchanger.service.ExchangeRateService;
import com.breakingtumble.exchanger.service.impl.ExchangeRateServiceImpl;
import com.breakingtumble.exchanger.util.ErrorSender;
import com.breakingtumble.exchanger.util.ExchangeRateDtoMapper;
import com.breakingtumble.exchanger.util.JsonResponseSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() {
        exchangeRateService = ExchangeRateServiceImpl.getService();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        if (req.getPathInfo() == null) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Please provide currency codes pair.");
            return;
        }
        String currencyCodes = req.getPathInfo().substring(1);
        if (currencyCodes.length() != 6) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Wrong currency code format.");
            return;
        }
        if (!currencyCodes.matches("^[A-Z]*$")) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Code must contain uppercase-only alphabets.");
            return;
        }
        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3);
        try {
            ExchangeRate exchangeRate = exchangeRateService.findExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate == null) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_NOT_FOUND, "Can't find exchange rate.");
                return;
            }
            JsonResponseSender.sendJsonResponse(resp, ExchangeRateDtoMapper.mapToDto(exchangeRate));
        } catch (SQLException e) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String data = br.readLine();
        if (req.getPathInfo() == null) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Please provide currency codes pair.");
            return;
        }
        if (data == null || !data.contains("rate=")) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Enter 'rate' field properly.");
            return;
        }
        String currencyCodes = req.getPathInfo().substring(1);
        String rate = data.substring(data.indexOf("rate=") + 5);
        if (currencyCodes.length() != 6) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Wrong currency code format.");
            return;
        }
        if (!currencyCodes.matches("^[A-Z]*$")) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Code must contain uppercase-only alphabets.");
            return;
        }
        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3);
        float newRate;
        try {
            newRate = Float.parseFloat(rate);
            ExchangeRate exchangeRate = exchangeRateService.findExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
            if (exchangeRate == null) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_NOT_FOUND, "Can't find specified exchange rate.");
                return;
            }
            JsonResponseSender.sendJsonResponse(resp, ExchangeRateDtoMapper.mapToDto(exchangeRateService.updateExchangeRate(exchangeRate, newRate)));
        } catch (SQLException | NumberFormatException e) {
            if (e instanceof NumberFormatException) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Field 'rate' must be decimal number.");
            } else {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
            }
        }
    }
}
