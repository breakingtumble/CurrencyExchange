package com.breakingtumble.exchanger.service.impl;

import com.breakingtumble.exchanger.dao.ExchangeRateDAO;
import com.breakingtumble.exchanger.dto.ExchangeRateDTO;
import com.breakingtumble.exchanger.exception.RateNotFoundException;
import com.breakingtumble.exchanger.model.ExchangeRate;
import com.breakingtumble.exchanger.service.ExchangeRateService;
import com.breakingtumble.exchanger.dao.impl.ExchangeRateDAOImpl;

import java.sql.SQLException;
import java.util.List;

public class ExchangeRateServiceImpl implements ExchangeRateService {
    private static ExchangeRateServiceImpl instance;
    private final ExchangeRateDAO exchangeRateDAO;

    private ExchangeRateServiceImpl() {
        exchangeRateDAO = new ExchangeRateDAOImpl();
    }

    public static synchronized ExchangeRateServiceImpl getService() {
        if (instance == null) {
            instance = new ExchangeRateServiceImpl();
        }
        return instance;
    }

    @Override
    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        return exchangeRateDAO.getAll();
    }

    @Override
    public ExchangeRate createExchangeRate(ExchangeRate exchangeRate) throws SQLException {
        return exchangeRateDAO.create(exchangeRate);
    }

    @Override
    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate, float rate) throws SQLException {
        return exchangeRateDAO.update(exchangeRate, rate);
    }

    @Override
    public ExchangeRate findExchangeRateByCodes(String base, String target) throws SQLException {
        return exchangeRateDAO.findByBaseAndTargetCode(base, target);
    }

    @Override
    public ExchangeRateDTO convertAmount(float amount, String baseCode, String targetCode) throws SQLException {
        float convertedAmount;
        ExchangeRate exchangeRate = findExchangeRateByCodes(baseCode, targetCode);
        if (exchangeRate != null) {
            convertedAmount = amount * exchangeRate.getRate();
            return new ExchangeRateDTO(exchangeRate.getBase(), exchangeRate.getTarget(), exchangeRate.getRate(),
                    amount, convertedAmount);
        }
        exchangeRate = findExchangeRateByCodes(targetCode, baseCode);
        if (exchangeRate != null) {
            float reversedRate = 1.0f / exchangeRate.getRate();
            convertedAmount = amount * reversedRate;
            return new ExchangeRateDTO(exchangeRate.getTarget(), exchangeRate.getBase(), reversedRate,
                    amount, convertedAmount);
        }
        exchangeRate = findExchangeRateByCodes("USD", baseCode);
        if (exchangeRate != null) {
            ExchangeRate secondExchangeRate = findExchangeRateByCodes("USD", targetCode);
            if (secondExchangeRate != null) {
                float crossRate = secondExchangeRate.getRate() / exchangeRate.getRate();
                convertedAmount = amount * crossRate;
                return new ExchangeRateDTO(exchangeRate.getTarget(), secondExchangeRate.getTarget(), crossRate,
                        amount, convertedAmount);
            }
        }
        throw new RateNotFoundException("Rate wasn't found");
    }
}
