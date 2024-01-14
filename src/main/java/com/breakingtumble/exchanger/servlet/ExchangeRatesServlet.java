package com.breakingtumble.exchanger.servlet;

import com.breakingtumble.exchanger.model.Currencyy;
import com.breakingtumble.exchanger.model.ExchangeRate;
import com.breakingtumble.exchanger.service.CurrencyService;
import com.breakingtumble.exchanger.service.ExchangeRateService;
import com.breakingtumble.exchanger.service.impl.CurrencyServiceImpl;
import com.breakingtumble.exchanger.service.impl.ExchangeRateServiceImpl;
import com.breakingtumble.exchanger.util.ErrorSender;
import com.breakingtumble.exchanger.util.ExchangeRateDtoMapper;
import com.breakingtumble.exchanger.util.JsonResponseSender;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private static final int UNIQUE_CONSTRAINT_FAILED_CODE = 19;
    private CurrencyService currencyService;
    private ExchangeRateService exchangeRateService;

    @Override
    public void init() {
        currencyService = CurrencyServiceImpl.getService();
        exchangeRateService = ExchangeRateServiceImpl.getService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            JsonResponseSender.sendJsonResponse(resp, ExchangeRateDtoMapper.mapListToDto(exchangeRateService.getAllExchangeRates()));
        } catch (SQLException e) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occurred: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rate = req.getParameter("rate");
        BigDecimal rateConverted;
        if (baseCurrencyCode == null || targetCurrencyCode == null || rate == null) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Some fields are absent, please, check the request body");
            return;
        }
        if (baseCurrencyCode.length() != 3 || targetCurrencyCode.length() != 3) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Wrong currency code format");
            return;
        }
        if (!baseCurrencyCode.matches("^[A-Z]*$")
                || !targetCurrencyCode.matches("^[A-Z]*$")) {
            ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Code must contain uppercase-only alphabets.");
            return;
        }
        try {
            rateConverted = new BigDecimal(rate).setScale(6, RoundingMode.DOWN);
            Currencyy base_currency = currencyService.getCurrencyByCode(baseCurrencyCode);
            if (base_currency == null) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_NOT_FOUND, "Base currency wasn't found");
                return;
            }

            Currencyy target_currency = currencyService.getCurrencyByCode(targetCurrencyCode);
            if (target_currency == null) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_NOT_FOUND, "Target currency wasn't found");
                return;
            }

            ExchangeRate exchangeRate = new ExchangeRate(base_currency, target_currency, rateConverted);
            JsonResponseSender.sendJsonResponse(resp, ExchangeRateDtoMapper.mapToDto(exchangeRateService.createExchangeRate(exchangeRate)));
        } catch (SQLException | NumberFormatException e) {
            if (e instanceof NumberFormatException) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_BAD_REQUEST, "Field 'rate' must be decimal number.");
            } else if (((SQLException) e).getErrorCode() == UNIQUE_CONSTRAINT_FAILED_CODE) {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_CONFLICT, "Provided exchange rate already exists");
            } else {
                ErrorSender.sendErrorJSON(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection error!");
            }
        }
    }
}
