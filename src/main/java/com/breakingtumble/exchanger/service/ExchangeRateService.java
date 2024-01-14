package com.breakingtumble.exchanger.service;

import com.breakingtumble.exchanger.dto.ExchangeRateResultDto;
import com.breakingtumble.exchanger.model.ExchangeRate;

import java.sql.SQLException;
import java.util.List;

public interface ExchangeRateService {
    List<ExchangeRate> getAllExchangeRates() throws SQLException;

    ExchangeRate createExchangeRate(ExchangeRate exchangeRate) throws SQLException;

    ExchangeRate updateExchangeRate(ExchangeRate exchangeRate, float rate) throws SQLException;

    ExchangeRate findExchangeRateByCodes(String base, String target) throws SQLException;

    ExchangeRateResultDto convertAmount(float amount, String baseCode, String targetCode) throws SQLException;
}
