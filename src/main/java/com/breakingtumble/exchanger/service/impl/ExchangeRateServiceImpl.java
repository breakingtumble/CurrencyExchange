package com.breakingtumble.exchanger.service.impl;

import com.breakingtumble.exchanger.dao.ExchangeRateDAO;
import com.breakingtumble.exchanger.dto.ExchangeRateResultDto;
import com.breakingtumble.exchanger.exception.RateNotFoundException;
import com.breakingtumble.exchanger.model.ExchangeRate;
import com.breakingtumble.exchanger.service.ExchangeRateService;
import com.breakingtumble.exchanger.dao.impl.ExchangeRateDAOImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    public ExchangeRateResultDto convertAmount(float amount, String baseCode, String targetCode) throws SQLException {
        BigDecimal decimalAmount = new BigDecimal(amount);
        BigDecimal convertedAmount;
        ExchangeRate exchangeRate = findExchangeRateByCodes(baseCode, targetCode);
        if (exchangeRate != null) {
            convertedAmount = exchangeRate.getRate().multiply(decimalAmount);
            return new ExchangeRateResultDto(exchangeRate.getBase(), exchangeRate.getTarget(),
                    exchangeRate.getRate().setScale(2, RoundingMode.DOWN),
                    decimalAmount.setScale(2, RoundingMode.DOWN),
                    convertedAmount.setScale(2, RoundingMode.DOWN));
        }
        exchangeRate = findExchangeRateByCodes(targetCode, baseCode);
        if (exchangeRate != null) {
            BigDecimal reversedRate = BigDecimal.ONE.divide(exchangeRate.getRate(), 6,  RoundingMode.DOWN);
            convertedAmount = decimalAmount.multiply(reversedRate);
            return new ExchangeRateResultDto(exchangeRate.getTarget(),
                    exchangeRate.getBase(), reversedRate.setScale(2, RoundingMode.DOWN),
                    decimalAmount.setScale(2, RoundingMode.DOWN),
                    convertedAmount.setScale(2, RoundingMode.DOWN));
        }
        exchangeRate = findExchangeRateByCodes("USD", baseCode);
        if (exchangeRate != null) {
            ExchangeRate secondExchangeRate = findExchangeRateByCodes("USD", targetCode);
            if (secondExchangeRate != null) {
                BigDecimal crossRate = secondExchangeRate.getRate().divide(exchangeRate.getRate(), RoundingMode.DOWN);
                convertedAmount = decimalAmount.multiply(crossRate);
                return new ExchangeRateResultDto(exchangeRate.getTarget(),
                        secondExchangeRate.getTarget(), crossRate.setScale(2, RoundingMode.DOWN),
                        decimalAmount.setScale(2, RoundingMode.DOWN),
                        convertedAmount.setScale(2, RoundingMode.DOWN));
            }
        }
        throw new RateNotFoundException("Rate wasn't found");
    }
}
